package com.shebbasoft.storm.mine.command;

import com.shebbasoft.shebbasoftlib.minecraft.command.SimpleCommand;
import com.shebbasoft.storm.mine.Mine;
import com.shebbasoft.storm.mine.MineController;
import com.shebbasoft.storm.mine.StormMines;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class PatternsSetMaterialCommand extends SimpleCommand {

    private static final String COMMAND_ALIAS = "set";
    private static final List<String> COMMAND_ALIASES = Collections.singletonList(COMMAND_ALIAS);

    private final MineController mineController;

    public PatternsSetMaterialCommand(StormMines plugin) {
        mineController = plugin.getMineController();
    }

    @Override
    public @NotNull List<String> getAliases() {
        return COMMAND_ALIASES;
    }

    @Override
    public void onCommand(@NotNull CommandSender sender, @NotNull String[] arguments) {
        if (arguments.length != 3) {
            sender.sendMessage(ChatColor.RED + "Error: invalid command syntax.");
            getUsages(sender).forEach(usage -> sender.sendMessage(ChatColor.AQUA + usage));
            return;
        }

        Material material;
        try {
            material = Material.valueOf(arguments[1]);
        } catch (IllegalArgumentException e) {
            sender.sendMessage(ChatColor.RED + "Error: Invalid material.");
            return;
        }

        double chance;
        try {
            chance = Double.parseDouble(arguments[2]);
        } catch (NumberFormatException e) {
            sender.sendMessage(ChatColor.RED + "Error: Invalid chance.");
            return;
        }

        Optional<Mine> optionalMine = mineController.getMine(arguments[0]);

        if (optionalMine.isEmpty()) {
            sender.sendMessage(ChatColor.RED + "Error: Could not find a mine with this name.");
            return;
        }

        Mine mine = optionalMine.get();

        if (chance <= 0.0) {
            mine.removePatternEntry(material);
        } else {
            mine.setPatternEntry(material, chance);
        }

        mine.reset();
        sender.sendMessage(ChatColor.GREEN + "Updated mine pattern.");
    }

    @Override
    public @NotNull List<String> getUsages(@NotNull CommandSender sender) {
        return Collections.singletonList("/" + getPath() + COMMAND_ALIAS + " <mine> <material> <chance>");
    }
}
