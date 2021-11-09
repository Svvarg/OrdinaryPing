package ru.flametaichou.ordinaryping.Handlers;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.common.gameevent.TickEvent;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.network.internal.FMLProxyPacket;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.WorldServer;
import ru.flametaichou.ordinaryping.OrdinaryPing;

/**
 * 09-11-21
 * @author Swarg
 */
public class FMLEventHandlerServer {

    public static final FMLEventHandlerServer INSTANCE = new FMLEventHandlerServer();
    private static long nextPingSend = 0;


    @SubscribeEvent
    public void onServerTick(TickEvent.ServerTickEvent event) {
        if (event.phase == TickEvent.Phase.START) {
            if (event.side.isServer()) {

                //debugLagEmulation();

                /*DedicatedServerSide common SysTimeHandler (mcfbackendlib)
                  Use the system time updated at the beginning of each Server
                  tick. To reduce the frequency of requests to the system.
                  In order for it to work when there is no needed library (SinglePlay)*/
                long now = OrdinaryPing.systime.get();//ServerTime org.swarg.mcforge.common.SysTimeHandler.instance().getNowTimeMillis();
                //long now = MinecraftServer.getSystemTimeMillis();// => System.currentTimeMillis();

                if (now >= nextPingSend) {
                    sendPingToAllPlayers(now);
                }
            }
        }
    }


    /**
     *
     * @param now currentTimeMillis
     */
    private void sendPingToAllPlayers(long now) {
        nextPingSend = now + OrdinaryPing.pingInterval;

        for (WorldServer worldServer : MinecraftServer.getServer().worldServers) {
            for (Object playerObj : worldServer.playerEntities) {
                EntityPlayerMP player = (EntityPlayerMP) playerObj;
                //isRealPlayerCheck
                if (player.playerNetServerHandler != null) {
                    long ping = player.ping;

                    FMLProxyPacket packet = PingPacketHandler.getLongPacket(Side.CLIENT, ping);
                    OrdinaryPing.pingChannel.sendTo(packet, player);
                }
            }
        }
    }

    /**
     * Debug-Experimental
     */
    public void debugLagEmulation() {
        if (System.nanoTime() % 100 == 0 ) {
            try {
                Thread.sleep(5000);
            }
            catch (Exception e) {
            }
        }
    }

}
