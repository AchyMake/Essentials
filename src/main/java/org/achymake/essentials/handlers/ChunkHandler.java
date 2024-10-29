package org.achymake.essentials.handlers;

import org.bukkit.Chunk;

public record ChunkHandler(Chunk getChunk) {
    public long getChunkKey() {
        return (long) getChunk().getX() & 4294967295L | ((long) getChunk().getZ() & 4294967295L) << 32;
    }
    @Override
    public Chunk getChunk() {
        return getChunk;
    }
}