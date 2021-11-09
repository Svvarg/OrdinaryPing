package ru.flametaichou.ordinaryping.Handlers;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent;
import net.minecraft.client.Minecraft;
import ru.flametaichou.ordinaryping.OrdinaryPing;

public final class FMLEventHandlerClient {

    public static final FMLEventHandlerClient INSTANCE = new FMLEventHandlerClient();
    private static long nextFpsUpdate = 0;

    
    @SubscribeEvent
    public void onPlayerUpdate(TickEvent.PlayerTickEvent event) {
        if (event.phase == TickEvent.Phase.START) {
            if (event.side.isClient()) {
                long now = Minecraft.getSystemTime();
                if (now >= nextFpsUpdate) {
                    nextFpsUpdate = now + OrdinaryPing.pingInterval;
                    OrdinaryPing.instance.setFps(parseFps());
                }
            }
        }
    }

    public int parseFps() {
        try {
            //this.fps = Integer.parseInt(Minecraft.getMinecraft().debug.split(" ")[0]);
            final String d = Minecraft.getMinecraft().debug;
            final String sfps = d.substring(0, d.indexOf(' '));
            return Integer.parseInt( sfps );
        } catch (Exception e) {
            return 0;
        }
    }

}
 
