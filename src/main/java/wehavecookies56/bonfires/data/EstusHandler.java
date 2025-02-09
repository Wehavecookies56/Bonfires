package wehavecookies56.bonfires.data;

import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.common.util.INBTSerializable;
import wehavecookies56.bonfires.Bonfires;

import java.util.UUID;

public class EstusHandler {

    public static EstusHandlerInstance getHandler(Player player) {
        if (!player.hasData(Bonfires.ESTUS)) {
            player.setData(Bonfires.ESTUS, new EstusHandlerInstance(null));
        }
        return player.getData(Bonfires.ESTUS);
    }

    public interface IEstusHandler extends INBTSerializable<CompoundTag> {
        UUID lastRested();
        void setLastRested(UUID id);
    }

    public static class EstusHandlerInstance implements IEstusHandler {
        private UUID bonfire;

        public EstusHandlerInstance(UUID bonfire) {
            this.bonfire = bonfire;
        }

        @Override
        public CompoundTag serializeNBT(HolderLookup.Provider provider) {
            final CompoundTag tag = new CompoundTag();
            if (lastRested() != null) {
                tag.putUUID("lastRested", lastRested());
            }
            return tag;
        }

        @Override
        public void deserializeNBT(HolderLookup.Provider provider, CompoundTag tag) {
            if (tag.contains("lastRested")) {
                setLastRested(tag.getUUID("lastRested"));
            }
        }

        @Override
        public UUID lastRested() {
            return bonfire;
        }

        @Override
        public void setLastRested(UUID id) {
            bonfire = id;
        }
    }

}
