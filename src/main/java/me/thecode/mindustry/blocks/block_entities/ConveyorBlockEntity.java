package me.thecode.mindustry.blocks.block_entities;

import me.thecode.mindustry.ConveyorItem;
import me.thecode.mindustry.ConveyorSideType;
import me.thecode.mindustry.blocks.ConveyorBlock;
import me.thecode.mindustry.blocks.ModBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class ConveyorBlockEntity extends BlockEntity {
    public static final double speed = 0.05D;
    private static final int maxItemCount = 3;
    private static final double minConveyorItemDistance = 0.36D;
    public static final Vec3 middle = new Vec3(0.5F, 0,  0.5F);

    public ConveyorBlockEntity(BlockPos pWorldPosition, BlockState pBlockState) {
        super(ModBlocks.CONVEYOR_BLOCK_ENTITY.get(), pWorldPosition, pBlockState);
    }
    private boolean a = false;
    public void addItem(ItemStack itemStack) {
        ItemStack newItemStack = itemStack.copy();
        newItemStack.setCount(1);

        Direction direction = ConveyorBlock.getSides(getBlockState(), ConveyorSideType.INPUT).get(0);
        Vec3 vec = new Vec3(direction.getNormal().getX()/2.8+0.5F, 0, direction.getNormal().getZ()/2.8+0.5F);

        a = false;
        ConveyorItem.conveyorItems.forEach((blockEntity, conveyorItems) -> {
            for(ConveyorItem conveyorItem : conveyorItems) {
                if(conveyorItem.pos.add(blockEntity.getBlockPos().getX(), blockEntity.getBlockPos().getY(), blockEntity.getBlockPos().getZ()).distanceTo(vec.add(getBlockPos().getX(), getBlockPos().getY(), getBlockPos().getZ())) < minConveyorItemDistance) {
                    a = true;
                    break;
                }
            }
        });
        if(a) {
            return;
        }

        ConveyorItem.addNewConveyorItem(this, new ConveyorItem(newItemStack, vec));
        setChanged();
        itemStack.setCount(itemStack.getCount()-1);
    }
    public List<ConveyorItem> getConveyorItems() {
        return ConveyorItem.getConveyorItems(this);
    }
    public void removeConveyorItem(int conveyorItemIndex) {
        ConveyorItem.removeConveyorItem(this, conveyorItemIndex);
        setChanged();
    }

    @Override
    protected void saveAdditional(@NotNull CompoundTag tag) {
        super.saveAdditional(tag);
        tag.put("items", new CompoundTag());
        CompoundTag items = tag.getCompound("items");
        for(int i = 0; i < ConveyorItem.getConveyorItems(this).size(); i++) {
            ConveyorItem conveyorItem = ConveyorItem.getConveyorItems(this).get(i);
            items.put(""+i, new CompoundTag());
            CompoundTag compoundTag = items.getCompound(""+i);
            compoundTag.put("item", conveyorItem.itemStack.serializeNBT());
            compoundTag.putBoolean("reachedMiddle", false);
            compoundTag.putDouble("posX", conveyorItem.pos.x);
            compoundTag.putDouble("posZ", conveyorItem.pos.z);
            compoundTag.putInt("time", conveyorItem.time);
        }
    }

    @Override
    public void load(CompoundTag tag) {
        super.load(tag);
        CompoundTag items = tag.getCompound("items");
        for(int i = 0; i < items.size(); i++) {
            CompoundTag compoundTag = items.getCompound(""+i);
            ItemStack itemStack = ItemStack.of(compoundTag.getCompound("item"));
            Vec3 pos = new Vec3(compoundTag.getDouble("posX"), 0, compoundTag.getDouble("posZ"));
            boolean reachedMiddle = compoundTag.getBoolean("reachedMiddle");
            int time = compoundTag.getInt("time");
            ConveyorItem conveyorItem = new ConveyorItem(itemStack, pos, time, reachedMiddle);
            ConveyorItem.addNewConveyorItem(this, conveyorItem);
        }
    }

    private static boolean b = false;
    public static void tick(Level level, BlockPos pos, BlockState blockState, ConveyorBlockEntity blockEntity) {
        for(ConveyorItem conveyorItem : ConveyorItem.getConveyorItems(blockEntity)) {
            Direction output = ConveyorBlock.getSides(blockState, ConveyorSideType.OUTPUT).get(0);
            Vec3 vec = new Vec3(output.getNormal().getX()/2.0+0.5F, 0, output.getNormal().getZ()/2.0+0.5F);

            //ROTATION
            Vec3 delta;
            if(!conveyorItem.reachedMiddle) {
                delta = middle.subtract(conveyorItem.pos);
            }
            else {
                delta = vec.subtract(conveyorItem.pos);
            }
            double x = delta.x < -0.1D ? -1 : (delta.x >= 0.1D ? 1 : 0);
            double z = delta.z < -0.1D ? -1 : (delta.z >= 0.1D ? 1 : 0);
            Direction dir = null;
            for(Direction direction : Direction.values()) {
                if(direction.equals(Direction.UP) || direction.equals(Direction.DOWN)) {
                    continue;
                }
                if(direction.getStepX() == x && direction.getStepZ() == z) {
                    dir = direction;
                }
            }
            if(dir != null) {
                conveyorItem.rotation = dir.getRotation();
            }
            else {
                conveyorItem.rotation = output.getRotation();
            }

            //MOVE
            if(vec.distanceTo(conveyorItem.pos) < 0.01 && conveyorItem.reachedMiddle) {
                continue;
            }
            b = false;
            ConveyorItem.conveyorItems.forEach((_blockEntity, conveyorItems) -> {
                for(ConveyorItem _conveyorItem : conveyorItems) {
                    if(_conveyorItem.equals(conveyorItem)) {
                        continue;
                    }
                    if(_conveyorItem.time < conveyorItem.time) {
                        continue;
                    }
                    if(_conveyorItem.pos.add(_blockEntity.getBlockPos().getX(), _blockEntity.getBlockPos().getY(), _blockEntity.getBlockPos().getZ()).distanceTo(conveyorItem.pos.add(pos.getX(), pos.getY(), pos.getZ())) < minConveyorItemDistance) {
                        b = true;
                        break;
                    }
                }
            });
            if(b) {
                continue;
            }
            Vec3 move = delta.normalize().multiply(speed, speed, speed);
            conveyorItem.pos = conveyorItem.pos.add(move);
            conveyorItem.time += 1;

            double distanceToMiddle = conveyorItem.pos.distanceTo(middle);
            if(distanceToMiddle < 0.01) {
                conveyorItem.reachedMiddle = true;
            }
            blockEntity.setChanged();
        }
    }
}
