package me.thecode.mindustry.items;

import me.thecode.mindustry.blocks.ModBlocks;
import me.thecode.mindustry.creative_tabs.ModCreativeTabs;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;

public class MechanicalDrillBlockItem extends BlockItem {
    public MechanicalDrillBlockItem() {
        super(ModBlocks.MECHANICAL_DRILL.get(), new Item.Properties().tab(ModCreativeTabs.MINDUSTRY_TAB));
    }
}
