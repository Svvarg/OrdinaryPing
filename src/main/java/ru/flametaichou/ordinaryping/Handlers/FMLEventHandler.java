package ru.flametaichou.ordinaryping.Handlers;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent;
import cpw.mods.fml.common.network.internal.FMLProxyPacket;
import cpw.mods.fml.relauncher.Side;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import ru.flametaichou.ordinaryping.OrdinaryPing;

public final class FMLEventHandler {

    private static long latestFpsUpdate = 0;
    private static long latestPingSend = 0;

    public static final FMLEventHandler INSTANCE = new FMLEventHandler();
    
    @SubscribeEvent
    public void onPlayerUpdate(TickEvent.PlayerTickEvent event) {
        if (event.phase == TickEvent.Phase.START) {
            if (event.side.isClient()) {
                long now = Minecraft.getSystemTime();
                if (now - latestFpsUpdate >= OrdinaryPing.pingInterval) {
                    latestFpsUpdate = now;
                    OrdinaryPing.instance.setFps();
                }
            } else {

                EntityPlayer player = event.player;
                long now = MinecraftServer.getSystemTimeMillis();
                if (now - latestPingSend >= OrdinaryPing.pingInterval) {
                    latestPingSend = now;

                    EntityPlayerMP entityPlayerMP = (EntityPlayerMP) player;
                    String ping = String.valueOf(entityPlayerMP.ping);

                    FMLProxyPacket packet = PingPacketHandler.getStringPacket(Side.CLIENT, ping);
                    OrdinaryPing.pingChannel.sendTo(packet, entityPlayerMP);
                }
            }
        }
    }
}
