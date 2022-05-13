package me.thecode.mindustry.blocks;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.material.Material;

public class MechanicalDrillBlock extends Block {
    public MechanicalDrillBlock() {
        super(Properties.of(Material.METAL).strength(3.5F).sound(SoundType.METAL));
    }

}
