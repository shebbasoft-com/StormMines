package com.shebbasoft.storm.mine.command;

import com.shebbasoft.shebbasoftlib.minecraft.command.SimpleCommand;
import com.shebbasoft.storm.mine.StormMines;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.List;

public class PatternRemoveMaterialCommand extends SimpleCommand {

    private static final String COMMAND_ALIAS = "remove";
    private static final List<String> COMMAND_ALIASES = Collections.singletonList(COMMAND_ALIAS);

    public PatternRemoveMaterialCommand(StormMines plugin) {

    }

    @Override
    public @NotNull List<String> getAliases() {
        return null;
    }

    @Override
    public void onCommand(@NotNull CommandSender commandSender, @NotNull String[] strings) {

    }

    @Override
    public @NotNull List<String> getUsages(@NotNull CommandSender commandSender) {
        return Collections.singletonList("/" + getPath() + COMMAND_ALIAS + " <material>");
    }
}
