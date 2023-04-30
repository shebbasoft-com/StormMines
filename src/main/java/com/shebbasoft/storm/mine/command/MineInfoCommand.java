package com.shebbasoft.storm.mine.command;

import com.shebbasoft.shebbasoftlib.minecraft.command.SimpleCommand;
import com.shebbasoft.storm.mine.Area;
import com.shebbasoft.storm.mine.Mine;
import com.shebbasoft.storm.mine.MineController;
import com.shebbasoft.storm.mine.StormMines;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.StringJoiner;

public class MineInfoCommand extends SimpleCommand {

    private static final String COMMAND_ALIAS = "info";
    private static final List<String> COMMAND_ALIASES = Collections.singletonList(COMMAND_ALIAS);

    private final MineController mineController;

    public MineInfoCommand(StormMines plugin) {
        this.mineController = plugin.getMineController();
    }

    @Override
    public @NotNull List<String> getAliases() {
        return COMMAND_ALIASES;
    }

    @Override
    public void onCommand(@NotNull CommandSender sender, @NotNull String[] arguments) {
        if (arguments.length != 1) {
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
        Area area = mine.getArea();

        String message = "&3&lMine Info:" +
                "\n&7Name: &f" + mine.getName() +
                "\n&7Display Name: &f" + mine.getDisplayName() +
                "\n&7Pattern: &f" + getPatternMessage(mine.getPattern()) +
                "\n&7World: &f" + area.getWorldName() +
                "\n&7Size: &f" + (area.getVolume() - mine.getBlocksBroken()) + "/" + area.getVolume() +
                "\n&7Minimum: &fx" + area.getMinimumX() + " y" + area.getMinimumY() + " z" + area.getMinimumZ() +
                "\n&7Maximum: &fy" + area.getMaximumX() + " y" + area.getMaximumY() + " z" + area.getMaximumZ() +
                "\n&7Reset Percentage: &f" + mine.getResetPercentage() + " " + getStateMessage(mine.isResetPercentageEnabled()) +
                "\n&7Reset Time: &f" + mine.getResetTime() + " " + getStateMessage(mine.isResetTimeEnabled());

        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', message));
    }

    @Override
    public @NotNull List<String> getUsages(@NotNull CommandSender commandSender) {
        return Collections.singletonList("/" + getPath() + COMMAND_ALIAS + " <mine>");
    }

    private String getStateMessage(boolean state) {
        if (state) {
            return "&a&lENABLED";
        }
        return "&c&lDISABLED";
    }

    private String getPatternMessage(Map<Material, Double> pattern) {
        StringJoiner builder = new StringJoiner(", ");
        pattern.forEach(((material, chance) -> builder.add(material.name() + ": " + chance.toString())));
        return builder.toString();
    }
}
