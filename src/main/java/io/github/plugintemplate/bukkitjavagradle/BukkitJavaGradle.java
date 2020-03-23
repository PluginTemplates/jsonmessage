package io.github.plugintemplate.bukkitjavagradle;

import co.aikar.commands.BukkitCommandManager;
import co.aikar.commands.ConditionFailedException;
import io.github.plugintemplate.bukkitjavagradle.commands.BukkitJavaGradleCommand;
import java.util.Optional;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

// TODO Change the class name as you want.
public final class BukkitJavaGradle extends JavaPlugin {

    private static BukkitJavaGradleAPI api;

    private static BukkitJavaGradle instance;

    @NotNull
    public static BukkitJavaGradle getInstance() {
        return Optional.ofNullable(BukkitJavaGradle.instance).orElseThrow(() ->
            new IllegalStateException("You cannot be used BukkitJavaGradle plugin before its start!")
        );
    }

    private void setInstance(@NotNull final BukkitJavaGradle instance) {
        if (Optional.ofNullable(BukkitJavaGradle.instance).isPresent()) {
            throw new IllegalStateException("You can't use #setInstance method twice!");
        }
        synchronized (this) {
            BukkitJavaGradle.instance = instance;
        }
    }

    @NotNull
    public static BukkitJavaGradleAPI getAPI() {
        return Optional.ofNullable(BukkitJavaGradle.api).orElseThrow(() ->
            new IllegalStateException("You cannot be used BukkitJavaGradle plugin before its start!")
        );
    }

    private void setAPI(@NotNull final BukkitJavaGradleAPI loader) {
        if (Optional.ofNullable(BukkitJavaGradle.api).isPresent()) {
            throw new IllegalStateException("You can't use #setAPI method twice!");
        }
        synchronized (this) {
            BukkitJavaGradle.api = loader;
        }
    }

    @Override
    public void onLoad() {
        this.setInstance(this);
    }

    @Override
    public void onDisable() {
        if (BukkitJavaGradle.api != null) {
            BukkitJavaGradle.api.disablePlugin();
        }
    }

    @Override
    public void onEnable() {
        final BukkitCommandManager manager = new BukkitCommandManager(this);
        final BukkitJavaGradleAPI api = new BukkitJavaGradleAPI(this);
        this.setAPI(api);
        this.getServer().getScheduler().runTask(this, () ->
            this.getServer().getScheduler().runTaskAsynchronously(this, () ->
                api.reloadPlugin(true)
            )
        );
        manager.getCommandConditions().addCondition(String[].class, "player", (c, exec, value) -> {
            if (value == null || value.length == 0) {
                return;
            }
            final String name = value[c.getConfigValue("arg", 0)];
            if (c.hasConfig("arg") && Bukkit.getPlayer(name) == null) {
                throw new ConditionFailedException(
                    api.languageFile.errors.player_not_found.build("%player_name%", () -> name)
                );
            }
        });
        manager.registerCommand(
            new BukkitJavaGradleCommand(api)
        );
    }

}
