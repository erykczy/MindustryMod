package me.thecode.mindustry.blocks;

import me.thecode.mindustry.Mindustry;
import me.thecode.mindustry.blocks.block_entities.ConveyorBlockEntity;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModBlocks {
    private static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, Mindustry.MODID);
    private static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES = DeferredRegister.create(ForgeRegistries.BLOCK_ENTITIES, Mindustry.MODID);

    public static void register(IEventBus bus) {
        BLOCKS.register(bus);
        BLOCK_ENTITIES.register(bus);
    }

    //BLOCKS
    public static final RegistryObject<Block> MECHANICAL_DRILL = BLOCKS.register("mechanical_drill", MechanicalDrillBlock::new);
    public static final RegistryObject<Block> CONVEYOR = BLOCKS.register("conveyor", ConveyorBlock::new);
    //BLOCK ENTITIES
    public static final RegistryObject<BlockEntityType<ConveyorBlockEntity>> CONVEYOR_BLOCK_ENTITY = BLOCK_ENTITIES.register("conveyor", () -> BlockEntityType.Builder.of(ConveyorBlockEntity::new, CONVEYOR.get()).build(null));
}
