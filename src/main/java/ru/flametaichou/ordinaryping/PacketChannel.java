package ru.flametaichou.ordinaryping;

public enum PacketChannel {

    PING(3);

    private final int id;

    private PacketChannel(int id) {
        this.id = id;
    }

    public int getId() {
        return this.id;
    }
}
