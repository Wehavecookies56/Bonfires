package wehavecookies56.bonfires.items;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.world.World;
import wehavecookies56.bonfires.BonfiresGroup;
import wehavecookies56.bonfires.setup.ItemSetup;

/**
 * Created by Toby on 05/11/2016.
 */
public class EstusShardItem extends Item {

    public EstusShardItem() {
        super(new Properties().tab(BonfiresGroup.INSTANCE));
    }

    @Override
    public ActionResult<ItemStack> use(World world, PlayerEntity player, Hand hand) {
        if (!world.isClientSide) {
            for (int i = 0; i < player.inventory.items.size(); ++i) {
                if (!ItemStack.isSame(player.getItemInHand(hand), ItemStack.EMPTY)) {
                    if (!ItemStack.isSame(player.inventory.getItem(i), ItemStack.EMPTY)) {
                        if (player.inventory.getItem(i).getItem() == ItemSetup.estus_flask.get()) {
                            CompoundNBT compound = player.inventory.getItem(i).getTag();
                            if (compound != null) {
                                if (compound.getInt("uses") + player.getItemInHand(hand).getCount() <= 15) {
                                    compound.putInt("uses", compound.getInt("uses") + player.getItemInHand(hand).getCount());
                                    player.setItemInHand(hand, ItemStack.EMPTY);
                                    return ActionResult.success(player.getItemInHand(hand));
                                } else if (compound.getInt("uses") < 15) {
                                    int remaining = player.getItemInHand(hand).getCount() - (15 - compound.getInt("uses"));
                                    player.setItemInHand(hand, new ItemStack(this, remaining));
                                    compound.putInt("uses", 15);
                                    return ActionResult.success(player.getItemInHand(hand));
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
