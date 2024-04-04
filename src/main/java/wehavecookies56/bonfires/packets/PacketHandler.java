package wehavecookies56.bonfires.packets;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.FabricPacket;
import net.fabricmc.fabric.api.networking.v1.PlayerLookup;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import wehavecookies56.bonfires.packets.server.LightBonfire;
import wehavecookies56.bonfires.packets.server.ReinforceItem;
import wehavecookies56.bonfires.packets.server.RequestDimensionsFromServer;
import wehavecookies56.bonfires.packets.server.Travel;

public class PacketHandler {

    public static void init() {
        ServerPlayNetworking.registerGlobalReceiver(LightBonfire.TYPE, (packet, player, responseSender) -> packet.handle(player));
		ServerPlayNetworking.registerGlobalReceiver(ReinforceItem.TYPE, (packet, player, responseSender) -> packet.handle(player));
		ServerPlayNetworking.registerGlobalReceiver(RequestDimensionsFromServer.TYPE, (packet, player, responseSender) -> packet.handle(player));
		ServerPlayNetworking.registerGlobalReceiver(Travel.TYPE, (packet, player, responseSender) -> packet.handle(player));
    }

    /*
	public static final Identifier DELETE_SCREENSHOT = new Identifier(Bonfires.modid, "delete_screenshot");

	public static Map<Identifier, PacketByteBuf> packets = new HashMap<>();

	public static void registerPackets () {
		packets.put(DELETE_SCREENSHOT, );

		//From Server to Client
		registerMessage(OpenBonfireGUI.class, OpenBonfireGUI::encode, OpenBonfireGUI::new, OpenBonfireGUI::handle);
		registerMessage(SyncBonfire.class, SyncBonfire::encode, SyncBonfire::new, SyncBonfire::handle);
		registerMessage(SyncSaveData.class, SyncSaveData::encode, SyncSaveData::new, SyncSaveData::handle);
		registerMessage(SendBonfiresToClient.class, SendBonfiresToClient::encode, SendBonfiresToClient::new, SendBonfiresToClient::handle);
		registerMessage(OpenCreateScreen.class, OpenCreateScreen::encode, OpenCreateScreen::new, OpenCreateScreen::handle);
		registerMessage(DisplayTitle.class, DisplayTitle::encode, DisplayTitle::new, DisplayTitle::handle);
		registerMessage(SyncEstusData.class, SyncEstusData::encode, SyncEstusData::new, SyncEstusData::handle);
		registerMessage(QueueBonfireScreenshot.class, QueueBonfireScreenshot::encode, QueueBonfireScreenshot::new, QueueBonfireScreenshot::handle);
		registerMessage(DeleteScreenshot.class, DeleteScreenshot::encode, DeleteScreenshot::new, DeleteScreenshot::handle);

		//From Client to Server
		registerMessage(LightBonfire.class, LightBonfire::encode, LightBonfire::new, LightBonfire::handle);
		registerMessage(ReinforceItem.class, ReinforceItem::encode, ReinforceItem::new, ReinforceItem::handle);
		registerMessage(Travel.class, Travel::encode, Travel::new, Travel::handle);
		registerMessage(RequestDimensionsFromServer.class, RequestDimensionsFromServer::encode, RequestDimensionsFromServer::new, RequestDimensionsFromServer::handle);

	}

	private static <T extends Packet<T>> void registerMessage (Class<T> clazz, BiConsumer<T, FriendlyByteBuf> encode, Function<FriendlyByteBuf, T> decode, BiConsumer<T, Supplier<NetworkEvent.Context>> handler) {
		HANDLER.registerMessage(packetId++, clazz, Packet::encode, decode, handler);
	}
	*/

	public static void sendTo (FabricPacket packet, ServerPlayerEntity player) {
        ServerPlayNetworking.send(player, packet);
	}

	public static void sendToAll (FabricPacket packet, MinecraftServer server) {
		for (ServerPlayerEntity player : PlayerLookup.all(server)) {
			ServerPlayNetworking.send(player, packet);
		}
	}

	public static void sendToServer(FabricPacket packet) {
        ClientPlayNetworking.send(packet);
	}
/*
	public static void sendToAllAround (Packet<?> packet, double x, double y, double z, double range, ResourceKey<Level> dimension) {
		HANDLER.send(PacketDistributor.NEAR.with(PacketDistributor.TargetPoint.p(x, y, z, range, dimension)), packet);
	}

 */
}

