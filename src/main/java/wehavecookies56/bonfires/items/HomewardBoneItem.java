package wehavecookies56.bonfires.items;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;
import wehavecookies56.bonfires.Bonfires;
import wehavecookies56.bonfires.bonfire.Bonfire;
import wehavecookies56.bonfires.data.BonfireHandler;
import wehavecookies56.bonfires.data.EstusHandler;
import wehavecookies56.bonfires.packets.PacketHandler;
import wehavecookies56.bonfires.packets.client.DisplayBonfireTitle;
import wehavecookies56.bonfires.world.BonfireTeleporter;

import java.util.UUID;

public class HomewardBoneItem extends Item {

    public HomewardBoneItem(String name) {
        super(new Settings().registryKey(RegistryKey.of(RegistryKeys.ITEM, Identifier.of(Bonfires.modid, name))));
    }

    public HomewardBoneItem(Settings settings) {
        super(settings);
    }

    public boolean teleport(World world, PlayerEntity player) {
        if (!world.isClient) {
            if (player.getServer() != null) {
                UUID lastRested = EstusHandler.getHandler(player).lastRested();
                if (lastRested != null) {
                    Bonfire bonfire = BonfireHandler.getServerHandler(world.getServer()).getRegistry().getBonfire(lastRested);
                    if (bonfire != null) {
                        BonfireTeleporter.travelToBonfire((ServerPlayerEntity) player, bonfire.getPos(), bonfire.getDimension());
                        PacketHandler.sendTo(new DisplayBonfireTitle(bonfire), (ServerPlayerEntity) player);
                        return true;
                    }
                }
            }
        }
        return false;
    }

    @Override
    public ActionResult use(World world, PlayerEntity player, Hand hand) {
        if (teleport(world, player)) {
            player.getStackInHand(hand).decrement(1);
            return ActionResult.SUCCESS;
        }
        return ActionResult.PASS;
    }
}
