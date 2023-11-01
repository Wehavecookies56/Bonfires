package wehavecookies56.bonfires.setup;

import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.ForgeRegistries;
import net.neoforged.neoforge.registries.RegistryObject;
import wehavecookies56.bonfires.Bonfires;
import wehavecookies56.bonfires.tiles.BonfireTileEntity;

public class EntitySetup {

    public static final DeferredRegister<BlockEntityType<?>> TILE_ENTITIES = DeferredRegister.create(ForgeRegistries.BLOCK_ENTITY_TYPES, Bonfires.modid);

    public static final RegistryObject<BlockEntityType<BonfireTileEntity>> BONFIRE = TILE_ENTITIES.register("bonfire", () -> BlockEntityType.Builder.of(BonfireTileEntity::new, BlockSetup.ash_bone_pile.get()).build(null));

}
