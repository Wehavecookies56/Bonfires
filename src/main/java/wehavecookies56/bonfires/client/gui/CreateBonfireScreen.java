package wehavecookies56.bonfires.client.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.core.Vec3i;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import wehavecookies56.bonfires.BonfiresConfig;
import wehavecookies56.bonfires.LocalStrings;
import wehavecookies56.bonfires.client.ScreenshotUtils;
import wehavecookies56.bonfires.client.gui.widgets.GuiButtonCheckBox;
import wehavecookies56.bonfires.client.gui.widgets.NameTextField;
import wehavecookies56.bonfires.packets.PacketHandler;
import wehavecookies56.bonfires.packets.server.LightBonfire;
import wehavecookies56.bonfires.tiles.BonfireTileEntity;

import java.util.UUID;

/**
 * Created by Toby on 10/11/2016.
 */
public class CreateBonfireScreen extends Screen {

    private NameTextField nameBox;
    private Button accept;
    private final BonfireTileEntity te;
    private GuiButtonCheckBox isPrivate;

    public CreateBonfireScreen(BonfireTileEntity bonfireTE) {
        super(Component.empty());
        this.te = bonfireTE;
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTicks) {
        if (!ScreenshotUtils.isTimerStarted()) {
            super.render(guiGraphics, mouseX, mouseY, partialTicks);
            guiGraphics.drawString(minecraft.font, Component.translatable(LocalStrings.TEXT_NAME), (width / 2) - minecraft.font.width(Component.translatable(LocalStrings.TEXT_NAME)) / 2, (height / 2) - (minecraft.font.lineHeight / 2) - 20, 0xFFFFFF);
            nameBox.render(guiGraphics, mouseX, mouseY, partialTicks);
        }
    }
    UUID uuid;
    String name;

    @Override
    public boolean charTyped(char c, int key) {
        boolean nameBoxReturn = true;
        if (nameBox.isFocused()) {
            nameBoxReturn = nameBox.charTyped(c, key);
            updateButtons();
        }
        return nameBoxReturn;
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        nameBox.mouseClicked(mouseX, mouseY, button);
        updateButtons();
        return super.mouseClicked(mouseX, mouseY, button);
    }

    protected void action(int id) {
        switch (id) {
            case 0:
                if (!nameBox.getValue().isEmpty()) {
                    Minecraft.getInstance().level.playSound(Minecraft.getInstance().player, te.getBlockPos(), SoundEvents.UI_TOAST_CHALLENGE_COMPLETE, SoundSource.BLOCKS, 1, 1);
                    PacketHandler.sendToServer(new LightBonfire(nameBox.getValue(), te, !isPrivate.isChecked(), BonfiresConfig.Client.enableAutomaticScreenshotOnCreation));
                    if (!BonfiresConfig.Client.enableAutomaticScreenshotOnCreation) {
                        onClose();
                    }
                }
                break;
        }
        updateButtons();
    }

    @Override
    public void onClose() {
        super.onClose();
    }

    @Override
    public boolean shouldCloseOnEsc() {
        return !ScreenshotUtils.isTimerStarted();
    }

    @Override
    public void tick() {
        if (te.getBlockPos().distManhattan(new Vec3i((int) minecraft.player.position().x, (int) minecraft.player.position().y, (int) minecraft.player.position().z)) > minecraft.player.getBlockReach()+3) {
            onClose();
        }
        if (nameBox != null) {
            nameBox.tick();
        }
        if (te.isRemoved()) {
            onClose();
        }
        super.tick();
    }

    public void updateButtons() {
        accept.active = !nameBox.getValue().isEmpty();
    }

    @Override
    public void init() {
        super.init();
        addRenderableWidget(nameBox = new NameTextField(minecraft.font, (width / 2) - (100 / 2), (height / 2) - (15 / 2), 100, 15));
        addRenderableWidget(accept = Button.builder(Component.translatable(LocalStrings.BUTTON_ACCEPT), press -> action(0)).pos((width / 2) - (80 / 2), (height / 2) - (20 / 2) + 40).size(80, 20).build());
        isPrivate = new GuiButtonCheckBox(0, 0, LocalStrings.BUTTON_SET_PRIVATE, false);
        isPrivate.setX((width / 2) - (isPrivate.getWidth() / 2));
        isPrivate.setY((height / 2) - (10 / 2) + 20);
        addRenderableWidget(isPrivate);
        nameBox.setMaxLength(14);
        updateButtons();
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }
}
