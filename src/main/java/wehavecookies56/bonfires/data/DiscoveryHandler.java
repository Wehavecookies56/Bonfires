package wehavecookies56.bonfires.data;

import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.common.util.INBTSerializable;
import wehavecookies56.bonfires.Bonfires;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class DiscoveryHandler {

    public static IDiscoveryHandler getHandler(Player player) {
        if (!player.hasData(Bonfires.DISCOVERY)) {
            player.setData(Bonfires.DISCOVERY, new DiscoveryHandlerInstance());
        }
        return player.getData(Bonfires.DISCOVERY);
    }

    public interface IDiscoveryHandler extends INBTSerializable<CompoundTag> {
        void setDiscovered(UUID bonfire, Instant time);
        void discover(UUID bonfire);
        Map<UUID, Instant> getDiscovered();
    }

    public static class DiscoveryHandlerInstance implements IDiscoveryHandler {
        private Map<UUID, Instant> discovered = new HashMap<>();

        @Override
        public CompoundTag serializeNBT(HolderLookup.Provider provider) {
            final CompoundTag tag = new CompoundTag();
            discovered.forEach((uuid, instant) -> {
                CompoundTag time = new CompoundTag();
                time.putLong("epoch", instant.getEpochSecond());
                time.putInt("nano", instant.getNano());
                tag.put(uuid.toString(), time);
            });
            return tag;
        }
        @Override
        public void deserializeNBT(HolderLookup.Provider provider, CompoundTag tag) {
            tag.getAllKeys().forEach(uuidString -> {
                CompoundTag timeTag = tag.getCompound(uuidString);
                Instant time = Instant.ofEpochSecond(timeTag.getLong("epoch"), timeTag.getInt("nano"));
                discovered.put(UUID.fromString(uuidString), time);
            });
        }
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
    }
}
