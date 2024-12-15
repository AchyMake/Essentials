package org.achymake.essentials.handlers;

import java.util.Random;

public class RandomHandler {
    public boolean isTrue(double chance) {
        return chance >= 1.0 - new Random().nextDouble(1.0);
    }
    public boolean isTrue(int chance) {
        return chance >= 100 - new Random().nextInt(100);
    }
}