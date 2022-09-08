package wehavecookies56.bonfires.blocks;

import net.minecraft.block.FallingBlock;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;

/**
 * Created by Toby on 16/11/2016.
 */
public class AshBlockBlock extends FallingBlock {

    public AshBlockBlock() {
        super(Properties.of(Material.SAND).sound(SoundType.SAND).strength(0.25F));
    }
}
