package wehavecookies56.bonfires.items;

import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.ToolMaterial;

public class CoiledSwordItem extends SwordItem {

    static ToolMaterial material = new ToolMaterial(null, 105, 8, 4, 1, null);

    public CoiledSwordItem(Properties properties) {
        super(material, 3, -2.4F, properties.stacksTo(1));
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
