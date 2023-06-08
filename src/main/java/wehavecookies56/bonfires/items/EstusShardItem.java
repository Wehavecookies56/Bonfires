package wehavecookies56.bonfires.items;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import wehavecookies56.bonfires.setup.ItemSetup;

/**
 * Created by Toby on 05/11/2016.
 */
public class EstusShardItem extends Item {

    public EstusShardItem() {
        super(new Properties());
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level world, Player player, InteractionHand hand) {
        if (!world.isClientSide) {
            for (int i = 0; i < player.getInventory().items.size(); ++i) {
                if (!ItemStack.isSameItem(player.getItemInHand(hand), ItemStack.EMPTY)) {
                    if (!ItemStack.isSameItem(player.getInventory().getItem(i), ItemStack.EMPTY)) {
                        if (player.getInventory().getItem(i).getItem() == ItemSetup.estus_flask.get()) {
                            CompoundTag compound = player.getInventory().getItem(i).getTag();
                            if (compound != null) {
                                if (compound.getInt("uses") + player.getItemInHand(hand).getCount() <= 15) {
                                    compound.putInt("uses", compound.getInt("uses") + player.getItemInHand(hand).getCount());
                                    player.setItemInHand(hand, ItemStack.EMPTY);
                                    return InteractionResultHolder.success(player.getItemInHand(hand));
                                } else if (compound.getInt("uses") < 15) {
                                    int remaining = player.getItemInHand(hand).getCount() - (15 - compound.getInt("uses"));
                                    player.setItemInHand(hand, new ItemStack(this, remaining));
                                    compound.putInt("uses", 15);
                                    return InteractionResultHolder.success(player.getItemInHand(hand));
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
