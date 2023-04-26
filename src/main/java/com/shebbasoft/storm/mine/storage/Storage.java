package com.shebbasoft.storm.mine.storage;

public interface Storage extends MineStorage {

    void initialize();

    void close();

}
