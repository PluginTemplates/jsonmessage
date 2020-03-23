package io.github.portlek.jsonmessage.hooks;

import io.github.portlek.jsonmessage.Wrapped;
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
