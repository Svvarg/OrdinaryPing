package ru.flametaichou.ordinaryping;

import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.common.FMLCommonHandler;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.common.MinecraftForge;

import ru.flametaichou.ordinaryping.Handlers.FMLEventHandlerClient;
import ru.flametaichou.ordinaryping.gui.OrdinaryPingHUD;
import ru.flametaichou.ordinaryping.time.ServerTime;

@SuppressWarnings("UnusedDeclaration")
public final class OrdinaryPingClientProxy extends OrdinaryPingProxy {

    @Override
    public void registerGui() {
        MinecraftForge.EVENT_BUS.register(OrdinaryPingHUD.INSTANCE);
    }

    @Override
    public EntityPlayer getPlayer() {
        return FMLClientHandler.instance().getClient().thePlayer;
    }

    @Override
    public void registerFMLEventHandler() {
        OrdinaryPing.systime = new ServerTime();
        FMLCommonHandler.instance().bus().register(FMLEventHandlerClient.INSTANCE);
    }

}
