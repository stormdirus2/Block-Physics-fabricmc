package net.fenrir.blockphysics;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.tag.TagRegistry;
import net.minecraft.block.Block;
import net.minecraft.tag.Tag;
import net.minecraft.util.Identifier;

import java.util.logging.Level;
import java.util.logging.Logger;

public class BlockPhysics implements ModInitializer {
    public static final String MOD_ID = "blockphysics";
    public static final String MOD_NAME = "Block Physics";

    public static final Tag<Block> STICKY_BLOCKS = TagRegistry.block(new Identifier(MOD_ID,"sticky_blocks"));
    public static final Tag<Block> PROTECTED_BLOCKS = TagRegistry.block(new Identifier(MOD_ID,"protected_blocks"));
    public static final Tag<Block> FLOATS = TagRegistry.block(new Identifier(MOD_ID,"floats"));
    public static final Tag<Block> USE_DEFAULT_STATE = TagRegistry.block(new Identifier(MOD_ID,"use_default_state"));

    @Override
    public void onInitialize() {
        Logger.getLogger(MOD_ID).log(Level.INFO, "[" + MOD_NAME + "]" + "Initialized");
    }
}

