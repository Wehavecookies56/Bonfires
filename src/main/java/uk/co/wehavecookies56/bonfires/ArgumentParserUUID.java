package uk.co.wehavecookies56.bonfires;

import net.ilexiconn.llibrary.server.command.argument.IArgumentParser;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

/**
 * Created by Toby on 10/11/2016.
 */
public enum ArgumentParserUUID implements IArgumentParser {

    UUID {
        @Override
        public Object parseArgument(MinecraftServer server, ICommandSender sender, String argument) throws CommandException {
            return java.util.UUID.fromString(argument);
        }

        @Override
        public List<String> getTabCompletion(MinecraftServer server, ICommandSender sender, String[] args, BlockPos pos) {
            return Collections.emptyList();
        }
    }
}
