package ru.flametaichou.ordinaryping.Handlers;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent;
import cpw.mods.fml.common.network.internal.FMLProxyPacket;
import cpw.mods.fml.relauncher.Side;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import ru.flametaichou.ordinaryping.OrdinaryPing;

import java.util.Date;

public final class FMLEventHandler {

    public static final FMLEventHandler INSTANCE = new FMLEventHandler();
    
    @SubscribeEvent
    public void onPlayerUpdate(TickEvent.PlayerTickEvent event) {
        if (event.side.isClient()) {
            if (event.phase == TickEvent.Phase.START) {
                EntityPlayer player = event.player;
                if (player.worldObj.getTotalWorldTime() % 40 == 0) {
                    if (OrdinaryPing.instance.getLastTimeSendClient() == 0) {
                        FMLProxyPacket packet = PingPacketHandler.getEmptyPacketClient(Side.CLIENT);
                        OrdinaryPing.pingChannelClient.sendToAll(packet);
                        OrdinaryPing.instance.setLastTimeSendClient(new Date().getTime());
                    }
                    if (OrdinaryPing.instance.getLastTimeSendServer() == 0) {
                        FMLProxyPacket packet = PingPacketHandler.getEmptyPacketServer(Side.SERVER);
                        OrdinaryPing.pingChannel.sendToServer(packet);
                        OrdinaryPing.instance.setLastTimeSendServer(new Date().getTime());
                    }
                    OrdinaryPing.instance.setFps();
                }
            }
        } else {
            if (event.phase == TickEvent.Phase.START) {
                EntityPlayer player = event.player;
                if (player.worldObj.getTotalWorldTime() % 20 == 0) {

                    EntityPlayerMP entityPlayerMP = (EntityPlayerMP) player;
                    String ping = String.valueOf(entityPlayerMP.ping);

                    FMLProxyPacket packet = PingPacketHandler.getStringPacket(Side.CLIENT, ping);
                    OrdinaryPing.pingChannelFact.sendTo(packet, entityPlayerMP);
                }
            }
        }
    }

}
