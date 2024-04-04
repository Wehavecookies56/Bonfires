package wehavecookies56.bonfires.packets.server;

import net.fabricmc.fabric.api.networking.v1.FabricPacket;
import net.fabricmc.fabric.api.networking.v1.PacketType;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import wehavecookies56.bonfires.Bonfires;
import wehavecookies56.bonfires.LocalStrings;
import wehavecookies56.bonfires.advancements.BonfireLitTrigger;
import wehavecookies56.bonfires.blocks.AshBonePileBlock;
import wehavecookies56.bonfires.data.EstusHandler;
import wehavecookies56.bonfires.packets.PacketHandler;
import wehavecookies56.bonfires.packets.client.DisplayTitle;
import wehavecookies56.bonfires.packets.client.QueueBonfireScreenshot;
import wehavecookies56.bonfires.packets.client.SendBonfiresToClient;
import wehavecookies56.bonfires.tiles.BonfireTileEntity;

import java.util.UUID;

public class LightBonfire implements FabricPacket {

    public static final PacketType<LightBonfire> TYPE = PacketType.create(new Identifier(Bonfires.modid, "light_bonfire"), LightBonfire::new);

    private String name;
    private int x, y, z;
    private boolean isPublic, createScreenshot;

    public LightBonfire(PacketByteBuf buffer) {
        decode(buffer);
    }

    public LightBonfire(String name, BonfireTileEntity bonfire, boolean isPublic, boolean createScreenshot) {
        this.name = name;
        this.x = bonfire.getPos().getX();
        this.y = bonfire.getPos().getY();
        this.z = bonfire.getPos().getZ();
        this.isPublic = isPublic;
        this.createScreenshot = createScreenshot;
    }

    public void decode(PacketByteBuf buffer) {
        this.name = buffer.readString();
        this.x = buffer.readInt();
        this.y = buffer.readInt();
        this.z = buffer.readInt();
        this.isPublic = buffer.readBoolean();
        this.createScreenshot = buffer.readBoolean();
    }

    public void handle(ServerPlayerEntity player) {
        BlockPos pos = new BlockPos(x, y, z);
        if (player != null) {
            BonfireTileEntity te = (BonfireTileEntity) player.getWorld().getBlockEntity(pos);
            if (te != null && !te.isLit()) {
                te.setLit(true);
                UUID id = UUID.randomUUID();
                te.createBonfire(name, id, player.getUuid(), isPublic);
                te.setID(id);
                player.getWorld().setBlockState(pos, player.getWorld().getBlockState(pos).with(AshBonePileBlock.LIT, true), 2);
                player.setSpawnPoint(te.getWorld().getRegistryKey(), te.getPos(), player.getYaw(), false, true);
                EstusHandler.getHandler(player).setLastRested(te.getID());
                BonfireLitTrigger.INSTANCE.trigger(player);
                player.incrementStat(Bonfires.BONFIRES_LIT);
                //PacketHandler.sendToAll(new SyncBonfire(te.isBonfire(), te.getBonfireType(), te.isLit(), te.getID(), te));
                //PacketHandler.sendToAll(new SyncSaveData(BonfireHandler.getHandler(player.level()).getRegistry().getBonfires()));
                PacketHandler.sendToAll(new SendBonfiresToClient(player.server), player.server);
                if (createScreenshot) {
                    PacketHandler.sendTo(new QueueBonfireScreenshot(name, id), player);
                }
                PacketHandler.sendTo(new DisplayTitle(LocalStrings.TEXT_LIT, name, 15, 20, 15), player);
                Bonfires.LOGGER.info("Bonfire" + "'" + name + "'" + " lit at: X" + x + " Y" + y + " Z" + z + " by " + player.getDisplayName().getString());
            }
        }
    }

    @Override
    public void write(PacketByteBuf buffer) {
        buffer.writeString(name);
        buffer.writeInt(x);
        buffer.writeInt(y);
        buffer.writeInt(z);
        buffer.writeBoolean(isPublic);
        buffer.writeBoolean(createScreenshot);
    }

    @Override
    public PacketType<?> getType() {
        return TYPE;
    }
}