package com.shebbasoft.storm.mine;

import org.bukkit.Material;
import org.bukkit.block.Block;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class MineBuilder {

    private String name;
    private String displayName;
    private Map<Material, Double> pattern = new HashMap<>();
    private Area area;
    private boolean isResetTimeEnabled = false;
    private long resetTime = 6000L;
    private boolean isResetPercentageEnabled = false;
    private double resetPercentage = 0.5D;

    public MineBuilder setName(String name) {
        this.name = name;
        return this;
    }

    public MineBuilder setDisplayName(String name) {
        this.displayName = displayName;
        return this;
    }

    public MineBuilder setPattern(Map<Material, Double> pattern) {
        this.pattern = pattern;
        return this;
    }

    public MineBuilder setArea(Area area) {
        this.area = area;
        return this;
    }

    public MineBuilder setResetTimeEnabled(boolean enabled) {
        this.isResetTimeEnabled = enabled;
        return this;
    }

    public MineBuilder setResetTime(long time) {
        this.resetTime = time;
        return this;
    }

    public MineBuilder setResetPercentageEnabled(boolean enabled) {
        this.isResetPercentageEnabled = enabled;
        return this;
    }

    public MineBuilder setResetPercentage(double percentage) {
        this.resetPercentage = percentage;
        return this;
    }

    public WorldEditMine build() {
        Objects.requireNonNull(name);
        Objects.requireNonNull(area);

        if (displayName == null) {
            displayName = name;
        }

        return new WorldEditMine(name, displayName, pattern, area, isResetTimeEnabled, resetTime, isResetPercentageEnabled, resetPercentage);
    }


}
