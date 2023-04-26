package com.shebbasoft.storm.mine;

import com.sk89q.worldedit.EditSession;
import com.sk89q.worldedit.MaxChangedBlocksException;
import com.sk89q.worldedit.WorldEdit;
import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldedit.bukkit.BukkitWorld;
import com.sk89q.worldedit.function.pattern.Pattern;
import com.sk89q.worldedit.function.pattern.RandomPattern;
import com.sk89q.worldedit.math.BlockVector3;
import com.sk89q.worldedit.regions.CuboidRegion;
import com.sk89q.worldedit.regions.Region;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class WorldEditMine implements Mine {

    private final String name;
    private String displayName;
    private Map<Material, Double> pattern = new HashMap<>();
    private Area area;
    private boolean isResetTimeEnabled;
    private long resetTime;
    private boolean isResetPercentageEnabled;
    private double resetPercentage;

    private int blocksBroken = 0;
    private boolean isResetting = false;
    private Pattern worldEditPattern;
    private Region worldEditRegion;
    private int timedResetTask = -1;
    private boolean isDirty = false;

    public WorldEditMine(String name, String displayName, Map<Material, Double> pattern, Area area,
                         boolean isResetTimeEnabled, long resetTime,
                         boolean isResetPercentageEnabled, double resetPercentage) {
        this.name = name;
        this.displayName = displayName;
        this.pattern = pattern;
        this.area = area;
        this.isResetTimeEnabled = isResetTimeEnabled;
        this.resetTime = resetTime;
        this.isResetPercentageEnabled = isResetPercentageEnabled;
        this.resetPercentage = resetPercentage;
    }

    public WorldEditMine(String name, Area area) {
        this.name = name;
        this.displayName = name;
        this.isResetTimeEnabled = false;
        this.resetTime = 6000; // 5~ minutes in ticks
        this.isResetPercentageEnabled = false;
        this.resetPercentage = 0.5; // 50%
        this.isDirty = true;

        setPatternEntry(Material.STONE, 1.0); // 100% stone
        setArea(area);
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getDisplayName() {
        return displayName;
    }

    @Override
    public void setDisplayName(String name) {
        this.displayName = name;
    }

    @Override
    public Map<Material, Double> getPattern() {
        // should not be editable as it will not update the WorldEdit pattern. READ ONLY
        return Collections.unmodifiableMap(pattern);
    }

    @Override
    public void setPattern(Map<Material, Double> pattern) {
        this.pattern = pattern; // todo copy/clone the pattern?
        updateWorldEditPattern();
    }

    @Override
    public void setPatternEntry(Material material, double chance) {
        pattern.put(material, chance);
        updateWorldEditPattern();
    }

    @Override
    public boolean removePatternEntry(Material material) {
        if (pattern.remove(material) == null) {
            return false;
        }
        updateWorldEditPattern();
        return true;
    }

    private void updateWorldEditPattern() {
        RandomPattern randomPattern = new RandomPattern();
        pattern.forEach((material, chance) -> {
            randomPattern.add(BukkitAdapter.adapt(material.createBlockData()), chance);
        });
        worldEditPattern = randomPattern;
        isDirty = true;
    }

    @Override
    public Area getArea() {
        return area;
    }

    @Override
    public void setArea(Area area) {
        if (area instanceof WorldEditArea worldEditArea) {
            worldEditRegion = worldEditArea.getRegion();
        } else {
            World world = Bukkit.getWorld(area.getWorldName());
            BlockVector3 minimum = BlockVector3.at(area.getMinimumX(), area.getMinimumY(), area.getMinimumZ());
            BlockVector3 maximum = BlockVector3.at(area.getMaximumX(), area.getMaximumY(), area.getMaximumZ());
            worldEditRegion = new CuboidRegion(new BukkitWorld(world), minimum, maximum);
        }
        this.area = area;
        isDirty = true;
    }

    @Override
    public boolean isInside(Block block) {
        if (area.getWorldName().equals(block.getWorld().getName())) {
            return false;
        }
        return block.getX() >= area.getMinimumX() && block.getX() <= area.getMaximumX() &&
                block.getY() >= area.getMinimumY() && block.getY() <= area.getMaximumY() &&
                block.getZ() >= area.getMinimumZ() && block.getZ() <= area.getMaximumZ();

    }

    @Override
    public boolean isResetTimeEnabled() {
        return isResetTimeEnabled;
    }

    @Override
    public void setResetTimeEnabled(boolean enabled) {
        isResetTimeEnabled = enabled;
        isDirty = true;
    }

    @Override
    public long getResetTime() {
        return resetTime;
    }

    @Override
    public void setResetTime(long time) {
        resetTime = time;
        isDirty = true;
    }

    @Override
    public boolean isResetPercentageEnabled() {
        return isResetPercentageEnabled;
    }

    @Override
    public void setResetPercentageEnabled(boolean enabled) {
        isResetPercentageEnabled = enabled;
        isDirty = true;
    }

    @Override
    public double getResetPercentage() {
        return resetPercentage;
    }

    @Override
    public void setResetPercentage(double percentage) {
        resetPercentage = percentage;
        isDirty = true;
    }

    @Override
    public void setBlocksBroken(int amount) {
        this.blocksBroken = amount;

        if (isResetPercentageEnabled && resetPercentage > 0 && blocksBroken >= (area.getVolume() * (1.0 - resetPercentage))) {
            reset();
            return; // already resetting
        }

        if (isResetTimeEnabled && resetTime > 0 && timedResetTask != -1 && blocksBroken <= 0) {
            // start time reset
            timedResetTask = Bukkit.getScheduler().scheduleSyncDelayedTask(StormMines.getInstance(), this::reset, resetTime * 20L);
        }
    }

    @Override
    public int getBlocksBroken() {
        return blocksBroken;
    }

    @Override
    public void reset() {
        if (isResetting) {
            return;
        }

        try (EditSession editSession = WorldEdit.getInstance().newEditSession(worldEditRegion.getWorld())) {
            isResetting = true;
            editSession.setBlocks(worldEditRegion, worldEditPattern);
        } catch (MaxChangedBlocksException e) {
            throw new RuntimeException(e);
        } finally {
            blocksBroken = 0;
        }

        isResetting = false;
    }

    @Override
    public boolean isDirty() {
        return isDirty;
    }

    @Override
    public void setDirty(boolean isDirty) {
        this.isDirty = isDirty;
    }
}
