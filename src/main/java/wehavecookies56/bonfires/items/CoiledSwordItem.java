package wehavecookies56.bonfires.items;

import net.minecraft.entity.LivingEntity;
import net.minecraft.item.IItemTier;
import net.minecraft.item.ItemStack;
import net.minecraft.item.SwordItem;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.SoundEvents;
import wehavecookies56.bonfires.BonfiresGroup;
import wehavecookies56.bonfires.data.ReinforceHandler;

import javax.annotation.Nullable;

/**
 * Created by Toby on 05/11/2016.
 */
public class CoiledSwordItem extends SwordItem {

    public CoiledSwordItem() {
        super(new IItemTier() {
            @Override
            public int getUses() {
                return 105;
            }

            @Override
            public float getSpeed() {
                return 8;
            }

            @Override
            public float getAttackDamageBonus() {
                return 4;
            }

            @Override
            public int getLevel() {
                return 3;
            }

            @Override
            public int getEnchantmentValue() {
                return 0;
            }

            @Override
            public Ingredient getRepairIngredient() {
                return null;
            }
        }, 3, -2.4F, new Properties().stacksTo(1).tab(BonfiresGroup.INSTANCE));
    }

    @Override
    public boolean hurtEnemy(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        attacker.playSound(SoundEvents.FIRECHARGE_USE, 1.0F, 1.0F);
        target.setSecondsOnFire(3);
        return super.hurtEnemy(stack, target, attacker);
    }
}
