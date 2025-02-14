package wehavecookies56.bonfires.items;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.StackReference;
import net.minecraft.item.ItemStack;
import net.minecraft.item.SwordItem;
import net.minecraft.item.ToolMaterial;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.screen.slot.Slot;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.ClickType;
import net.minecraft.util.Identifier;
import wehavecookies56.bonfires.Bonfires;

public class CoiledSwordItem extends SwordItem {

    static ToolMaterial material = new ToolMaterial(null, 105, 8, 4, 1, null);

    public CoiledSwordItem(String name) {
        super(material, 3, -2.4F, new Settings().registryKey(RegistryKey.of(RegistryKeys.ITEM, Identifier.of(Bonfires.modid, name))).maxCount(1));
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
