package wehavecookies56.bonfires.setup;

import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import wehavecookies56.bonfires.Bonfires;
import wehavecookies56.bonfires.tiles.BonfireTileEntity;

public class EntitySetup {

    public static final DeferredRegister<BlockEntityType<?>> TILE_ENTITIES = DeferredRegister.create(Registries.BLOCK_ENTITY_TYPE, Bonfires.modid);

    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<BonfireTileEntity>> BONFIRE = TILE_ENTITIES.register("bonfire", () -> new BlockEntityType<>(BonfireTileEntity::new, BlockSetup.ash_bone_pile.get()));

}
