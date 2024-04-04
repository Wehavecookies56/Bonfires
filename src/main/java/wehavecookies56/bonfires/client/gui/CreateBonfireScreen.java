package wehavecookies56.bonfires.client.gui;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.math.Vec3i;
import wehavecookies56.bonfires.Bonfires;
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
    private ButtonWidget accept;
    private final BonfireTileEntity te;
    private GuiButtonCheckBox isPrivate;

    public CreateBonfireScreen(BonfireTileEntity bonfireTE) {
        super(Text.empty());
        this.te = bonfireTE;
    }

    @Override
    public void render(DrawContext guiGraphics, int mouseX, int mouseY, float partialTicks) {
        if (!ScreenshotUtils.isTimerStarted()) {
            super.render(guiGraphics, mouseX, mouseY, partialTicks);
            guiGraphics.drawText(client.textRenderer, Text.translatable(LocalStrings.TEXT_NAME), (width / 2) - client.textRenderer.getWidth(Text.translatable(LocalStrings.TEXT_NAME)) / 2, (height / 2) - (client.textRenderer.fontHeight / 2) - 20, 0xFFFFFF, true);
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
                if (!nameBox.getText().isEmpty()) {
                    MinecraftClient.getInstance().world.playSound(MinecraftClient.getInstance().player, te.getPos(), SoundEvents.UI_TOAST_CHALLENGE_COMPLETE, SoundCategory.BLOCKS, 1, 1);
                    PacketHandler.sendToServer(new LightBonfire(nameBox.getText(), te, !isPrivate.isChecked(), Bonfires.CONFIG.client.enableAutomaticScreenshotOnCreation()));
                    if (!Bonfires.CONFIG.client.enableAutomaticScreenshotOnCreation()) {
                        close();
                    }
                }
                break;
        }
        updateButtons();
    }

    @Override
    public void close() {
        super.close();
    }

    @Override
    public boolean shouldCloseOnEsc() {
        return !ScreenshotUtils.isTimerStarted();
    }

    @Override
    public void tick() {
        if (te.getPos().getManhattanDistance(new Vec3i((int) client.player.getPos().x, (int) client.player.getPos().y, (int) client.player.getPos().z)) > MinecraftClient.getInstance().interactionManager.getReachDistance()+3) {
            close();
        }
        if (nameBox != null) {
            nameBox.tick();
        }
        if (te.isRemoved()) {
            close();
        }
        super.tick();
    }

    public void updateButtons() {
        accept.active = !nameBox.getText().isEmpty();
    }

    @Override
    public void init() {
        super.init();
        addDrawableChild(nameBox = new NameTextField(client.textRenderer, (width / 2) - (100 / 2), (height / 2) - (15 / 2), 100, 15));
        addDrawableChild(accept = ButtonWidget.builder(Text.translatable(LocalStrings.BUTTON_ACCEPT), press -> action(0)).position((width / 2) - (80 / 2), (height / 2) - (20 / 2) + 40).size(80, 20).build());
        isPrivate = new GuiButtonCheckBox(0, 0, LocalStrings.BUTTON_SET_PRIVATE, false);
        isPrivate.setX((width / 2) - (isPrivate.getWidth() / 2));
        isPrivate.setY((height / 2) - (10 / 2) + 20);
        addDrawableChild(isPrivate);
        nameBox.setMaxLength(14);
        updateButtons();
    }
    @Override
    public boolean shouldPause() {
        return false;
    }
}
