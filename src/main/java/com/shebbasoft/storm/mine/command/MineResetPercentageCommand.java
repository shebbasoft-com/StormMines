package com.shebbasoft.storm.mine.command;

import com.shebbasoft.shebbasoftlib.minecraft.command.SimpleCommand;
import com.shebbasoft.storm.mine.Mine;
import com.shebbasoft.storm.mine.MineController;
import com.shebbasoft.storm.mine.StormMines;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class MineResetPercentageCommand extends SimpleCommand {

    private static final String COMMAND_ALIAS = "setresetpercentage";
    private static final List<String> COMMAND_ALIASES = Collections.singletonList(COMMAND_ALIAS);

    private final MineController mineController;

    public MineResetPercentageCommand(StormMines plugin) {
        mineController = plugin.getMineController();
    }

    @Override
    public @NotNull List<String> getAliases() {
        return COMMAND_ALIASES;
    }

    @Override
    public void onCommand(@NotNull CommandSender sender, @NotNull String[] arguments) {

        if (arguments.length != 2) {
            sender.sendMessage(ChatColor.RED + "Error: invalid command syntax.");
            getUsages(sender).forEach(usage -> sender.sendMessage(ChatColor.AQUA + usage));
            return;
        }

        double percentage;
        try {
            percentage = Double.parseDouble(arguments[1]);
        } catch (NumberFormatException e) {
            sender.sendMessage(ChatColor.RED + "Error: Invalid percentage (100% = 1.0)");
            return;
        }

        Optional<Mine> optionalMine = mineController.getMine(arguments[0]);

        if (optionalMine.isEmpty()) {
            sender.sendMessage(ChatColor.RED + "Error: Could not find a mine with this name.");
            return;
        }

        Mine mine = optionalMine.get();
        mine.setResetPercentageEnabled(!(percentage <= 0.0));
        mine.setResetPercentage(percentage);
        sender.sendMessage(ChatColor.GREEN + "Set mine reset percentage successfully.");
    }

    @Override
    public @NotNull List<String> getUsages(@NotNull CommandSender sender) {
        return Collections.singletonList("/" + getPath() + COMMAND_ALIAS + " <name> <percentage>");
    }
}
