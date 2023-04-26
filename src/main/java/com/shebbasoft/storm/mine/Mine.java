package com.shebbasoft.storm.mine;

import org.bukkit.Material;
import org.bukkit.block.Block;

import java.util.Map;

public interface Mine {

    String getName();

    String getDisplayName();

    void setDisplayName(String name);

    Map<Material, Double> getPattern();

    void setPattern(Map<Material, Double> pattern);

    void setPatternEntry(Material material, double chance);

    boolean removePatternEntry(Material material);

    Area getArea();

    void setArea(Area area);

    boolean isInside(Block block);

    boolean isResetTimeEnabled();

    void setResetTimeEnabled(boolean enabled);

    long getResetTime();

    void setResetTime(long time);

    boolean isResetPercentageEnabled();

    void setResetPercentageEnabled(boolean enabled);

    double getResetPercentage();

    void setResetPercentage(double percentage);

    void setBlocksBroken(int amount);

    int getBlocksBroken();

    void reset();

    boolean isDirty();

    void setDirty(boolean isDirty);

    
}
