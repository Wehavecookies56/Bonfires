package wehavecookies56.bonfires.items;

import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.block.Block;

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
            public TagKey<Block> getIncorrectBlocksForDrops() {
                return null;
            }

            @Override
            public int getEnchantmentValue() {
                return 0;
            }

            @Override
            public Ingredient getRepairIngredient() {
                return null;
            }
        }, new Properties().stacksTo(1));
    }

    @Override
    public boolean hurtEnemy(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        if ((attacker instanceof Player player && player.getAttackStrengthScale(0) == 1.0F) || !(attacker instanceof Player)) {
            attacker.level().playSound(null, attacker, SoundEvents.FIRECHARGE_USE, SoundSource.PLAYERS, 1.0F, 1.0F);
            target.igniteForTicks(60);
        }
        return super.hurtEnemy(stack, target, attacker);
    }
}
