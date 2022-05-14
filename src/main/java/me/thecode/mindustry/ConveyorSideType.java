package me.thecode.mindustry;

import net.minecraft.util.StringRepresentable;

public enum ConveyorSideType implements StringRepresentable {
    NONE("none"),
    INPUT("input"),
    OUTPUT("output")
    ;
    private final String name;

    ConveyorSideType(String name) {
        this.name = name;
    }

    @Override
    public String getSerializedName() {
        return this.name;
    }
}
