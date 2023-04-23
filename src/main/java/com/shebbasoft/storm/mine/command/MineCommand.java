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
        addOption(new CreateCommand(plugin));
        addOption(new ResetCommand(plugin));
        addOption(new ListCommand(plugin));
        addOption(new SetPatternCommand(plugin));
        addOption(new SetResetTimeCommand(plugin));
        addOption(new SetResetPercentageCommand(plugin));
    }

    @Override
    public @NotNull List<String> getAliases() {
        return COMMAND_ALIASES;
    }
}
