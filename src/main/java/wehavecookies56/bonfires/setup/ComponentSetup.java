package wehavecookies56.bonfires.setup;

import net.minecraft.core.component.DataComponentType;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import wehavecookies56.bonfires.Bonfires;
import wehavecookies56.bonfires.blocks.AshBonePileBlock;
import wehavecookies56.bonfires.data.ReinforceHandler;
import wehavecookies56.bonfires.items.EstusFlaskItem;

public class ComponentSetup {

    public static final DeferredRegister.DataComponents COMPONENTS = DeferredRegister.createDataComponents(Bonfires.modid);

    public static final DeferredHolder<DataComponentType<?>, DataComponentType<EstusFlaskItem.Estus>> ESTUS = COMPONENTS.registerComponentType("estus", builder -> builder.persistent(EstusFlaskItem.Estus.CODEC).networkSynchronized(EstusFlaskItem.Estus.STREAM_CODEC));
    public static final DeferredHolder<DataComponentType<?>, DataComponentType<ReinforceHandler.ReinforceLevel>> REINFORCE_LEVEL = COMPONENTS.registerComponentType("reinforce", builder -> builder.persistent(ReinforceHandler.ReinforceLevel.CODEC).networkSynchronized(ReinforceHandler.ReinforceLevel.STREAM_CODEC));
    public static final DeferredHolder<DataComponentType<?>, DataComponentType<AshBonePileBlock.BonfireData>> BONFIRE_DATA = COMPONENTS.registerComponentType("bonfire_data", builder -> builder.persistent(AshBonePileBlock.BonfireData.CODEC).networkSynchronized(AshBonePileBlock.BonfireData.STREAM_CODEC));

}
