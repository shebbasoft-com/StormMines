package com.shebbasoft.storm.mine.command;

import com.shebbasoft.shebbasoftlib.minecraft.command.SimpleCommand;
import com.shebbasoft.storm.mine.Mine;
import com.shebbasoft.storm.mine.MineController;
import com.shebbasoft.storm.mine.StormMines;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class ListCommand extends SimpleCommand {

    private static final String COMMAND_ALIAS = "list";
    private static final List<String> COMMAND_ALIASES = Collections.singletonList(COMMAND_ALIAS);

    private final MineController mineController;

    public ListCommand(StormMines plugin) {
        mineController = plugin.getMineController();
    }

    @Override
    public @NotNull List<String> getAliases() {
        return COMMAND_ALIASES;
    }

    @Override
    public void onCommand(@NotNull CommandSender sender, @NotNull String[] arguments) {
        Collection<Mine> mines = mineController.getMines();

        if (mines.isEmpty()) {
            sender.sendMessage(ChatColor.RED + "Error: There are no mines registered.");
            return;
        }

        StringBuilder message = new StringBuilder();

        message.append("&3&lMines:");
        mines.forEach(mine -> message.append("\n&f").append(mine.getName()).append(" &7- ").append(mine.getDisplayName()));

        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', message.toString()));
    }

    @Override
    public @NotNull List<String> getUsages(@NotNull CommandSender sender) {
        return Collections.singletonList("/" + getPath() + COMMAND_ALIAS);
    }

}
