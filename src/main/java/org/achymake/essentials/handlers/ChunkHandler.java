package org.achymake.essentials.handlers;

import org.bukkit.Chunk;

public record ChunkHandler(Chunk getChunk) {
    @Override
    public Chunk getChunk() {
        return getChunk;
    }
}