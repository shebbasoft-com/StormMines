package com.shebbasoft.storm.mine;

import org.bukkit.block.Block;

public interface Mine {

    String getName();

    String getDisplayName();

    void setDisplayName(String name);

    String getPattern();

    void setPattern(String pattern) throws IllegalArgumentException;

    Area getArea();

    void setArea(Area area);

    boolean isInside(Block block);

    long getResetTime();

    void setResetTime(long time);

    double getResetPercentage();

    void setResetPercentage(double percentage);

    void setBlocksBroken(int amount);

    int getBlocksBroken();

    void reset();

    
}
