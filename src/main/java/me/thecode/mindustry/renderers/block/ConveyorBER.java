package me.thecode.mindustry.renderers.block;

import com.mojang.blaze3d.vertex.PoseStack;
import me.thecode.mindustry.ConveyorItem;
import me.thecode.mindustry.blocks.block_entities.ConveyorBlockEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;

public class ConveyorBER implements BlockEntityRenderer<ConveyorBlockEntity> {
    private final BlockEntityRendererProvider.Context context;

    public ConveyorBER(BlockEntityRendererProvider.Context context) {
        this.context = context;
    }
    @Override
    public void render(ConveyorBlockEntity pBlockEntity, float pPartialTick, PoseStack stack, MultiBufferSource pBufferSource, int pPackedLight, int pPackedOverlay) {
        for(ConveyorItem conveyorItem : pBlockEntity.getConveyorItems()) {
            stack.pushPose();
            stack.translate(conveyorItem.pos.x, 0.453F, conveyorItem.pos.z);
            stack.scale(0.5F, 0.5F, 0.5F);
            stack.mulPose(conveyorItem.rotation);
            Minecraft.getInstance().getItemRenderer().renderStatic(Minecraft.getInstance().player, conveyorItem.itemStack, ItemTransforms.TransformType.FIXED, false, stack, pBufferSource, Minecraft.getInstance().level, pPackedLight, pPackedOverlay, 0);
            stack.popPose();
        }
    }
}
