package com.shebbasoft.storm.mine.storage;

import com.shebbasoft.storm.mine.Area;
import com.shebbasoft.storm.mine.Mine;
import com.shebbasoft.storm.mine.MineBuilder;
import com.shebbasoft.storm.mine.StormMines;
import com.shebbasoft.storm.mine.WorldEditArea;
import com.shebbasoft.storm.mine.WorldEditMine;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.MemoryConfiguration;
import org.bukkit.configuration.MemorySection;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

public class FileStorage implements Storage {

    private static final String MINE_FOLDER_NAME = "Mines";

    private final StormMines plugin;
    private final File folder;

    public FileStorage(StormMines plugin) {
        this.plugin = plugin;
        this.folder = new File(plugin.getDataFolder(), MINE_FOLDER_NAME);

        if (!this.folder.exists() && !this.folder.mkdirs()) {
            plugin.getLogger().warning("Failed to create mines folder!");
        }
    }

    @Override
    public void initialize() {
        // ignored
    }

    @Override
    public Collection<Mine> loadMines() {
        List<Mine> mines = new ArrayList<>();

        File[] files = folder.listFiles((dir, name) -> name.endsWith(".yml"));
        if (files == null) {
            return mines;
        }

        for (File file : files) {
            YamlConfiguration configuration = YamlConfiguration.loadConfiguration(file);

            String name = configuration.getString("name");
            String worldName = configuration.getString("world");

            if (!configuration.contains("x1") || configuration.contains("y1") || configuration.contains("z1") ||
                    !configuration.contains("x2") || configuration.contains("y2") || configuration.contains("z2") ||
                    name == null || worldName == null) {
                plugin.getLogger().warning("Mine file was missing minimum values and got ignored: " + file.getName());
                continue;
            }

            int x1 = configuration.getInt("x1");
            int y1 = configuration.getInt("y1");
            int z1 = configuration.getInt("z1");
            int x2 = configuration.getInt("x2");
            int y2 = configuration.getInt("y2");
            int z2 = configuration.getInt("z2");


            Area area = new WorldEditArea(worldName, x1, y1, z1, x2, y2, z2);
            MineBuilder builder = new MineBuilder();
            builder.setName(name);
            builder.setArea(area);

            String displayName = configuration.getString("display-name");
            builder.setDisplayName(displayName);

            ConfigurationSection patternSection = configuration.getConfigurationSection("pattern");
            if (patternSection != null) {
                Set<String> keys = patternSection.getKeys(false);

                Map<Material, Double> pattern = new HashMap<>();

                keys.forEach(key -> {
                    Material material;
                    try {
                        material = Material.valueOf(key);
                    } catch (IllegalArgumentException e) {
                        return; // continue
                    }

                    double chance = patternSection.getDouble(material.name());

                    pattern.put(material, chance);
                });

                builder.setPattern(pattern);
            }

            builder.setResetTimeEnabled(configuration.getBoolean("is-reset-time-enabled"));
            builder.setResetTime(configuration.getLong("reset-time", 6000L));
            builder.setResetPercentageEnabled(configuration.getBoolean("is-reset-percentage-enabled"));
            builder.setResetPercentage(configuration.getDouble("reset-percentage", 0.5D));
            mines.add(builder.build());
        }

        return mines;
    }

    @Override
    public void saveMine(Mine mine) {
        YamlConfiguration configuration = new YamlConfiguration();

        configuration.set("name", mine.getName());

        Area area = mine.getArea();
        configuration.set("world", area.getWorldName());
        configuration.set("x1", area.getMinimumX());
        configuration.set("y1", area.getMinimumY());
        configuration.set("z1", area.getMinimumZ());
        configuration.set("x2", area.getMaximumX());
        configuration.set("y2", area.getMaximumY());
        configuration.set("z2", area.getMaximumZ());

        configuration.set("display-name", mine.getDisplayName());

        ConfigurationSection patternSection = new MemoryConfiguration();
        mine.getPattern().forEach((material, chance) -> {
            patternSection.set(material.name(), chance);
        });
        configuration.set("pattern", patternSection);

        configuration.set("is-reset-time-enabled", mine.isResetTimeEnabled());
        configuration.set("reset-time", mine.getResetTime());
        configuration.set("is-reset-percentage-enabled", mine.isResetPercentageEnabled());
        configuration.set("reset-percentage", mine.getResetPercentage());

        String fileName = mine.getName() + ".yml";
        File mineFile = new File(folder, fileName);
        try {
            configuration.save(mineFile);
        } catch (IOException e) {
            plugin.getLogger().warning("Something went wrong while saving: " + fileName);
        }

    }

    @Override
    public void removeMine(Mine mine) {
        String fileName = mine.getName() + ".yml";
        File mineFile = new File(folder, fileName);
        try {
            if (!mineFile.delete()) {
                plugin.getLogger().warning("Something went wrong while deleting: " + fileName);
            }
        } catch (SecurityException e) {
            plugin.getLogger().warning("Something went wrong while deleting (SecurityException): " + fileName);
        }
    }

    @Override
    public void close() {
        // ignored
    }
}
