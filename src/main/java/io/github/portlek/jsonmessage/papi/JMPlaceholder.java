package io.github.portlek.jsonmessage.papi;

import io.github.portlek.jsonmessage.JsonMessage;
import io.github.portlek.jsonmessage.handle.User;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.entity.Player;

public final class JMPlaceholder extends PlaceholderExpansion {

    @Override
    public String getIdentifier() {
        return "jsonmessage";
    }

    @Override
    public String getAuthor() {
        return "portlek";
    }

    @Override
    public String getVersion() {
        return "1.0";
    }

    @Override
    public boolean persist() {
        return true;
    }

    @Override
    public boolean canRegister() {
        return true;
    }

    @Override
    public String onPlaceholderRequest(final Player player, final String identifier) {
        if (player == null) {
            return "";
        }
        final User user = JsonMessage.getAPI().usersFile.getOrCreateUser(player.getUniqueId());
        if ("player_color_code".equals(identifier)) {
            return user.getColorCode();
        }
        if ("player_format_code".equals(identifier)) {
            return user.getFormatCode().toString();
        }
        return "";
    }

}
