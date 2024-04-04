package wehavecookies56.bonfires.items;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;
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
                    if (!ItemStack.areItemsEqual(player.getInventory().getStack(i), ItemStack.EMPTY)) {
                        if (player.getInventory().getStack(i).getItem() == ItemSetup.estus_flask) {
                            NbtCompound compound = player.getInventory().getStack(i).getNbt();
                            if (compound != null) {
                                if (compound.getInt("uses") + player.getStackInHand(hand).getCount() <= 15) {
                                    compound.putInt("uses", compound.getInt("uses") + player.getStackInHand(hand).getCount());
                                    player.setStackInHand(hand, ItemStack.EMPTY);
                                    return TypedActionResult.success(player.getStackInHand(hand));
                                } else if (compound.getInt("uses") < 15) {
                                    int remaining = player.getStackInHand(hand).getCount() - (15 - compound.getInt("uses"));
                                    player.setStackInHand(hand, new ItemStack(this, remaining));
                                    compound.putInt("uses", 15);
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
