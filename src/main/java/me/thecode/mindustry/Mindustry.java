package me.thecode.mindustry;

import com.mojang.logging.LogUtils;
import me.thecode.mindustry.blocks.ModBlocks;
import me.thecode.mindustry.items.ModItems;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.InterModComms;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.lifecycle.InterModEnqueueEvent;
import net.minecraftforge.fml.event.lifecycle.InterModProcessEvent;
import net.minecraftforge.event.server.ServerStartingEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.slf4j.Logger;

import java.util.stream.Collectors;

// The value here should match an entry in the META-INF/mods.toml file
@Mod(Mindustry.MODID)
public class Mindustry
{
    // Directly reference a slf4j logger
    public static final String MODID = "mindustry";
    private static final Logger LOGGER = LogUtils.getLogger();

    public Mindustry()
    {
        IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();
        ModBlocks.register(bus);
        ModItems.register(bus);

        MinecraftForge.EVENT_BUS.register(this);
    }
}
