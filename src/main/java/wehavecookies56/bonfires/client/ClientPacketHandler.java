package wehavecookies56.bonfires.client;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.client.resource.language.I18n;
import net.minecraft.text.Text;
import org.apache.commons.lang3.text.WordUtils;
import wehavecookies56.bonfires.Bonfires;
import wehavecookies56.bonfires.LocalStrings;
import wehavecookies56.bonfires.bonfire.Bonfire;
import wehavecookies56.bonfires.client.gui.BonfireScreen;
import wehavecookies56.bonfires.client.gui.CreateBonfireScreen;
import wehavecookies56.bonfires.data.EstusHandler;
import wehavecookies56.bonfires.packets.client.*;
import wehavecookies56.bonfires.tiles.BonfireTileEntity;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;
import java.util.UUID;

/**
 * Created by Toby on 07/11/2016.
 */
public class ClientPacketHandler {


    public static void openBonfire(OpenBonfireGUI packet) {
        MinecraftClient.getInstance().setScreen(new BonfireScreen((BonfireTileEntity) MinecraftClient.getInstance().world.getBlockEntity(packet.tileEntity), packet.ownerName, packet.dimensions.stream().filter(dim -> !Bonfires.CONFIG.client.hiddenDimensions().contains(dim.getValue().toString())).toList(), packet.registry, packet.canReinforce));
    }

    public static void setBonfiresFromServer(SendBonfiresToClient packet) {
        if (MinecraftClient.getInstance().currentScreen != null) {
            if (MinecraftClient.getInstance().currentScreen instanceof BonfireScreen gui) {
                gui.updateDimensionsFromServer(packet.registry, packet.dimensions.stream().filter(dim -> !Bonfires.CONFIG.client.hiddenDimensions().contains(dim.getValue().toString())).toList());
            }
        }
    }

    public static void init() {
        ClientPlayNetworking.registerGlobalReceiver(DeleteScreenshot.TYPE, (packet, player, responseSender) -> packet.handle());
        ClientPlayNetworking.registerGlobalReceiver(DisplayBonfireTitle.TYPE, (packet, player, responseSender) -> packet.handle());
        ClientPlayNetworking.registerGlobalReceiver(DisplayTitle.TYPE, (packet, player, responseSender) -> packet.handle());
        ClientPlayNetworking.registerGlobalReceiver(OpenCreateScreen.TYPE, (packet, player, responseSender) -> packet.handle());
        ClientPlayNetworking.registerGlobalReceiver(OpenBonfireGUI.TYPE, (packet, player, responseSender) -> packet.handle());
        ClientPlayNetworking.registerGlobalReceiver(QueueBonfireScreenshot.TYPE, (packet, player, responseSender) -> packet.handle());
        ClientPlayNetworking.registerGlobalReceiver(SendBonfiresToClient.TYPE, (packet, player, responseSender) -> packet.handle());
    }

    public static void openCreateScreen(BonfireTileEntity te) {
        MinecraftClient.getInstance().setScreen(new CreateBonfireScreen(te));
    }

    public static void displayTitle(DisplayTitle packet) {
        InGameHud gui = MinecraftClient.getInstance().inGameHud;
        gui.setTitle(Text.translatable(packet.title));
        gui.setSubtitle(Text.translatable(packet.subtitle));
        gui.setTitleTicks(packet.fadein, packet.stay, packet.fadeout);
    }

    /*
    public static void syncBonfire(SyncBonfire packet) {
        BlockPos pos = new BlockPos(packet.x, packet.y, packet.z);
        World level = MinecraftClient.getInstance().world;
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
     */

    public static void syncSaveData(Map<UUID, Bonfire> bonfires) {
        //BonfireHandler.getHandler(MinecraftClient.getInstance().world).getRegistry().setBonfires(bonfires);
    }

    public static void syncEstusData(UUID lastRested) {
        EstusHandler.IEstusHandler handler = EstusHandler.getHandler(MinecraftClient.getInstance().player);
        handler.setLastRested(lastRested);
    }

    public static void displayBonfireTravelled(Bonfire bonfire) {
        String formattedDimName;
        if (I18n.hasTranslation(LocalStrings.getDimensionKey(bonfire.getDimension()))) {
            String dimName = (bonfire.getDimension().getValue().getPath().replaceAll("_", " "));
            formattedDimName = WordUtils.capitalizeFully(dimName);
        } else {
            formattedDimName = I18n.translate(LocalStrings.getDimensionKey(bonfire.getDimension()));
        }
        InGameHud gui = MinecraftClient.getInstance().inGameHud;
        gui.setTitle(Text.translatable(bonfire.getName()));
        gui.setSubtitle(Text.translatable(formattedDimName));
        gui.setTitleTicks(10, 20, 10);
    }

    public static void queueBonfireScreenshot(String name, UUID uuid) {
        ScreenshotUtils.startScreenshotTimer(name, uuid);
    }

    public static void deleteScreenshot(UUID uuid, String name) {
        if (Bonfires.CONFIG.client.deleteScreenshotsOnDestroyed()) {
            Path screenshotsDir = Paths.get(MinecraftClient.getInstance().runDirectory.getPath(), "bonfires/");
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
