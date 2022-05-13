package me.thecode.mindustry.blocks;

import com.mojang.math.Vector3f;
import me.thecode.mindustry.Mindustry;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Vec3i;
import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.level.material.PushReaction;
import net.minecraftforge.common.Tags;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Random;

public class MechanicalDrillBlock extends Block {
    private static HashMap<Direction, Vec3i> offset = new HashMap<>();
    static {
        offset.put(Direction.NORTH, new Vec3i(0, 0, 0));
        offset.put(Direction.SOUTH, new Vec3i(-1, 0, 1));
        offset.put(Direction.EAST, new Vec3i(0, 0, 1));
        offset.put(Direction.WEST, new Vec3i(-1, 0, 0));
    }

    public MechanicalDrillBlock() {
        super(Properties.of(Material.METAL).strength(3.5F).sound(SoundType.METAL));
        registerDefaultState(defaultBlockState().setValue(BlockStateProperties.HORIZONTAL_FACING, Direction.NORTH));
    }
    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> pBuilder) {
        pBuilder.add(BlockStateProperties.HORIZONTAL_FACING);
    }
    @Override
    public PushReaction getPistonPushReaction(BlockState pState) {
        return PushReaction.BLOCK;
    }

    @Override
    public void onRemove(BlockState pState, Level pLevel, BlockPos pPos, BlockState pNewState, boolean pIsMoving) {
        Mindustry.LOGGER.info("remove");
        Direction direction = pState.getValue(BlockStateProperties.HORIZONTAL_FACING);
        BlockPos northPos = pPos.offset(offset.get(direction));
        destroy(pLevel, northPos);
    }
    public BlockState getStateForPlacement(BlockPlaceContext pContext) {
        Direction direction = pContext.getHorizontalDirection();
        BlockPos northPos = pContext.getClickedPos().offset(offset.get(direction));

        return canBePlaced(pContext.getLevel(), northPos) ? this.defaultBlockState().setValue(BlockStateProperties.HORIZONTAL_FACING, direction) : null;
    }
    @Override
    public void setPlacedBy(Level pLevel, BlockPos pPos, BlockState pState, @Nullable LivingEntity pPlacer, ItemStack pStack) {
        Direction direction = pState.getValue(BlockStateProperties.HORIZONTAL_FACING);
        BlockPos northPos = pPos.offset(offset.get(direction));
        place(pLevel, northPos);
    }

    @Override
    public void animateTick(BlockState pState, Level pLevel, BlockPos pPos, Random pRandom) {
        Random random = pLevel.random;
        Direction direction = Direction.UP;
        BlockPos blockpos = pPos.relative(direction);
        if (!pLevel.getBlockState(blockpos).isSolidRender(pLevel, blockpos)) {
            Direction.Axis direction$axis = direction.getAxis();
            double d1 = direction$axis == Direction.Axis.X ? 0.5D + 0.5625D * (double)direction.getStepX() : (double)random.nextFloat();
            double d2 = direction$axis == Direction.Axis.Y ? 0.5D + 0.5625D * (double)direction.getStepY() : (double)random.nextFloat();
            double d3 = direction$axis == Direction.Axis.Z ? 0.5D + 0.5625D * (double)direction.getStepZ() : (double)random.nextFloat();
            float rng = (float)random.nextInt(8)/10f;
            DustParticleOptions dustParticleOptions = new DustParticleOptions(new Vector3f(rng, rng, rng), 1.0F);
            pLevel.addParticle(dustParticleOptions, (double)pPos.getX() + d1, (double)pPos.getY() + d2, (double)pPos.getZ() + d3, 0.0D, 0.0D, 0.0D);
        }
    }

    private void place(Level level, BlockPos pos) {
        level.setBlock(pos, defaultBlockState().setValue(BlockStateProperties.HORIZONTAL_FACING, Direction.NORTH), 3);
        level.setBlock(pos.subtract(offset.get(Direction.SOUTH)), defaultBlockState().setValue(BlockStateProperties.HORIZONTAL_FACING, Direction.SOUTH), 3);
        level.setBlock(pos.subtract(offset.get(Direction.EAST)), defaultBlockState().setValue(BlockStateProperties.HORIZONTAL_FACING, Direction.EAST), 3);
        level.setBlock(pos.subtract(offset.get(Direction.WEST)), defaultBlockState().setValue(BlockStateProperties.HORIZONTAL_FACING, Direction.WEST), 3);
    }
    private void destroy(Level level, BlockPos pos) {
        level.setBlock(pos, Blocks.AIR.defaultBlockState(), 3);
        level.setBlock(pos.subtract(offset.get(Direction.SOUTH)), Blocks.AIR.defaultBlockState(), 3);
        level.setBlock(pos.subtract(offset.get(Direction.EAST)), Blocks.AIR.defaultBlockState(), 3);
        level.setBlock(pos.subtract(offset.get(Direction.WEST)), Blocks.AIR.defaultBlockState(), 3);
    }
    private boolean canBePlaced(Level level, BlockPos pos) {
        boolean a = level.getBlockState(pos).isAir() &&
                level.getBlockState(pos.subtract(offset.get(Direction.SOUTH))).isAir() &&
                level.getBlockState(pos.subtract(offset.get(Direction.EAST))).isAir() &&
                level.getBlockState(pos.subtract(offset.get(Direction.WEST))).isAir();
        boolean b = level.getBlockState(pos.below()).getTags().toList().contains(Tags.Blocks.ORES) ||
                level.getBlockState(pos.subtract(offset.get(Direction.SOUTH)).below()).getTags().toList().contains(Tags.Blocks.ORES) ||
                level.getBlockState(pos.subtract(offset.get(Direction.EAST)).below()).getTags().toList().contains(Tags.Blocks.ORES) ||
                level.getBlockState(pos.subtract(offset.get(Direction.WEST)).below()).getTags().toList().contains(Tags.Blocks.ORES);
        return a && b;
    }
}
