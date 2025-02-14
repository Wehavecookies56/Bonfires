package wehavecookies56.bonfires.blocks;

import com.mojang.serialization.MapCodec;
import net.minecraft.block.FallingBlock;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.util.Identifier;
import wehavecookies56.bonfires.Bonfires;

public class AshBlockBlock extends FallingBlock {

    private static final MapCodec<AshBlockBlock> CODEC = createCodec(AshBlockBlock::new);

    public AshBlockBlock(String name) {
        this(Settings.create().registryKey(RegistryKey.of(RegistryKeys.BLOCK, Identifier.of(Bonfires.modid, name))).sounds(BlockSoundGroup.SAND).strength(0.25F));
    }

    public AshBlockBlock(Settings settings) {
        super(settings);
    }

    @Override
    protected MapCodec<? extends FallingBlock> getCodec() {
        return CODEC;
    }
}
