package wehavecookies56.bonfires.items;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.StackReference;
import net.minecraft.item.ItemStack;
import net.minecraft.item.SwordItem;
import net.minecraft.item.ToolMaterial;
import net.minecraft.recipe.Ingredient;
import net.minecraft.screen.slot.Slot;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.ClickType;

/**
 * Created by Toby on 05/11/2016.
 */
public class CoiledSwordItem extends SwordItem {

    public CoiledSwordItem() {
        super(new ToolMaterial() {
            @Override
            public int getDurability() {
                return 105;
            }

            @Override
            public float getMiningSpeedMultiplier() {
                return 8;
            }

            @Override
            public float getAttackDamage() {
                return 4;
            }

            @Override
            public int getMiningLevel() {
                return 3;
            }

            @Override
            public int getEnchantability() {
                return 0;
            }

            @Override
            public Ingredient getRepairIngredient() {
                return null;
            }
        }, 3, -2.4F, new Settings().maxCount(1));
    }

    @Override
    public boolean postHit(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        if ((attacker instanceof PlayerEntity player && player.getAttackCooldownProgress(0) == 1.0F) || !(attacker instanceof PlayerEntity)) {
            attacker.getWorld().playSoundFromEntity(null, attacker, SoundEvents.ITEM_FIRECHARGE_USE, SoundCategory.PLAYERS, 1.0F, 1.0F);
            target.setOnFireFor(3);
        }
        return super.postHit(stack, target, attacker);
    }

    @Override
    public boolean onClicked(ItemStack stack, ItemStack otherStack, Slot slot, ClickType clickType, PlayerEntity player, StackReference cursorStackReference) {
        return super.onClicked(stack, otherStack, slot, clickType, player, cursorStackReference);
    }
}
