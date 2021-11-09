package ru.flametaichou.ordinaryping.Handlers;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.network.FMLNetworkEvent;
import cpw.mods.fml.common.network.internal.FMLProxyPacket;
import cpw.mods.fml.relauncher.Side;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import ru.flametaichou.ordinaryping.*;

public final class PingPacketHandler {

    /**
     * Make Ping packet for ClientSide
     * @param side
     * @param data
     * @return 
     */
    public static FMLProxyPacket getLongPacket(Side side, long data) {
        ByteBuf buf = Unpooled.buffer(2+8);
        buf.writeByte(1);   //reserve
        buf.writeByte(2);   //reserve
        buf.writeLong(data);
        FMLProxyPacket pkt = new FMLProxyPacket(buf, PacketChannel.PING.name());
        pkt.setTarget(side);
        return pkt;
    }


    @SubscribeEvent
    public void onClientPacket(FMLNetworkEvent.ClientCustomPacketEvent event) {
        if (event.packet.channel().equals(PacketChannel.PING.name())) {
            handleFactPacketFromServer(event.packet, OrdinaryPing.proxy.getPlayer());
        }
    }

    /**
     * ClientSide
     * @param packet
     * @param player
     */
    public static void handleFactPacketFromServer(FMLProxyPacket packet, EntityPlayer player) {
        ByteBuf buf = packet.payload();

        final byte b0 = buf.readByte(); //1 Reserve
        final byte b1 = buf.readByte(); //2 Reserve
        final long data = buf.readLong();

        final OrdinaryPing ping = OrdinaryPing.instance;
        long now = Minecraft.getSystemTime();
        if (ping.getLatestPingTime() != 0) {
            ping.setLagg(now - (ping.getLatestPingTime() + OrdinaryPing.pingInterval));
        }

        ping.setLatestPingTime(now);
        ping.setPing(data);
    }
    
}
