package uk.co.wehavecookies56.bonfires.blocks;

import net.minecraft.block.BlockFalling;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.Item;
import uk.co.wehavecookies56.bonfires.Bonfires;

import javax.annotation.Nullable;
import java.util.Random;

/**
 * Created by Toby on 16/11/2016.
 */
public class BlockAshBlock extends BlockFalling {

    public BlockAshBlock(String name, Material blockMaterialIn) {
        super(blockMaterialIn);
        setRegistryName(Bonfires.modid, name);
        setUnlocalizedName(getRegistryName().toString().replace(Bonfires.modid + ":", ""));
        setCreativeTab(Bonfires.tabBonfires);
    }

    @Nullable
    @Override
    public Item getItemDropped(IBlockState state, Random rand, int fortune) {
        return Bonfires.ashPile;
    }

    @Override
    public int quantityDropped(Random random) {
        return 4;
    }
}
