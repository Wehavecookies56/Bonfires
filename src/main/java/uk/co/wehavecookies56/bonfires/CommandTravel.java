package uk.co.wehavecookies56.bonfires;

import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.TextFormatting;
import uk.co.wehavecookies56.bonfires.Bonfire;
import uk.co.wehavecookies56.bonfires.BonfireRegistry;
import uk.co.wehavecookies56.bonfires.LocalStrings;
import uk.co.wehavecookies56.bonfires.world.BonfireTeleporter;
import uk.co.wehavecookies56.bonfires.world.BonfireWorldSavedData;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Created by Toby on 17/12/2016.
 */
class CommandTravel extends CommandBase {

    @Override
    public int getRequiredPermissionLevel() {
        return 2;
    }

    @Override
    @Nonnull
    public List<String> getAliases() {
        List<String> aliases = new ArrayList<>();
        aliases.add("travel");
        aliases.add("gotobonfire");
        aliases.add("tpb");
        return aliases;
    }

    @Override
    @Nonnull
    public List<String> getTabCompletions(MinecraftServer server, ICommandSender sender, String[] args, @Nullable BlockPos pos) {
        return super.getTabCompletions(server, sender, args, pos);
    }

    @Override
    @Nonnull
    public String getName() {
        return "travel";
    }

    @Override
    @Nonnull
    public String getUsage(@Nonnull ICommandSender sender) {
        return LocalStrings.COMMAND_TRAVEL_USAGE;
    }

    @Override
    public void execute(@Nonnull MinecraftServer server, @Nonnull ICommandSender sender, @Nonnull String[] args) throws CommandException {
        if (args.length == 1) {
            try {
                UUID id = UUID.fromString(args[0]);
                if (sender instanceof EntityPlayer) {
                    if (BonfireWorldSavedData.get(server.getEntityWorld()).bonfires.getBonfire(id) != null) {
                        Bonfire bonfire = BonfireWorldSavedData.get(server.getEntityWorld()).bonfires.getBonfire(id);
                        BonfireTeleporter tp = new BonfireTeleporter(bonfire.getPos());
                        if (((EntityPlayer)sender).world.provider.getDimension() != bonfire.getDimension()) {
                            ((EntityPlayer)sender).changeDimension(bonfire.dimension, tp);
                        } else {
                            tp.placeEntity(((EntityPlayer)sender).world, (EntityPlayer)sender, ((EntityPlayer) sender).rotationYaw);
                        }
                    }
                }
            } catch (IllegalArgumentException e) {
                TextComponentTranslation message = new TextComponentTranslation(LocalStrings.COMMAND_TRAVEL_INVALID);
                message.getStyle().setColor(TextFormatting.DARK_RED);
                sender.sendMessage(message);
            }
        } else {
            TextComponentTranslation message = new TextComponentTranslation(getUsage(sender));
            message.getStyle().setColor(TextFormatting.RED);
            sender.sendMessage(message);
        }
    }
}
