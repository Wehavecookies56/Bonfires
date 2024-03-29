package wehavecookies56.bonfires.packets;

import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.Level;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.network.PacketDistributor;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlerEvent;
import net.neoforged.neoforge.network.handling.PlayPayloadContext;
import net.neoforged.neoforge.network.registration.IPayloadRegistrar;
import wehavecookies56.bonfires.Bonfires;
import wehavecookies56.bonfires.packets.client.*;
import wehavecookies56.bonfires.packets.server.LightBonfire;
import wehavecookies56.bonfires.packets.server.ReinforceItem;
import wehavecookies56.bonfires.packets.server.RequestDimensionsFromServer;
import wehavecookies56.bonfires.packets.server.Travel;

public class PacketHandler {

	@SubscribeEvent
	public static void register(final RegisterPayloadHandlerEvent event) {
		final IPayloadRegistrar registrar = event.registrar(Bonfires.modid);
		registerPackets(registrar);
	}
	public static void registerPackets (final IPayloadRegistrar registrar) {
		//From Server to Client
		registrar.play(OpenBonfireGUI.ID, OpenBonfireGUI::new, handler -> handler.client(PacketHandler::handlePacket));
		registrar.play(SyncBonfire.ID, SyncBonfire::new, handler -> handler.client(PacketHandler::handlePacket));
		registrar.play(SendBonfiresToClient.ID, SendBonfiresToClient::new, handler -> handler.client(PacketHandler::handlePacket));
		registrar.play(OpenCreateScreen.ID, OpenCreateScreen::new, handler -> handler.client(PacketHandler::handlePacket));
		registrar.play(DisplayTitle.ID, DisplayTitle::new, handler -> handler.client(PacketHandler::handlePacket));
		registrar.play(SyncEstusData.ID, SyncEstusData::new, handler -> handler.client(PacketHandler::handlePacket));
		registrar.play(QueueBonfireScreenshot.ID, QueueBonfireScreenshot::new, handler -> handler.client(PacketHandler::handlePacket));
		registrar.play(DeleteScreenshot.ID, DeleteScreenshot::new, handler -> handler.client(PacketHandler::handlePacket));
		registrar.play(DisplayBonfireTitle.ID, DisplayBonfireTitle::new, handler -> handler.client(PacketHandler::handlePacket));

		//From Client to Server
		registrar.play(LightBonfire.ID, LightBonfire::new, handler -> handler.server(PacketHandler::handlePacket));
		registrar.play(ReinforceItem.ID, ReinforceItem::new, handler -> handler.server(PacketHandler::handlePacket));
		registrar.play(Travel.ID, Travel::new, handler -> handler.server(PacketHandler::handlePacket));
		registrar.play(RequestDimensionsFromServer.ID, RequestDimensionsFromServer::new, handler -> handler.server(PacketHandler::handlePacket));
	}

	public static void sendTo (Packet<?> packet, ServerPlayer player) {
		PacketDistributor.PLAYER.with(player).send(packet);
	}

	public static void sendToAll (Packet<?> packet) {
		PacketDistributor.ALL.noArg().send(packet);
	}

	public static void sendToServer(Packet<?> packet) {
		PacketDistributor.SERVER.noArg().send(packet);
	}

	public static void sendToAllAround (Packet<?> packet, double x, double y, double z, double range, ResourceKey<Level> dimension) {
		PacketDistributor.NEAR.with(PacketDistributor.TargetPoint.p(x, y, z, range, dimension).get()).send(packet);
	}

	public static <T extends Packet<T>>void handlePacket(final T data, final PlayPayloadContext context) {
		context.workHandler().submitAsync(() -> data.handle(context)).exceptionally(e -> {
			Bonfires.LOGGER.warn("Packet \"%s\" handling failed, something is likely broken", data.id());
			return null;
		});
	}

}
