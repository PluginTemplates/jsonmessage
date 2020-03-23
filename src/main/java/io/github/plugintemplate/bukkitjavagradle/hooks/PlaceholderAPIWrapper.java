package io.github.plugintemplate.bukkitjavagradle.hooks;

import io.github.plugintemplate.bukkitjavagradle.Wrapped;
import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public final class PlaceholderAPIWrapper implements Wrapped {

    @NotNull
    public String apply(@NotNull final Player player, @NotNull final String string) {
        return PlaceholderAPI.setPlaceholders(player, string);
    }

    // TODO Add new PlaceholderAPI methods as you want.

}
