package wehavecookies56.bonfires.data;

import dev.onyxstudios.cca.api.v3.component.ComponentKey;
import dev.onyxstudios.cca.api.v3.component.ComponentRegistry;
import dev.onyxstudios.cca.api.v3.component.sync.AutoSyncedComponent;
import dev.onyxstudios.cca.api.v3.entity.EntityComponentFactoryRegistry;
import dev.onyxstudios.cca.api.v3.entity.EntityComponentInitializer;
import dev.onyxstudios.cca.api.v3.entity.RespawnCopyStrategy;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.Identifier;
import wehavecookies56.bonfires.Bonfires;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class DiscoveryHandler implements EntityComponentInitializer {

    public static final ComponentKey<IDiscoveryHandler> DISCOVERY = ComponentRegistry.getOrCreate(new Identifier(Bonfires.modid, "discovery"), IDiscoveryHandler.class);

    @Override
    public void registerEntityComponentFactories(EntityComponentFactoryRegistry registry) {
        registry.registerForPlayers(DISCOVERY, player -> new DiscoveryHandler.Default(), RespawnCopyStrategy.ALWAYS_COPY);
    }

    public static IDiscoveryHandler getHandler(PlayerEntity player) {
        return DISCOVERY.get(player);
    }

    public interface IDiscoveryHandler extends AutoSyncedComponent {
        void setDiscovered(UUID bonfire, Instant time);
        void discover(UUID bonfire);
        Map<UUID, Instant> getDiscovered();
    }

    public class Default implements IDiscoveryHandler {
        private Map<UUID, Instant> discovered = new HashMap<>();

        @Override
        public void setDiscovered(UUID bonfire, Instant time) {
            discovered.put(bonfire, time);
        }

        @Override
        public void discover(UUID bonfire) {
            if (!discovered.containsKey(bonfire)) {
                discovered.put(bonfire, Instant.now());
            }
        }

        @Override
        public Map<UUID, Instant> getDiscovered() {
            return discovered;
        }

        @Override
        public void readFromNbt(NbtCompound tag) {
            tag.getKeys().forEach(uuidString -> {
                NbtCompound timeTag = tag.getCompound(uuidString);
                Instant time = Instant.ofEpochSecond(timeTag.getLong("epoch"), timeTag.getInt("nano"));
                discovered.put(UUID.fromString(uuidString), time);
            });
        }

        @Override
        public void writeToNbt(NbtCompound tag) {
            discovered.forEach((uuid, instant) -> {
                NbtCompound time = new NbtCompound();
                time.putLong("epoch", instant.getEpochSecond());
                time.putInt("nano", instant.getNano());
                tag.put(uuid.toString(), time);
            });
        }
    }

}
