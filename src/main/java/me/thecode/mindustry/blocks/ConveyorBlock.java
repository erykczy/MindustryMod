package me.thecode.mindustry.blocks;

import me.thecode.mindustry.ConveyorShape;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.level.material.PushReaction;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.lwjgl.system.CallbackI;
aaaaaabbbbcccc
public class ConveyorBlock extends Block {
    public static final EnumProperty<ConveyorShape> SHAPE = EnumProperty.create("shape", ConveyorShape.class);;

    public ConveyorBlock() {
        super(Properties.of(Material.METAL).strength(3.5F).sound(SoundType.METAL));
        registerDefaultState(defaultBlockState().setValue(SHAPE, ConveyorShape.N));
    }
    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> pBuilder) {
        pBuilder.add(SHAPE);
    }
    @Override
    public PushReaction getPistonPushReaction(BlockState pState) {
        return PushReaction.BLOCK;
    }

    public BlockState getStateForPlacement(BlockPlaceContext pContext) {
        return this.defaultBlockState().setValue(SHAPE, ConveyorShape.getShapeByHorizontalDirection(pContext.getHorizontalDirection()));
    }
    @Override
    public VoxelShape getShape(BlockState pState, BlockGetter pLevel, BlockPos pPos, CollisionContext pContext) {
        return Block.box(0, 0, 0, 16, 7, 16);
    }
    public void neighborChanged(BlockState pState, Level pLevel, BlockPos pPos, Block pBlock, BlockPos pFromPos, boolean pIsMoving) {
        BlockState neighborBlockState = pLevel.getBlockState(pFromPos);
        Block neighborBlock = neighborBlockState.getBlock();
        ConveyorShape neighborShape = neighborBlockState.getValue(SHAPE);
        if (!neighborBlock.equals(this)) {
            return;
        }
        ConveyorShape shape = pState.getValue(SHAPE);
        if(shape.isStraight()) {
            Direction direction = shape.getDirection();
            if(neighborShape.isStraight()) {
                Direction neighborDirection = neighborShape.getDirection();
                if(direction.getClockWise().equals(neighborDirection) || direction.getCounterClockWise().equals(neighborDirection)) {
                    //TODO
                }
            }
        }

    }
}
