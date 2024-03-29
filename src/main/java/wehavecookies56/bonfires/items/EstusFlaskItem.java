package wehavecookies56.bonfires.items;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.level.Level;
import wehavecookies56.bonfires.BonfiresConfig;
import wehavecookies56.bonfires.LocalStrings;
import wehavecookies56.bonfires.data.ReinforceHandler;

import javax.annotation.Nullable;
import java.util.List;

/**
 * Created by Toby on 05/11/2016.
 */
public class EstusFlaskItem extends Item {

    public EstusFlaskItem() {
        super(new Properties().stacksTo(1).food(new FoodProperties.Builder().alwaysEat().nutrition(0).saturationMod(0).build()));
    }

    @Override
    public void inventoryTick(ItemStack stack, Level level, Entity entity, int p_41407_, boolean p_41408_) {
        if (stack.getTag() == null) {
            stack.setTag(new CompoundTag());
        }
        if (!stack.getTag().contains("estus")) {
            stack.getTag().putInt("estus", 3);
        }
        if (!stack.getTag().contains("uses")) {
            stack.getTag().putInt("uses", 3);
        }
        super.inventoryTick(stack, level, entity, p_41407_, p_41408_);
    }

    @Override
    public UseAnim getUseAnimation(ItemStack p_77661_1_) {
        return UseAnim.DRINK;
    }

    @Override
    public ItemStack finishUsingItem(ItemStack stack, Level world, LivingEntity entity) {
        if (!world.isClientSide) {
            if (stack.getTag() != null) {
                if (stack.getTag().getInt("estus") > 0) {
                    stack.getTag().putInt("estus", stack.getTag().getInt("estus") - 1);
                    float heal = (float) BonfiresConfig.Server.estusFlaskBaseHeal;
                    if (ReinforceHandler.canReinforce(stack)) {
                        ReinforceHandler.ReinforceLevel rlevel = ReinforceHandler.getReinforceLevel(stack);
                        if (rlevel != null) {
                            heal += (BonfiresConfig.Server.estusFlaskHealPerLevel * rlevel.level());
                        }
                    }
                    entity.heal(heal);
                }
            }
        }
        return stack;
    }

    @Override
    public int getBarColor(ItemStack stack) {
        if (stack.getTag() != null) {
            float f = Math.max(0.0F, (float)stack.getTag().getInt("estus")) / stack.getTag().getInt("uses");
            return Mth.hsvToRgb(f / 3.0F, 1.0F, 1.0F);
        } else {
            return 0x00000000;
        }
    }

    @Override
    public boolean isBarVisible(ItemStack stack) {
        if (stack.getTag() != null) {
            return stack.getTag().getInt("estus") < stack.getTag().getInt("uses");
        }
        return false;
    }

    @Override
    public int getBarWidth(ItemStack stack) {
        if (stack.getTag() != null) {
            return Math.round((float)13 * ((float)stack.getTag().getInt("estus") / (float) stack.getTag().getInt("uses")));
        } else {
            return 0;
        }
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level world, List<Component> tooltip, TooltipFlag advanced) {
        int level = ReinforceHandler.getReinforceLevel(stack).level();
        tooltip.add(Component.translatable(LocalStrings.TOOLTIP_ESTUS_HEAL, (BonfiresConfig.Server.estusFlaskBaseHeal + (BonfiresConfig.Server.estusFlaskHealPerLevel * level)) * 0.5F));
        if (stack.getTag() != null) {
            if (stack.getTag().contains("uses")) {
                if (stack.getTag().contains("estus")) {
                    tooltip.add(Component.translatable("Uses: " + stack.getTag().getInt("estus") + "/" + stack.getTag().getInt("uses")));
                }
            }
        }
    }
}
