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

@CommandAlias("jsonmessage|jm")
public final class JsonMessageCommand extends BaseCommand {

    @Default
    @CommandPermission("jsonmessage.command.main")
    public void defaultCommand(final CommandSender sender) {
        sender.sendMessage(
            (String) JsonMessage.getAPI().languageFile.help_messages.buildMap(list ->
                new ListToString(list).value()
            )
        );
    }

    @Subcommand("help")
    @CommandPermission("jsonmessage.command.help")
    public void helpCommand(final CommandSender sender) {
        sender.sendMessage(
            (String) JsonMessage.getAPI().languageFile.help_messages.buildMap(list ->
                new ListToString(list).value()
            )
        );
    }

    @Subcommand("reload")
    @CommandPermission("jsonmessage.command.reload")
    public void reloadCommand(final CommandSender sender) {
        final long millis = System.currentTimeMillis();
        JsonMessage.getAPI().reloadPlugin(false);
        sender.sendMessage(
            JsonMessage.getAPI().languageFile.generals.reload_complete.build(
                "%ms%", () -> String.valueOf(System.currentTimeMillis() - millis)
            )
        );
    }

    @Subcommand("menu")
    @CommandPermission("jsonmessage.command.menu")
    public void menuCommand(final Player player) {
        JsonMessage.getAPI().menuFile.userMenu.inventory(player).open(player);
    }

}
