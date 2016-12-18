package uk.co.wehavecookies56.bonfires;


import com.google.common.base.Function;
import com.google.common.collect.Lists;
import com.mojang.authlib.GameProfile;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.DimensionType;
import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.fml.relauncher.ReflectionHelper;

import javax.annotation.Nullable;
import java.lang.reflect.Field;
import java.util.*;

/**
 * Created by Toby on 17/12/2016.
 */
public class CommandBonfires extends CommandBase {

    @Override
    public List<String> getAliases() {
        List<String> aliases = new ArrayList<>();
        aliases.add("bonfires");
        aliases.add("bonfirelist");
        return aliases;
    }

    @Override
    public int getRequiredPermissionLevel() {
        return 2;
    }

    @Override
    public List<String> getTabCompletions(MinecraftServer server, ICommandSender sender, String[] args, @Nullable BlockPos pos) {
        List<String> filters = new ArrayList<>();
        filters.add("all");
        filters.add("dim");
        filters.add("name");
        filters.add("owner");
        filters.add("radius");
        if (args.length == 1 && !filters.contains(args[0])) {
            return filters;
        } else if (args.length == 2 && args[1].isEmpty()) {
            if (args[0].equals("dim")) {
                try {
                    Field dimensionsF = ReflectionHelper.findField(DimensionManager.class, "dimensions");
                    dimensionsF.setAccessible(true);
                    Object dimIDs = dimensionsF.get(new DimensionManager());
                    Hashtable<Integer, DimensionType> dimensionIDs = (Hashtable<Integer, DimensionType>)dimIDs ;
                    List<Integer> dimList = new ArrayList<>(dimensionIDs.keySet());
                    dimList = Lists.reverse(dimList);
                    Function<Integer, String> toString = new Function<Integer, String>() {
                        @Nullable
                        @Override
                        public String apply(@Nullable Integer input) {
                            return input.toString();
                        }
                    };
                    dimensionsF.setAccessible(false);
                    return Lists.transform(dimList, toString);
                } catch (IllegalAccessException e) {

                }
            }
            else if (args[0].equals("owner")) {
                return new ArrayList<String>(Arrays.asList(sender.getServer().getPlayerProfileCache().getUsernames()));
            }
        }
        return super.getTabCompletions(server, sender, args, pos);
    }

    @Override
    public String getName() {
        return "bonfires";
    }

    @Override
    public String getUsage(ICommandSender sender) {
        return LocalStrings.COMMAND_BONFIRES_USAGE;
    }

    public static void listQueriedBonfires(List<Bonfire> query, ICommandSender sender) {
        query.forEach((bonfires -> {
            GameProfile owner = sender.getServer().getPlayerProfileCache().getProfileByUUID(bonfires.getOwner());
            String name = new TextComponentTranslation(LocalStrings.COMMAND_NA).getUnformattedComponentText();
            if(owner != null) {
                name = owner.getName();
            }
            TextComponentTranslation messageName = new TextComponentTranslation(LocalStrings.COMMAND_NAME, bonfires.getName());
            TextComponentTranslation messageID = new TextComponentTranslation(LocalStrings.COMMAND_ID, bonfires.getId());
            TextComponentTranslation messageOwner = new TextComponentTranslation(LocalStrings.COMMAND_OWNER, bonfires.getOwner().getMostSignificantBits());
            TextComponentTranslation messagePos = new TextComponentTranslation(LocalStrings.COMMAND_POS, bonfires.getPos().getX(), bonfires.getPos().getY(), bonfires.getPos().getZ());

            sender.sendMessage(new TextComponentString(messageName.getUnformattedText() + " " + messageID.getUnformattedText() + " " + messageOwner.getUnformattedText() + " " + messagePos.getUnformattedText()));
        }));
    }

    @Override
    public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
        if (args.length == 2) {
            if (args[0].toLowerCase().equals("dim")) {
                try {
                    if (DimensionManager.isDimensionRegistered(Integer.parseInt(args[1]))) {
                        List<Bonfire> query = BonfireRegistry.INSTANCE.getBonfiresByDimension(Integer.parseInt(args[1]));
                        if (query.isEmpty()) {
                            TextComponentTranslation message = new TextComponentTranslation(LocalStrings.COMMAND_DIM_NOMATCH, Integer.parseInt(args[1]));
                            message.getStyle().setColor(TextFormatting.RED);
                            sender.sendMessage(message);
                        } else {
                            TextComponentTranslation message = new TextComponentTranslation(LocalStrings.COMMAND_DIM_MATCH, query.size(), sender.getServer().worldServerForDimension(Integer.parseInt(args[1])).provider.getDimensionType().getName() + "(" + args[1] + ")");
                            sender.sendMessage(message);
                            listQueriedBonfires(query, sender);
                        }
                    } else {
                        TextComponentTranslation error = new TextComponentTranslation(LocalStrings.COMMAND_DIM_NODIM, Integer.parseInt(args[1]));
                        error.getStyle().setColor(TextFormatting.DARK_RED);
                        sender.sendMessage(error);
                    }
                } catch (NumberFormatException e) {
                    TextComponentTranslation error = new TextComponentTranslation(LocalStrings.COMMAND_DIM_INVALID, args[1]);
                    error.getStyle().setColor(TextFormatting.DARK_RED);
                    sender.sendMessage(error);
                }

            } else if (args[0].toLowerCase().equals("owner")) {
                UUID ownerID = sender.getServer().getPlayerProfileCache().getGameProfileForUsername(args[1]).getId();
                if (ownerID != null) {
                    List<Bonfire> query = BonfireRegistry.INSTANCE.getBonfiresByOwner(ownerID);
                    if (query.isEmpty()) {
                        TextComponentTranslation message = new TextComponentTranslation(LocalStrings.COMMAND_NOMATCH, args[1]);
                        message.getStyle().setColor(TextFormatting.RED);
                        sender.sendMessage(message);
                    } else {
                        TextComponentTranslation message = new TextComponentTranslation(LocalStrings.COMMAND_MATCH, query.size(), args[1]);
                        sender.sendMessage(message);
                        listQueriedBonfires(query, sender);
                    }
                } else {
                    TextComponentTranslation message = new TextComponentTranslation(LocalStrings.COMMAND_NOUSER, args[1]);
                    message.getStyle().setColor(TextFormatting.DARK_RED);
                    sender.sendMessage(message);
                }
            } else if (args[0].toLowerCase().equals("name")) {
                List<Bonfire> query = BonfireRegistry.INSTANCE.getBonfiresByName(args[1]);
                if (query.isEmpty()) {
                    TextComponentTranslation message = new TextComponentTranslation(LocalStrings.COMMAND_NOMATCH, args[1]);
                    message.getStyle().setColor(TextFormatting.RED);
                    sender.sendMessage(message);
                } else {
                    TextComponentTranslation message = new TextComponentTranslation(LocalStrings.COMMAND_MATCH, query.size(), args[1]);
                    sender.sendMessage(message);
                    listQueriedBonfires(query, sender);
                }
            } else if (args[0].toLowerCase().equals("radius")) {
                try {
                    List<Bonfire> query = BonfireRegistry.INSTANCE.getBonfiresInRadius(sender.getPosition(), Integer.parseInt(args[1]), sender.getEntityWorld().provider.getDimension());
                    if (query.isEmpty()) {
                        TextComponentTranslation message = new TextComponentTranslation(LocalStrings.COMMAND_RADIUS_NOMATCH, args[1]);
                        message.getStyle().setColor(TextFormatting.RED);
                        sender.sendMessage(message);
                    } else {
                        TextComponentTranslation message = new TextComponentTranslation(LocalStrings.COMMAND_RADIUS_MATCH, query.size(), args[1]);
                        sender.sendMessage(message);
                        listQueriedBonfires(query, sender);
                    }
                } catch (NumberFormatException e) {
                    TextComponentTranslation error = new TextComponentTranslation(LocalStrings.COMMAND_RADIUS_INVALID, args[1]);
                    error.getStyle().setColor(TextFormatting.DARK_RED);
                    sender.sendMessage(error);
                }
            } else {
                TextComponentTranslation message = new TextComponentTranslation(LocalStrings.COMMAND_FILTER_INVALID);
                message.getStyle().setColor(TextFormatting.RED);
                sender.sendMessage(message);
            }
        } else if (args.length == 1) {
            if (args[0].toLowerCase().equals("filters")) {
                sender.sendMessage(new TextComponentTranslation(LocalStrings.COMMAND_ALL_DESC, "all"));
                sender.sendMessage(new TextComponentTranslation(LocalStrings.COMMAND_DIM_DESC, "dim"));
                sender.sendMessage(new TextComponentTranslation(LocalStrings.COMMAND_NAME_DESC, "name"));
                sender.sendMessage(new TextComponentTranslation(LocalStrings.COMMAND_OWNER_DESC, "owner"));
                sender.sendMessage(new TextComponentTranslation(LocalStrings.COMMAND_RADIUS_DESC, "radius"));
            } else if (args[0].toLowerCase().equals("all")) {
                List<Bonfire> query = new ArrayList<>(BonfireRegistry.INSTANCE.getBonfires().values());
                if (query.isEmpty()) {
                    TextComponentTranslation message = new TextComponentTranslation(LocalStrings.COMMAND_NOMATCH, "all");
                    message.getStyle().setColor(TextFormatting.RED);
                    sender.sendMessage(message);
                } else {
                    TextComponentTranslation message = new TextComponentTranslation(LocalStrings.COMMAND_MATCH, query.size(), "all");
                    sender.sendMessage(message);
                    listQueriedBonfires(query, sender);
                }
            } else {
                TextComponentTranslation message = new TextComponentTranslation(LocalStrings.COMMAND_FILTER_INVALID);
                message.getStyle().setColor(TextFormatting.RED);
                sender.sendMessage(message);
            }
        } else {
            TextComponentTranslation message = new TextComponentTranslation(getUsage(sender));
            message.getStyle().setColor(TextFormatting.RED);
            sender.sendMessage(message);
        }
    }
}
