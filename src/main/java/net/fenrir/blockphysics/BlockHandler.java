package net.fenrir.blockphysics;

import net.minecraft.block.*;
import net.minecraft.entity.FallingBlockEntity;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class BlockHandler {

    public static boolean blockNotFall(World world, BlockPos pos, Block origin) {
        BlockState state = world.getBlockState(pos);
        Material material = state.getMaterial();
        if (state.isAir()) {
            return false;
        }
        if (material.isLiquid()) {
            return origin.getSlipperiness() > 0.6f || BlockPhysics.FLOATS.contains(origin);
        }
        return true;
    }

    public static boolean blockViable(World world, BlockPos pos) {
        BlockState state = world.getBlockState(pos);
        Block block = state.getBlock();
        if (blockNotFall(world,pos,block)) {
            float resis = state.getBlock().getBlastResistance();
            return resis > 0.0f;
        }
        return false;
    }

    public static boolean upEncased(World world, BlockPos pos) {
        for (int a = -1; a <= 1; a++) {
            for (int b = -1; b <= 1; b++) {
                for (int c = 0; c <= 1; c++) {
                    if (!blockViable(world, pos.north(a).east(b).up(c))) {
                        return false;
                    }
                }
            }
        }
        return true;
    }

    public static boolean upCheck(World world, BlockPos pos) {
        if (!blockViable(world,pos)) {
            return false;
        }
        float resis = world.getBlockState(pos).getBlock().getBlastResistance();
        if (resis >= 1200 || upEncased(world,pos)) {
            return true;
        }
        return upCheck(world,pos.up());
    }

    public static boolean isSticky(BlockState state) {
        return BlockPhysics.STICKY_BLOCKS.contains(state.getBlock()) || state.getSoundGroup() == BlockSoundGroup.GLASS;
    }

    public static boolean check(World world, BlockPos pos, int Recursion) {
        if (!blockViable(world, pos)) {
            if (isSticky(world.getBlockState(pos.up(Recursion)))) {
                return upCheck(world, pos.up(Recursion));
            } else if (isSticky(world.getBlockState(pos.up(Recursion + 1)))) {
                return upCheck(world, pos.up(Recursion + 1));
            }
            return false;
        }
        float resis = world.getBlockState(pos).getBlock().getBlastResistance();
        if (resis >= 1200 || Encased(world, pos)) {
            return true;
        }
        return check(world, pos.down(), Recursion + 1);
    }

    public static int Reach(World world, BlockPos pos) {
        BlockState state = world.getBlockState(pos);
        Block block = state.getBlock();
        double resis = block.getBlastResistance();
        if (BlockPhysics.PROTECTED_BLOCKS.contains(block)) {
            resis = Math.max(resis,3.0D);
        }
        if (state.getSoundGroup() == BlockSoundGroup.GLASS) {
            resis = Math.max(resis,6.0D);
        }
        return (int) Math.ceil(resis);
    }

    public static boolean Encased(World world, BlockPos pos) {
        for (int a = -1; a <= 1; a++) {
            for (int b = -1; b <= 1; b++) {
                for (int c = 0; c <= 1; c++) {
                    if (!blockViable(world, pos.north(a).east(b).down(c))) {
                        return false;
                    }
                }
            }
        }
        return true;
    }

    public static boolean scan(World world, BlockPos pos) {
        for (int a = -1; a <= 1; a++) {
            for (int b = -1; b <= 1; b++) {
                if (a != 0 || b != 0) {
                    for (int i = 1; i <= Reach(world, pos); i++) {
                        if (blockViable(world,pos.north(a * i).east(b * i))) {
                            if (check(world, pos.north(a * i).east(b * i),0)) {
                                return true;
                            }
                        } else {
                            break;
                        }
                    }
                }
            }
        }
        return false;
    }
    public static void fall(World world, BlockPos pos) {
        BlockState state = world.getBlockState(pos);
        FallingBlockEntity fallingBlockEntity = new FallingBlockEntity(world, (double)pos.getX() + 0.5D, pos.getY(), (double)pos.getZ() + 0.5D, state);
        fallingBlockEntity.timeFalling = 1;
        fallingBlockEntity.setHurtEntities(true);
        world.setBlockState(pos,Blocks.AIR.getDefaultState());
        world.spawnEntity(fallingBlockEntity);
    }

    public static void updateNeighbors(World world, BlockPos pos) {
        for (int a = -1; a <= 1; a++) {
            for (int b = -1; b <= 1; b++) {
                for (int c = -1; c <= 1; c++) {
                    if (a != 0 || b != 0 || c != 0) {
                        update(world, pos.north(a).east(b).up(c));
                    }
                }
            }
        }
    }
    public static void update(World world, BlockPos pos) {
        BlockState state = world.getBlockState(pos);
        Block block = state.getBlock();
        if (blockViable(world,pos)) {
            if (!blockNotFall(world,pos.down(),block) && !scan(world,pos)) {
                if (!block.hasBlockEntity()) {
                    fall(world, pos);
                } else {
                    world.breakBlock(pos,true);
                }
                updateNeighbors(world,pos);
            }
        }
    }
}
