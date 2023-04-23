package com.shebbasoft.storm.mine;

import com.sk89q.worldedit.EditSession;
import com.sk89q.worldedit.MaxChangedBlocksException;
import com.sk89q.worldedit.WorldEdit;
import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldedit.function.pattern.Pattern;
import com.sk89q.worldedit.regions.Region;
import org.bukkit.Material;

public class SimpleMine implements Mine {

    private final String name;
    private Region region;
    private String displayName;
    private Pattern pattern;

    public SimpleMine(Region region, String name) {
        this.region = region.clone();
        this.name = name;
        this.displayName = name;
        this.pattern = BukkitAdapter.adapt(Material.STONE.createBlockData());
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
    public Pattern getPattern() {
        return pattern;
    }

    @Override
    public void setPattern(Pattern pattern) {
        this.pattern = pattern;
    }

    @Override
    public Region getRegion() {
        return region;
    }

    @Override
    public void setRegion(Region region) {
        this.region = region;
    }

    @Override
    public long getResetTime() {
        return 0; // todo
    }

    @Override
    public void setResetTime(long time) {
        // todo
    }

    @Override
    public double getResetPercentage() {
        return 0; // todo
    }

    @Override
    public void setResetPercentage(double percentage) {
        // todo
    }

    @Override
    public void setBlocksBroken(int blocks) {
        // todo
    }

    @Override
    public int getBlocksBroken() {
        return 0; // todo
    }

    @Override
    public void reset() {
        try (EditSession editSession = WorldEdit.getInstance().newEditSession(region.getWorld())) {
            editSession.setBlocks(region, pattern);

        } catch (MaxChangedBlocksException e) {
            throw new RuntimeException(e);
        }
    }
}
