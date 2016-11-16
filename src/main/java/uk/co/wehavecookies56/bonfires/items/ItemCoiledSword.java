package uk.co.wehavecookies56.bonfires.items;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;

/**
 * Created by Toby on 05/11/2016.
 */
public class ItemCoiledSword extends ItemSword {

    public ItemCoiledSword(ToolMaterial material) {
        super(material);
    }

    @Override
    public boolean hitEntity(ItemStack stack, EntityLivingBase target, EntityLivingBase attacker) {
        target.setFire(3);
        return super.hitEntity(stack, target, attacker);
    }
}
