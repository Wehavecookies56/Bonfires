package wehavecookies56.bonfires.advancements;

import com.mojang.serialization.Codec;
import net.minecraft.advancements.CriterionTrigger;
import net.minecraft.advancements.CriterionTriggerInstance;
import net.minecraft.advancements.critereon.ContextAwarePredicate;
import net.minecraft.advancements.critereon.CriterionValidator;
import net.minecraft.advancements.critereon.SimpleCriterionTrigger;
import net.minecraft.core.registries.Registries;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import wehavecookies56.bonfires.Bonfires;

import java.util.Optional;

public class BonfireLitTrigger extends SimpleCriterionTrigger<BonfireLitTrigger.Instance> {

    public static final DeferredRegister<CriterionTrigger<?>> CRITERION_TRIGGERS = DeferredRegister.create(Registries.TRIGGER_TYPE, Bonfires.modid);

    public static final DeferredHolder<CriterionTrigger<?>, ?> TRIGGER_BONFIRE_LIT = CRITERION_TRIGGERS.register("bonfire_lit", BonfireLitTrigger::new);

    @Override
    public Codec<Instance> codec() {
        return Codec.unit(new Instance());
    }

    public void trigger(ServerPlayer player) {
        this.trigger(player, p -> true);
    }

    static class Instance implements CriterionTriggerInstance, SimpleInstance {

        @Override
        public void validate(CriterionValidator p_312552_) {
        }

        @Override
        public Optional<ContextAwarePredicate> player() {
            return Optional.empty();
        }
    }


}
