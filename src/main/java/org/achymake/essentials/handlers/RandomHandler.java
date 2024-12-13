package org.achymake.essentials.handlers;

import java.util.Random;

public class RandomHandler {
    public boolean isTrue(double chance) {
        return chance >= 1.0 - new Random().nextDouble(1.0);
    }
}