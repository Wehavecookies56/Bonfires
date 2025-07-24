package wehavecookies56.bonfires.blocks;

import net.minecraft.world.level.block.FallingBlock;
import net.minecraft.world.level.block.SoundType;

public class AshBlockBlock extends FallingBlock {

    public AshBlockBlock() {
        super(Properties.of().sound(SoundType.SAND).strength(0.25F));
    }
}
