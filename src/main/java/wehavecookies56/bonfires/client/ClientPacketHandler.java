package wehavecookies56.bonfires.client;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import org.apache.commons.lang3.text.WordUtils;
import wehavecookies56.bonfires.Bonfires;
import wehavecookies56.bonfires.BonfiresConfig;
import wehavecookies56.bonfires.LocalStrings;
import wehavecookies56.bonfires.bonfire.Bonfire;
import wehavecookies56.bonfires.client.gui.BonfireScreen;
import wehavecookies56.bonfires.client.gui.CreateBonfireScreen;
import wehavecookies56.bonfires.data.EstusHandler;
import wehavecookies56.bonfires.packets.client.DisplayTitle;
import wehavecookies56.bonfires.packets.client.OpenBonfireGUI;
import wehavecookies56.bonfires.packets.client.SendBonfiresToClient;
import wehavecookies56.bonfires.packets.client.SyncBonfire;
import wehavecookies56.bonfires.tiles.BonfireTileEntity;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

/**
 * Created by Toby on 07/11/2016.
 */
public class ClientPacketHandler {

    public static void openBonfire(OpenBonfireGUI packet) {
        Minecraft.getInstance().setScreen(new BonfireScreen((BonfireTileEntity) Minecraft.getInstance().level.getBlockEntity(packet.tileEntity), packet.ownerName, packet.dimensions.stream().filter(dim -> !BonfiresConfig.Client.hiddenDimensions.contains(dim.location().toString())).toList(), packet.registry, packet.canReinforce));

    }

    public static void setBonfiresFromServer(SendBonfiresToClient packet) {
        if (Minecraft.getInstance().screen != null) {
            if (Minecraft.getInstance().screen instanceof BonfireScreen gui) {
                gui.updateDimensionsFromServer(packet.registry, packet.dimensions.stream().filter(dim -> !BonfiresConfig.Client.hiddenDimensions.contains(dim.location().toString())).toList());
            }
        }
    }

    public static void openCreateScreen(BonfireTileEntity te) {
        Minecraft.getInstance().setScreen(new CreateBonfireScreen(te));
    }

    public static void displayTitle(DisplayTitle packet) {
        Gui gui = Minecraft.getInstance().gui;
        gui.setTitle(Component.translatable(packet.title));
        gui.setSubtitle(Component.translatable(packet.subtitle));
        gui.setTimes(packet.fadein, packet.stay, packet.fadeout);
    }

    public static void syncBonfire(SyncBonfire packet) {
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

    public static void syncEstusData(UUID lastRested) {
        EstusHandler.EstusHandlerInstance handler = EstusHandler.getHandler(Minecraft.getInstance().player);
        handler.setLastRested(lastRested);
    }

    public static void displayBonfireTravelled(Bonfire bonfire) {
        Player player = Minecraft.getInstance().player;
        player.level().playSound(player, player.blockPosition(), SoundEvents.ENDERMAN_TELEPORT, SoundSource.PLAYERS, 1, 1);
        player.level().playSound(player, bonfire.getPos(), SoundEvents.ENDERMAN_TELEPORT, SoundSource.PLAYERS, 1, 1);
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

    public static void queueBonfireScreenshot(String name, UUID uuid) {
        ScreenshotUtils.startScreenshotTimer(name, uuid);
    }

    public static void deleteScreenshot(UUID uuid, String name) {
        if (BonfiresConfig.Client.deleteScreenshotsOnDestroyed) {
            Path screenshotsDir = Paths.get(Minecraft.getInstance().gameDirectory.getPath(), "bonfires/");
            String fileName = ScreenshotUtils.getFileNameString(name, uuid);
            File screenshotFile = new File(screenshotsDir.toFile(), fileName);
            if (screenshotFile.exists() && screenshotFile.isFile()) {
                String path = screenshotFile.getPath();
                if (!screenshotFile.delete()) {
                    Bonfires.LOGGER.warn("Failed to delete screenshot file " + path);
                } else {
                    Bonfires.LOGGER.info("Deleted screenshot for destroyed bonfire " + fileName);
                }
            }
        }
    }
}
