package wehavecookies56.bonfires.packets;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.networking.v1.PlayerLookup;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import wehavecookies56.bonfires.packets.client.*;
import wehavecookies56.bonfires.packets.server.LightBonfire;
import wehavecookies56.bonfires.packets.server.ReinforceItem;
import wehavecookies56.bonfires.packets.server.RequestDimensionsFromServer;
import wehavecookies56.bonfires.packets.server.Travel;

public class PacketHandler {

    public static void init() {
		PayloadTypeRegistry.playS2C().register(DeleteScreenshot.TYPE, DeleteScreenshot.STREAM_CODEC);
		PayloadTypeRegistry.playS2C().register(DisplayBonfireTitle.TYPE, DisplayBonfireTitle.STREAM_CODEC);
		PayloadTypeRegistry.playS2C().register(DisplayTitle.TYPE, DisplayTitle.STREAM_CODEC);
		PayloadTypeRegistry.playS2C().register(OpenBonfireGUI.TYPE, OpenBonfireGUI.STREAM_CODEC);
		PayloadTypeRegistry.playS2C().register(OpenCreateScreen.TYPE, OpenCreateScreen.STREAM_CODEC);
		PayloadTypeRegistry.playS2C().register(QueueBonfireScreenshot.TYPE, QueueBonfireScreenshot.STREAM_CODEC);
		PayloadTypeRegistry.playS2C().register(SendBonfiresToClient.TYPE, SendBonfiresToClient.STREAM_CODEC);

		PayloadTypeRegistry.playC2S().register(LightBonfire.TYPE, LightBonfire.STREAM_CODEC);
		PayloadTypeRegistry.playC2S().register(ReinforceItem.TYPE, ReinforceItem.STREAM_CODEC);
		PayloadTypeRegistry.playC2S().register(RequestDimensionsFromServer.TYPE, RequestDimensionsFromServer.STREAM_CODEC);
		PayloadTypeRegistry.playC2S().register(Travel.TYPE, Travel.STREAM_CODEC);

		ServerPlayNetworking.registerGlobalReceiver(LightBonfire.TYPE, (packet, context) -> packet.handle(context.player()));
		ServerPlayNetworking.registerGlobalReceiver(ReinforceItem.TYPE, (packet, context) -> packet.handle(context.player()));
		ServerPlayNetworking.registerGlobalReceiver(RequestDimensionsFromServer.TYPE, (packet, context) -> packet.handle(context.player()));
		ServerPlayNetworking.registerGlobalReceiver(Travel.TYPE, (packet, context) -> packet.handle(context.player()));
    }

	public static void sendTo (CustomPayload packet, ServerPlayerEntity player) {
        ServerPlayNetworking.send(player, packet);
	}

	public static void sendToAll (CustomPayload packet, MinecraftServer server) {
		for (ServerPlayerEntity player : PlayerLookup.all(server)) {
			ServerPlayNetworking.send(player, packet);
		}
	}

	public static void sendToServer(CustomPayload packet) {
        ClientPlayNetworking.send(packet);
	}
}

