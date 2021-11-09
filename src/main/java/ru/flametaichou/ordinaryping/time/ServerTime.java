package ru.flametaichou.ordinaryping.time;

import net.minecraft.server.MinecraftServer;

/**
 * For SinglePlay simply return System.CurrentTimeMillis via MCSrv
 * For Use with DedicatedServerTime
 * 09-11-21
 * @author Swarg
 */
public class ServerTime {
    public long get() {
        return MinecraftServer.getSystemTimeMillis();
    }
}
