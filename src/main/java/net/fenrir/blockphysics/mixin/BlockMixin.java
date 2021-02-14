package net.fenrir.blockphysics.mixin;

import net.fenrir.blockphysics.BlockHandler;
import net.minecraft.block.*;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;
import net.minecraft.world.explosion.Explosion;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Block.class)
public abstract class BlockMixin {

    @Inject(method = "onBroken", at = @At("RETURN"), cancellable = true)
    protected void blockBroken(WorldAccess world, BlockPos pos, BlockState state, CallbackInfo ci) {
        if (!world.isClient()) {
            BlockHandler.updateNeighbors((World) world, pos);
        }
    }
    @Inject(method = "onPlaced", at = @At("RETURN"), cancellable = true)
    protected void blockPlaced(World world, BlockPos pos, BlockState state, LivingEntity placer, ItemStack itemStack, CallbackInfo ci) {
        if (!world.isClient()) {
            BlockHandler.update(world, pos);
            BlockHandler.updateNeighbors(world, pos);
        }
    }
    @Inject(method = "onDestroyedByExplosion", at = @At("RETURN"), cancellable = true)
    protected void blockExploded(World world, BlockPos pos, Explosion explosion, CallbackInfo ci) {
        if (!world.isClient()) {
            BlockHandler.updateNeighbors(world,pos);
        }
    }
}
