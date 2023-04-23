package com.shebbasoft.storm.mine;

import com.shebbasoft.shebbasoftlib.minecraft.command.CommandManager;
import com.shebbasoft.shebbasoftlib.minecraft.command.SimpleCommandManager;
import com.shebbasoft.storm.mine.command.MineCommand;
import org.bukkit.plugin.java.JavaPlugin;

public class StormMines extends JavaPlugin {

    private static StormMines instance;
    private MineController mineController;

    @Override
    public void onLoad() {
        instance = this;
    }

    @Override
    public void onEnable() {
        mineController = new MineController(this);
        CommandManager commandManager = new SimpleCommandManager(this);
        commandManager.register(new MineCommand(this));
    }

    public MineController getMineController() {
        return mineController;
    }

    public static StormMines getInstance() {
        return instance;
    }
}
