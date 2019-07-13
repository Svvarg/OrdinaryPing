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

    public static FMLEventChannel pingChannel, pingChannelClient, pingChannelFact;

    private static final int pings_count = 5;

    private Long[] pings = new Long[pings_count];

    private Long ping = 0L;
    private Long pingClient = 0L;
    private Long pingFact = -1L;
    private Integer fps = 0;

    private Long lastTimeSendServer = 0L;
    private Long lastTimeSendClient = 0L;

    @EventHandler
    public void load(FMLInitializationEvent event) {
        MinecraftForge.EVENT_BUS.register(new PlayerEventHandler());
        PingPacketHandler sk = new PingPacketHandler();
        pingChannel = NetworkRegistry.INSTANCE.newEventDrivenChannel(PacketChannel.PINGSERVER.name());
        pingChannel.register(sk);
        pingChannelClient = NetworkRegistry.INSTANCE.newEventDrivenChannel(PacketChannel.PINGCLIENT.name());
        pingChannelClient.register(sk);
        pingChannelFact = NetworkRegistry.INSTANCE.newEventDrivenChannel(PacketChannel.PINGFACT.name());
        pingChannelFact.register(sk);
        proxy.registerGui();
    }

    @EventHandler
    public void load(FMLPreInitializationEvent event) {
        FMLCommonHandler.instance().bus().register(FMLEventHandler.INSTANCE);
    }

    public Long getPing() {
        return ping;
    }

    public void setPing(Long ping) {
        this.ping = ping;
        tossPing(ping);
    }

    public Long getLastTimeSendServer() {
        return lastTimeSendServer;
    }

    public void setLastTimeSendServer(Long lastTimeSendServer) {
        this.lastTimeSendServer = lastTimeSendServer;
    }

    public Long getLastTimeSendClient() {
        return lastTimeSendClient;
    }

    public void setLastTimeSendClient(Long lastTimeSendClient) {
        this.lastTimeSendClient = lastTimeSendClient;
    }

    public Long getPingClient() {
        return pingClient;
    }

    public void setPingClient(Long pingClient) {
        this.pingClient = pingClient;
    }

    public void clearPings() {
        for (Long ping : pings) {
            ping = null;
        }
        pingFact = -1L;
    }

    private void tossPing(Long newPing) {
        for (int i = pings.length-1; i > 0; i--) {
            pings[i] = pings[i - 1];
        }
        pings[0] = newPing;
    }

    public Long getEstablishedPing() {
        Long summ = 0L;
        for (Long ping : pings) {
            if (ping == null) {
                return -1L;
            } else {
                summ += ping;
            }
        }
        return summ / pings_count;
    }

    public Long getPingFact() {
        return pingFact;
    }

    public void setPingFact(Long pingFact) {
        this.pingFact = pingFact;
    }

    public void setFps() {
        int fps = 0;

        try {
            fps = Integer.parseInt(Minecraft.getMinecraft().debug.split(" ")[0]);
        } catch (Exception e) {
            fps = 0;
        }

        this.fps = fps;
    }

    public Integer getFps() {
        return fps;
    }
}
