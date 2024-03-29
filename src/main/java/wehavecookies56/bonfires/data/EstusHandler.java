package wehavecookies56.bonfires.data;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.attachment.AttachmentType;
import net.neoforged.neoforge.common.util.INBTSerializable;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;
import wehavecookies56.bonfires.Bonfires;

import java.util.UUID;
import java.util.function.Supplier;

/**
 * Created by Toby on 05/11/2016.
 */

public class EstusHandler {

    public static final DeferredRegister<AttachmentType<?>> ATTACHMENT_TYPES = DeferredRegister.create(NeoForgeRegistries.ATTACHMENT_TYPES, Bonfires.modid);
    public static final Supplier<AttachmentType<EstusHandlerInstance>> ESTUS = ATTACHMENT_TYPES.register("estus", () -> AttachmentType.serializable(() -> new EstusHandlerInstance(null)).copyOnDeath().build());


    public static EstusHandlerInstance getHandler(Player player) {
        if (!player.hasData(ESTUS)) {
            player.setData(ESTUS, new EstusHandlerInstance(null));
        }
        return player.getData(ESTUS);
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
        public CompoundTag serializeNBT() {
            final CompoundTag tag = new CompoundTag();
            if (lastRested() != null) {
                tag.putUUID("lastRested", lastRested());
            }
            return tag;
        }

        @Override
        public void deserializeNBT(CompoundTag tag) {
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
