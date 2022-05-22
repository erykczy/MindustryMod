package me.thecode.mindustry;

import com.mojang.math.Quaternion;
import me.thecode.mindustry.blocks.block_entities.ConveyorBlockEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class ConveyorItem {
    public static HashMap<ConveyorBlockEntity, List<ConveyorItem>> conveyorItems = new HashMap<>();
    public ItemStack itemStack;
    public boolean reachedMiddle = false;
    public Vec3 pos;
    public int time = 0;
    public Quaternion rotation = new Quaternion(90, 0, 0, true);

    public ConveyorItem(ItemStack itemStack, Vec3 pos) {
        this.itemStack = itemStack;
        this.pos = pos;
    }
    public ConveyorItem(ItemStack itemStack, Vec3 pos, int time, boolean reachedMiddle) {
        this.itemStack = itemStack;
        this.pos = pos;
        this.time = time;
        this.reachedMiddle = reachedMiddle;
    }
    public static void addNewConveyorItem(ConveyorBlockEntity blockEntity, ConveyorItem conveyorItem) {
        if(conveyorItems.containsKey(blockEntity)) {
            conveyorItems.get(blockEntity).add(conveyorItem);
        }
        else {
            conveyorItems.put(blockEntity, new ArrayList<>(Arrays.asList(conveyorItem)));
        }
    }
    public static void removeConveyorItem(ConveyorBlockEntity blockEntity, int conveyorItemIndex) {
        if(conveyorItems.containsKey(blockEntity)) {
            conveyorItems.get(blockEntity).remove(conveyorItemIndex);
        }
    }
    public static List<ConveyorItem> getConveyorItems(ConveyorBlockEntity blockEntity) {
        if(conveyorItems.get(blockEntity) == null) {
            return new ArrayList<>();
        }
        return conveyorItems.get(blockEntity);
    }
    public static List<ConveyorItem> getAllConveyorItems() {
        List<ConveyorItem> conveyorItems = new ArrayList<>();
        for(List<ConveyorItem> conveyorItemList : ConveyorItem.conveyorItems.values()) {
            conveyorItems.addAll(conveyorItemList);
        }
        return conveyorItems;
    }
}
