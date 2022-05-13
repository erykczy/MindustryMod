package me.thecode.mindustry;

import net.minecraft.core.Direction;
import net.minecraft.util.StringRepresentable;

public enum ConveyorShape implements StringRepresentable {
    N("n"),
    W("w"),
    E("e"),
    S("s"),

    N_W("n_w"),
    N_E("n_e"),
    S_W("s_w"),
    S_E("s_e"),
    W_S("w_s"),
    W_N("w_n"),
    E_N("e_n"),
    E_S("e_s"),

    EN("en"),
    ES("es"),
    WN("wn"),
    WS("ws"),
    NE("ne"),
    NW("nw"),
    SE("se"),
    SW("sw"),

    ENS("ens"),
    WNS("wns"),
    NEW("new"),
    SEW("sew"),

    N_U("n_u"),
    W_U("w_u"),
    E_U("e_u"),
    S_U("s_u")
    ;
    private final String name;

    ConveyorShape(String name) {
        this.name = name;
    }

    @Override
    public String getSerializedName() {
        return this.name;
    }
    public static ConveyorShape getShapeByHorizontalDirection(Direction direction) {
        switch (direction) {
            case NORTH -> {
                return N;
            }
            case SOUTH -> {
                return S;
            }
            case WEST -> {
                return W;
            }
            case EAST -> {
                return E;
            }
        }
        return null;
    }
    public Direction getDirection() {
        switch (this) {
            case N -> {
                return Direction.NORTH;
            }
            case S -> {
                return Direction.SOUTH;
            }
            case W -> {
                return Direction.WEST;
            }
            case E -> {
                return Direction.EAST;
            }
        }
        return null;
    }
    public boolean isStraight() {
        return
                this.equals(N) ||
                this.equals(S) ||
                this.equals(E) ||
                this.equals(W);
    }
}
