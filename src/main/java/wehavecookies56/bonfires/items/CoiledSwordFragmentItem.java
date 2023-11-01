package wehavecookies56.bonfires.items;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.fml.DistExecutor;
import wehavecookies56.bonfires.bonfire.Bonfire;
import wehavecookies56.bonfires.client.ClientPacketHandler;
import wehavecookies56.bonfires.data.BonfireHandler;
import wehavecookies56.bonfires.data.EstusHandler;
import wehavecookies56.bonfires.world.BonfireTeleporter;

import java.util.UUID;

/**
 * Created by Toby on 16/11/2016.
 */
public class CoiledSwordFragmentItem extends Item {

    public CoiledSwordFragmentItem() {
        super(new Properties().stacksTo(1));
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level world, Player player, InteractionHand hand) {
        if (!world.isClientSide) {
            if (player.getServer() != null) {
                UUID lastRested = EstusHandler.getHandler(player).lastRested();
                if (lastRested != null) {
                    Bonfire bonfire = BonfireHandler.getServerHandler(world.getServer()).getRegistry().getBonfire(lastRested);
                    if (bonfire != null) {
                        BonfireTeleporter.travelToBonfire((ServerPlayer) player, bonfire.getPos(), bonfire.getDimension());
                        return InteractionResultHolder.success(player.getItemInHand(hand));
                    }
                }
            }
        } else {
            UUID lastRested = EstusHandler.getHandler(player).lastRested();
            if (lastRested != null) {
                Bonfire bonfire = BonfireHandler.getHandler(world).getRegistry().getBonfire(lastRested);
                if (bonfire != null) {
                    player.level().playSound(player, player.blockPosition(), SoundEvents.ENDERMAN_TELEPORT, SoundSource.PLAYERS, 1, 1);
                    player.level().playSound(player, bonfire.getPos(), SoundEvents.ENDERMAN_TELEPORT, SoundSource.PLAYERS, 1, 1);
                    DistExecutor.safeRunWhenOn(Dist.CLIENT, () -> ClientPacketHandler.displayBonfireTravelled(bonfire));
                    return InteractionResultHolder.success(player.getItemInHand(hand));
                }
            }
        }
        return super.use(world, player, hand);
    }
}
