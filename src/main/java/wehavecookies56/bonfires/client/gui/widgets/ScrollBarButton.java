package wehavecookies56.bonfires.client.gui.widgets;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.Text;

import java.awt.*;

public class ScrollBarButton extends ButtonWidget {

    double clickX, clickY;
    public int startX, startY, handleY, handleYMax, localHandleY, localHandleYMax, visibleHeight;
    float scrollPercent;
    private int contentHeight, handleHeight;

    public float scrollOffset;

    public ScrollBarButton(int buttonId, int x, int y, int width, int height, int visibleHeight, int contentHeight) {
        super(x, y, width, height, Text.empty(), button -> {}, ButtonWidget.DEFAULT_NARRATION_SUPPLIER);
        this.visibleHeight = visibleHeight;
        this.handleYMax = getY();
        int handleBottom = getBottom();
        handleY = handleYMax;
        localHandleYMax = handleBottom - handleYMax;
        setContentHeight(contentHeight);
    }

    public int getBottom() {
        return getY() + height;
    }

    public void setHandleY(int handleY) {
        if (handleY < handleYMax) {
            this.handleY = handleYMax;
        } else {
            this.handleY = handleY;
        }
    }

    public void setHandleHeight(int height) {
        this.handleHeight = height;
        if (handleY > getHandleBottom()) {
            handleY = getHandleBottom() + 1;
        } else if (handleY < handleYMax) {
            handleY = handleYMax;
        }
    }

    public int getHandleBottom() {
        return getBottom() - handleHeight;
    }

    public void setContentHeight(int contentHeight) {
        this.contentHeight = contentHeight;
        float visiblePercentage = ((float) visibleHeight / contentHeight) * 100;
        setHandleHeight((int) (localHandleYMax * (visiblePercentage / 100)));
        if (visibleHeight > contentHeight) {
            scrollOffset = 0;
        }
    }

    public void updateScroll() {
        if (visible && contentHeight > visibleHeight) {
            localHandleY = handleY - handleYMax;
            scrollPercent = ((float) localHandleY / (localHandleYMax - handleHeight)) * 100;
            int totalScroll = contentHeight - visibleHeight;
            scrollOffset = totalScroll * (scrollPercent/100);
        } else {
            scrollOffset = 0;
        }
    }

    @Override
    public void render(DrawContext guiGraphics, int mouseX, int mouseY, float partialTicks) {
        if (visible) {
            guiGraphics.setShaderColor(1, 1, 1, 0.5F);
            guiGraphics.fill(getX(), getY(), getX() + width, getBottom(), new Color(0, 0, 0, 0.5F).hashCode());
            guiGraphics.setShaderColor(1, 1, 1, 1);
            guiGraphics.fill(getX(), handleY, getX()+8, handleY+handleHeight, new Color(81, 86, 71).hashCode());
            guiGraphics.fill(getX()+1, handleY+1, getX()+1+6, handleY+1+(handleHeight-2), new Color(114, 118, 95).hashCode());
        }
    }

    @Override
    public boolean mouseDragged(double mouseX, double mouseY, int button, double dragX, double dragY) {
        if (active && contentHeight > visibleHeight) {
            if (clickX >= getX() && clickX <= getX() + width) {
                updateScroll();
                if (startY - (clickY - mouseY) >= getHandleBottom()) {
                    handleY = getHandleBottom();
                } else if (startY - (clickY - mouseY) <= handleYMax) {
                    handleY = handleYMax;
                } else {
                    handleY = (int) (startY - (clickY - mouseY));
                }
            }
        }
        return super.mouseDragged(mouseX, mouseY, button, dragX, dragY);
    }

    @Override
    public boolean mouseReleased(double p_231048_1_, double p_231048_3_, int p_231048_5_) {
        return true;
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (button == 0) {
            clickX = mouseX;
            clickY = mouseY;
            startX = getX();
            startY = handleY;
            if (clickY >= getY() && clickY <= getY() + getBottom() && clickX >= getX() && clickX <= getX() + width && visible) {
                playDownSound(MinecraftClient.getInstance().getSoundManager());
            }
        }
        return false;
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double scrollDelta) {
        if (visible && contentHeight > visibleHeight) {
            int scrollFactor = 5;
            int oldY = handleY;
            if (scrollDelta > 0) {
                handleY = (int) Math.max(handleY - (scrollDelta * scrollFactor), handleYMax);
            }
            if (scrollDelta < 0) {
                handleY = (int) Math.min(handleY - (scrollDelta * scrollFactor), getHandleBottom());
            }
            if(oldY != handleY) {
                playDownSound(MinecraftClient.getInstance().getSoundManager());
            }
            updateScroll();
        }
        return super.mouseScrolled(mouseX, mouseY, scrollDelta);
    }

}
