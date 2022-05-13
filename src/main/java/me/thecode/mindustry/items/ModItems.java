package me.thecode.mindustry.items;

import me.thecode.mindustry.Mindustry;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModItems {
    private static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, Mindustry.MODID);

    public static void register(IEventBus bus) {
        ITEMS.register(bus);
    }

    public static final RegistryObject<BlockItem> MECHANICAL_DRILL = ITEMS.register("mechanical_drill", MechanicalDrillBlockItem::new);
    public static final RegistryObject<BlockItem> CONVEYOR = ITEMS.register("conveyor", ConveyorBlockItem::new);
}
