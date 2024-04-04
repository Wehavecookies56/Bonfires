package wehavecookies56.bonfires.advancements;

import com.google.gson.JsonObject;
import net.minecraft.advancement.criterion.AbstractCriterion;
import net.minecraft.advancement.criterion.AbstractCriterionConditions;
import net.minecraft.advancement.criterion.CriterionConditions;
import net.minecraft.predicate.entity.AdvancementEntityPredicateDeserializer;
import net.minecraft.predicate.entity.AdvancementEntityPredicateSerializer;
import net.minecraft.predicate.entity.LootContextPredicate;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;
import wehavecookies56.bonfires.Bonfires;

public class BonfireLitTrigger extends AbstractCriterion<BonfireLitTrigger.Instance> {

    public static BonfireLitTrigger INSTANCE;

    private static final Identifier ID = new Identifier(Bonfires.modid, "bonfire_lit");

    public void trigger(ServerPlayerEntity player) {
        this.trigger(player, p -> true);
    }

    @Override
    protected Instance conditionsFromJson(JsonObject obj, LootContextPredicate playerPredicate, AdvancementEntityPredicateDeserializer predicateDeserializer) {
        return new Instance(ID, playerPredicate);
    }

    @Override
    public Identifier getId() {
        return ID;
    }

    static class Instance extends AbstractCriterionConditions implements CriterionConditions {

        public Instance(Identifier id, LootContextPredicate entity) {
            super(id, entity);
        }

        @Override
        public Identifier getId() {
            return ID;
        }

        @Override
        public JsonObject toJson(AdvancementEntityPredicateSerializer predicateSerializer) {
            return new JsonObject();
        }
    }
}
