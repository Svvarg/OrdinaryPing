package ru.flametaichou.ordinaryping;

public enum PacketChannel {

    PINGCLIENT(0),
    PINGSERVER(1),
    PINGFACT(3);

    private final int id;

    private PacketChannel(int id) {
        this.id = id;
    }

    public int getId() {
        return this.id;
    }
}
