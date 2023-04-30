package com.shebbasoft.storm.mine.command;

import com.shebbasoft.shebbasoftlib.minecraft.command.OptionCommand;
import com.shebbasoft.storm.mine.StormMines;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.List;

public class MineCommand extends OptionCommand {

    private static final String COMMAND_ALIAS = "mine";
    private static final List<String> COMMAND_ALIASES = Collections.singletonList(COMMAND_ALIAS);

    public MineCommand(StormMines plugin) {
        addOption(new MineCreateCommand(plugin));
        addOption(new MineInfoCommand(plugin));
        addOption(new MineResetCommand(plugin));
        addOption(new MineListCommand(plugin));
        addOption(new MinePatternCommand(plugin));
        addOption(new MineResetTimeCommand(plugin));
        addOption(new MineResetPercentageCommand(plugin));
    }

    @Override
    public @NotNull List<String> getAliases() {
        return COMMAND_ALIASES;
    }
}
