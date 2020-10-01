package com.nik7.upgradecraft.blocks;

import com.nik7.upgradecraft.tileentity.FluidFurnaceTileEntity;
import net.minecraft.block.material.Material;

public class FluidFurnaceBlock extends AbstractMachineBlock {
    public FluidFurnaceBlock() {
        super(Properties.create(Material.ROCK)
                        .notSolid()
                        .setRequiresTool()
                        .hardnessAndResistance(3.5F),
                (state, world) -> new FluidFurnaceTileEntity());
    }
}
