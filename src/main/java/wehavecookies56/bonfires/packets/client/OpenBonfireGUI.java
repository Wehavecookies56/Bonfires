package wehavecookies56.bonfires.packets.client;

import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;
import net.neoforged.fml.loading.FMLEnvironment;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import net.neoforged.neoforge.server.ServerLifecycleHooks;
import wehavecookies56.bonfires.Bonfires;
import wehavecookies56.bonfires.bonfire.BonfireRegistry;
import wehavecookies56.bonfires.client.ClientPacketHandler;
import wehavecookies56.bonfires.packets.Packet;
import wehavecookies56.bonfires.tiles.BonfireTileEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public record OpenBonfireGUI(BlockPos pos, Map<UUID, String> ownerNames, BonfireRegistry registry, boolean canReinforce, List<ResourceKey<Level>> dimensions) implements Packet {

    public OpenBonfireGUI(BonfireTileEntity bonfire, Map<UUID, String> ownerNames, BonfireRegistry registry, boolean canReinforce) {
        this(bonfire.getBlockPos(), ownerNames, registry, canReinforce, new ArrayList<>(ServerLifecycleHooks.getCurrentServer().levelKeys()));
    }

    public static final Type<OpenBonfireGUI> TYPE = new Type<>(ResourceLocation.fromNamespaceAndPath(Bonfires.modid, "open_bonfire_gui"));

    public static final StreamCodec<FriendlyByteBuf, OpenBonfireGUI> STREAM_CODEC = StreamCodec.composite(
            BlockPos.STREAM_CODEC,
            OpenBonfireGUI::pos,
            Bonfires.OWNER_NAMES,
            OpenBonfireGUI::ownerNames,
            BonfireRegistry.STREAM_CODEC,
            OpenBonfireGUI::registry,
            ByteBufCodecs.BOOL,
            OpenBonfireGUI::canReinforce,
            ByteBufCodecs.collection(ArrayList::new, ResourceKey.streamCodec(Registries.DIMENSION)),
            OpenBonfireGUI::dimensions,
            OpenBonfireGUI::new
    );

    @Override
    public void handle(IPayloadContext context) {
        if (FMLEnvironment.dist.isClient()) {
            ClientPacketHandler.openBonfire(this);
        }
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
