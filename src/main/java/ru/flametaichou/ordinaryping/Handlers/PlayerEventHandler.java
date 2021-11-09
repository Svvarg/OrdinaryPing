package ru.flametaichou.ordinaryping.Handlers;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import ru.flametaichou.ordinaryping.OrdinaryPing;

public class PlayerEventHandler {

    //ClientSide
    @SubscribeEvent
    public void onPlayerJoin(EntityJoinWorldEvent event) {
        if (event.world.isRemote) {
            if (event.entity instanceof EntityPlayer && ((EntityPlayer) event.entity).getDisplayName().equals(Minecraft.getMinecraft().thePlayer.getDisplayName())) {
                OrdinaryPing.instance.clearPings();
            }
        }
    }
}
