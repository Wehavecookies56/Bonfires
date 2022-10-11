package wehavecookies56.bonfires.items;

import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.*;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import wehavecookies56.bonfires.BonfiresGroup;
import wehavecookies56.bonfires.LocalStrings;
import wehavecookies56.bonfires.data.ReinforceHandler;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

/**
 * Created by Toby on 05/11/2016.
 */
public class EstusFlaskItem extends Item {

    public EstusFlaskItem() {
        super(new Properties().tab(BonfiresGroup.INSTANCE).stacksTo(1).food(new Food.Builder().alwaysEat().nutrition(0).saturationMod(0).build()));
    }

    @Override
    public UseAction getUseAnimation(ItemStack p_77661_1_) {
        return UseAction.DRINK;
    }

    @Override
    public ItemStack finishUsingItem(ItemStack stack, World world, LivingEntity entity) {
        if (!world.isClientSide) {
            if (stack.getTag() != null) {
                if (stack.getTag().getInt("estus") > 0) {
                    stack.getTag().putInt("estus", stack.getTag().getInt("estus") - 1);
                    int heal = 6;
                    if (ReinforceHandler.hasHandler(stack)) {
                        heal += ReinforceHandler.getReinforceLevel(stack).level();
                    }
                    entity.heal(heal);
                }
            }
        }
        return stack;
    }

    @Override
    public double getDurabilityForDisplay(ItemStack stack) {
        if (stack.getTag() != null) {
            return 1 - (double)stack.getTag().getInt("estus") / (double)stack.getTag().getInt("uses");
        } else {
            return 1 - (double)0 / (double)15;
        }
    }

    @Override
    public int getRGBDurabilityForDisplay(@Nonnull ItemStack stack) {
        if (stack.getTag() != null) {
            return MathHelper.hsvToRgb(Math.max(0.0F, 1.0F / ((float)stack.getTag().getInt("uses") - (float)stack.getTag().getInt("estus")) / (float)stack.getTag().getInt("uses")) / 3.0F, 1.0F, 1.0F);
        } else {
            return 0x00000000;
        }
    }

    @Override
    public boolean showDurabilityBar(ItemStack stack) {
        if (stack.getTag() != null) {
            return stack.getTag().getInt("estus") < stack.getTag().getInt("uses");
        }
        return false;
    }

    @Override
    public void fillItemCategory(ItemGroup tab, NonNullList<ItemStack> items) {
        if (tab.equals(BonfiresGroup.INSTANCE)) {
            for (int i = 3; i < 16; ++i) {
                ItemStack stack = new ItemStack(this);
                stack.setTag(new CompoundNBT());
                if (stack.getTag() != null) {
                    stack.getTag().putInt("uses", i);
                    stack.getTag().putInt("estus", i);
                }
                items.add(stack);
            }
        }
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable World world, List<ITextComponent> tooltip, ITooltipFlag advanced) {
        int level = ReinforceHandler.getReinforceLevel(stack).level();
        tooltip.add(new TranslationTextComponent(LocalStrings.TOOLTIP_ESTUS_HEAL, (6 + level) * 0.5F));
        if (stack.getTag() != null) {
            if (stack.getTag().contains("uses")) {
                if (stack.getTag().contains("estus")) {
                    tooltip.add(new TranslationTextComponent("Uses: " + stack.getTag().getInt("estus") + "/" + stack.getTag().getInt("uses")));
                }
            }
        }
    }
}
