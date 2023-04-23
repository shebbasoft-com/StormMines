package com.shebbasoft.storm.mine;

import com.sk89q.worldedit.function.pattern.Pattern;
import com.sk89q.worldedit.regions.Region;

public interface Mine {

    String getName();

    String getDisplayName();

    void setDisplayName(String name);

    Pattern getPattern();

    void setPattern(Pattern pattern);

    Region getRegion();

    void setRegion(Region region);

    long getResetTime();

    void setResetTime(long time);

    double getResetPercentage();

    void setResetPercentage(double percentage);

    void setBlocksBroken(int blocks);

    int getBlocksBroken();

    void reset();

    
}
