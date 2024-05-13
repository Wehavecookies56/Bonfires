package wehavecookies56.bonfires.items;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;
import wehavecookies56.bonfires.setup.ComponentSetup;
import wehavecookies56.bonfires.setup.ItemSetup;

/**
 * Created by Toby on 05/11/2016.
 */
public class EstusShardItem extends Item {

    public EstusShardItem() {
        super(new Settings());
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity player, Hand hand) {
        if (!world.isClient) {
            for (int i = 0; i < player.getInventory().main.size(); ++i) {
                if (!ItemStack.areItemsEqual(player.getStackInHand(hand), ItemStack.EMPTY)) {
                    ItemStack stack = player.getInventory().getStack(i);
                    if (!stack.isEmpty()) {
                        if (stack.getItem() == ItemSetup.estus_flask) {
                            EstusFlaskItem.Estus estus = stack.get(ComponentSetup.ESTUS);
                            if (estus != null) {
                                if (estus.maxUses() + player.getStackInHand(hand).getCount() <= 15) {
                                    stack.set(ComponentSetup.ESTUS, new EstusFlaskItem.Estus(estus.uses(), estus.maxUses() + player.getStackInHand(hand).getCount()));
                                    player.setStackInHand(hand, ItemStack.EMPTY);
                                    return TypedActionResult.success(player.getStackInHand(hand));
                                } else if (estus.maxUses() < 15) {
                                    int remaining = player.getStackInHand(hand).getCount() - (15 - estus.maxUses());
                                    player.setStackInHand(hand, new ItemStack(this, remaining));
                                    stack.set(ComponentSetup.ESTUS, new EstusFlaskItem.Estus(estus.uses(), 15));
                                    return TypedActionResult.success(player.getStackInHand(hand));
                                }
                            }
                        }
                    }
                }
            }
        }
        return super.use(world, player, hand);
    }
}
