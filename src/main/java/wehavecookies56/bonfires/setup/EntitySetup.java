package wehavecookies56.bonfires.setup;

import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import wehavecookies56.bonfires.Bonfires;
import wehavecookies56.bonfires.tiles.BonfireTileEntity;

public class EntitySetup {

    public static final BlockEntityType<BonfireTileEntity> BONFIRE = FabricBlockEntityTypeBuilder.create(BonfireTileEntity::new, BlockSetup.ash_bone_pile).build();

    public static void init() {
        Registry.register(Registries.BLOCK_ENTITY_TYPE, new Identifier(Bonfires.modid, "bonfire"), BONFIRE);
    }

}
