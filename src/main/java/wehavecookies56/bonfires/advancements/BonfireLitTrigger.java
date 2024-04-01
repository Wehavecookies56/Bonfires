package wehavecookies56.bonfires.advancements;

import com.mojang.serialization.Codec;
import net.minecraft.advancements.Criterion;
import net.minecraft.advancements.CriterionTriggerInstance;
import net.minecraft.advancements.critereon.ContextAwarePredicate;
import net.minecraft.advancements.critereon.CriterionValidator;
import net.minecraft.advancements.critereon.SimpleCriterionTrigger;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import wehavecookies56.bonfires.Bonfires;

import java.util.Optional;

public class BonfireLitTrigger extends SimpleCriterionTrigger<BonfireLitTrigger.Instance> {

    public static BonfireLitTrigger TRIGGER_BONFIRE_LIT;

    public static final ResourceLocation ID = new ResourceLocation(Bonfires.modid, "bonfire_lit");
    @Override
    public Codec<Instance> codec() {
        return Codec.unit(new Instance());
    }

    public void trigger(ServerPlayer player) {
        this.trigger(player, p -> true);
    }

    @Override
    public Criterion<Instance> createCriterion(Instance pTriggerInstance) {
        return super.createCriterion(pTriggerInstance);
    }

    static class Instance implements CriterionTriggerInstance, SimpleInstance {
        @Override
        public void validate(CriterionValidator p_311475_) {
        }

        @Override
        public Optional<ContextAwarePredicate> player() {
            return Optional.empty();
        }
    }
}
