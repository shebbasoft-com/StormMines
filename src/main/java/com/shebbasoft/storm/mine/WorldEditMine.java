package com.shebbasoft.storm.mine;

import com.sk89q.worldedit.EditSession;
import com.sk89q.worldedit.MaxChangedBlocksException;
import com.sk89q.worldedit.WorldEdit;
import com.sk89q.worldedit.bukkit.BukkitPlayer;
import com.sk89q.worldedit.bukkit.BukkitWorld;
import com.sk89q.worldedit.extension.input.InputParseException;
import com.sk89q.worldedit.extension.input.ParserContext;
import com.sk89q.worldedit.function.pattern.Pattern;
import com.sk89q.worldedit.math.BlockVector3;
import com.sk89q.worldedit.regions.CuboidRegion;
import com.sk89q.worldedit.regions.Region;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

public class WorldEditMine implements Mine {

    private final String name;
    private String displayName;
    private String pattern;
    private Area area;
    private long resetTime;
    private double resetPercentage;

    private int blocksBroken;
    private boolean isResetting = false;
    private Pattern worldEditPattern;
    private Region worldEditRegion;
    private int timedResetTask = -1;

    public WorldEditMine(String name, Area area) {
        this.name = name;
        this.displayName = name;
        this.resetTime = -1L;
        this.resetPercentage = -1.0D;

        setArea(area);
        setPattern("minecraft:stone");
        reset();
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
    public String getPattern() {
        return pattern;
    }

    @Override
    public void setPattern(String pattern) throws IllegalArgumentException {
        ParserContext context = new ParserContext();

        // probably not enough, try WorldEditMine#setPattern(pattern, player)
        World world = Bukkit.getWorld(area.getWorldName());
        if (world != null) {
            context.setWorld(new BukkitWorld(world));
        }

        try {
            this.worldEditPattern = WorldEdit.getInstance().getPatternFactory().parseFromInput(pattern, context);
        } catch (InputParseException e) {
            throw new IllegalArgumentException("Invalid WorldEdit pattern.");
        }

        this.pattern = pattern;

    }

    public void setPattern(String pattern, Player player) throws IllegalArgumentException {
        ParserContext context = new ParserContext();
        BukkitPlayer bukkitPlayer = new BukkitPlayer(player);
        context.setActor(bukkitPlayer);
        context.setWorld(bukkitPlayer.getWorld());
        context.setSession(WorldEdit.getInstance().getSessionManager().getIfPresent(bukkitPlayer));

        try {
            worldEditPattern = WorldEdit.getInstance().getPatternFactory().parseFromInput(pattern, context);
        } catch (InputParseException e) {
            throw new IllegalArgumentException("Invalid WorldEdit pattern.");
        }

        this.pattern = pattern;
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
    public long getResetTime() {
        return resetTime;
    }

    @Override
    public void setResetTime(long time) {
        this.resetTime = time;
    }

    @Override
    public double getResetPercentage() {
        return resetPercentage;
    }

    @Override
    public void setResetPercentage(double percentage) {
        this.resetPercentage = percentage;
    }

    @Override
    public void setBlocksBroken(int amount) {
        this.blocksBroken = amount;

        if (resetPercentage > 0 && blocksBroken >= (area.getVolume() * (1.0 - resetPercentage))) {
            reset();
            return; // already resetting
        }

        if (resetTime > 0 && timedResetTask != -1 && blocksBroken <= 0) {
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
            // this probably will not be accurate when using FAWE, needs testing, check if FAWE has a thenAccept() method
            blocksBroken = 0;
        } catch (MaxChangedBlocksException e) {
            throw new RuntimeException(e);
        }

        isResetting = false;
    }
}
