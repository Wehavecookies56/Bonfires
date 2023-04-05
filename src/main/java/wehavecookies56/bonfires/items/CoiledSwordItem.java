package wehavecookies56.bonfires.items;

import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.crafting.Ingredient;

/**
 * Created by Toby on 05/11/2016.
 */
public class CoiledSwordItem extends SwordItem {

    public CoiledSwordItem() {
        super(new Tier() {
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
        }, 3, -2.4F, new Properties().stacksTo(1));
    }

    @Override
    public boolean hurtEnemy(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        attacker.playSound(SoundEvents.FIRECHARGE_USE, 1.0F, 1.0F);
        target.setSecondsOnFire(3);
        return super.hurtEnemy(stack, target, attacker);
    }
}
