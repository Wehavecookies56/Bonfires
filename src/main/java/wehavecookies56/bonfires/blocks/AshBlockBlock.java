package wehavecookies56.bonfires.blocks;

import com.mojang.serialization.MapCodec;
import net.minecraft.block.FallingBlock;
import net.minecraft.sound.BlockSoundGroup;

/**
 * Created by Toby on 16/11/2016.
 */
public class AshBlockBlock extends FallingBlock {

    private static final MapCodec<AshBlockBlock> CODEC = createCodec(AshBlockBlock::new);

    public AshBlockBlock() {
        this(Settings.create().sounds(BlockSoundGroup.SAND).strength(0.25F));
    }

    public AshBlockBlock(Settings settings) {
        super(settings);
    }

    @Override
    protected MapCodec<? extends FallingBlock> getCodec() {
        return CODEC;
    }
}
