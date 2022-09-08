package wehavecookies56.bonfires.setup;

import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import wehavecookies56.bonfires.Bonfires;
import wehavecookies56.bonfires.tiles.BonfireTileEntity;

public class EntitySetup {

    public static final DeferredRegister<TileEntityType<?>> TILE_ENTITIES = DeferredRegister.create(ForgeRegistries.TILE_ENTITIES, Bonfires.modid);

    public static final RegistryObject<TileEntityType<BonfireTileEntity>> BONFIRE = TILE_ENTITIES.register("bonfire", () -> TileEntityType.Builder.of(BonfireTileEntity::new, BlockSetup.ash_bone_pile.get()).build(null));

}
