package com.shebbasoft.storm.mine;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.regex.Pattern;

public class MineController {

    private static final Pattern NAME_PATTERN = Pattern.compile("^[A-Za-z0-9_]{3,16}$");
    private final Map<String, Mine> mineMap = new HashMap<>();
    private final StormMines plugin;

    public MineController(StormMines plugin) {
        this.plugin = plugin;
    }

    public void registerMine(Mine mine) {
        String name = mine.getName().toLowerCase(Locale.ENGLISH);

        if (!NAME_PATTERN.matcher(name).matches()) {
            throw new IllegalArgumentException("Invalid mine name: \"" + name + "\"");
        }

        if (mineMap.containsKey(name)) {
            throw new IllegalArgumentException("There was already a mine with the name \"" + name + "\" registered.");
        }

        mineMap.put(name, mine);
        plugin.getLogger().info("Registered the mine with the name \"" + name + "\" successfully.");
    }

    public void unRegisterMine(Mine mine) {
        String name = mine.getName().toLowerCase(Locale.ENGLISH);

        if (mineMap.remove(name) != null) {
            plugin.getLogger().info("Unregistered a mine with the name \"" + name + "\" successfully.");
        }

    }

    public Collection<Mine> getMines() {
        return Collections.unmodifiableCollection(mineMap.values());
    }

    public Optional<Mine> getMine(String name) {
        return Optional.ofNullable(mineMap.get(name.toLowerCase(Locale.ENGLISH)));
    }

}
