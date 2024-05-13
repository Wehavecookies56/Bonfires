package wehavecookies56.bonfires.setup;

import net.minecraft.component.DataComponentType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import wehavecookies56.bonfires.Bonfires;
import wehavecookies56.bonfires.blocks.AshBonePileBlock;
import wehavecookies56.bonfires.data.ReinforceHandler;
import wehavecookies56.bonfires.items.EstusFlaskItem;

public class ComponentSetup {

    public static DataComponentType<EstusFlaskItem.Estus> ESTUS = Registry.register(Registries.DATA_COMPONENT_TYPE, new Identifier(Bonfires.modid, "estus"), DataComponentType.<EstusFlaskItem.Estus>builder().codec(EstusFlaskItem.Estus.CODEC).packetCodec(EstusFlaskItem.Estus.STREAM_CODEC).build());
    public static DataComponentType<ReinforceHandler.ReinforceLevel> REINFORCE_LEVEL = Registry.register(Registries.DATA_COMPONENT_TYPE, new Identifier(Bonfires.modid, "reinforce"), DataComponentType.<ReinforceHandler.ReinforceLevel>builder().codec(ReinforceHandler.ReinforceLevel.CODEC).packetCodec(ReinforceHandler.ReinforceLevel.STREAM_CODEC).build());
    public static DataComponentType<AshBonePileBlock.BonfireData> BONFIRE_DATA = Registry.register(Registries.DATA_COMPONENT_TYPE, new Identifier(Bonfires.modid, "bonfire_data"), DataComponentType.<AshBonePileBlock.BonfireData>builder().codec(AshBonePileBlock.BonfireData.CODEC).packetCodec(AshBonePileBlock.BonfireData.STREAM_CODEC).build());

}
