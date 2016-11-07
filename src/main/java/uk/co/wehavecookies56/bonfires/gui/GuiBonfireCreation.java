package uk.co.wehavecookies56.bonfires.gui;

import net.ilexiconn.llibrary.client.gui.ElementGUI;
import net.ilexiconn.llibrary.client.gui.element.InputElement;
import net.ilexiconn.llibrary.client.gui.element.LabelElement;
import net.ilexiconn.llibrary.client.gui.element.ListElement;
import net.ilexiconn.llibrary.client.gui.element.ScrollbarElement;
import net.minecraft.client.Minecraft;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

/**
 * Created by Toby on 07/11/2016.
 */
public class GuiBonfireCreation extends ElementGUI {

    private ListElement<GuiBonfireCreation> list;

    List<String> names;

    public GuiBonfireCreation() {
        names = new ArrayList<String>();
        names.add("NAME1");
        names.add("NAME2");
        names.add("NAME3");
        names.add("NAME4");

    }

    @Override
    public void drawScreen(float mouseX, float mouseY, float partialTicks) {

    }

    @Override
    protected void initElements() {
        this.clearElements();
        this.addElement(new InputElement<GuiBonfireCreation>(this, 200, 200, 100, "Enter Name", guiBonfireCreationInputElementBase -> {
            //Set name
            System.out.println(guiBonfireCreationInputElementBase.getText());
        }));
        this.addElement(list = new ListElement<GuiBonfireCreation>(this, 0, 0, 100, 100, names, 10, listElement -> {
            System.out.println(listElement.getSelectedEntry());
            LabelElement label = new LabelElement(this, "SELECTED:" + listElement.getSelectedEntry(), 200, 10);
            if(listElement.getSelectedEntry().equals("NAME1")) {
                this.addElement(label);
            } else {
                this.removeElement(label);
                this.drawString(Minecraft.getMinecraft().fontRendererObj, listElement.getSelectedEntry(), 20, 20, 0xFFFFFF);
            }
            return true;
        }));
    }
}
