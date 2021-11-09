package ru.flametaichou.ordinaryping;

import ru.flametaichou.ordinaryping.time.ServerTime;
import ru.flametaichou.ordinaryping.time.DedicatedServerTime;
import cpw.mods.fml.common.FMLCommonHandler;
import net.minecraft.entity.player.EntityPlayer;
import ru.flametaichou.ordinaryping.Handlers.FMLEventHandlerServer;

public class OrdinaryPingProxy {

    public void tryUseMUD() {
    }

    public void registerGui() {
    }

    public EntityPlayer getPlayer() {
        return null;
    }

    public void registerFMLEventHandler() {
        FMLCommonHandler.instance().bus().register(FMLEventHandlerServer.INSTANCE);
        ServerTime st;
        try {
            //check dependencies for integratedServer (singlePlay)
            Class.forName("org.swarg.mcforge.common.SysTimeHandler");
            st = new DedicatedServerTime();
        }
        catch (Throwable t) {
            st = new ServerTime();
        }
        OrdinaryPing.systime = st;
    }
}
