package uk.co.wehavecookies56.bonfires.items;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import uk.co.wehavecookies56.bonfires.Bonfires;

import javax.annotation.Nonnull;

/**
 * Created by Toby on 05/11/2016.
 */
public class ItemCoiledSword extends ItemSword {

    public ItemCoiledSword(String name, ToolMaterial material) {
        super(material);
        setRegistryName(name);
        setUnlocalizedName(name);
        setCreativeTab(Bonfires.tabBonfires);
    }

    @Override
    public boolean hitEntity(ItemStack stack, EntityLivingBase target, @Nonnull EntityLivingBase attacker) {
        attacker.playSound(SoundEvents.ITEM_FIRECHARGE_USE, 1.0F, 1.0F);
        target.setFire(3);
        return super.hitEntity(stack, target, attacker);
    }

}
