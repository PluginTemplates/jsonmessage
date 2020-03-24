package io.github.portlek.jsonmessage;

import co.aikar.commands.BukkitCommandManager;
import co.aikar.commands.ConditionFailedException;
import io.github.portlek.jsonmessage.commands.JsonMessageCommand;
import java.util.Optional;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

public final class JsonMessage extends JavaPlugin {

    private static JsonMessageAPI api;

    private static JsonMessage instance;

    @NotNull
    public static JsonMessage getInstance() {
        return Optional.ofNullable(JsonMessage.instance).orElseThrow(() ->
            new IllegalStateException("You cannot be used JsonMessage plugin before its start!")
        );
    }

    private void setInstance(@NotNull final JsonMessage instance) {
        if (Optional.ofNullable(JsonMessage.instance).isPresent()) {
            throw new IllegalStateException("You can't use #setInstance method twice!");
        }
        synchronized (this) {
            JsonMessage.instance = instance;
        }
    }

    @NotNull
    public static JsonMessageAPI getAPI() {
        return Optional.ofNullable(JsonMessage.api).orElseThrow(() ->
            new IllegalStateException("You cannot be used JsonMessage plugin before its start!")
        );
    }

    private void setAPI(@NotNull final JsonMessageAPI loader) {
        if (Optional.ofNullable(JsonMessage.api).isPresent()) {
            throw new IllegalStateException("You can't use #setAPI method twice!");
        }
        synchronized (this) {
            JsonMessage.api = loader;
        }
    }

    @Override
    public void onLoad() {
        this.setInstance(this);
    }

    @Override
    public void onEnable() {
        final BukkitCommandManager manager = new BukkitCommandManager(this);
        final JsonMessageAPI api = new JsonMessageAPI(this);
        this.setAPI(api);
        this.getServer().getScheduler().runTask(this, () ->
            this.getServer().getScheduler().runTaskAsynchronously(this, () ->
                api.reloadPlugin(true)
            )
        );
        manager.getCommandConditions().addCondition(String[].class, "player", (context, exec, value) -> {
            if (value == null || value.length == 0) {
                return;
            }
            final String name = value[context.getConfigValue("arg", 0)];
            if (context.hasConfig("arg") && Bukkit.getPlayer(name) == null) {
                throw new ConditionFailedException(
                    api.languageFile.errors.player_not_found.build("%player_name%", () -> name)
                );
            }
        });
        manager.registerCommand(new JsonMessageCommand());
    }

}
