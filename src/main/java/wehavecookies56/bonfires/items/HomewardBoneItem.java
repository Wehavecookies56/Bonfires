package wehavecookies56.bonfires.items;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import wehavecookies56.bonfires.BonfiresGroup;
import wehavecookies56.bonfires.bonfire.Bonfire;
import wehavecookies56.bonfires.client.ClientPacketHandler;
import wehavecookies56.bonfires.data.BonfireHandler;
import wehavecookies56.bonfires.data.EstusHandler;
import wehavecookies56.bonfires.world.BonfireTeleporter;

import java.util.UUID;

/**
 * Created by Toby on 16/11/2016.
 */
public class HomewardBoneItem extends Item {

    public HomewardBoneItem() {
        super(new Properties().tab(BonfiresGroup.INSTANCE));
    }

    @Override
    public ActionResult<ItemStack> use(World world, PlayerEntity player, Hand hand) {
        if (!world.isClientSide) {
            if (player.getServer() != null) {
                UUID lastRested = EstusHandler.getHandler(player).lastRested();
                if (lastRested != null) {
                    Bonfire bonfire = BonfireHandler.getServerHandler(world.getServer()).getRegistry().getBonfire(lastRested);
                    if (bonfire != null) {
                        BonfireTeleporter.travelToBonfire((ServerPlayerEntity) player, bonfire.getPos(), bonfire.getDimension());
                        player.getItemInHand(hand).shrink(1);
                        return ActionResult.success(player.getItemInHand(hand));
                    }
                }
            }
        } else {
            UUID lastRested = EstusHandler.getHandler(player).lastRested();
            if (lastRested != null) {
                Bonfire bonfire = BonfireHandler.getHandler(world).getRegistry().getBonfire(lastRested);
                if (bonfire != null) {
                    player.level.playSound(player, player.blockPosition(), SoundEvents.ENDERMAN_TELEPORT, SoundCategory.PLAYERS, 1, 1);
                    player.level.playSound(player, bonfire.getPos(), SoundEvents.ENDERMAN_TELEPORT, SoundCategory.PLAYERS, 1, 1);
                    DistExecutor.safeRunWhenOn(Dist.CLIENT, () -> ClientPacketHandler.displayBonfireTravelled(bonfire));
                    return ActionResult.success(player.getItemInHand(hand));
                }
            }
        }
        return super.use(world, player, hand);
    }
}
