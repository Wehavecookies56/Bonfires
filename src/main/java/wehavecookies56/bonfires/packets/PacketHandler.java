package wehavecookies56.bonfires.packets;

import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.Level;
import net.minecraftforge.network.ChannelBuilder;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.PacketDistributor;
import net.minecraftforge.network.SimpleChannel;
import wehavecookies56.bonfires.Bonfires;
import wehavecookies56.bonfires.packets.client.*;
import wehavecookies56.bonfires.packets.server.LightBonfire;
import wehavecookies56.bonfires.packets.server.ReinforceItem;
import wehavecookies56.bonfires.packets.server.RequestDimensionsFromServer;
import wehavecookies56.bonfires.packets.server.Travel;

public class PacketHandler {
	private static final int PROTOCOL_VERSION = 1;
	private static final SimpleChannel HANDLER = ChannelBuilder
				.named(new ResourceLocation(Bonfires.modid, "main_channel"))
				.networkProtocolVersion(PROTOCOL_VERSION)
				.acceptedVersions((s, v) -> PROTOCOL_VERSION == v)
				.simpleChannel();

				public static void registerPackets() {
					HANDLER
							//From Server to Client
							.messageBuilder(OpenBonfireGUI.class, NetworkDirection.PLAY_TO_CLIENT)
								.encoder(OpenBonfireGUI::encode)
								.decoder(OpenBonfireGUI::new)
								.consumerMainThread(OpenBonfireGUI::handlePacket)
								.add()
							.messageBuilder(SyncBonfire.class, NetworkDirection.PLAY_TO_CLIENT)
								.encoder(SyncBonfire::encode)
								.decoder(SyncBonfire::new)
								.consumerMainThread(SyncBonfire::handle)
								.add()
							.messageBuilder(SendBonfiresToClient.class, NetworkDirection.PLAY_TO_CLIENT)
								.encoder(SendBonfiresToClient::encode)
								.decoder(SendBonfiresToClient::new)
								.consumerMainThread(SendBonfiresToClient::handle)
								.add()
							.messageBuilder(OpenCreateScreen.class, NetworkDirection.PLAY_TO_CLIENT)
								.encoder(OpenCreateScreen::encode)
								.decoder(OpenCreateScreen::new)
								.consumerMainThread(OpenCreateScreen::handle)
								.add()
							.messageBuilder(DisplayTitle.class, NetworkDirection.PLAY_TO_CLIENT)
								.encoder(DisplayTitle::encode)
								.decoder(DisplayTitle::new)
								.consumerMainThread(DisplayTitle::handle)
								.add()
							.messageBuilder(SyncEstusData.class, NetworkDirection.PLAY_TO_CLIENT)
								.encoder(SyncEstusData::encode)
								.decoder(SyncEstusData::new)
								.consumerMainThread(SyncEstusData::handle)
								.add()
							.messageBuilder(QueueBonfireScreenshot.class, NetworkDirection.PLAY_TO_CLIENT)
								.encoder(QueueBonfireScreenshot::encode)
								.decoder(QueueBonfireScreenshot::new)
								.consumerMainThread(QueueBonfireScreenshot::handle)
								.add()
							.messageBuilder(DeleteScreenshot.class, NetworkDirection.PLAY_TO_CLIENT)
								.encoder(DeleteScreenshot::encode)
								.decoder(DeleteScreenshot::new)
								.consumerMainThread(DeleteScreenshot::handle)
								.add()
							.messageBuilder(DisplayBonfireTitle.class, NetworkDirection.PLAY_TO_CLIENT)
								.encoder(DisplayBonfireTitle::encode)
								.decoder(DisplayBonfireTitle::new)
								.consumerMainThread(DisplayBonfireTitle::handle)
								.add()

							//From Client to Server
							.messageBuilder(LightBonfire.class, NetworkDirection.PLAY_TO_SERVER)
								.encoder(LightBonfire::encode)
								.decoder(LightBonfire::new)
								.consumerMainThread(LightBonfire::handle)
								.add()
							.messageBuilder(ReinforceItem.class, NetworkDirection.PLAY_TO_SERVER)
								.encoder(ReinforceItem::encode)
								.decoder(ReinforceItem::new)
								.consumerMainThread(ReinforceItem::handle)
								.add()
							.messageBuilder(Travel.class, NetworkDirection.PLAY_TO_SERVER)
								.encoder(Travel::encode)
								.decoder(Travel::new)
								.consumerMainThread(Travel::handle)
								.add()
							.messageBuilder(RequestDimensionsFromServer.class, NetworkDirection.PLAY_TO_SERVER)
								.encoder(RequestDimensionsFromServer::encode)
								.decoder(RequestDimensionsFromServer::new)
								.consumerMainThread(RequestDimensionsFromServer::handle)
								.add();
				}

	public static void sendTo (Packet<?> packet, ServerPlayer player) {
		HANDLER.send(packet, PacketDistributor.PLAYER.with(player));
	}

	public static void sendToAll (Packet<?> packet) {
		HANDLER.send(packet, PacketDistributor.ALL.noArg());
	}

	public static void sendToServer(Packet<?> packet) {
		HANDLER.send(packet, PacketDistributor.SERVER.noArg());
	}

	public static void sendToAllAround (Packet<?> packet, double x, double y, double z, double range, ResourceKey<Level> dimension) {
		HANDLER.send(packet, PacketDistributor.NEAR.with(new PacketDistributor.TargetPoint(x, y, z, range, dimension)));
	}

}
