package net.fenrir.blockphysics.mixin;

import net.fenrir.blockphysics.BlockHandler;
import net.minecraft.block.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Random;

@Mixin(FireBlock.class)
public abstract class FireBlockMixin extends BlockMixin {

    @Inject(method = "trySpreadingFire", at = @At("RETURN"), cancellable = true)
    protected void fireSpread(World world, BlockPos pos, int spreadFactor, Random rand, int currentAge, CallbackInfo ci) {
        if (!world.isClient()) {
            if (world.getBlockState(pos).isAir()) {
                BlockHandler.updateNeighbors(world,pos);
            }
        }
    }
}
