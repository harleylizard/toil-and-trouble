package com.harleylizard.trouble.common.ritual;

import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;

public final class EmptyCauldronRitual implements Ritual {
    public static final Codec<EmptyCauldronRitual> CODEC = Codec.unit(EmptyCauldronRitual::new);

    @Override
    public void apply(Level level, BlockPos blockPos) {

    }
}