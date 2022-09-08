package wehavecookies56.bonfires;


import com.mojang.authlib.GameProfile;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.suggestion.SuggestionProvider;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.command.ISuggestionProvider;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.RegistryKey;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.text.*;
import net.minecraft.util.text.event.ClickEvent;
import net.minecraft.util.text.event.HoverEvent;
import net.minecraft.world.World;
import net.minecraftforge.fml.server.ServerLifecycleHooks;
import wehavecookies56.bonfires.bonfire.Bonfire;
import wehavecookies56.bonfires.data.BonfireHandler;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by Toby on 17/12/2016.
 */
public class BonfiresCommand {

    public static SuggestionProvider<CommandSource> SUGGEST_DIMENSIONS = (context, builder) -> {
        List<String> list = ServerLifecycleHooks.getCurrentServer().levelKeys().stream().map(rk -> rk.location().toString()).collect(Collectors.toList());
        return ISuggestionProvider.suggest(list.stream().map(StringArgumentType::escapeIfRequired), builder);
    };

    public static SuggestionProvider<CommandSource> SUGGEST_USERNAMES = (context, builder) -> {
        Collection<String> list = context.getSource().getOnlinePlayerNames();
        return ISuggestionProvider.suggest(list.stream().map(StringArgumentType::escapeIfRequired), builder);
    };

    public static void register(final CommandDispatcher<CommandSource> dispatcher) {
        LiteralArgumentBuilder<CommandSource> builder = Commands.literal("bonfires").requires(commandSource -> commandSource.hasPermission(2));

        builder.then(Commands.literal("all").executes(context -> BonfiresCommand.executeCommand(context, "all")));
        builder.then(Commands.literal("dim").then(Commands.argument("dimension", StringArgumentType.string()).suggests(SUGGEST_DIMENSIONS).executes(context -> BonfiresCommand.executeCommand(context, "dim"))));
        builder.then(Commands.literal("name").then(Commands.argument("bonfirename", StringArgumentType.string()).executes(context -> BonfiresCommand.executeCommand(context, "name"))));
        builder.then(Commands.literal("owner").then(Commands.argument("ownername", StringArgumentType.string()).suggests(SUGGEST_USERNAMES).executes(context -> BonfiresCommand.executeCommand(context, "owner"))));
        builder.then(Commands.literal("radius").then(Commands.argument("searchradius", IntegerArgumentType.integer()).executes(context -> BonfiresCommand.executeCommand(context, "radius"))));
        builder.then(Commands.literal("filters").executes(context -> BonfiresCommand.executeCommand(context, "filters")));
        dispatcher.register(builder);
    }


    private static int executeCommand(CommandContext<CommandSource> context, String filter) throws CommandSyntaxException {
        List<Bonfire> query;
        String input;
        MinecraftServer server = ServerLifecycleHooks.getCurrentServer();
        switch (filter) {
            case "all":
                query = new ArrayList<>(BonfireHandler.getHandler(context.getSource().getLevel()).getRegistry().getBonfires().values());
                if (query.isEmpty()) {
                    context.getSource().sendSuccess(new TranslationTextComponent(LocalStrings.COMMAND_NOMATCH, "all"), false);
                } else {
                    context.getSource().sendSuccess(new TranslationTextComponent(LocalStrings.COMMAND_MATCH, query.size(), "all"), false);
                    listQueriedBonfires(query, context.getSource());
                }
                break;
            case "dim":
                input = StringArgumentType.getString(context, "dimension");
                if (input.contains(":")) {
                    RegistryKey<World> dimensionKey = RegistryKey.create(Registry.DIMENSION_REGISTRY, new ResourceLocation(input));
                    if (ServerLifecycleHooks.getCurrentServer().levelKeys().contains(dimensionKey)) {
                        query = BonfireHandler.getHandler(context.getSource().getLevel()).getRegistry().getBonfiresByDimension(dimensionKey.location());
                        if (query.isEmpty()) {
                            context.getSource().sendSuccess(new TranslationTextComponent(LocalStrings.COMMAND_DIM_NOMATCH, input), false);
                        } else {
                            context.getSource().sendSuccess(new TranslationTextComponent(LocalStrings.COMMAND_DIM_MATCH, query.size(), input), false);
                            listQueriedBonfires(query, context.getSource());
                        }
                    } else {
                        context.getSource().sendFailure(new TranslationTextComponent(LocalStrings.COMMAND_DIM_NODIM, input));
                    }
                } else {
                    context.getSource().sendFailure(new TranslationTextComponent(LocalStrings.COMMAND_DIM_INVALID, input));
                }
                break;
            case "name":
                input = StringArgumentType.getString(context, "bonfirename");
                query = BonfireHandler.getHandler(context.getSource().getLevel()).getRegistry().getBonfiresByName(input);
                if (query.isEmpty()) {
                    context.getSource().sendSuccess(new TranslationTextComponent(LocalStrings.COMMAND_NOMATCH, input), false);
                } else {
                    context.getSource().sendSuccess(new TranslationTextComponent(LocalStrings.COMMAND_MATCH, query.size(), input), false);
                    listQueriedBonfires(query, context.getSource());
                }
                break;
            case "owner":
                input = StringArgumentType.getString(context, "ownername");
                GameProfile ownerProfile = ServerLifecycleHooks.getCurrentServer().getProfileCache().get(input);
                if (ownerProfile != null) {
                    UUID ownerID = ownerProfile.getId();
                    if (ownerID != null) {
                        query = BonfireHandler.getHandler(context.getSource().getLevel()).getRegistry().getBonfiresByOwner(ownerID);
                        if (query.isEmpty()) {
                            context.getSource().sendSuccess(new TranslationTextComponent(LocalStrings.COMMAND_NOMATCH, input), false);
                        } else {
                            context.getSource().sendSuccess(new TranslationTextComponent(LocalStrings.COMMAND_MATCH, query.size(), input), false);
                            listQueriedBonfires(query, context.getSource());
                        }
                    } else {
                        context.getSource().sendSuccess(new TranslationTextComponent(LocalStrings.COMMAND_NOUSER, input), false);
                    }
                }
                break;
            case "radius":
                int radius = IntegerArgumentType.getInteger(context, "searchradius");
                query = BonfireHandler.getHandler(context.getSource().getLevel()).getRegistry().getBonfiresInRadius(new BlockPos(context.getSource().getPosition()), radius, context.getSource().getLevel().dimension().location());
                if (query.isEmpty()) {
                    context.getSource().sendSuccess(new TranslationTextComponent(LocalStrings.COMMAND_RADIUS_NOMATCH, radius), false);
                } else {
                    context.getSource().sendSuccess(new TranslationTextComponent(LocalStrings.COMMAND_RADIUS_MATCH, query.size(), radius), false);
                    listQueriedBonfires(query, context.getSource());
                }
                break;
            case "filters":
                context.getSource().sendSuccess(new TranslationTextComponent(LocalStrings.COMMAND_ALL_DESC, "all"), false);
                context.getSource().sendSuccess(new TranslationTextComponent(LocalStrings.COMMAND_DIM_DESC, "dim"), false);
                context.getSource().sendSuccess(new TranslationTextComponent(LocalStrings.COMMAND_NAME_DESC, "name"), false);
                context.getSource().sendSuccess(new TranslationTextComponent(LocalStrings.COMMAND_OWNER_DESC, "owner"), false);
                context.getSource().sendSuccess(new TranslationTextComponent(LocalStrings.COMMAND_RADIUS_DESC, "radius"), false);
                break;
            default:
                return 0;
        }
        return 1;
    }

    private static void listQueriedBonfires(List<Bonfire> query, CommandSource sender) {
        query.forEach((bonfires -> {
            GameProfile owner = ServerLifecycleHooks.getCurrentServer().getProfileCache().get(bonfires.getOwner());
            String name = new TranslationTextComponent(LocalStrings.COMMAND_NA).getString();
            if (owner != null) {
                name = owner.getName();
            }
            IFormattableTextComponent messageName = new TranslationTextComponent(LocalStrings.COMMAND_NAME, bonfires.getName()).withStyle(style -> {
                return style.withClickEvent(new ClickEvent(ClickEvent.Action.COPY_TO_CLIPBOARD, bonfires.getName())).withHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new TranslationTextComponent("chat.copy.click")));
            });
            IFormattableTextComponent messageID = new TranslationTextComponent(LocalStrings.COMMAND_ID, bonfires.getId()).withStyle(style -> {
                return style.withClickEvent(new ClickEvent(ClickEvent.Action.COPY_TO_CLIPBOARD, bonfires.getId().toString())).withHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new TranslationTextComponent("chat.copy.click")));
            });
            String finalName = name;
            IFormattableTextComponent messageOwner = new TranslationTextComponent(LocalStrings.COMMAND_OWNER, name).withStyle(style -> {
                return style.withClickEvent(new ClickEvent(ClickEvent.Action.COPY_TO_CLIPBOARD, finalName)).withHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new TranslationTextComponent("chat.copy.click")));
            });
            IFormattableTextComponent messagePos = new TranslationTextComponent(LocalStrings.COMMAND_POS, bonfires.getPos().getX(), bonfires.getPos().getY(), bonfires.getPos().getZ()).withStyle(style -> {
                return style.withClickEvent(new ClickEvent(ClickEvent.Action.COPY_TO_CLIPBOARD, "" + bonfires.getPos().getX() + " " + bonfires.getPos().getY() + " " + bonfires.getPos().getZ())).withHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new TranslationTextComponent("chat.copy.click")));
            });

            StringTextComponent space = new StringTextComponent(" ");

            sender.sendSuccess(messageName.append(space).append(messageID).append(space).append(messageOwner).append(space).append(messagePos), false);
        }));
    }
}
