package com.shebbasoft.storm.mine;

import com.sk89q.worldedit.regions.Region;

public class WorldEditArea implements Area {

    private final Region region;

    public WorldEditArea(Region region) {
        this.region = region;
    }
    @Override
    public int getMinimumX() {
        return region.getMinimumPoint().getX();
    }

    @Override
    public int getMinimumY() {
        return region.getMinimumPoint().getY();
    }

    @Override
    public int getMinimumZ() {
        return region.getMinimumPoint().getZ();
    }

    @Override
    public int getMaximumX() {
        return region.getMaximumPoint().getX();
    }

    @Override
    public int getMaximumY() {
        return region.getMaximumPoint().getX();
    }

    @Override
    public int getMaximumZ() {
        return region.getMaximumPoint().getX();
    }

    @Override
    public long getVolume() {
        return region.getVolume();
    }

    @Override
    public String getWorldName() {
        return region.getWorld().getName(); // just let it throw a npe for now
    }

    public Region getRegion() {
        return region;
    }
}
