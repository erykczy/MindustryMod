package me.thecode.mindustry.items;

import me.thecode.mindustry.blocks.ModBlocks;
import me.thecode.mindustry.creative_tabs.ModCreativeTabs;
import net.minecraft.world.item.BlockItem;

public class ConveyorBlockItem extends BlockItem {
    public ConveyorBlockItem() {
        super(ModBlocks.CONVEYOR.get(), new Properties().tab(ModCreativeTabs.MINDUSTRY_TAB));
    }
}
