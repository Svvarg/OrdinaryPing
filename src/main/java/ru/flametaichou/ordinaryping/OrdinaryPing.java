package ru.flametaichou.ordinaryping;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.network.FMLEventChannel;
import cpw.mods.fml.common.network.NetworkRegistry;
import net.minecraft.client.Minecraft;
import net.minecraftforge.common.MinecraftForge;
import ru.flametaichou.ordinaryping.Handlers.*;


@Mod(modid = OrdinaryPing.ID, name = "Ordinary Ping", version = "1.0", acceptableRemoteVersions = "*")
public final class OrdinaryPing {

    public final static String ID = "ordinaryping";

    @Instance(value = ID)
    public static OrdinaryPing instance;

    @SidedProxy(clientSide = "ru.flametaichou.ordinaryping.OrdinaryPingClientProxy", serverSide = "ru.flametaichou.ordinaryping.OrdinaryPingProxy")
    public static OrdinaryPingProxy proxy;

    public static FMLEventChannel pingChannel;

    private Long ping = -1L;
    private Integer fps = 0;

    @EventHandler
    public void load(FMLInitializationEvent event) {
        MinecraftForge.EVENT_BUS.register(new PlayerEventHandler());
        PingPacketHandler sk = new PingPacketHandler();
        pingChannel = NetworkRegistry.INSTANCE.newEventDrivenChannel(PacketChannel.PING.name());
        pingChannel.register(sk);
        proxy.registerGui();
    }

    @EventHandler
    public void load(FMLPreInitializationEvent event) {
        FMLCommonHandler.instance().bus().register(FMLEventHandler.INSTANCE);
    }

    public void clearPings() {
        ping = -1L;
    }

    public Long getPing() {
        return ping;
    }

    public void setPing(Long ping) {
        this.ping = ping;
    }

    public void setFps() {
        try {
            this.fps = Integer.parseInt(Minecraft.getMinecraft().debug.split(" ")[0]);
        } catch (Exception e) {
            this.fps = 0;
        }
    }

    public Integer getFps() {
        return fps;
    }
}
