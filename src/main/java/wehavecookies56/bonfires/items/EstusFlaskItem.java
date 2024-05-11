package wehavecookies56.bonfires.items;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
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
import wehavecookies56.bonfires.setup.ComponentSetup;

import java.util.List;

/**
 * Created by Toby on 05/11/2016.
 */
public class EstusFlaskItem extends Item {

    public record Estus(int uses, int maxUses){
        public static final Codec<Estus> CODEC = RecordCodecBuilder.create(
                estusInstance -> estusInstance.group(
                        Codec.INT.fieldOf("uses").forGetter(Estus::uses),
                        Codec.INT.fieldOf("max_uses").forGetter(Estus::maxUses)
                ).apply(estusInstance, Estus::new)
        );
        public static final StreamCodec<FriendlyByteBuf, Estus> STREAM_CODEC = StreamCodec.composite(
                ByteBufCodecs.INT,
                Estus::uses,
                ByteBufCodecs.INT,
                Estus::maxUses,
                Estus::new
        );
        public Estus subtract(int amount) {
            return new Estus(Math.max(this.uses - amount, 0), this.maxUses);
        }
    }

    public EstusFlaskItem() {
        super(new Properties().stacksTo(1).food(new FoodProperties.Builder().alwaysEdible().nutrition(0).saturationModifier(0).build()));
    }

    @Override
    public void inventoryTick(ItemStack stack, Level level, Entity entity, int p_41407_, boolean p_41408_) {
        if (!stack.has(ComponentSetup.ESTUS)) {
            stack.set(ComponentSetup.ESTUS, new Estus(3, 3));
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
            if (stack.has(ComponentSetup.ESTUS)) {
                Estus estus = stack.get(ComponentSetup.ESTUS);
                if (estus.uses > 0) {
                    stack.set(ComponentSetup.ESTUS, estus.subtract(1));
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
        if (stack.has(ComponentSetup.ESTUS)) {
            Estus estus = stack.get(ComponentSetup.ESTUS);
            float f = Math.max(0.0F, (float) estus.uses / (float) estus.maxUses);
            return Mth.hsvToRgb(f / 3.0F, 1.0F, 1.0F);
        } else {
            return 0x00000000;
        }
    }

    @Override
    public boolean isBarVisible(ItemStack stack) {
        if (stack.has(ComponentSetup.ESTUS)) {
            Estus estus = stack.get(ComponentSetup.ESTUS);
            return estus.uses < estus.maxUses;
        }
        return false;
    }

    @Override
    public int getBarWidth(ItemStack stack) {
        if (stack.has(ComponentSetup.ESTUS)) {
            Estus estus = stack.get(ComponentSetup.ESTUS);
            return Math.round((float)13 * ((float)estus.uses / (float) estus.maxUses));
        } else {
            return 0;
        }
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltip, TooltipFlag advanced) {
        int level = ReinforceHandler.getReinforceLevel(stack).level();
        tooltip.add(Component.translatable(LocalStrings.TOOLTIP_ESTUS_HEAL, (BonfiresConfig.Server.estusFlaskBaseHeal + (BonfiresConfig.Server.estusFlaskHealPerLevel * level)) * 0.5F));
        if (stack.has(ComponentSetup.ESTUS)) {
            Estus estus = stack.get(ComponentSetup.ESTUS);
            tooltip.add(Component.translatable("Uses: " + estus.uses + "/" + estus.maxUses));
        }
    }
}
