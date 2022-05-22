package me.thecode.mindustry.blocks;

import me.thecode.mindustry.ConveyorItem;
import me.thecode.mindustry.ConveyorSideType;
import me.thecode.mindustry.blocks.block_entities.ConveyorBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.level.material.PushReaction;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ConveyorBlock extends BaseEntityBlock {
    public static final EnumProperty<ConveyorSideType> NORTH = EnumProperty.create("north", ConveyorSideType.class);;
    public static final EnumProperty<ConveyorSideType> SOUTH = EnumProperty.create("south", ConveyorSideType.class);;
    public static final EnumProperty<ConveyorSideType> EAST = EnumProperty.create("east", ConveyorSideType.class);;
    public static final EnumProperty<ConveyorSideType> WEST = EnumProperty.create("west", ConveyorSideType.class);;

    public ConveyorBlock() {
        super(Properties.of(Material.METAL).strength(3.5F).sound(SoundType.METAL));
        registerDefaultState(defaultBlockState().setValue(NORTH, ConveyorSideType.OUTPUT));
        registerDefaultState(defaultBlockState().setValue(SOUTH, ConveyorSideType.INPUT));
        registerDefaultState(defaultBlockState().setValue(EAST, ConveyorSideType.NONE));
        registerDefaultState(defaultBlockState().setValue(WEST, ConveyorSideType.NONE));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> pBuilder) {
        pBuilder.add(NORTH);
        pBuilder.add(SOUTH);
        pBuilder.add(EAST);
        pBuilder.add(WEST);
    }
    @Override
    public PushReaction getPistonPushReaction(BlockState pState) {
        return PushReaction.BLOCK;
    }

    public BlockState getStateForPlacement(BlockPlaceContext pContext) {
        BlockState blockState = super.defaultBlockState()
                .setValue(getConveyorSidePropertyByDirection(pContext.getHorizontalDirection()), ConveyorSideType.OUTPUT)
                .setValue(getConveyorSidePropertyByDirection(pContext.getHorizontalDirection().getOpposite()), ConveyorSideType.INPUT)
                .setValue(getConveyorSidePropertyByDirection(pContext.getHorizontalDirection().getClockWise()), ConveyorSideType.NONE)
                .setValue(getConveyorSidePropertyByDirection(pContext.getHorizontalDirection().getCounterClockWise()), ConveyorSideType.NONE)
        ;
        return blockState;
    }
    @Override
    public VoxelShape getShape(BlockState pState, BlockGetter pLevel, BlockPos pPos, CollisionContext pContext) {
        return Block.box(0, 0, 0, 16, 7, 16);
    }
    public void neighborChanged(BlockState pState, Level pLevel, BlockPos pPos, Block pBlock, BlockPos pFromPos, boolean pIsMoving) {
        update(pLevel, pState, pPos);
    }
    @Override
    public void setPlacedBy(Level pLevel, BlockPos pPos, BlockState pState, @Nullable LivingEntity pPlacer, ItemStack pStack) {
        update(pLevel, pState, pPos);
    }

    private void update(Level level, BlockState blockState, BlockPos pos) {
        BlockState newBlockState = blockState;
        List<Direction> noneSides = getSides(blockState, ConveyorSideType.NONE);
        List<Direction> outputSides = getSides(blockState, ConveyorSideType.OUTPUT);
        List<Direction> inputSides = getSides(blockState, ConveyorSideType.INPUT);
        List<Direction> connectedInputSides = getConnectedSides(blockState, pos, level, ConveyorSideType.INPUT);
        List<Direction> unconnectedInputSides = getSides(blockState, ConveyorSideType.INPUT);
        unconnectedInputSides.removeAll(getConnectedSides(blockState, pos, level, ConveyorSideType.INPUT));

        if(connectedInputSides.size() >= 1 && unconnectedInputSides.size() >= 1) {
            for(Direction direction : unconnectedInputSides) {
                newBlockState = blockState.setValue(getConveyorSidePropertyByDirection(direction), ConveyorSideType.NONE);
            }
        }

        for(int i = 0; i < noneSides.size(); i++) {
            BlockPos _pos = pos.relative(noneSides.get(i));
            BlockState _blockState = level.getBlockState(_pos);
            if(!_blockState.is(this)) {
                continue;
            }

            if(_blockState.getValue(getConveyorSidePropertyByDirection(noneSides.get(i).getOpposite())).equals(ConveyorSideType.INPUT)) {
                newBlockState = blockState.setValue(getConveyorSidePropertyByDirection(noneSides.get(i)), ConveyorSideType.OUTPUT);

                if(outputSides.size() == 1) {
                    newBlockState = newBlockState.setValue(getConveyorSidePropertyByDirection(outputSides.get(0)), ConveyorSideType.NONE);
                }

                level.setBlock(pos, newBlockState, 3);
                continue;
            }
            if(_blockState.getValue(getConveyorSidePropertyByDirection(noneSides.get(i).getOpposite())).equals(ConveyorSideType.OUTPUT)) {
                newBlockState = blockState.setValue(getConveyorSidePropertyByDirection(noneSides.get(i)), ConveyorSideType.INPUT);

                if(connectedInputSides.size() == 0) {
                    newBlockState = newBlockState.setValue(getConveyorSidePropertyByDirection(inputSides.get(0)), ConveyorSideType.NONE);
                }
            }
        }
        level.setBlock(pos, newBlockState, 3);
    }
    public static EnumProperty<ConveyorSideType> getConveyorSidePropertyByDirection(Direction direction) {
        switch (direction) {
            case NORTH -> {
                return NORTH;
            }
            case SOUTH -> {
                return SOUTH;
            }
            case EAST -> {
                return EAST;
            }
            case WEST -> {
                return WEST;
            }
        }
        return null;
    }
    public static List<Direction> getSides(BlockState blockState, ConveyorSideType sideType) {
        List<Direction> sides = new ArrayList<>();
        List<Direction> horizontalDirections = new ArrayList<>(Arrays.asList(Direction.NORTH, Direction.SOUTH, Direction.EAST, Direction.WEST));
        for(Direction direction : horizontalDirections) {
            EnumProperty<ConveyorSideType> sideProperty = getConveyorSidePropertyByDirection(direction);
            if(blockState.getValue(sideProperty).equals(sideType)) {
                sides.add(direction);
            }
        }
        return sides;
    }
    public static List<Direction> getConnectedSides(BlockState blockState, BlockPos pos, Level level, ConveyorSideType sideType) {
        List<Direction> allSides = getSides(blockState, sideType);
        List<Direction> connectedSides = new ArrayList<>();
        for(Direction direction : allSides) {
            BlockState neighborBlockState = level.getBlockState(pos.relative(direction));
            if(!neighborBlockState.is(ModBlocks.CONVEYOR.get())) {
                continue;
            }
            if(neighborBlockState.getValue(getConveyorSidePropertyByDirection(direction.getOpposite())).equals(sideType.equals(ConveyorSideType.OUTPUT) ? ConveyorSideType.INPUT : ConveyorSideType.OUTPUT)) {
                connectedSides.add(direction);
            }
        }
        return connectedSides;
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pPos, BlockState pState) {
        return new ConveyorBlockEntity(pPos, pState);
    }

    @Override
    public InteractionResult use(BlockState pState, Level pLevel, BlockPos pPos, Player pPlayer, InteractionHand pHand, BlockHitResult pHit) {
        ConveyorBlockEntity blockEntity = (ConveyorBlockEntity)pLevel.getBlockEntity(pPos);
        if(pPlayer.getItemInHand(pHand).isEmpty()) {
            List<ConveyorItem> conveyorItems = blockEntity.getConveyorItems();
            if(conveyorItems != null) {
                for(int i = conveyorItems.size()-1; i >= 0; i--) {
                    int slot = pPlayer.getInventory().findSlotMatchingItem(conveyorItems.get(i).itemStack.copy());
                    if(
                            (slot != -1 && pPlayer.getInventory().getItem(slot).getCount() < conveyorItems.get(i).itemStack.getItem().getMaxStackSize()) ||
                            pPlayer.getInventory().getFreeSlot() != -1
                    ) {
                        pPlayer.addItem(conveyorItems.get(i).itemStack.copy());

                        blockEntity.removeConveyorItem(i);
                    }
                }
            }
        }
        else {
            blockEntity.addItem(pPlayer.getItemInHand(pHand));
        }
        return InteractionResult.SUCCESS;
    }
    public RenderShape getRenderShape(BlockState pState) {
        return RenderShape.MODEL;
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level pLevel, BlockState pState, BlockEntityType<T> pBlockEntityType) {
        return createTickerHelper(pBlockEntityType, ModBlocks.CONVEYOR_BLOCK_ENTITY.get(), ConveyorBlockEntity::tick);
    }
}
