package wehavecookies56.bonfires.items;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import wehavecookies56.bonfires.bonfire.Bonfire;
import wehavecookies56.bonfires.data.BonfireHandler;
import wehavecookies56.bonfires.data.EstusHandler;
import wehavecookies56.bonfires.packets.PacketHandler;
import wehavecookies56.bonfires.packets.client.DisplayBonfireTitle;
import wehavecookies56.bonfires.world.BonfireTeleporter;

import java.util.UUID;

/**
 * Created by Toby on 16/11/2016.
 */
public class HomewardBoneItem extends Item {

    public HomewardBoneItem(Item.Properties properties) {
        super(properties);
    }

    public boolean teleport(Level level, Player player) {
        if (!level.isClientSide) {
            if (player.getServer() != null) {
                UUID lastRested = EstusHandler.getHandler(player).lastRested();
                if (lastRested != null) {
                    Bonfire bonfire = BonfireHandler.getServerHandler(level.getServer()).getRegistry().getBonfire(lastRested);
                    if (bonfire != null) {
                        BonfireTeleporter.travelToBonfire((ServerPlayer) player, bonfire.getPos(), bonfire.getDimension());
                        PacketHandler.sendTo(new DisplayBonfireTitle(bonfire), (ServerPlayer) player);
                        return true;
                    }
                }
            }
        }
        return false;
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level world, Player player, InteractionHand hand) {
        if (teleport(world, player)) {
            player.getItemInHand(hand).shrink(1);
            return InteractionResultHolder.success(player.getItemInHand(hand));
        }
        return InteractionResultHolder.pass(player.getItemInHand(hand));
    }
}
