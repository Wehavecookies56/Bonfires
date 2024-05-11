package wehavecookies56.bonfires.tiles;

import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.Connection;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import wehavecookies56.bonfires.BonfiresConfig;
import wehavecookies56.bonfires.LocalStrings;
import wehavecookies56.bonfires.bonfire.Bonfire;
import wehavecookies56.bonfires.data.BonfireHandler;
import wehavecookies56.bonfires.setup.EntitySetup;

import java.time.Instant;
import java.util.UUID;
import javax.annotation.Nullable;

/**
 * Created by Toby on 06/11/2016.
 */
public class BonfireTileEntity extends BlockEntity {

    private boolean bonfire = false;
    private boolean lit = false;
    private UUID id = UUID.randomUUID();

    private Bonfire bonfireInstance;

    private boolean unlitPrivate = false;
    private String unlitName;
    public enum BonfireType {
        BONFIRE, PRIMAL, NONE;

        public static final StreamCodec<FriendlyByteBuf, BonfireType> STREAM_CODEC = StreamCodec.of((byteBuf, type) -> byteBuf.writeInt(type.ordinal()), byteBuf -> BonfireType.values()[byteBuf.readInt()]);
    }

    private BonfireType type = BonfireType.NONE;

    public BonfireTileEntity(BlockPos pos, BlockState state) {
        super(EntitySetup.BONFIRE.get(), pos, state);
    }

    @Override
    public void onLoad() {
        if (bonfireInstance == null) {
            if (level != null) {
                if (!level.isClientSide) {
                    bonfireInstance = BonfireHandler.getServerHandler(level.getServer()).getRegistry().getBonfire(id);
                }
            }
        }
    }

    @Override
    public void loadAdditional(CompoundTag compound, HolderLookup.Provider provider) {
        super.loadAdditional(compound, provider);
        bonfire = compound.getBoolean("bonfire");
        type = BonfireType.values()[compound.getInt("type")];
        lit = compound.getBoolean("lit");
        id = compound.getUUID("bonfire_id");
        if (compound.contains("unlit")) {
            CompoundTag unlit = compound.getCompound("unlit");
            setNameInternal(unlit.getString("name"));
            unlitPrivate = unlit.getBoolean("private");
        }
        if (lit && compound.contains("instance")) {
            bonfireInstance = new Bonfire(compound.getCompound("instance"));
        }
    }

    @Override
    protected void saveAdditional(CompoundTag compound, HolderLookup.Provider provider) {
        compound.putBoolean("bonfire", bonfire);
        compound.putInt("type", type.ordinal());
        compound.putBoolean("lit", lit);
        compound.putUUID("bonfire_id", id);
        if (unlitName != null && !unlitName.isEmpty()) {
            CompoundTag unlit = new CompoundTag();
            unlit.putString("name", unlitName);
            unlit.putBoolean("private", unlitPrivate);
            compound.put("unlit", unlit);
        }
        if (lit && bonfireInstance != null) {
            compound.put("instance", bonfireInstance.serializeNBT());
        }
        super.saveAdditional(compound, provider);
    }



    public Bonfire createBonfire(String name, UUID id, UUID owner, boolean isPublic) {
        Bonfire bonfire = new Bonfire(name, id, owner, this.getBlockPos(), this.level.dimension(), isPublic, Instant.now());
        BonfireHandler.getServerHandler(level.getServer()).addBonfire(bonfire);
        bonfireInstance = bonfire;
        return bonfire;
    }

    public void destroyBonfire(UUID id) {
        BonfireHandler.getServerHandler(level.getServer()).removeBonfire(id);
        bonfireInstance = null;
    }

    public boolean isBonfire() {
        return bonfire;
    }

    public BonfireType getBonfireType() {
        return type;
    }

    public void setBonfireType(BonfireType type) {
        this.type = type;
        setChanged();
    }

    public void setBonfire(boolean bonfire) {
        this.bonfire = bonfire;
        setChanged();
    }

    public boolean isLit() {
        return lit;
    }

    public void setLit(boolean lit) {
        this.lit = lit;
        setChanged();
    }

    public UUID getID() {
        return id;
    }

    public void setID(UUID id) {
        this.id = id;
        setChanged();
    }

    private void setNameInternal(String name) {
        this.unlitName = name.substring(0, Math.min(name.length(), 14));
    }

    public void setUnlitName(String name) {
        setNameInternal(name);
        setChanged();
    }

    public String getUnlitName() {
        return unlitName;
    }

    public void setUnlitPrivate(boolean unlitPrivate) {
        this.unlitPrivate = unlitPrivate;
        setChanged();
    }

    public boolean isUnlitPrivate() {
        return unlitPrivate;
    }

    public boolean hasUnlitName() {
        return unlitName != null && !unlitName.isEmpty();
    }

    @Override
    public CompoundTag getUpdateTag(HolderLookup.Provider provider) {
        CompoundTag tag = new CompoundTag();
        saveAdditional(tag, provider);
        return tag;
    }

    @Override
    public void handleUpdateTag(CompoundTag tag, HolderLookup.Provider provider) {
        this.loadAdditional(tag, provider);
    }

    @Override
    public void onDataPacket(Connection net, ClientboundBlockEntityDataPacket pkt, HolderLookup.Provider provider) {
        this.loadAdditional(pkt.getTag(), provider);
    }

    @Nullable
    @Override
    public ClientboundBlockEntityDataPacket getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }


    @OnlyIn(Dist.CLIENT)
    public Component getDisplayName() {
        if (!Minecraft.getInstance().player.isCrouching()) {
            if (getID() != null && BonfiresConfig.Client.renderTextAboveBonfire) {
                Bonfire bonfire = bonfireInstance;
                if (bonfire != null) {
                    if (bonfire.isPublic()) {
                        return Component.translatable(bonfire.getName());
                    } else {
                        return Component.translatable(LocalStrings.TILEENTITY_BONFIRE_LABEL, bonfire.getName());
                    }
                }
            }
        }
        return Component.empty();
    }

}
