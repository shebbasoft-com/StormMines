package com.shebbasoft.storm.mine;

import com.sk89q.worldedit.bukkit.BukkitWorld;
import com.sk89q.worldedit.math.BlockVector3;
import com.sk89q.worldedit.regions.CuboidRegion;
import com.sk89q.worldedit.regions.Region;
import org.bukkit.Bukkit;
import org.bukkit.World;

public class WorldEditArea implements Area {

    private final Region region;

    public WorldEditArea(Region region) {
        this.region = region;
    }

    public WorldEditArea(String worldName, int x1, int y1, int z1, int x2, int y2, int z2) {
        World world = Bukkit.getWorld(worldName);
        BlockVector3 minimum = BlockVector3.at(x1, y1, z1);
        BlockVector3 maximum = BlockVector3.at(x2, y2, z2);
        region = new CuboidRegion(new BukkitWorld(world), minimum, maximum);
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
        return region.getMaximumPoint().getY();
    }

    @Override
    public int getMaximumZ() {
        return region.getMaximumPoint().getZ();
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
