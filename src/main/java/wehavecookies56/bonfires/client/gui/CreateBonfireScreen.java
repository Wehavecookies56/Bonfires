package wehavecookies56.bonfires.client.gui;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.text.TranslationTextComponent;
import wehavecookies56.bonfires.LocalStrings;
import wehavecookies56.bonfires.client.gui.widgets.GuiButtonCheckBox;
import wehavecookies56.bonfires.client.gui.widgets.NameTextField;
import wehavecookies56.bonfires.packets.server.LightBonfire;
import wehavecookies56.bonfires.packets.PacketHandler;
import wehavecookies56.bonfires.tiles.BonfireTileEntity;

/**
 * Created by Toby on 10/11/2016.
 */
public class CreateBonfireScreen extends Screen {

    private NameTextField nameBox;
    private Button accept;
    private final BonfireTileEntity te;
    private GuiButtonCheckBox isPrivate;

    public CreateBonfireScreen(BonfireTileEntity bonfireTE) {
        super(new TranslationTextComponent(""));
        this.te = bonfireTE;
    }

    @Override
    public void render(MatrixStack stack, int mouseX, int mouseY, float partialTicks) {
        super.render(stack, mouseX, mouseY, partialTicks);
        drawString(stack, minecraft.font, new TranslationTextComponent(LocalStrings.TEXT_NAME), (width / 2) - minecraft.font.width(new TranslationTextComponent(LocalStrings.TEXT_NAME)) / 2, (height / 2) - (minecraft.font.lineHeight / 2) - 20, 0xFFFFFF);
        nameBox.render(stack, mouseX, mouseY, partialTicks);
    }

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
                    Minecraft.getInstance().level.playSound(Minecraft.getInstance().player, te.getBlockPos(), SoundEvents.UI_TOAST_CHALLENGE_COMPLETE, SoundCategory.BLOCKS, 1, 1);
                    PacketHandler.sendToServer(new LightBonfire(nameBox.getValue(), te, !isPrivate.isChecked()));
                    minecraft.setScreen(null);
                }
                break;
        }
        updateButtons();
    }


    @Override
    public void tick() {
        if (nameBox != null) {
            nameBox.tick();
        }
        super.tick();
    }

    public void updateButtons() {
        accept.active = !nameBox.getValue().isEmpty();
    }

    @Override
    public void init() {
        super.init();
        addWidget(nameBox = new NameTextField(minecraft.font, (width / 2) - (100 / 2), (height / 2) - (15 / 2), 100, 15));
        buttons.clear();
        addButton(accept = new Button((width / 2) - (80 / 2), (height / 2) - (20 / 2) + 40, 80, 20, new TranslationTextComponent(LocalStrings.BUTTON_ACCEPT), press -> action(0)));
        isPrivate = new GuiButtonCheckBox(0, 0, LocalStrings.BUTTON_SET_PRIVATE, false);
        isPrivate.x = (width / 2) - (isPrivate.getWidth() / 2);
        isPrivate.y = (height / 2) - (10 / 2) + 20;
        addButton(isPrivate);
        nameBox.setMaxLength(14);
        updateButtons();
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }
}