package wehavecookies56.bonfires.client;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.fml.DistExecutor;
import org.apache.commons.lang3.text.WordUtils;
import wehavecookies56.bonfires.Bonfires;
import wehavecookies56.bonfires.LocalStrings;
import wehavecookies56.bonfires.bonfire.Bonfire;
import wehavecookies56.bonfires.client.gui.BonfireScreen;
import wehavecookies56.bonfires.client.gui.CreateBonfireScreen;
import wehavecookies56.bonfires.data.BonfireHandler;
import wehavecookies56.bonfires.data.EstusHandler;
import wehavecookies56.bonfires.data.ReinforceHandler;
import wehavecookies56.bonfires.packets.client.DisplayTitle;
import wehavecookies56.bonfires.packets.client.OpenBonfireGUI;
import wehavecookies56.bonfires.packets.client.SendDimensionsToClient;
import wehavecookies56.bonfires.packets.client.SyncBonfire;
import wehavecookies56.bonfires.packets.client.SyncReinforceData;
import wehavecookies56.bonfires.packets.client.SyncSaveData;
import wehavecookies56.bonfires.tiles.BonfireTileEntity;

import java.util.UUID;

/**
 * Created by Toby on 07/11/2016.
 */
public class ClientPacketHandler {

    public static DistExecutor.SafeRunnable openBonfire(OpenBonfireGUI packet) {
        return new DistExecutor.SafeRunnable() {
            @Override
            public void run() {
                Minecraft.getInstance().setScreen(new BonfireScreen((BonfireTileEntity) Minecraft.getInstance().level.getBlockEntity(packet.tileEntity), packet.ownerName, packet.dimensions, packet.registry, packet.canReinforce));
            }
        };
    }

    public static DistExecutor.SafeRunnable setDimensionsFromServer(SendDimensionsToClient packet) {
        return new DistExecutor.SafeRunnable() {
            @Override
            public void run() {
                if (Minecraft.getInstance().screen instanceof BonfireScreen) {
                    BonfireScreen gui = (BonfireScreen) Minecraft.getInstance().screen;
                    gui.updateDimensionsFromServer(packet.dimensions);
                } else {
                    Bonfires.LOGGER.debug("Bonfire GUI not open when Dimensions requested");
                }
            }
        };
    }

    public static DistExecutor.SafeRunnable openCreateScreen(BonfireTileEntity te) {
        return new DistExecutor.SafeRunnable() {
            @Override
            public void run() {
                Minecraft.getInstance().setScreen(new CreateBonfireScreen(te));
            }
        };
    }

    public static DistExecutor.SafeRunnable displayTitle(DisplayTitle packet) {
        return new DistExecutor.SafeRunnable() {
            @Override
            public void run() {
                Gui gui = Minecraft.getInstance().gui;
                gui.setTitle(Component.translatable(packet.title));
                gui.setSubtitle(Component.translatable(packet.subtitle));
                gui.setTimes(packet.fadein, packet.stay, packet.fadeout);
            }
        };
    }

    public static DistExecutor.SafeRunnable syncBonfire(SyncBonfire packet) {
        return new DistExecutor.SafeRunnable() {
            @Override
            public void run() {
                BlockPos pos = new BlockPos(packet.x, packet.y, packet.z);
                Level level = Minecraft.getInstance().level;
                if (level.getBlockEntity(pos) != null && level.getBlockEntity(pos) instanceof BonfireTileEntity) {
                    BonfireTileEntity te = (BonfireTileEntity) level.getBlockEntity(pos);
                    if (te != null) {
                        te.setBonfire(packet.bonfire);
                        te.setBonfireType(packet.type);
                        te.setLit(packet.lit);
                        if (packet.lit)
                            te.setID(packet.id);
                    }
                }
            }
        };
    }

    public static DistExecutor.SafeRunnable syncReinforceData(SyncReinforceData packet) {
        return new DistExecutor.SafeRunnable() {
            @Override
            public void run() {
                ItemStack stack = Minecraft.getInstance().player.getInventory().getItem(packet.slot);
                ReinforceHandler.IReinforceHandler handler = ReinforceHandler.getHandler(stack);
                handler.setMaxLevel(packet.maxLevel);
                handler.setLevel(packet.level);
                Minecraft.getInstance().player.getInventory().setItem(packet.slot, stack);
            }
        };
    }

    public static DistExecutor.SafeRunnable syncSaveData(SyncSaveData packet) {
        return new DistExecutor.SafeRunnable() {
            @Override
            public void run() {
                BonfireHandler.getHandler(Minecraft.getInstance().level).getRegistry().setBonfires(packet.bonfires);
            }
        };
    }

    public static DistExecutor.SafeRunnable syncEstusData(UUID lastRested, int uses) {
        return new DistExecutor.SafeRunnable() {
            @Override
            public void run() {
                EstusHandler.IEstusHandler handler = EstusHandler.getHandler(Minecraft.getInstance().player);
                handler.setLastRested(lastRested);
                handler.setUses(uses);
            }
        };
    }

    public static DistExecutor.SafeRunnable displayBonfireTravelled(Bonfire bonfire) {
        return new DistExecutor.SafeRunnable() {
            @Override
            public void run() {
                String formattedDimName;
                if (I18n.exists(LocalStrings.getDimensionKey(bonfire.getDimension()))) {
                    String dimName = (bonfire.getDimension().location().getPath().replaceAll("_", " "));
                    formattedDimName = WordUtils.capitalizeFully(dimName);
                } else {
                    formattedDimName = I18n.get(LocalStrings.getDimensionKey(bonfire.getDimension()));
                }
                Gui gui = Minecraft.getInstance().gui;
                gui.setTitle(Component.translatable(bonfire.getName()));
                gui.setSubtitle(Component.translatable(formattedDimName));
                gui.setTimes(10, 20, 10);
            }
        };
    }

}
