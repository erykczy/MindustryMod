package me.thecode.mindustry.creative_tabs;

import me.thecode.mindustry.items.ModItems;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;

public class ModCreativeTabs {
    public static final CreativeModeTab MINDUSTRY_TAB = new CreativeModeTab("mindustry_tab") {
        @Override
        public ItemStack makeIcon() {
            return new ItemStack(ModItems.MECHANICAL_DRILL.get());
        }
    };
}
