package wehavecookies56.bonfires;

import net.minecraft.util.RegistryKey;
import net.minecraft.world.World;

/**
 * Created by Toby on 15/11/2016.
 */
public class LocalStrings {

    public static final String
            COMMAND_MATCH = "command.bonfires.match",
            COMMAND_NOMATCH = "command.bonfires.nomatch",
            COMMAND_NOUSER = "command.bonfires.nouser",
            COMMAND_DIM_MATCH = "command.bonfires.dim.match",
            COMMAND_DIM_NOMATCH = "command.bonfires.dim.nomatch",
            COMMAND_DIM_NODIM = "command.bonfires.dim.nodim",
            COMMAND_DIM_INVALID = "command.bonfires.dim.invalid",
            COMMAND_RADIUS_MATCH = "command.bonfires.radius.match",
            COMMAND_RADIUS_NOMATCH = "command.bonfires.radius.nomatch",
            COMMAND_RADIUS_INVALID = "command.bonfires.radius.invalid",
            COMMAND_NAME = "command.bonfires.name",
            COMMAND_ID = "command.bonfires.id",
            COMMAND_OWNER = "command.bonfires.owner",
            COMMAND_POS = "command.bonfires.pos",
            COMMAND_NA = "command.bonfires.na",
            COMMAND_BONFIRES_USAGE = "command.bonfires.bonfires.usage",
            COMMAND_FILTER_INVALID = "command.bonfires.filter.invalid",
            COMMAND_TRAVEL_USAGE = "command.bonfires.travel.usage",
            COMMAND_TRAVEL_INVALID = "command.bonfires.travel.invalid",

            COMMAND_ALL_DESC = "command.bonfires.all.desc",
            COMMAND_DIM_DESC = "command.bonfires.dim.desc",
            COMMAND_NAME_DESC = "command.bonfires.name.desc",
            COMMAND_OWNER_DESC = "command.bonfires.owner.desc",
            COMMAND_RADIUS_DESC = "command.bonfires.radius.desc",

            BUTTON_TRAVEL = "button.bonfires.travel",
            BUTTON_LEAVE = "button.bonfires.leave",
            BUTTON_ACCEPT = "button.bonfires.accept",
            BUTTON_SET_PRIVATE = "button.bonfires.set_private",
            BUTTON_REINFORCE = "button.bonfires.reinforce",

            TEXT_NAME = "text.bonfires.name",
            TEXT_LIT = "bonfire.lit",
            TEXT_PRIVATE = "text.bonfires.private",
            TEXT_REINFORCE = "text.bonfires.reinforce",
            TEXT_MAX_LEVEL = "text.bonfires.max_level",

            TOOLTIP_REINFORCE = "tooltip.bonfires.reinforce",

            TILEENTITY_BONFIRE_LABEL = "tileentity.bonfires.bonfire.label",

            ITEMGROUP_BONFIRES = "bonfires"
    ;

    public static String getDimensionKey(RegistryKey<World> dimension) {
        return "dimensions." + dimension.location().getNamespace() + "." + dimension.location().getPath();
    }
}
