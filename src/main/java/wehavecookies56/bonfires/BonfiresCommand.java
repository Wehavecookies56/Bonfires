package wehavecookies56.bonfires;

import com.mojang.authlib.GameProfile;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.suggestion.SuggestionProvider;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.*;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import wehavecookies56.bonfires.bonfire.Bonfire;
import wehavecookies56.bonfires.data.BonfireHandler;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

public class BonfiresCommand {

    public static SuggestionProvider<ServerCommandSource> SUGGEST_DIMENSIONS = (context, builder) -> {
        List<String> list = context.getSource().getServer().getWorldRegistryKeys().stream().map(rk -> rk.getValue().toString()).toList();
        list.stream().map(StringArgumentType::escapeIfRequired).forEach(builder::suggest);
        return builder.buildFuture();
    };

    public static SuggestionProvider<ServerCommandSource> SUGGEST_USERNAMES = (context, builder) -> {
        Collection<String> list = context.getSource().getPlayerNames();
        list.stream().map(StringArgumentType::escapeIfRequired).forEach(builder::suggest);
        return builder.buildFuture();
    };

    public static void register(final CommandDispatcher<ServerCommandSource> dispatcher) {
        LiteralArgumentBuilder<ServerCommandSource> builder = CommandManager.literal("bonfires").requires(commandSource -> commandSource.hasPermissionLevel(2));

        builder.then(CommandManager.literal("all").executes(context -> BonfiresCommand.executeCommand(context, "all")));
        builder.then(CommandManager.literal("dim").then(CommandManager.argument("dimension", StringArgumentType.string()).suggests(SUGGEST_DIMENSIONS).executes(context -> BonfiresCommand.executeCommand(context, "dim"))));
        builder.then(CommandManager.literal("name").then(CommandManager.argument("bonfirename", StringArgumentType.string()).executes(context -> BonfiresCommand.executeCommand(context, "name"))));
        builder.then(CommandManager.literal("owner").then(CommandManager.argument("ownername", StringArgumentType.string()).suggests(SUGGEST_USERNAMES).executes(context -> BonfiresCommand.executeCommand(context, "owner"))));
        builder.then(CommandManager.literal("radius").then(CommandManager.argument("searchradius", IntegerArgumentType.integer()).executes(context -> BonfiresCommand.executeCommand(context, "radius"))));
        builder.then(CommandManager.literal("filters").executes(context -> BonfiresCommand.executeCommand(context, "filters")));
        dispatcher.register(builder);
    }


    private static int executeCommand(CommandContext<ServerCommandSource> context, String filter) throws CommandSyntaxException {
        List<Bonfire> query;
        String input;
        MinecraftServer server = context.getSource().getServer();
        switch (filter) {
            case "all":
                query = new ArrayList<>(BonfireHandler.getServerHandler(server).getRegistry().getBonfires().values());
                if (query.isEmpty()) {
                    context.getSource().sendFeedback(() -> Text.translatable(LocalStrings.COMMAND_NOMATCH, "all"), false);
                } else {
                    context.getSource().sendFeedback(() -> Text.translatable(LocalStrings.COMMAND_MATCH, query.size(), "all"), false);
                    listQueriedBonfires(query, context.getSource());
                }
                break;
            case "dim":
                input = StringArgumentType.getString(context, "dimension");
                if (input.contains(":")) {
                    RegistryKey<World> dimensionKey = RegistryKey.of(RegistryKeys.WORLD, new Identifier(input));
                    if (server.getWorldRegistryKeys().contains(dimensionKey)) {
                        query = BonfireHandler.getServerHandler(server).getRegistry().getBonfiresByDimension(dimensionKey.getValue());
                        if (query.isEmpty()) {
                            context.getSource().sendFeedback(() -> Text.translatable(LocalStrings.COMMAND_DIM_NOMATCH, input), false);
                        } else {
                            context.getSource().sendFeedback(() -> Text.translatable(LocalStrings.COMMAND_DIM_MATCH, query.size(), input), false);
                            listQueriedBonfires(query, context.getSource());
                        }
                    } else {
                        context.getSource().sendError(Text.translatable(LocalStrings.COMMAND_DIM_NODIM, input));
                    }
                } else {
                    context.getSource().sendError(Text.translatable(LocalStrings.COMMAND_DIM_INVALID, input));
                }
                break;
            case "name":
                input = StringArgumentType.getString(context, "bonfirename");
                query = BonfireHandler.getServerHandler(server).getRegistry().getBonfiresByName(input);
                if (query.isEmpty()) {
                    context.getSource().sendFeedback(() -> Text.translatable(LocalStrings.COMMAND_NOMATCH, input), false);
                } else {
                    context.getSource().sendFeedback(() -> Text.translatable(LocalStrings.COMMAND_MATCH, query.size(), input), false);
                    listQueriedBonfires(query, context.getSource());
                }
                break;
            case "owner":
                input = StringArgumentType.getString(context, "ownername");
                GameProfile ownerProfile = server.getUserCache().findByName(input).orElse(null);
                if (ownerProfile != null) {
                    UUID ownerID = ownerProfile.getId();
                    if (ownerID != null) {
                        query = BonfireHandler.getServerHandler(server).getRegistry().getBonfiresByOwner(ownerID);
                        if (query.isEmpty()) {
                            context.getSource().sendFeedback(() -> Text.translatable(LocalStrings.COMMAND_NOMATCH, input), false);
                        } else {
                            context.getSource().sendFeedback(() -> Text.translatable(LocalStrings.COMMAND_MATCH, query.size(), input), false);
                            listQueriedBonfires(query, context.getSource());
                        }
                    } else {
                        context.getSource().sendFeedback(() -> Text.translatable(LocalStrings.COMMAND_NOUSER, input), false);
                    }
                }
                break;
            case "radius":
                int radius = IntegerArgumentType.getInteger(context, "searchradius");
                query = BonfireHandler.getServerHandler(server).getRegistry().getBonfiresInRadius(new BlockPos((int) context.getSource().getPosition().x, (int) context.getSource().getPosition().y, (int) context.getSource().getPosition().z), radius, context.getSource().getWorld().getRegistryKey().getValue());
                if (query.isEmpty()) {
                    context.getSource().sendFeedback(() -> Text.translatable(LocalStrings.COMMAND_RADIUS_NOMATCH, radius), false);
                } else {
                    context.getSource().sendFeedback(() -> Text.translatable(LocalStrings.COMMAND_RADIUS_MATCH, query.size(), radius), false);
                    listQueriedBonfires(query, context.getSource());
                }
                break;
            case "filters":
                context.getSource().sendFeedback(() -> Text.translatable(LocalStrings.COMMAND_ALL_DESC, "all"), false);
                context.getSource().sendFeedback(() -> Text.translatable(LocalStrings.COMMAND_DIM_DESC, "dim"), false);
                context.getSource().sendFeedback(() -> Text.translatable(LocalStrings.COMMAND_NAME_DESC, "name"), false);
                context.getSource().sendFeedback(() -> Text.translatable(LocalStrings.COMMAND_OWNER_DESC, "owner"), false);
                context.getSource().sendFeedback(() -> Text.translatable(LocalStrings.COMMAND_RADIUS_DESC, "radius"), false);
                break;
            default:
                return 0;
        }
        return 1;
    }

    private static void listQueriedBonfires(List<Bonfire> query, ServerCommandSource sender) {
        query.forEach((bonfires -> {
            GameProfile owner = sender.getServer().getUserCache().getByUuid(bonfires.getOwner()).orElse(null);
            String name = Text.translatable(LocalStrings.COMMAND_NA).getString();
            if (owner != null) {
                name = owner.getName();
            }
            MutableText messageName = Text.translatable(LocalStrings.COMMAND_NAME, bonfires.getName()).setStyle(Style.EMPTY.
                withClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/travel " + bonfires.getId().toString() + " " + sender.getPlayer().getName().getString())).withHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, Text.translatable(LocalStrings.COMMAND_CLICK_TRAVEL, bonfires.getName())))
            );
            MutableText messageID = Text.translatable(LocalStrings.COMMAND_ID, bonfires.getId()).setStyle(Style.EMPTY.
                withClickEvent(new ClickEvent(ClickEvent.Action.COPY_TO_CLIPBOARD, bonfires.getId().toString())).withHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, Text.translatable("chat.copy.click")))
            );
            String finalName = name;
            MutableText messageOwner = Text.translatable(LocalStrings.COMMAND_OWNER, name).setStyle(Style.EMPTY.
                withClickEvent(new ClickEvent(ClickEvent.Action.COPY_TO_CLIPBOARD, finalName)).withHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, Text.translatable("chat.copy.click")))
            );
            MutableText messagePos = Text.translatable(LocalStrings.COMMAND_POS, bonfires.getPos().getX(), bonfires.getPos().getY(), bonfires.getPos().getZ()).setStyle(Style.EMPTY.
                withClickEvent(new ClickEvent(ClickEvent.Action.COPY_TO_CLIPBOARD, "" + bonfires.getPos().getX() + " " + bonfires.getPos().getY() + " " + bonfires.getPos().getZ())).withHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, Text.translatable("chat.copy.click")))
            );

            Text space = Text.literal(" ");

            sender.sendFeedback(() -> messageName.append(space).append(messageID).append(space).append(messageOwner).append(space).append(messagePos), false);
        }));
    }
}
