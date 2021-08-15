package dev._2lstudios.worldsentinel.utils;

import java.io.InputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.CopyOption;
import java.io.File;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;

public class ConfigurationUtil {
    private final Plugin plugin;

    public ConfigurationUtil(final Plugin plugin) {
        this.plugin = plugin;
    }

    public YamlConfiguration getConfiguration(final String filePath) {
        final File dataFolder = this.plugin.getDataFolder();
        final File file = new File(filePath.replace("%datafolder%", dataFolder.toPath().toString()));
        if (file.exists()) {
            return YamlConfiguration.loadConfiguration(file);
        }
        return new YamlConfiguration();
    }

    public void createConfiguration(String file) {
        try {
            final File dataFolder = this.plugin.getDataFolder();
            file = file.replace("%datafolder%", dataFolder.toPath().toString());
            final File configFile = new File(file);
            if (!configFile.exists()) {
                final String[] files = file.split("/");
                final InputStream inputStream = this.plugin.getClass().getClassLoader()
                        .getResourceAsStream(files[files.length - 1]);
                final File parentFile = configFile.getParentFile();
                if (parentFile != null) {
                    parentFile.mkdirs();
                }
                if (inputStream != null) {
                    Files.copy(inputStream, configFile.toPath(), new CopyOption[0]);
                    System.out.print(("[%pluginname%] File " + configFile + " has been created!")
                            .replace("%pluginname%", this.plugin.getDescription().getName()));
                } else {
                    configFile.createNewFile();
                }
            }
        } catch (IOException e) {
            System.out.print("[%pluginname%] Unable to create configuration file!".replace("%pluginname%",
                    this.plugin.getDescription().getName()));
        }
    }

    public void saveConfigurationSync(final YamlConfiguration yamlConfiguration, String path) {
        path = path.replace("%datafolder%", this.plugin.getDataFolder().toPath().toString());
        try {
            yamlConfiguration.save(path);
            System.out.print(("[%pluginname%] Saved " + path + "!").replace("%pluginname%",
                    this.plugin.getDescription().getName()));
        } catch (IOException e) {
            System.out.print(("[%pluginname%] Unable to save " + path + "!").replace("%pluginname%",
                    this.plugin.getDescription().getName()));
        }
    }

    public void saveConfigurationAsync(final YamlConfiguration yamlConfiguration, final String file) {
        this.plugin.getServer().getScheduler().runTaskAsynchronously(this.plugin,
                () -> this.saveConfigurationSync(yamlConfiguration, file));
    }

    public void deleteConfigurationSync(final String file) {
        final File file2 = new File(file.replace("%datafolder%", this.plugin.getDataFolder().toPath().toString()));
        if (file2.exists()) {
            file2.delete();
        }
    }

    public void deleteConfigurationAsync(final String file) {
        this.plugin.getServer().getScheduler().runTaskAsynchronously(this.plugin,
                () -> this.deleteConfigurationSync(file));
    }
}
