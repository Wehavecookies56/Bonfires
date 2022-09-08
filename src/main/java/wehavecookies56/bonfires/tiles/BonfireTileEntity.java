package wehavecookies56.bonfires.tiles;

import net.minecraft.block.BlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SUpdateTileEntityPacket;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.text.TextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import wehavecookies56.bonfires.*;
import wehavecookies56.bonfires.bonfire.Bonfire;
import wehavecookies56.bonfires.data.BonfireHandler;
import wehavecookies56.bonfires.setup.EntitySetup;

import javax.annotation.Nullable;
import java.util.UUID;

/**
 * Created by Toby on 06/11/2016.
 */
public class BonfireTileEntity extends TileEntity {

    private boolean bonfire = false;
    private boolean lit = false;
    private UUID id = UUID.randomUUID();

    public enum BonfireType {
        BONFIRE, PRIMAL, NONE
    }

    private BonfireType type = BonfireType.NONE;

    public BonfireTileEntity() {
        super(EntitySetup.BONFIRE.get());
    }

    @Override
    public void load(BlockState state, CompoundNBT compound) {
        super.load(state, compound);
        bonfire = compound.getBoolean("bonfire");
        type = BonfireType.values()[compound.getInt("type")];
        lit = compound.getBoolean("lit");
        id = compound.getUUID("bonfire_id");
    }

    @Override
    public CompoundNBT save(CompoundNBT compound) {
        compound.putBoolean("bonfire", bonfire);
        compound.putInt("type", type.ordinal());
        compound.putBoolean("lit", lit);
        compound.putUUID("bonfire_id", id);
        return super.save(compound);
    }

    public void createBonfire(String name, UUID id, UUID owner, boolean isPublic) {
        Bonfire bonfire = new Bonfire(name, id, owner, this.getBlockPos(), this.level.dimension(), isPublic);
        BonfireHandler.getServerHandler(level.getServer()).addBonfire(bonfire);
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

    @Override
    public CompoundNBT getUpdateTag() {
        return this.save(new CompoundNBT());
    }

    @Override
    public void handleUpdateTag(BlockState state, CompoundNBT tag) {
        this.load(state, tag);
    }

    @Override
    public void onDataPacket(NetworkManager net, SUpdateTileEntityPacket pkt) {
        this.load(level.getBlockState(pkt.getPos()), pkt.getTag());
    }

    @Nullable
    @Override
    public SUpdateTileEntityPacket getUpdatePacket() {
        CompoundNBT compound = new CompoundNBT();
        this.save(compound);
        return new SUpdateTileEntityPacket(this.getBlockPos(), -1, compound);
    }


    @OnlyIn(Dist.CLIENT)
    public TextComponent getDisplayName() {
        if (!Minecraft.getInstance().player.isCrouching()) {
            if (getID() != null && BonfiresConfig.Client.renderTextAboveBonfire) {
                Bonfire bonfire = BonfireHandler.getHandler(Minecraft.getInstance().level).getRegistry().getBonfire(getID());
                if (bonfire != null) {
                    if (bonfire.isPublic()) {
                        return new TranslationTextComponent(bonfire.getName());
                    } else {
                        return new TranslationTextComponent(LocalStrings.TILEENTITY_BONFIRE_LABEL, bonfire.getName());
                    }
                }
            }
        }
        return new TranslationTextComponent("");
    }

}
