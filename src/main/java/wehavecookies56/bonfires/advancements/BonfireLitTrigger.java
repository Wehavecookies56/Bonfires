package wehavecookies56.bonfires.advancements;

import com.mojang.serialization.Codec;
import net.minecraft.advancement.AdvancementCriterion;
import net.minecraft.advancement.criterion.AbstractCriterion;
import net.minecraft.predicate.entity.LootContextPredicate;
import net.minecraft.predicate.entity.LootContextPredicateValidator;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;
import wehavecookies56.bonfires.Bonfires;

import java.util.Optional;

public class BonfireLitTrigger extends AbstractCriterion<BonfireLitTrigger.Instance> {

    public static BonfireLitTrigger INSTANCE;

    public static final Identifier ID = new Identifier(Bonfires.modid, "bonfire_lit");

    public void trigger(ServerPlayerEntity player) {
        this.trigger(player, p -> true);
    }

    @Override
    public Codec<Instance> getConditionsCodec() {
        return Codec.unit(Instance::new);
    }

    @Override
    public AdvancementCriterion<Instance> create(Instance conditions) {
        return super.create(conditions);
    }

    static class Instance implements Conditions {
        @Override
        public void validate(LootContextPredicateValidator validator) {
            Conditions.super.validate(validator);
        }

        @Override
        public Optional<LootContextPredicate> player() {
            return Optional.empty();
        }
    }
}
