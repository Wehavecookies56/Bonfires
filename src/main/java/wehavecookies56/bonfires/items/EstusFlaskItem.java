package wehavecookies56.bonfires.items;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.client.item.TooltipType;
import net.minecraft.component.type.FoodComponent;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.text.Text;
import net.minecraft.util.UseAction;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import wehavecookies56.bonfires.Bonfires;
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
        public static final PacketCodec<PacketByteBuf, Estus> STREAM_CODEC = PacketCodec.tuple(
                PacketCodecs.INTEGER,
                Estus::uses,
                PacketCodecs.INTEGER,
                Estus::maxUses,
                Estus::new
        );
        public Estus subtract(int amount) {
            return new Estus(Math.max(this.uses - amount, 0), this.maxUses);
        }
    }

    public EstusFlaskItem() {
        super(new Settings().maxCount(1).food(new FoodComponent.Builder().alwaysEdible().nutrition(0).saturationModifier(0).build()));
    }

    @Override
    public UseAction getUseAction(ItemStack stack) {
        return UseAction.DRINK;
    }

    @Override
    public void inventoryTick(ItemStack stack, World world, Entity entity, int slot, boolean selected) {
        if (stack.get(ComponentSetup.ESTUS) == null) {
            stack.set(ComponentSetup.ESTUS, new Estus(3, 3));
        }
        super.inventoryTick(stack, world, entity, slot, selected);
    }

    @Override
    public ItemStack finishUsing(ItemStack stack, World world, LivingEntity user) {
        if (!world.isClient) {
            if (stack.get(ComponentSetup.ESTUS) != null) {
                Estus estus = stack.get(ComponentSetup.ESTUS);
                if (estus.uses > 0) {
                    stack.set(ComponentSetup.ESTUS, estus.subtract(1));
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
        if (stack.get(ComponentSetup.ESTUS) != null) {
            Estus estus = stack.get(ComponentSetup.ESTUS);
            float f = Math.max(0.0F, (float) estus.uses / (float) estus.maxUses);
            return MathHelper.hsvToRgb(f / 3.0F, 1.0F, 1.0F);
        } else {
            return 0x00000000;
        }
    }

    @Override
    public boolean isItemBarVisible(ItemStack stack) {
        if (stack.get(ComponentSetup.ESTUS) != null) {
            Estus estus = stack.get(ComponentSetup.ESTUS);
            return estus.uses < estus.maxUses;
        }
        return false;
    }

    @Override
    public int getItemBarStep(ItemStack stack) {
        if (stack.get(ComponentSetup.ESTUS) != null) {
            Estus estus = stack.get(ComponentSetup.ESTUS);
            return Math.round((float)13 * ((float)estus.uses / (float) estus.maxUses));
        } else {
            return 0;
        }
    }

    @Override
    public void appendTooltip(ItemStack stack, TooltipContext context, List<Text> tooltip, TooltipType type) {
        int level = ReinforceHandler.getReinforceLevel(stack).level();
        tooltip.add(Text.translatable(LocalStrings.TOOLTIP_ESTUS_HEAL, (Bonfires.CONFIG.common.estusFlaskBaseHeal() + (Bonfires.CONFIG.common.estusFlaskHealPerLevel() * level)) * 0.5F));
        if (stack.get(ComponentSetup.ESTUS) != null) {
            Estus estus = stack.get(ComponentSetup.ESTUS);
            tooltip.add(Text.translatable("Uses: " + estus.uses + "/" + estus.maxUses));
        }
    }
}
