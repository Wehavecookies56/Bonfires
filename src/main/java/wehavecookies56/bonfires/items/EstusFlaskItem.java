package wehavecookies56.bonfires.items;

import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.FoodComponent;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.text.Text;
import net.minecraft.util.UseAction;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import wehavecookies56.bonfires.Bonfires;
import wehavecookies56.bonfires.LocalStrings;
import wehavecookies56.bonfires.data.ReinforceHandler;

import java.util.List;

/**
 * Created by Toby on 05/11/2016.
 */
public class EstusFlaskItem extends Item {

    public EstusFlaskItem() {
        super(new Settings().maxCount(1).food(new FoodComponent.Builder().alwaysEdible().hunger(0).saturationModifier(0).build()));
    }

    @Override
    public UseAction getUseAction(ItemStack stack) {
        return UseAction.DRINK;
    }

    @Override
    public void inventoryTick(ItemStack stack, World world, Entity entity, int slot, boolean selected) {
        if (stack.getNbt() == null) {
            stack.setNbt(new NbtCompound());
        }
        if (!stack.getNbt().contains("estus")) {
            stack.getNbt().putInt("estus", 3);
        }
        if (!stack.getNbt().contains("uses")) {
            stack.getNbt().putInt("uses", 3);
        }
        super.inventoryTick(stack, world, entity, slot, selected);
    }

    @Override
    public ItemStack finishUsing(ItemStack stack, World world, LivingEntity user) {
        if (!world.isClient) {
            if (stack.getNbt() != null) {
                if (stack.getNbt().getInt("estus") > 0) {
                    stack.getNbt().putInt("estus", stack.getNbt().getInt("estus") - 1);
                    float heal = (float) Bonfires.CONFIG.common.estusFlaskBaseHeal();
                    if (ReinforceHandler.canReinforce(stack)) {
                        ReinforceHandler.ReinforceLevel rlevel = ReinforceHandler.getReinforceLevel(stack);
                        if (rlevel != null) {
                            heal += (Bonfires.CONFIG.common.estusFlaskHealPerLevel() * rlevel.level());
                        }
                    }
                    user.heal(heal);
                }
            }
        }
        return stack;
    }

    @Override
    public int getItemBarColor(ItemStack stack) {
        if (stack.getNbt() != null) {
            float f = Math.max(0.0F, (float)stack.getNbt().getInt("estus")) / stack.getNbt().getInt("uses");
            return MathHelper.hsvToRgb(f / 3.0F, 1.0F, 1.0F);
        } else {
            return 0x00000000;
        }
    }

    @Override
    public boolean isItemBarVisible(ItemStack stack) {
        if (stack.getNbt() != null) {
            return stack.getNbt().getInt("estus") < stack.getNbt().getInt("uses");
        }
        return false;
    }

    @Override
    public int getItemBarStep(ItemStack stack) {
        if (stack.getNbt() != null) {
            return Math.round((float)13 * ((float)stack.getNbt().getInt("estus") / (float) stack.getNbt().getInt("uses")));
        } else {
            return 0;
        }
    }

    @Override
    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
        int level = ReinforceHandler.getReinforceLevel(stack).level();
        tooltip.add(Text.translatable(LocalStrings.TOOLTIP_ESTUS_HEAL, (Bonfires.CONFIG.common.estusFlaskBaseHeal() + (Bonfires.CONFIG.common.estusFlaskHealPerLevel() * level)) * 0.5F));
        if (stack.getNbt() != null) {
            if (stack.getNbt().contains("uses")) {
                if (stack.getNbt().contains("estus")) {
                    tooltip.add(Text.translatable("Uses: " + stack.getNbt().getInt("estus") + "/" + stack.getNbt().getInt("uses")));
                }
            }
        }
    }
}
