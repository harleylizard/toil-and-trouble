package com.harleylizard.trouble.common.ritual;

import com.harleylizard.trouble.common.blockentity.BrewingCauldronBlockEntity;
import com.harleylizard.trouble.common.registry.ToilAndTroubleRitualTypes;
import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.Containers;
import net.minecraft.world.level.Level;

public final class EmptyCauldronRitual implements Ritual {
    public static final Codec<EmptyCauldronRitual> CODEC = Codec.unit(EmptyCauldronRitual::new);

    @Override
    public void apply(Level level, BlockPos blockPos) {
        if (!level.isClientSide && level.getBlockEntity(blockPos) instanceof BrewingCauldronBlockEntity blockEntity) {
            var j = blockPos.getX();
            var k = blockPos.getY();
            var l = blockPos.getZ();

            for (var itemStack : blockEntity.getIngredients()) {
                Containers.dropItemStack(level, j, k + 1.0D, l, itemStack);
            }
            blockEntity.clear();

            var x = j + 0.5D;
            var y = k + 0.5D;
            var z = l + 0.5D;
            for (var i = 0; i < 10; i++) {
                level.addParticle(ParticleTypes.CLOUD, x, y, z, 0, 0, 0);
            }
            level.playSound(null, blockPos, SoundEvents.BUCKET_EMPTY, SoundSource.BLOCKS, 1.0F, 1.0F);
        }
    }

    @Override
    public RitualType<?> getType() {
        return ToilAndTroubleRitualTypes.EMPTY_CAULDRON;
    }
}
