package org.achymake.essentials.handlers;

import org.bukkit.World;

public record WorldHandler(World getWorld) {
    public String getName() {
        return getWorld().getName();
    }
    @Override
    public World getWorld() {
        return getWorld;
    }
    public void setMorning() {
        setTime(0);
    }
    public void setDay() {
        setTime(1000);
    }
    public void setNoon() {
        setTime(6000);
    }
    public void setNight() {
        setTime(13000);
    }
    public void setMidnight() {
        setTime(18000);
    }
    public void setTime(long value) {
        getWorld().setTime(value);
    }
    public void addTime(long value) {
        setTime(getWorld().getTime() + value);
    }
    public void removeTime(long value) {
        setTime(getWorld().getTime() - value);
    }
}