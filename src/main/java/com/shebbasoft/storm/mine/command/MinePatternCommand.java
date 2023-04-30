package com.shebbasoft.storm.mine.command;

import com.shebbasoft.shebbasoftlib.minecraft.command.OptionCommand;
import com.shebbasoft.storm.mine.StormMines;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class MinePatternCommand extends OptionCommand {

    private static final String COMMAND_ALIAS = "pattern";
    private static final List<String> COMMAND_ALIASES = Collections.singletonList(COMMAND_ALIAS);

    public MinePatternCommand(StormMines plugin) {
        addOption(new PatternsSetMaterialCommand(plugin));
        addOption(new PatternUnsetMaterialCommand(plugin));
    }

    @Override
    public @NotNull List<String> getAliases() {
        return COMMAND_ALIASES;
    }
}
