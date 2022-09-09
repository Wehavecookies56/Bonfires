package wehavecookies56.bonfires.blocks;

import net.minecraft.world.level.block.FallingBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.material.Material;

/**
 * Created by Toby on 16/11/2016.
 */
public class AshBlockBlock extends FallingBlock {

    public AshBlockBlock() {
        super(Properties.of(Material.SAND).sound(SoundType.SAND).strength(0.25F));
    }
}
