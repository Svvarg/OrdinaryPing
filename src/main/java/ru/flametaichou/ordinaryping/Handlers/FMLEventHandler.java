package ru.flametaichou.ordinaryping.Handlers;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent;
import cpw.mods.fml.common.network.internal.FMLProxyPacket;
import cpw.mods.fml.relauncher.Side;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import ru.flametaichou.ordinaryping.OrdinaryPing;

public final class FMLEventHandler {

    public static final FMLEventHandler INSTANCE = new FMLEventHandler();
    
    @SubscribeEvent
    public void onPlayerUpdate(TickEvent.PlayerTickEvent event) {
        if (event.side.isClient()) {
            if (event.phase == TickEvent.Phase.START) {
                EntityPlayer player = event.player;
                if (player.worldObj.getTotalWorldTime() % 40 == 0) {
                    OrdinaryPing.instance.setFps();
                }
            }
        } else {
            if (event.phase == TickEvent.Phase.START) {
                EntityPlayer player = event.player;
                if (player.worldObj.getTotalWorldTime() % 40 == 0) {

                    System.out.println(MinecraftServer.getServer().getServerModName());
                    System.out.println(MinecraftServer.getServer().getWorldName());


                    EntityPlayerMP entityPlayerMP = (EntityPlayerMP) player;
                    String ping = String.valueOf(entityPlayerMP.ping);

                    FMLProxyPacket packet = PingPacketHandler.getStringPacket(Side.CLIENT, ping);
                    OrdinaryPing.pingChannel.sendTo(packet, entityPlayerMP);
                }
            }
        }
    }

}
