package wehavecookies56.bonfires.items;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import wehavecookies56.bonfires.setup.ComponentSetup;
import wehavecookies56.bonfires.setup.ItemSetup;

public class EstusShardItem extends Item {

    public EstusShardItem(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResult use(Level world, Player player, InteractionHand hand) {
        if (!world.isClientSide) {
            for (int i = 0; i < player.getInventory().items.size(); ++i) {
                if (!ItemStack.isSameItem(player.getItemInHand(hand), ItemStack.EMPTY)) {
                    ItemStack stack = player.getInventory().getItem(i);
                    if (!stack.isEmpty()) {
                        if (stack.getItem() == ItemSetup.estus_flask.get()) {
                            if (stack.has(ComponentSetup.ESTUS)) {
                                EstusFlaskItem.Estus estus = stack.get(ComponentSetup.ESTUS);
                                if (estus.maxUses() + player.getItemInHand(hand).getCount() <= 15) {
                                    stack.set(ComponentSetup.ESTUS, new EstusFlaskItem.Estus(estus.uses(), estus.maxUses() + player.getItemInHand(hand).getCount()));
                                    player.setItemInHand(hand, ItemStack.EMPTY);
                                    return InteractionResult.SUCCESS;
                                } else if (estus.maxUses() < 15) {
                                    int remaining = player.getItemInHand(hand).getCount() - (15 - estus.maxUses());
                                    player.setItemInHand(hand, new ItemStack(this, remaining));
                                    stack.set(ComponentSetup.ESTUS, new EstusFlaskItem.Estus(estus.uses(), 15));
                                    return InteractionResult.SUCCESS;
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
