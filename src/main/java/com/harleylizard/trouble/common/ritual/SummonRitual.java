package com.harleylizard.trouble.common.ritual;

import com.harleylizard.trouble.common.registry.ToilAndTroubleRitualTypes;
import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;

public final class SummonRitual implements Ritual {
    public static final Codec<SummonRitual> CODEC = Codec.unit(SummonRitual::new);

    @Override
    public void apply(Level level, BlockPos blockPos) {
    }

    @Override
    public RitualType<?> getType() {
        return ToilAndTroubleRitualTypes.SUMMON;
    }
}
