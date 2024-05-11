package wehavecookies56.bonfires.packets;

import net.minecraft.server.level.ServerPlayer;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.network.PacketDistributor;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;
import wehavecookies56.bonfires.Bonfires;
import wehavecookies56.bonfires.packets.client.*;
import wehavecookies56.bonfires.packets.server.LightBonfire;
import wehavecookies56.bonfires.packets.server.ReinforceItem;
import wehavecookies56.bonfires.packets.server.RequestDimensionsFromServer;
import wehavecookies56.bonfires.packets.server.Travel;

public class PacketHandler {

	@SubscribeEvent
	public static void register(final RegisterPayloadHandlersEvent event) {
		final PayloadRegistrar registrar = event.registrar(Bonfires.modid);
		registerPackets(registrar);
	}
	public static void registerPackets (final PayloadRegistrar registrar) {
		//From Server to Client
		registrar.playToClient(OpenBonfireGUI.TYPE, OpenBonfireGUI.STREAM_CODEC, PacketHandler::handlePacket);
		registrar.playToClient(SyncBonfire.TYPE, SyncBonfire.STREAM_CODEC, PacketHandler::handlePacket);
		registrar.playToClient(SendBonfiresToClient.TYPE, SendBonfiresToClient.STREAM_CODEC, PacketHandler::handlePacket);
		registrar.playToClient(OpenCreateScreen.TYPE, OpenCreateScreen.STREAM_CODEC, PacketHandler::handlePacket);
		registrar.playToClient(DisplayTitle.TYPE, DisplayTitle.STREAM_CODEC, PacketHandler::handlePacket);
		registrar.playToClient(SyncEstusData.TYPE, SyncEstusData.STREAM_CODEC, PacketHandler::handlePacket);
		registrar.playToClient(QueueBonfireScreenshot.TYPE, QueueBonfireScreenshot.STREAM_CODEC, PacketHandler::handlePacket);
		registrar.playToClient(DeleteScreenshot.TYPE, DeleteScreenshot.STREAM_CODEC, PacketHandler::handlePacket);
		registrar.playToClient(DisplayBonfireTitle.TYPE, DisplayBonfireTitle.STREAM_CODEC, PacketHandler::handlePacket);

		//From Client to Server
		registrar.playToServer(LightBonfire.TYPE, LightBonfire.STREAM_CODEC, PacketHandler::handlePacket);
		registrar.playToServer(ReinforceItem.TYPE, ReinforceItem.STREAM_CODEC, PacketHandler::handlePacket);
		registrar.playToServer(Travel.TYPE, Travel.STREAM_CODEC, PacketHandler::handlePacket);
		registrar.playToServer(RequestDimensionsFromServer.TYPE, RequestDimensionsFromServer.STREAM_CODEC, PacketHandler::handlePacket);
	}

	public static void sendTo (Packet packet, ServerPlayer player) {
		PacketDistributor.sendToPlayer(player, packet);
	}

	public static void sendToAll (Packet packet) {
		PacketDistributor.sendToAllPlayers(packet);
	}

	public static void sendToServer(Packet packet) {
		PacketDistributor.sendToServer(packet);
	}

	public static <T extends Packet>void handlePacket(final T data, final IPayloadContext context) {
		context.enqueueWork(() -> data.handle(context)).exceptionally(e -> {
			Bonfires.LOGGER.warn("Packet \"%s\" handling failed, something is likely broken", data.type());
			return null;
		});
	}

}
