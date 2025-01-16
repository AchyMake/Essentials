JitPack.io Check Repository = https://jitpack.io/#AchyMake/Essentials
```xml
    <repositories>
        <repository>
            <id>jitpack.io</id>
            <url>https://jitpack.io</url>
        </repository>
    </repositories>

    <dependencies>
        <dependency>
            <groupId>com.github.AchyMake</groupId>
            <artifactId>Essentials</artifactId>
            <version>LATEST</version>
            <scope>provided</scope>
        </dependency>
    </dependencies>
```
Example for getting Userdata
```java
package org.example.yourplugin;

import org.achymake.essentials.Essentials;
import org.achymake.essentials.data.Userdata;
import org.bukkit.OfflinePlayer;
import org.bukkit.plugin.java.JavaPlugin;

public final class YourPlugin extends JavaPlugin {
    private static YourPlugin instance;
    @Override
    public void onEnable() {
        instance = this;
    }
    @Override
    public void onDisable() {
    }
    public boolean isVanished(OfflinePlayer offlinePlayer) {
        return getEssentials().getUserdata().isVanished(offlinePlayer);
    }
    public boolean isPVP(OfflinePlayer offlinePlayer) {
        return getEssentials().getUserdata().isPVP(offlinePlayer);
    }
    public boolean setPVP(OfflinePlayer offlinePlayer, boolean value) {
        return getEssentials().getUserdata().setBoolean(offlinePlayer, "settings.pvp", value);
    }
    public Essentials getEssentials() {
        return Essentials.getInstance();
    }
    public static YourPlugin getInstance() {
        return instance;
    }
}
```
