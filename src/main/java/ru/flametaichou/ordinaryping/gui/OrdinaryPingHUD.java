package ru.flametaichou.ordinaryping.gui;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.StatCollector;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent.ElementType;
import ru.flametaichou.ordinaryping.OrdinaryPing;

import java.util.List;

public final class OrdinaryPingHUD extends Gui {

    public static final OrdinaryPingHUD INSTANCE = new OrdinaryPingHUD();
    private static final long laggTimeForWarning = 500;

    private void addToText(List<String> left) {
        left.add(
                getServerName() +
                " " +
                (OrdinaryPing.instance.getLagg() >= laggTimeForWarning ? (EnumChatFormatting.RED + StatCollector.translateToLocal("gui.lagg") +
                ": " + OrdinaryPing.instance.getLagg() + StatCollector.translateToLocal("gui.ms") + EnumChatFormatting.WHITE + " ") : "") +
                StatCollector.translateToLocal("gui.fps") +
                ": " +
                OrdinaryPing.instance.getFps() +
                ", " +
                StatCollector.translateToLocal("gui.ping") +
                ": " +
                (OrdinaryPing.instance.getPing() != -1L ? + OrdinaryPing.instance.getPing() + StatCollector.translateToLocal("gui.ms") : StatCollector.translateToLocal("gui.stabilizes"))
        );
    }

    private String getServerName() {
        if (Minecraft.getMinecraft().func_147104_D() != null) {
            return Minecraft.getMinecraft().func_147104_D().serverName;
        }
        return StatCollector.translateToLocal("gui.unknownServer");
    }

    @SubscribeEvent
    public void renderLvlUpHUD(RenderGameOverlayEvent.Pre event) {
        if (OrdinaryPing.proxy.getPlayer() != null) {
            if (event.type == ElementType.TEXT)
                addToText(((RenderGameOverlayEvent.Text)event).right);
        }
    }
}
