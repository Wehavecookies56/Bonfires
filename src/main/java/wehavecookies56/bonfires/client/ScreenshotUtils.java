package wehavecookies56.bonfires.client;

import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderContext;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.texture.NativeImage;
import net.minecraft.client.util.ScreenshotRecorder;
import wehavecookies56.bonfires.Bonfires;
import wehavecookies56.bonfires.client.gui.BonfireScreen;
import wehavecookies56.bonfires.client.gui.CreateBonfireScreen;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

public class ScreenshotUtils {

    private static boolean takingScreenshot = false;

    private static String name;
    private static UUID uuid;
    public static int width = 103;
    public static int height = 110;

    public static boolean isTakingScreenshot() {
        return takingScreenshot;
    }

    public static String getFileNameString(String bonfireName, UUID bonfireUUID) {
        String nameNoInvalid = bonfireName.replaceAll("[\\\\/:*?\"<>|]", "_").toLowerCase();
        return nameNoInvalid + "_" + bonfireUUID.toString() + ".png";
    }

    public static void startScreenshotTimer(String bonfireName, UUID bonfireUUID) {
        name = bonfireName;
        uuid = bonfireUUID;
        takingScreenshot = true;
        MinecraftClient.getInstance().options.hudHidden = true;
    }

    private static void takeScreenshot(String bonfireName, UUID bonfireUUID) {
        Path p = Paths.get(MinecraftClient.getInstance().runDirectory.getPath(), "bonfires");
        String fileName = getFileNameString(bonfireName, bonfireUUID);
        File fileToCreate = new File(p.toFile(), fileName);
        try {
            Files.createDirectories(p);
        } catch (IOException e) {
            e.printStackTrace();
        }
        //String nameNoInvalid = bonfireName.replaceAll("[\\\\/:*?\"<>|]", "_").toLowerCase();
        NativeImage image = ScreenshotRecorder.takeScreenshot(MinecraftClient.getInstance().getFramebuffer());

        try {
            image.writeTo(fileToCreate);
            Bonfires.LOGGER.info("Saved bonfire screenshot " + fileName);
        } catch (IOException ioexception) {
            Bonfires.LOGGER.warn("Couldn't save screenshot", (Throwable)ioexception);
        } finally {
            image.close();
        }
        //Screenshot.grab(p.toFile(), nameNoInvalid + "_" + bonfireUUID.toString() + ".png", Minecraft.getInstance().getMainRenderTarget(), (m) -> {});
    }

    public static void clientTick(WorldRenderContext context) {
        MinecraftClient client = MinecraftClient.getInstance();
        if (client.world != null) {
            if (client.player != null) {
                if (takingScreenshot) {
                    takingScreenshot = false;
                    client.options.hudHidden = false;
                    takeScreenshot(name, uuid);
                    if (client.currentScreen != null) {
                        if (client.currentScreen instanceof CreateBonfireScreen create) {
                            create.close();
                        }
                        if (client.currentScreen instanceof BonfireScreen bonfire) {
                            bonfire.loadBonfireScreenshot();
                        }
                    }
                }
            }
        }
    }
}
