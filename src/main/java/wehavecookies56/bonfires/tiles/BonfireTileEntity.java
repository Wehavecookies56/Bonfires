package wehavecookies56.bonfires.tiles;

import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.Connection;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import wehavecookies56.bonfires.BonfiresConfig;
import wehavecookies56.bonfires.LocalStrings;
import wehavecookies56.bonfires.bonfire.Bonfire;
import wehavecookies56.bonfires.data.BonfireHandler;
import wehavecookies56.bonfires.setup.EntitySetup;

import javax.annotation.Nullable;
import java.time.Instant;
import java.util.UUID;

/**
 * Created by Toby on 06/11/2016.
 */
public class BonfireTileEntity extends BlockEntity {

    private boolean bonfire = false;
    private boolean lit = false;
    private UUID id = UUID.randomUUID();

    private boolean unlitPrivate = false;
    private String unlitName;

    public enum BonfireType {
        BONFIRE, PRIMAL, NONE
    }

    private BonfireType type = BonfireType.NONE;

    public BonfireTileEntity(BlockPos pos, BlockState state) {
        super(EntitySetup.BONFIRE.get(), pos, state);
    }



    @Override
    public void load(CompoundTag compound) {
        super.load(compound);
        bonfire = compound.getBoolean("bonfire");
        type = BonfireType.values()[compound.getInt("type")];
        lit = compound.getBoolean("lit");
        id = compound.getUUID("bonfire_id");
        if (compound.contains("unlit")) {
            CompoundTag unlit = compound.getCompound("unlit");
            setNameInternal(unlit.getString("name"));
            unlitPrivate = unlit.getBoolean("private");
        }
    }

    @Override
    protected void saveAdditional(CompoundTag compound) {
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
        super.saveAdditional(compound);
    }

    public Bonfire createBonfire(String name, UUID id, UUID owner, boolean isPublic) {
        Bonfire bonfire = new Bonfire(name, id, owner, this.getBlockPos(), this.level.dimension(), isPublic, Instant.now());
        BonfireHandler.getServerHandler(level.getServer()).addBonfire(bonfire);
        return bonfire;
    }

    public void destroyBonfire(UUID id) {
        BonfireHandler.getHandler(level).removeBonfire(id);
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
    public CompoundTag getUpdateTag() {
        return serializeNBT();
    }

    @Override
    public void handleUpdateTag(CompoundTag tag) {
        this.load(tag);
    }

    @Override
    public void onDataPacket(Connection net, ClientboundBlockEntityDataPacket pkt) {
        this.load(pkt.getTag());
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
                Bonfire bonfire = BonfireHandler.getHandler(Minecraft.getInstance().level).getRegistry().getBonfire(getID());
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
