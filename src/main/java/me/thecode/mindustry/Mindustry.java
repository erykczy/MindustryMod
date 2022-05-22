package me.thecode.mindustry;

import com.mojang.logging.LogUtils;
import me.thecode.mindustry.blocks.ModBlocks;
import me.thecode.mindustry.blocks.block_entities.ConveyorBlockEntity;
import me.thecode.mindustry.items.ModItems;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.slf4j.Logger;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

// The value here should match an entry in the META-INF/mods.toml file
@Mod(Mindustry.MODID)
public class Mindustry
{
    // Directly reference a slf4j logger
    public static final String MODID = "mindustry";
    public static final Logger LOGGER = LogUtils.getLogger();
    public static final Random random = new Random();

    public Mindustry()
    {
        IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();
        ModBlocks.register(bus);
        ModItems.register(bus);

        MinecraftForge.EVENT_BUS.register(this);
    }
}
