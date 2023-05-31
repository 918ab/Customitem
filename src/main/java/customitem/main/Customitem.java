package customitem.main;

import customitem.main.command.command;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public final class Customitem extends JavaPlugin {

    @Override
    public void onEnable() {
        Bukkit.getLogger().info("[Customitem] Enable");
        getCommand("item").setExecutor(new command());
        Path targetPath = Paths.get("Plugins", "Customitem", "items", "test.yaml");
        if (!Files.exists(targetPath)) {
            try (InputStream inputStream = getClass().getResourceAsStream("/test.yaml")) {
                Files.createDirectories(targetPath.getParent());
                Files.copy(inputStream, targetPath);
                Bukkit.getLogger().info("[Customitem] test.yaml 파일이 Plugins/Customitem/items 폴더에 생성되었습니다");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    @Override
    public void onDisable() {
        Bukkit.getLogger().info("[Customitem] Disable");
    }

}
