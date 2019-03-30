package ru.flametaichou.ordinaryping.Handlers;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.network.FMLNetworkEvent;
import cpw.mods.fml.common.network.internal.FMLProxyPacket;
import cpw.mods.fml.relauncher.Side;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.network.NetHandlerPlayServer;
import ru.flametaichou.ordinaryping.*;

import java.util.Date;

public final class PingPacketHandler {

    @SubscribeEvent
    public void onServerPacket(FMLNetworkEvent.ServerCustomPacketEvent event) {
        if (event.packet.channel().equals(PacketChannel.PINGSERVER.name())) {
            handleServerMessage(event.packet, ((NetHandlerPlayServer) event.handler).playerEntity);
        }
    }

    private void handleServerMessage(FMLProxyPacket packet, EntityPlayerMP entityPlayerMP) {
        FMLProxyPacket newPacket = PingPacketHandler.getEmptyPacketClient(Side.CLIENT);
        OrdinaryPing.pingChannel.sendTo(packet, entityPlayerMP);
    }

    @SubscribeEvent
    public void onClientPacket(FMLNetworkEvent.ClientCustomPacketEvent event) {
        if (event.packet.channel().equals(PacketChannel.PINGCLIENT.name())) {
            handlePacketFromClient(event.packet, OrdinaryPing.proxy.getPlayer());
        } else if (event.packet.channel().equals(PacketChannel.PINGSERVER.name())) {
            handlePacketFromServer(event.packet, OrdinaryPing.proxy.getPlayer());
        } else if (event.packet.channel().equals(PacketChannel.PINGFACT.name())) {
            handleFactPacketFromServer(event.packet, OrdinaryPing.proxy.getPlayer());
        }
    }

    private void handlePacketFromServer(FMLProxyPacket packet, EntityPlayer player) {
        Long timeSend = OrdinaryPing.instance.getLastTimeSendServer();
        Long timeReceive = new Date().getTime();
        OrdinaryPing.instance.setPing(timeReceive - timeSend - OrdinaryPing.instance.getPingClient());
        OrdinaryPing.instance.setLastTimeSendServer(0L);
    }

    public static void handlePacketFromClient(FMLProxyPacket packet, EntityPlayer player) {
        Long timeSend = OrdinaryPing.instance.getLastTimeSendClient();
        Long timeReceive = new Date().getTime();
        OrdinaryPing.instance.setPingClient(timeReceive - timeSend);
        OrdinaryPing.instance.setLastTimeSendClient(0L);
    }

    public static void handleFactPacketFromServer(FMLProxyPacket packet, EntityPlayer player) {
        ByteBuf buf = packet.payload();
        String packetString = "";
        for (byte bt : buf.array()) {
            if (bt != 0)
                packetString += (char) bt;
        }

        OrdinaryPing.instance.setPingFact(Long.parseLong(packetString));
    }

    public static FMLProxyPacket getEmptyPacketServer(Side side) {
        ByteBuf buf = Unpooled.buffer();
        FMLProxyPacket pkt = new FMLProxyPacket(buf, PacketChannel.PINGSERVER.name());
        pkt.setTarget(side);
        return pkt;
    }

    public static FMLProxyPacket getEmptyPacketClient(Side side) {
        ByteBuf buf = Unpooled.buffer();
        FMLProxyPacket pkt = new FMLProxyPacket(buf, PacketChannel.PINGCLIENT.name());
        pkt.setTarget(side);
        return pkt;
    }

    public static FMLProxyPacket getStringPacket(Side side, String data) {
        char[] dat = data.toCharArray();
        ByteBuf buf = Unpooled.buffer();
        for (int i = 0; i < dat.length; i++) {
            buf.writeByte(dat[i]);
        }
        FMLProxyPacket pkt = new FMLProxyPacket(buf, PacketChannel.PINGFACT.name());
        pkt.setTarget(side);
        return pkt;
    }
}
