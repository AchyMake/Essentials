package org.achymake.essentials;

import org.achymake.essentials.data.Message;
import org.achymake.essentials.handlers.ScheduleHandler;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import java.io.IOException;
import java.net.URL;
import java.util.Scanner;
import java.util.function.Consumer;

public class UpdateChecker {
    private Essentials getInstance() {
        return Essentials.getInstance();
    }
    private FileConfiguration getConfig() {
        return getInstance().getConfig();
    }
    private Message getMessage() {
        return getInstance().getMessage();
    }
    private ScheduleHandler getScheduler() {
        return getInstance().getScheduleHandler();
    }
    public String getResourceID() {
        return "120466";
    }
    public void getUpdate(Player player) {
        if (!player.hasPermission("essentials.event.join.update"))return;
        if (!getConfig().getBoolean("notify-update"))return;
        getScheduler().runLater(new Runnable() {
            @Override
            public void run() {
                getLatest((latest) -> {
                    if (getInstance().version().equals(latest))return;
                    player.sendMessage(getMessage().addColor(getInstance().name() + "&6 has new update"));
                    player.sendMessage(getMessage().addColor("-&a https://www.spigotmc.org/resources/" + getResourceID() + "/"));
                });
            }
        }, 3);
    }
    public void getUpdate() {
        if (!getConfig().getBoolean("notify-update"))return;
        getScheduler().runAsynchronously(new Runnable() {
            @Override
            public void run() {
                getLatest((latest) -> {
                    if (getInstance().version().equals(latest))return;
                    getInstance().sendInfo(getInstance().name() + " has new update:");
                    getInstance().sendInfo("- https://www.spigotmc.org/resources/" + getResourceID() + "/");
                });
            }
        });
    }
    public void getLatest(Consumer<String> consumer) {
        try (var inputStream = new URL("https://api.spigotmc.org/legacy/update.php?resource=" + getResourceID()).openStream()) {
            var scanner = new Scanner(inputStream);
            if (scanner.hasNext()) {
                consumer.accept(scanner.next());
                scanner.close();
            } else inputStream.close();
        } catch (IOException e) {
            getInstance().sendWarning(e.getMessage());
        }
    }
}