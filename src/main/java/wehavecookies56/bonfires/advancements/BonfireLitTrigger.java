package wehavecookies56.bonfires.advancements;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.google.gson.JsonObject;
import net.minecraft.advancements.ICriterionInstance;
import net.minecraft.advancements.ICriterionTrigger;
import net.minecraft.advancements.PlayerAdvancements;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.loot.ConditionArrayParser;
import net.minecraft.loot.ConditionArraySerializer;
import net.minecraft.util.ResourceLocation;
import wehavecookies56.bonfires.Bonfires;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class BonfireLitTrigger implements ICriterionTrigger<BonfireLitTrigger.Instance> {

    public static BonfireLitTrigger TRIGGER_BONFIRE_LIT;

    private static final ResourceLocation ID = new ResourceLocation(Bonfires.modid, "bonfire_lit");
    private final Map<PlayerAdvancements, Listeners> listeners = Maps.newHashMap();

    @Override
    @Nonnull
    public ResourceLocation getId() {
        return ID;
    }

    @Override
    public void addPlayerListener(PlayerAdvancements pPlayerAdvancements, Listener<Instance> pListener) {
        BonfireLitTrigger.Listeners listeners = this.listeners.get(pPlayerAdvancements);
        if (listeners == null) {
            listeners = new BonfireLitTrigger.Listeners(pPlayerAdvancements);
            this.listeners.put(pPlayerAdvancements, listeners);
        }
        listeners.add(pListener);
    }

    @Override
    public void removePlayerListener(PlayerAdvancements pPlayerAdvancements, Listener<Instance> pListener) {
        BonfireLitTrigger.Listeners listeners = this.listeners.get(pPlayerAdvancements);

        if (listeners != null) {
            listeners.remove(pListener);
            if (listeners.isEmpty()) {
                this.listeners.remove(pPlayerAdvancements);
            }
        }
    }

    @Override
    public void removePlayerListeners(PlayerAdvancements pPlayerAdvancements) {
        this.listeners.remove(pPlayerAdvancements);
    }

    @Override
    public Instance createInstance(JsonObject pObject, ConditionArrayParser pConditions) {
        return new BonfireLitTrigger.Instance();
    }

    public void trigger(ServerPlayerEntity player) {
        BonfireLitTrigger.Listeners listeners = this.listeners.get(player.getAdvancements());
        if (listeners != null) {
            listeners.trigger();
        }
    }

    static class Instance implements ICriterionInstance {

        @Override
        public ResourceLocation getCriterion() {
            return BonfireLitTrigger.ID;
        }

        @Override
        public JsonObject serializeToJson(ConditionArraySerializer pConditions) {
            return new JsonObject();
        }
    }

    static class Listeners {

        private final PlayerAdvancements playerAdvancements;
        private final Set<Listener<Instance>> listeners = Sets.newHashSet();

        Listeners(PlayerAdvancements playerAdvancements) {
            this.playerAdvancements = playerAdvancements;
        }

        boolean isEmpty() {
            return listeners.isEmpty();
        }

        void add(ICriterionTrigger.Listener<BonfireLitTrigger.Instance> listener) {
            this.listeners.add(listener);
        }

        void remove(ICriterionTrigger.Listener<BonfireLitTrigger.Instance> listener) {
            this.listeners.remove(listener);
        }

        public void trigger() {
            List<Listener<Instance>> list = null;

            for (ICriterionTrigger.Listener<BonfireLitTrigger.Instance> listener : this.listeners) {
                if (list == null) {
                    list = Lists.newArrayList();
                }
                list.add(listener);
            }
            if (list != null) {
                for (ICriterionTrigger.Listener<BonfireLitTrigger.Instance> listener1 : list) {
                    listener1.run(this.playerAdvancements);
                }
            }
        }
    }
}
