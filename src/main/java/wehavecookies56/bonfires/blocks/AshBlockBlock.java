package wehavecookies56.bonfires.blocks;

import net.minecraft.block.FallingBlock;
import net.minecraft.sound.BlockSoundGroup;

public class AshBlockBlock extends FallingBlock {

    public AshBlockBlock() {
        super(Settings.create().sounds(BlockSoundGroup.SAND).strength(0.25F));
    }
}
