package wehavecookies56.bonfires.items;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;
import wehavecookies56.bonfires.Bonfires;

public class CoiledSwordFragmentItem extends HomewardBoneItem {

    public CoiledSwordFragmentItem(String name) {
        super(new Settings().registryKey(RegistryKey.of(RegistryKeys.ITEM, Identifier.of(Bonfires.modid, name))).maxCount(1));
    }

    @Override
    public ActionResult use(World world, PlayerEntity player, Hand hand) {
        if (teleport(world, player)) {
            return ActionResult.SUCCESS;
        }
        return ActionResult.PASS;
    }
}
