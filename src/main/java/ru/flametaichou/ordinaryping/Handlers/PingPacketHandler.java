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

    private static long latestPingTime = 0;

    @SubscribeEvent
    public void onClientPacket(FMLNetworkEvent.ClientCustomPacketEvent event) {
        if (event.packet.channel().equals(PacketChannel.PING.name())) {
            handleFactPacketFromServer(event.packet, OrdinaryPing.proxy.getPlayer());
        }
    }

    public static void handleFactPacketFromServer(FMLProxyPacket packet, EntityPlayer player) {
        ByteBuf buf = packet.payload();
        String packetString = "";
        for (byte bt : buf.array()) {
            if (bt != 0)
                packetString += (char) bt;
        }

        long now = Minecraft.getSystemTime();
        if (latestPingTime != 0) {
            OrdinaryPing.instance.setLagg(now - (latestPingTime + OrdinaryPing.pingInterval));
        }
        latestPingTime = now;

        OrdinaryPing.instance.setPing(Long.parseLong(packetString));
    }

    public static FMLProxyPacket getStringPacket(Side side, String data) {
        char[] dat = data.toCharArray();
        ByteBuf buf = Unpooled.buffer();
        for (int i = 0; i < dat.length; i++) {
            buf.writeByte(dat[i]);
        }
        FMLProxyPacket pkt = new FMLProxyPacket(buf, PacketChannel.PING.name());
        pkt.setTarget(side);
        return pkt;
    }
}
