package com.shebbasoft.storm.mine.command;

import com.shebbasoft.shebbasoftlib.minecraft.command.OptionCommand;
import com.shebbasoft.storm.mine.StormMines;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.List;

public class MinePatternCommand extends OptionCommand {

    private static final String COMMAND_ALIAS = "setpattern";
    private static final List<String> COMMAND_ALIASES = Arrays.asList(COMMAND_ALIAS, "pattern");

    public MinePatternCommand(StormMines plugin) {
        addOption(new PatternsSetMaterialCommand(plugin));
        addOption(new PatternRemoveMaterialCommand(plugin));
    }

    @Override
    public @NotNull List<String> getAliases() {
        return COMMAND_ALIASES;
    }
}
