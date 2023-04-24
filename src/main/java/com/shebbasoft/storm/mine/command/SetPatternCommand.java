package com.shebbasoft.storm.mine.command;

import com.shebbasoft.shebbasoftlib.minecraft.command.SimpleCommand;
import com.shebbasoft.storm.mine.Mine;
import com.shebbasoft.storm.mine.MineController;
import com.shebbasoft.storm.mine.StormMines;
import com.shebbasoft.storm.mine.WorldEditMine;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class SetPatternCommand extends SimpleCommand {

    private static final String COMMAND_ALIAS = "setpattern";
    private static final List<String> COMMAND_ALIASES = Collections.singletonList(COMMAND_ALIAS);

    private final MineController mineController;

    public SetPatternCommand(StormMines plugin) {
        this.mineController = plugin.getMineController();
    }

    @Override
    public @NotNull List<String> getAliases() {
        return COMMAND_ALIASES;
    }

    @Override
    public void onCommand(@NotNull CommandSender sender, @NotNull String[] arguments) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage(ChatColor.RED + "Error: this command is for players only.");
            return;
        }

        if (arguments.length != 2) {
            sender.sendMessage(ChatColor.RED + "Error: invalid command syntax.");
            getUsages(sender).forEach(usage -> sender.sendMessage(ChatColor.AQUA + usage));
            return;
        }

        Optional<Mine> optionalMine = mineController.getMine(arguments[0]);

        if (optionalMine.isEmpty()) {
            sender.sendMessage(ChatColor.RED + "Error: Could not find a mine with this name.");
            return;
        }

        Mine mine = optionalMine.get();

        if (mine instanceof WorldEditMine worldEditMine) {
            worldEditMine.setPattern(arguments[1], player);
        } else {
            // things like #copy won't work as it is missing the player, but should we even support this?
            mine.setPattern(arguments[1]);
        }

        mine.reset();
        sender.sendMessage(ChatColor.GREEN + "Set mine pattern successfully.");
    }

    @Override
    public @NotNull List<String> getUsages(@NotNull CommandSender sender) {
        return Collections.singletonList("/" + getPath() + COMMAND_ALIAS + " <name> <pattern>");
    }
}
