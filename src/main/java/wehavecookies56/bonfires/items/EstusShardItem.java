package wehavecookies56.bonfires.items;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;
import wehavecookies56.bonfires.Bonfires;
import wehavecookies56.bonfires.setup.ComponentSetup;
import wehavecookies56.bonfires.setup.ItemSetup;

public class EstusShardItem extends Item {

    public EstusShardItem(String name) {
        super(new Settings().registryKey(RegistryKey.of(RegistryKeys.ITEM, Identifier.of(Bonfires.modid, name))));
    }

    @Override
    public ActionResult use(World world, PlayerEntity player, Hand hand) {
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
                                    return ActionResult.SUCCESS;
                                } else if (estus.maxUses() < 15) {
                                    int remaining = player.getStackInHand(hand).getCount() - (15 - estus.maxUses());
                                    player.setStackInHand(hand, new ItemStack(this, remaining));
                                    stack.set(ComponentSetup.ESTUS, new EstusFlaskItem.Estus(estus.uses(), 15));
                                    return ActionResult.SUCCESS;
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
