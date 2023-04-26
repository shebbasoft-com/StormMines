package com.shebbasoft.storm.mine.storage;

import com.shebbasoft.storm.mine.Mine;

import java.util.Collection;
import java.util.Optional;

public interface MineStorage {

    Collection<Mine> loadMines();

    void saveMine(Mine mine);

    void removeMine(Mine mine);

}
