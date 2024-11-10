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
import org.achymake.essentials.Essentials;
import org.achymake.essentials.data.Userdata;
import org.bukkit.Bukkit;
import org.bukkit.event.block.BlockBreakEvent;

public class BlockBreak implements Listener {
    private Essentials getInstance() {
        return Essentials.getInstance();
    }
    private Userdata getUserdata(OfflinePlayer offlinePlayer) {
        return getInstance().getUserdata(offlinePlayer);
    }
    public BlockBreak() {
        Bukkit.getPluginManager().registerEvents(this, getInstance());
    }
    @EventHandler(priority = EventPriority.NORMAL)
    public void onBlockBreak(BlockBreakEvent event) {
        var userdata = getUserdata(event.getPlayer());
        if (userdata.isDisabled) {
            event.setCancelled(true);
        }
    }
}
```
