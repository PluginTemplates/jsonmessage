package io.github.portlek.jsonmessage.commands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.CommandPermission;
import co.aikar.commands.annotation.Default;
import co.aikar.commands.annotation.Subcommand;
import io.github.portlek.configs.util.ListToString;
import io.github.portlek.jsonmessage.JsonMessage;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@CommandAlias("farbe")
public final class FarbeCommand extends BaseCommand {

    @Default
    @CommandPermission("jsonmessage.command.farbe")
    public void defaultCommand(final Player player) {
        JsonMessage.getAPI().menuFile.userMenu.inventory(player).open(player);
    }

}
