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

import java.util.UUID;

/**
 * Created by Toby on 05/11/2016.
 */

public class EstusHandler implements EntityComponentInitializer {

    public static final ComponentKey<IEstusHandler> ESTUS = ComponentRegistry.getOrCreate(new Identifier(Bonfires.modid, "estus"), IEstusHandler.class);

    @Override
    public void registerEntityComponentFactories(EntityComponentFactoryRegistry registry) {
        registry.registerForPlayers(ESTUS, player -> new Default(), RespawnCopyStrategy.ALWAYS_COPY);
    }

    public static IEstusHandler getHandler(PlayerEntity player) {
        return ESTUS.get(player);
    }

    public interface IEstusHandler extends AutoSyncedComponent {
        UUID lastRested();
        void setLastRested(UUID id);
    }

    public static class Default implements IEstusHandler {
        private UUID bonfire;

        @Override
        public UUID lastRested() {
            return bonfire;
        }

        @Override
        public void setLastRested(UUID id) {
            bonfire = id;
        }

        @Override
        public void readFromNbt(NbtCompound tag) {
            if (tag.contains("lastRested")) {
                setLastRested(tag.getUuid("lastRested"));
            }
        }

        @Override
        public void writeToNbt(NbtCompound tag) {
            if (lastRested() != null) {
                tag.putUuid("lastRested", lastRested());
            }
        }
    }

}
