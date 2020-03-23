package io.github.portlek.jsonmessage.commands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.CommandPermission;
import co.aikar.commands.annotation.Default;
import co.aikar.commands.annotation.Subcommand;
import io.github.portlek.configs.util.ListToString;
import io.github.portlek.jsonmessage.JsonMessageAPI;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

@CommandAlias("jsonmessage|jm")
public final class JsonMessageCommand extends BaseCommand {

    @NotNull
    private final JsonMessageAPI api;

    public JsonMessageCommand(@NotNull final JsonMessageAPI api) {
        this.api = api;
    }

    @Default
    @CommandPermission("jsonmessage.command.main")
    public void defaultCommand(final CommandSender sender) {
        sender.sendMessage(
            (String) this.api.languageFile.help_messages.buildMap(list ->
                new ListToString(list).value()
            )
        );
    }

    @Subcommand("help")
    @CommandPermission("jsonmessage.command.help")
    public void helpCommand(final CommandSender sender) {
        sender.sendMessage(
            (String) this.api.languageFile.help_messages.buildMap(list ->
                new ListToString(list).value()
            )
        );
    }

    @Subcommand("reload")
    @CommandPermission("jsonmessage.command.reload")
    public void reloadCommand(final CommandSender sender) {
        final long millis = System.currentTimeMillis();
        this.api.reloadPlugin(false);
        sender.sendMessage(
            this.api.languageFile.generals.reload_complete.build(
                "%ms%", () -> String.valueOf(System.currentTimeMillis() - millis)
            )
        );
    }

}
