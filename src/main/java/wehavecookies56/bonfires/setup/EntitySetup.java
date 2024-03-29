package wehavecookies56.bonfires.setup;

import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.neoforge.registries.DeferredRegister;
import wehavecookies56.bonfires.Bonfires;
import wehavecookies56.bonfires.tiles.BonfireTileEntity;

import java.util.function.Supplier;

public class EntitySetup {

    public static final DeferredRegister<BlockEntityType<?>> TILE_ENTITIES = DeferredRegister.create(Registries.BLOCK_ENTITY_TYPE, Bonfires.modid);

    public static final Supplier<BlockEntityType<BonfireTileEntity>> BONFIRE = TILE_ENTITIES.register("bonfire", () -> BlockEntityType.Builder.of(BonfireTileEntity::new, BlockSetup.ash_bone_pile.get()).build(null));

}
