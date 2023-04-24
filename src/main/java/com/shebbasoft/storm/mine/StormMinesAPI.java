package com.shebbasoft.storm.mine;

public class StormMinesAPI {

    private static final StormMines plugin = StormMines.getInstance();

    private StormMinesAPI() {}

    public static MineController getMineController() {
        return plugin.getMineController();
    }

}
