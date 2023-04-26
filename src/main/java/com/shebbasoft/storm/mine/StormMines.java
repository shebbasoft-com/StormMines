package com.shebbasoft.storm.mine;

import com.shebbasoft.shebbasoftlib.minecraft.command.CommandManager;
import com.shebbasoft.shebbasoftlib.minecraft.command.SimpleCommandManager;
import com.shebbasoft.storm.mine.command.MineCommand;
import com.shebbasoft.storm.mine.storage.FileStorage;
import com.shebbasoft.storm.mine.storage.Storage;
import org.bukkit.plugin.java.JavaPlugin;

public class StormMines extends JavaPlugin {

    private static StormMines instance;

    private Storage storage;
    private MineController mineController;

    @Override
    public void onLoad() {
        instance = this;
    }

    @Override
    public void onEnable() {

        storage = new FileStorage(this);
        storage.initialize();

        mineController = new MineController(this);
        CommandManager commandManager = new SimpleCommandManager(this);
        commandManager.register(new MineCommand(this));
    }

    public void reload() {
        mineController.close();
        storage.close();

        // reload config

        storage = new FileStorage(this);
        storage.initialize();
        mineController.initialize();
    }

    @Override
    public void onDisable() {
        mineController.close();
        storage.close();
    }

    public Storage getStorage() {
        return storage;
    }

    public MineController getMineController() {
        return mineController;
    }

    public static StormMines getInstance() {
        return instance;
    }
}
