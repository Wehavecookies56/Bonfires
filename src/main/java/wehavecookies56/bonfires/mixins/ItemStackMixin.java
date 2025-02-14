package wehavecookies56.bonfires.mixins;

import net.minecraft.component.ComponentType;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.AttributeModifierSlot;
import net.minecraft.component.type.AttributeModifiersComponent;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import wehavecookies56.bonfires.Bonfires;
import wehavecookies56.bonfires.data.ReinforceHandler;

@Mixin(ItemStack.class)
public class ItemStackMixin {

    @Redirect(method = "applyAttributeModifiers", at = @At(value = "INVOKE", target = "Lnet/minecraft/item/ItemStack;getOrDefault(Lnet/minecraft/component/ComponentType;Ljava/lang/Object;)Ljava/lang/Object;"))
    public Object getOrDefaultRedirect(ItemStack instance, ComponentType<AttributeModifiersComponent> dataComponentType, Object o) {
        if (ReinforceHandler.canReinforce(instance)) {
            ReinforceHandler.ReinforceLevel rlevel = ReinforceHandler.getReinforceLevel(instance);
            if (rlevel != null && rlevel.level() > 0) {
                return instance.getOrDefault(DataComponentTypes.ATTRIBUTE_MODIFIERS, AttributeModifiersComponent.DEFAULT).with(EntityAttributes.ATTACK_DAMAGE, new EntityAttributeModifier(Bonfires.reinforceDamageModifier, Bonfires.CONFIG.common.reinforceDamagePerLevel() * rlevel.level(), EntityAttributeModifier.Operation.ADD_VALUE), AttributeModifierSlot.MAINHAND);
            }
        }
        return instance.getOrDefault(dataComponentType, o);
    }
}
