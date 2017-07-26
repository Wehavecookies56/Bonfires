package uk.co.wehavecookies56.bonfires.gui;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.resources.I18n;
import uk.co.wehavecookies56.bonfires.LocalStrings;
import uk.co.wehavecookies56.bonfires.packets.LightBonfire;
import uk.co.wehavecookies56.bonfires.packets.PacketDispatcher;
import uk.co.wehavecookies56.bonfires.tiles.TileEntityBonfire;

import java.io.IOException;

/**
 * Created by Toby on 10/11/2016.
 */
class GuiCreateBonfire extends GuiScreen {

    private GuiNameTextField nameBox;
    @SuppressWarnings("")
    private GuiButton accept;
    private final TileEntityBonfire te;
    private GuiButtonCheckBox isPrivate;

    GuiCreateBonfire(TileEntityBonfire bonfireTE) {
        super();
        this.te = bonfireTE;
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        super.drawScreen(mouseX, mouseY, partialTicks);
        drawString(mc.fontRenderer, I18n.format(LocalStrings.TEXT_NAME), (width / 2) - mc.fontRenderer.getStringWidth(I18n.format(LocalStrings.TEXT_NAME)) / 2, (height / 2) - (mc.fontRenderer.FONT_HEIGHT / 2) - 20, 0xFFFFFF);
        nameBox.drawTextBox();
    }

    @Override
    protected void keyTyped(char typedChar, int keyCode) throws IOException {
        nameBox.textboxKeyTyped(typedChar, keyCode);
        updateButtons();
        super.keyTyped(typedChar, keyCode);
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        nameBox.mouseClicked(mouseX, mouseY, mouseButton);
        updateButtons();
        super.mouseClicked(mouseX, mouseY, mouseButton);
    }

    @Override
    protected void actionPerformed(GuiButton button) throws IOException {
        switch (button.id) {
            case 0:
                if (!nameBox.getText().isEmpty()) {
                    PacketDispatcher.sendToServer(new LightBonfire(nameBox.getText(), te, !isPrivate.isChecked()));
                    mc.displayGuiScreen(null);
                }
                break;
        }
        updateButtons();
        super.actionPerformed(button);
    }

    @Override
    public void updateScreen() {
        nameBox.updateCursorCounter();
        super.updateScreen();
    }

    public void updateButtons() {
        accept.enabled = !nameBox.getText().isEmpty();
    }

    @Override
    public void initGui() {
        super.initGui();
        nameBox = new GuiNameTextField(0, mc.fontRenderer, (width / 2) - (100 / 2), (height / 2) - (15 / 2), 100, 15);
        buttonList.clear();
        buttonList.add(accept = new GuiButton(0, (width / 2) - (80 / 2), (height / 2) - (20 / 2) + 40, 80, 20, I18n.format(LocalStrings.BUTTON_ACCEPT)));
        isPrivate = new GuiButtonCheckBox(1, 0, 0, I18n.format(LocalStrings.BUTTON_SET_PRIVATE));
        isPrivate.x = (width / 2) - (isPrivate.getButtonWidth() / 2);
        isPrivate.y = (height / 2) - (10 / 2) + 20;
        buttonList.add(isPrivate);
        nameBox.setMax(14);
        updateButtons();
    }
}
