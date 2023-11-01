package wehavecookies56.bonfires.advancements;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.google.gson.JsonObject;
import net.minecraft.advancements.CriterionTrigger;
import net.minecraft.advancements.CriterionTriggerInstance;
import net.minecraft.advancements.critereon.DeserializationContext;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.PlayerAdvancements;
import net.minecraft.server.level.ServerPlayer;
import wehavecookies56.bonfires.Bonfires;

import java.util.List;
import java.util.Map;
import java.util.Set;

public class BonfireLitTrigger implements CriterionTrigger<BonfireLitTrigger.Instance> {

    public static BonfireLitTrigger TRIGGER_BONFIRE_LIT;

    public static final ResourceLocation ID = new ResourceLocation(Bonfires.modid, "bonfire_lit");
    private final Map<PlayerAdvancements, Listeners> listeners = Maps.newHashMap();

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
    public Instance createInstance(JsonObject pObject, DeserializationContext pConditions) {
        return new BonfireLitTrigger.Instance();
    }

    public void trigger(ServerPlayer player) {
        BonfireLitTrigger.Listeners listeners = this.listeners.get(player.getAdvancements());
        if (listeners != null) {
            listeners.trigger();
        }
    }

    static class Instance implements CriterionTriggerInstance {
        @Override
        public JsonObject serializeToJson() {
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

        void add(CriterionTrigger.Listener<BonfireLitTrigger.Instance> listener) {
            this.listeners.add(listener);
        }

        void remove(CriterionTrigger.Listener<BonfireLitTrigger.Instance> listener) {
            this.listeners.remove(listener);
        }

        public void trigger() {
            List<Listener<Instance>> list = null;

            for (CriterionTrigger.Listener<BonfireLitTrigger.Instance> listener : this.listeners) {
                if (list == null) {
                    list = Lists.newArrayList();
                }
                list.add(listener);
            }
            if (list != null) {
                for (CriterionTrigger.Listener<BonfireLitTrigger.Instance> listener1 : list) {
                    listener1.run(this.playerAdvancements);
                }
            }
        }
    }
}
