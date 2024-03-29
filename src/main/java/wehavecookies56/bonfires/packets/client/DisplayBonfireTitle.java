package wehavecookies56.bonfires.packets.client;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.fml.loading.FMLEnvironment;
import net.neoforged.neoforge.network.handling.PlayPayloadContext;
import wehavecookies56.bonfires.Bonfires;
import wehavecookies56.bonfires.bonfire.Bonfire;
import wehavecookies56.bonfires.client.ClientPacketHandler;
import wehavecookies56.bonfires.packets.Packet;

public class DisplayBonfireTitle extends Packet<DisplayBonfireTitle> {

    public static final ResourceLocation ID = new ResourceLocation(Bonfires.modid, "display_bonfire_title");

    public DisplayBonfireTitle(FriendlyByteBuf buffer) {
        decode(buffer);
    }

    Bonfire bonfire;

    public DisplayBonfireTitle(Bonfire bonfire) {
        this.bonfire = bonfire;
    }

    @Override
    public void decode(FriendlyByteBuf buffer) {
        bonfire = new Bonfire(buffer.readNbt());
    }

    @Override
    public void write(FriendlyByteBuf buffer) {
        buffer.writeNbt(bonfire.serializeNBT());
    }

    @Override
    public void handle(PlayPayloadContext context) {
        if (FMLEnvironment.dist.isClient()) {
            ClientPacketHandler.displayBonfireTravelled(bonfire);
        }
    }

    @Override
    public ResourceLocation id() {
        return ID;
    }
}
