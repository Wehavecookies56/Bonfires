package wehavecookies56.bonfires.packets;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.Level;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.PacketDistributor;
import net.minecraftforge.network.simple.SimpleChannel;
import wehavecookies56.bonfires.Bonfires;
import wehavecookies56.bonfires.packets.client.*;
import wehavecookies56.bonfires.packets.server.LightBonfire;
import wehavecookies56.bonfires.packets.server.ReinforceItem;
import wehavecookies56.bonfires.packets.server.RequestDimensionsFromServer;
import wehavecookies56.bonfires.packets.server.Travel;

import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.function.Supplier;

public class PacketHandler {
	private static byte packetId = 0;

	private static final String PROTOCOL_VERSION = Integer.toString(1);
	private static final SimpleChannel HANDLER = NetworkRegistry.newSimpleChannel(new ResourceLocation(Bonfires.modid, "main_channel"), () -> PROTOCOL_VERSION, PROTOCOL_VERSION::equals, PROTOCOL_VERSION::equals);

	public static void registerPackets () {
		//From Server to Client
		registerMessage(OpenBonfireGUI.class, OpenBonfireGUI::encode, OpenBonfireGUI::new, OpenBonfireGUI::handle);
		registerMessage(SyncBonfire.class, SyncBonfire::encode, SyncBonfire::new, SyncBonfire::handle);
		registerMessage(SyncReinforceData.class, SyncReinforceData::encode, SyncReinforceData::new, SyncReinforceData::handle);
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

	public static void sendTo (Packet<?> packet, ServerPlayer player) {
		HANDLER.send(PacketDistributor.PLAYER.with(() -> player), packet);
	}

	public static void sendToAll (Packet<?> packet) {
		HANDLER.send(PacketDistributor.ALL.noArg(), packet);
	}

	public static void sendToServer(Packet<?> packet) {
		HANDLER.sendToServer(packet);
	}

	public static void sendToAllAround (Packet<?> packet, double x, double y, double z, double range, ResourceKey<Level> dimension) {
		HANDLER.send(PacketDistributor.NEAR.with(PacketDistributor.TargetPoint.p(x, y, z, range, dimension)), packet);
	}

}
