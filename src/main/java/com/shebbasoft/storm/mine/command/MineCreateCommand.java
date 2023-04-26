package com.shebbasoft.storm.mine.command;

import com.shebbasoft.shebbasoftlib.minecraft.command.SimpleCommand;
import com.shebbasoft.storm.mine.Mine;
import com.shebbasoft.storm.mine.MineController;
import com.shebbasoft.storm.mine.StormMines;
import com.shebbasoft.storm.mine.WorldEditArea;
import com.shebbasoft.storm.mine.WorldEditMine;
import com.sk89q.worldedit.IncompleteRegionException;
import com.sk89q.worldedit.LocalSession;
import com.sk89q.worldedit.WorldEdit;
import com.sk89q.worldedit.bukkit.BukkitPlayer;
import com.sk89q.worldedit.regions.Region;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class MineCreateCommand extends SimpleCommand {

    private static final String COMMAND_ALIAS = "create";
    private static final List<String> COMMAND_ALIASES = Collections.singletonList(COMMAND_ALIAS);

    private final MineController mineController;

    public MineCreateCommand(StormMines plugin) {
        mineController = plugin.getMineController();
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

        if (arguments.length < 1) {
            sender.sendMessage(ChatColor.RED + "Error: invalid command syntax.");
            getUsages(sender).forEach(usage -> sender.sendMessage(ChatColor.AQUA + usage));
            return;
        }

        BukkitPlayer bukkitPlayer = new BukkitPlayer(player);
        LocalSession session = WorldEdit.getInstance().getSessionManager().getIfPresent(bukkitPlayer);

        if (session == null) {
            sender.sendMessage(ChatColor.RED + "Error: could not find your WorldEdit session.");
            return;
        }

        Region selection;
        try {
            selection = session.getSelection(bukkitPlayer.getWorld());
        } catch (IncompleteRegionException e) {
            sender.sendMessage(ChatColor.RED + "Error: incomplete WorldEdit selection.");
            return;
        }

        Mine mine = new WorldEditMine(arguments[0], new WorldEditArea(selection));

        if (arguments.length > 1) {
            mine.setDisplayName(String.join(" ", Arrays.copyOfRange(arguments, 1, arguments.length)));
        }

        try {
            mineController.registerMine(mine);
        } catch (IllegalArgumentException e) {
            sender.sendMessage(ChatColor.RED + "Error: " + e.getMessage());
            return;
        }

        sender.sendMessage(ChatColor.GREEN + "Created mine successfully.");
    }

    @Override
    public @NotNull List<String> getUsages(@NotNull CommandSender sender) {
        return Collections.singletonList("/" + getPath() + COMMAND_ALIAS + " <name> [display-name]");
    }
}
