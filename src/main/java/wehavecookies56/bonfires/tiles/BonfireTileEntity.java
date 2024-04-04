package wehavecookies56.bonfires.tiles;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.client.MinecraftClient;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.listener.ClientPlayPacketListener;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.s2c.play.BlockEntityUpdateS2CPacket;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import wehavecookies56.bonfires.Bonfires;
import wehavecookies56.bonfires.LocalStrings;
import wehavecookies56.bonfires.bonfire.Bonfire;
import wehavecookies56.bonfires.data.BonfireHandler;
import wehavecookies56.bonfires.setup.EntitySetup;

import java.time.Instant;
import java.util.UUID;

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
        BONFIRE, PRIMAL, NONE
    }

    private BonfireType type = BonfireType.NONE;

    public BonfireTileEntity(BlockPos pos, BlockState state) {
        super(EntitySetup.BONFIRE, pos, state);
    }

    public static void tick(World world, BlockPos pos, BlockState state, BonfireTileEntity be) {
        if (be.bonfireInstance == null) {
            if (world != null) {
                if (!world.isClient) {
                    be.bonfireInstance = BonfireHandler.getServerHandler(world.getServer()).getRegistry().getBonfire(be.id);
                }
            }
        }
    }

    @Override
    public void readNbt(NbtCompound compound) {
        super.readNbt(compound);
        bonfire = compound.getBoolean("bonfire");
        type = BonfireType.values()[compound.getInt("type")];
        lit = compound.getBoolean("lit");
        id = compound.getUuid("bonfire_id");
        if (compound.contains("unlit")) {
            NbtCompound unlit = compound.getCompound("unlit");
            setNameInternal(unlit.getString("name"));
            unlitPrivate = unlit.getBoolean("private");
        }
        if (lit && compound.contains("instance")) {
            bonfireInstance = new Bonfire(compound.getCompound("instance"));
        }
    }

    @Override
    protected void writeNbt(NbtCompound compound) {
        compound.putBoolean("bonfire", bonfire);
        compound.putInt("type", type.ordinal());
        compound.putBoolean("lit", lit);
        compound.putUuid("bonfire_id", id);
        if (unlitName != null && !unlitName.isEmpty()) {
            NbtCompound unlit = new NbtCompound();
            unlit.putString("name", unlitName);
            unlit.putBoolean("private", unlitPrivate);
            compound.put("unlit", unlit);
        }
        if (lit && bonfireInstance != null) {
            compound.put("instance", bonfireInstance.serializeNBT());
        }
        super.writeNbt(compound);
    }

    public Bonfire createBonfire(String name, UUID id, UUID owner, boolean isPublic) {
        Bonfire bonfire = new Bonfire(name, id, owner, this.pos, this.world.getRegistryKey(), isPublic, Instant.now());
        BonfireHandler.getServerHandler(world.getServer()).addBonfire(bonfire);
        bonfireInstance = bonfire;
        return bonfire;
    }

    public void destroyBonfire(UUID id) {
        BonfireHandler.getServerHandler(world.getServer()).removeBonfire(id);
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
        markDirty();
    }

    public void setBonfire(boolean bonfire) {
        this.bonfire = bonfire;
        markDirty();
    }

    public boolean isLit() {
        return lit;
    }

    public void setLit(boolean lit) {
        this.lit = lit;
        markDirty();
    }

    public UUID getID() {
        return id;
    }

    public void setID(UUID id) {
        this.id = id;
        markDirty();
    }

    private void setNameInternal(String name) {
        this.unlitName = name.substring(0, Math.min(name.length(), 14));
    }

    public void setUnlitName(String name) {
        setNameInternal(name);
        markDirty();
    }

    public String getUnlitName() {
        return unlitName;
    }

    public void setUnlitPrivate(boolean unlitPrivate) {
        this.unlitPrivate = unlitPrivate;
        markDirty();
    }

    public boolean isUnlitPrivate() {
        return unlitPrivate;
    }

    public boolean hasUnlitName() {
        return unlitName != null && !unlitName.isEmpty();
    }
    @Nullable
    @Override
    public Packet<ClientPlayPacketListener> toUpdatePacket() {
        return BlockEntityUpdateS2CPacket.create(this);
    }

    @Override
    public NbtCompound toInitialChunkDataNbt() {
        return createNbt();
    }

    @Environment(EnvType.CLIENT)
    public Text getDisplayName() {
        if (!MinecraftClient.getInstance().player.isSneaking()) {
            if (getID() != null && Bonfires.CONFIG.client.renderTextAboveBonfire()) {
                Bonfire bonfire = bonfireInstance;
                if (bonfire != null) {
                    if (bonfire.isPublic()) {
                        return Text.translatable(bonfire.getName());
                    } else {
                        return Text.translatable(LocalStrings.TILEENTITY_BONFIRE_LABEL, bonfire.getName());
                    }
                }
            }
        }
        return Text.empty();
    }

}
