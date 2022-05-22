package me.thecode.mindustry.events;

import me.thecode.mindustry.Mindustry;
import me.thecode.mindustry.blocks.ModBlocks;
import me.thecode.mindustry.renderers.block.ConveyorBER;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = Mindustry.MODID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ClientModEvents {
    @SubscribeEvent
    public static void registerRenderers(EntityRenderersEvent.RegisterRenderers event) {
        event.registerBlockEntityRenderer(ModBlocks.CONVEYOR_BLOCK_ENTITY.get(), ConveyorBER::new);
    }
}
