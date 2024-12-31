package org.achymake.essentials.handlers;

import java.util.Random;

public class RandomHandler {
    private Random getRandom() {
        return new Random();
    }
    private double getRandomDouble() {
        return 1.0 - getRandom().nextDouble(1.0);
    }
    private double getRandomInt() {
        return 100 - getRandom().nextInt(100);
    }
    public boolean isTrue(double chance) {
        return chance >= getRandomDouble();
    }
    public boolean isTrue(int chance) {
        return chance >= getRandomInt();
    }
}