package wehavecookies56.bonfires.blocks;

import com.mojang.serialization.MapCodec;
import net.minecraft.world.level.block.FallingBlock;
import net.minecraft.world.level.block.SoundType;

public class AshBlockBlock extends FallingBlock {

    private static final MapCodec<AshBlockBlock> CODEC = simpleCodec(AshBlockBlock::new);

    public AshBlockBlock() {
        this(Properties.of().sound(SoundType.SAND).strength(0.25F));
    }

    public AshBlockBlock(Properties properties) {
        super(properties);
    }

    @Override
    protected MapCodec<? extends FallingBlock> codec() {
        return CODEC;
    }
}
