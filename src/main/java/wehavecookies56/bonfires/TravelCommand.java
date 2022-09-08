package wehavecookies56.bonfires;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.suggestion.SuggestionProvider;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.command.ISuggestionProvider;
import net.minecraft.command.arguments.EntityArgument;
import net.minecraft.command.arguments.UUIDArgument;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraftforge.fml.server.ServerLifecycleHooks;
import wehavecookies56.bonfires.bonfire.Bonfire;
import wehavecookies56.bonfires.data.BonfireHandler;
import wehavecookies56.bonfires.world.BonfireTeleporter;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Created by Toby on 17/12/2016.
 */
class TravelCommand {

    public static SuggestionProvider<CommandSource> SUGGEST_BONFIRES = (p_198296_0_, p_198296_1_) -> {
        List<String> list = BonfireHandler.getServerHandler(p_198296_0_.getSource().getLevel().getServer()).getRegistry().getBonfires().keySet().stream().map(UUID::toString).collect(Collectors.toList());
        return ISuggestionProvider.suggest(list.stream().map(StringArgumentType::escapeIfRequired), p_198296_1_);
    };

    public static SuggestionProvider<CommandSource> SUGGEST_PLAYERS = (context, builder) -> EntityArgument.players().listSuggestions(context, builder);

    public static void register(final CommandDispatcher<CommandSource> dispatcher) {
        dispatcher.register(
                Commands.literal("travel").requires(commandSource -> commandSource.hasPermission(2))
                    .then(Commands.argument("uuid", UUIDArgument.uuid()).suggests(SUGGEST_BONFIRES)
                            .then(Commands.argument("targets", EntityArgument.players()).suggests(SUGGEST_PLAYERS).executes(TravelCommand::executeCommand)
                    ).executes(TravelCommand::executeCommand))
        );
    }

    private static int executeCommand(CommandContext<CommandSource> context) throws CommandSyntaxException {
        Collection<ServerPlayerEntity> targets = new ArrayList<>();
        if (context.getInput().split(" ").length == 2) {
            targets.add(context.getSource().getPlayerOrException());
        } else {
            targets = EntityArgument.getPlayers(context, "targets");
        }
        UUID id = UUIDArgument.getUuid(context, "uuid");
        for (ServerPlayerEntity target : targets) {
            if (BonfireHandler.getServerHandler(ServerLifecycleHooks.getCurrentServer()).getRegistry().getBonfire(id) != null) {
                Bonfire bonfire = BonfireHandler.getServerHandler(ServerLifecycleHooks.getCurrentServer()).getRegistry().getBonfire(id);
                BonfireTeleporter.travelToBonfire(target, bonfire.getPos(), bonfire.getDimension());
            }
        }
        return 1;
    }
}