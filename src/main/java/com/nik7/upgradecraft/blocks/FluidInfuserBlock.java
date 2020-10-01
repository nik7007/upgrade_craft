package com.nik7.upgradecraft.blocks;

import net.minecraft.block.material.Material;

public class FluidInfuserBlock extends AbstractMachineBlock {
    public FluidInfuserBlock() {
        super(Properties.create(Material.ROCK)
                        .notSolid()
                        .setRequiresTool()
                        .hardnessAndResistance(3.5F),
                (state, world) -> null);
    }
}
