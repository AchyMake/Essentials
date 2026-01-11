package org.achymake.essentials.handlers;

import java.util.UUID;

public class UUIDHandler {
    public UUID get(String uuidString) {
        return UUID.fromString(uuidString);
    }
    public UUID get(byte[] bytes) {
        return UUID.nameUUIDFromBytes(bytes);
    }
    public UUID getRandom() {
        return UUID.randomUUID();
    }
}