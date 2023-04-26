package com.shebbasoft.storm.mine;

import com.shebbasoft.storm.mine.storage.Storage;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.regex.Pattern;

public class MineController implements Listener {

    private static final long SAVE_TASK_DELAY = 12000L;
    private static final long SAVE_TASK_PERIOD = 12000L;
    private static final Pattern NAME_PATTERN = Pattern.compile("^[A-Za-z0-9_]{3,16}$");

    private final Map<String, Mine> mineMap = new HashMap<>();
    private final StormMines plugin;
    private final Storage storage;
    private int taskId = -1;

    public MineController(StormMines plugin) {
        this.plugin = plugin;
        this.storage = plugin.getStorage();
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    public void initialize() {
        load();

        taskId = plugin.getServer().getScheduler()
                .scheduleSyncRepeatingTask(plugin, this::save, SAVE_TASK_DELAY, SAVE_TASK_PERIOD);
    }

    public void close() {
        if (taskId != -1) {
            plugin.getServer().getScheduler().cancelTask(taskId);
            taskId = -1;
        }
        save();
    }

    public void load() {
        plugin.getLogger().info("Loading mines...");
        mineMap.clear();

        storage.loadMines().forEach(this::registerMine);
    }

    public void save() {
        plugin.getLogger().info("Saving mines...");
        mineMap.values().stream().filter(Mine::isDirty).forEach(storage::saveMine);
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

    @EventHandler(ignoreCancelled = true, priority = EventPriority.MONITOR)
    public void onBlockBreakEvent(BlockBreakEvent event) {
        mineMap.values().stream()
                .filter(mine -> mine.isInside(event.getBlock()))
                .forEach(mine -> mine.setBlocksBroken(mine.getBlocksBroken() - 1));
        // probably best if Mine class handles this, so it doesn't break the blocks broken count.
    }


}
