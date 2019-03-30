package ru.flametaichou.ordinaryping;

import cpw.mods.fml.client.FMLClientHandler;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.common.MinecraftForge;
import ru.flametaichou.ordinaryping.gui.OrdinaryPingHUD;

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
}
