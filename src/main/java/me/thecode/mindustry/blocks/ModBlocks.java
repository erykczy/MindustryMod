package me.thecode.mindustry.blocks;

import me.thecode.mindustry.Mindustry;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModBlocks {
    private static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, Mindustry.MODID);

    public static void register(IEventBus bus) {
        BLOCKS.register(bus);
    }

    public static final RegistryObject<Block> MECHANICAL_DRILL = BLOCKS.register("mechanical_drill", MechanicalDrillBlock::new);
}
