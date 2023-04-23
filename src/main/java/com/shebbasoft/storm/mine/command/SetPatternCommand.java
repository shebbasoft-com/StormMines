package com.shebbasoft.storm.mine.command;

import com.shebbasoft.shebbasoftlib.minecraft.command.SimpleCommand;
import com.shebbasoft.storm.mine.Mine;
import com.shebbasoft.storm.mine.MineController;
import com.shebbasoft.storm.mine.StormMines;
import com.sk89q.worldedit.WorldEdit;
import com.sk89q.worldedit.bukkit.BukkitPlayer;
import com.sk89q.worldedit.extension.input.InputParseException;
import com.sk89q.worldedit.extension.input.ParserContext;
import com.sk89q.worldedit.function.pattern.Pattern;
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

        // The documentation stats to not make patterns this way, but its just so much easier.
        ParserContext context = new ParserContext();
        BukkitPlayer bukkitPlayer = new BukkitPlayer(player);
        context.setActor(bukkitPlayer);
        context.setWorld(bukkitPlayer.getWorld());
        context.setSession(WorldEdit.getInstance().getSessionManager().getIfPresent(bukkitPlayer));

        Pattern pattern;
        try {
            pattern = WorldEdit.getInstance().getPatternFactory().parseFromInput(arguments[1], context);
        } catch (InputParseException e) {
            sender.sendMessage(ChatColor.RED + "Error: Invalid WorldEdit pattern");
            return;
        }

        mine.setPattern(pattern);
        mine.reset();
        sender.sendMessage(ChatColor.GREEN + "Set mine pattern successfully.");
    }

    @Override
    public @NotNull List<String> getUsages(@NotNull CommandSender sender) {
        return Collections.singletonList("/" + getPath() + COMMAND_ALIAS + " <name> <pattern>");
    }
}
