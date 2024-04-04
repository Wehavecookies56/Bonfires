package wehavecookies56.bonfires;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.suggestion.SuggestionProvider;
import net.minecraft.command.argument.EntityArgumentType;
import net.minecraft.command.argument.UuidArgumentType;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import wehavecookies56.bonfires.bonfire.Bonfire;
import wehavecookies56.bonfires.data.BonfireHandler;
import wehavecookies56.bonfires.world.BonfireTeleporter;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

class TravelCommand {

    public static SuggestionProvider<ServerCommandSource> SUGGEST_BONFIRES = (context, builder) -> {
        List<String> list = BonfireHandler.getServerHandler(context.getSource().getServer()).getRegistry().getBonfires().keySet().stream().map(UUID::toString).toList();
        list.stream().map(StringArgumentType::escapeIfRequired).forEach(builder::suggest);
        return builder.buildFuture();
    };

    public static SuggestionProvider<ServerCommandSource> SUGGEST_PLAYERS = (context, builder) -> EntityArgumentType.players().listSuggestions(context, builder);

    public static void register(final CommandDispatcher<ServerCommandSource> dispatcher) {
        dispatcher.register(
                CommandManager.literal("travel").requires(commandSource -> commandSource.hasPermissionLevel(2))
                    .then(CommandManager.argument("uuid", UuidArgumentType.uuid()).suggests(SUGGEST_BONFIRES)
                            .then(CommandManager.argument("targets", EntityArgumentType.players()).suggests(SUGGEST_PLAYERS).executes(TravelCommand::executeCommand)
                    ).executes(TravelCommand::executeCommand))
        );
    }

    private static int executeCommand(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
        Collection<ServerPlayerEntity> targets = new ArrayList<>();
        if (context.getInput().split(" ").length == 2) {
            targets.add(context.getSource().getPlayer());
        } else {
            targets = EntityArgumentType.getPlayers(context, "targets");
        }
        UUID id = UuidArgumentType.getUuid(context, "uuid");
        for (ServerPlayerEntity target : targets) {
            if (BonfireHandler.getServerHandler(context.getSource().getServer()).getRegistry().getBonfire(id) != null) {
                Bonfire bonfire = BonfireHandler.getServerHandler(context.getSource().getServer()).getRegistry().getBonfire(id);
                BonfireTeleporter.travelToBonfire(target, bonfire.getPos(), bonfire.getDimension());
            }
        }
        return 1;
    }
}
