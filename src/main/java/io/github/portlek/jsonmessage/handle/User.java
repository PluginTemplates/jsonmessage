package io.github.portlek.jsonmessage.handle;

import io.github.portlek.configs.Managed;
import io.github.portlek.jsonmessage.JsonMessage;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public final class User {

    @NotNull
    private final UUID uniqueId;

    private boolean rainbow;

    private String colorCode;

    private List<String> formatCode;

    public User(@NotNull final UUID uniqueId, final String colorCode, final List<String> formatCode,
                final boolean rainbow) {
        this.uniqueId = uniqueId;
        this.colorCode = colorCode;
        this.formatCode = formatCode;
        this.rainbow = rainbow;
    }

    public boolean rainbow() {
        return this.rainbow;
    }

    @NotNull
    public String getColorName() {
        switch (this.colorCode) {
            case "&4":
                return "dark_red";
            case "&c":
                return "red";
            case "&6":
                return "gold";
            case "&e":
                return "yellow";
            case "&2":
                return "dark_green";
            case "&a":
                return "green";
            case "&b":
                return "aqua";
            case "&3":
                return "dark_aqua";
            case "&1":
                return "dark_blue";
            case "&9":
                return "blue";
            case "&d":
                return "light_purple";
            case "&5":
                return "dark_purple";
            case "&f":
                return "white";
            case "&7":
                return "gray";
            case "&8":
                return "dark_gray";
            case "&0":
                return "black";
            default:
                return "reset";
        }
    }

    public void reset() {
        this.setColorCode("");
        this.setFormatCode(new ArrayList<>());
    }

    public void setRainbow(final boolean rainbow) {
        this.rainbow = rainbow;
        this.colorCode = "";
        this.save();
    }

    public void save() {
        final Managed managed = JsonMessage.getAPI().usersFile;
        final String path = "users." + this.uniqueId.toString() + '.';
        managed.set(path + "color-code", this.colorCode);
        managed.set(path + "format-code", this.formatCode);
        managed.set(path + "rainbow", this.rainbow);
    }

    @NotNull
    public String getColorCode() {
        return this.colorCode;
    }

    public void setColorCode(@NotNull final String colorCode) {
        this.setRainbow(false);
        this.colorCode = colorCode;
        this.save();
    }

    public boolean underlined() {
        return this.formatCode.contains("&n");
    }

    public boolean strikethrough() {
        return this.formatCode.contains("&m");
    }

    public boolean italic() {
        return this.formatCode.contains("&o");
    }

    public boolean bold() {
        return this.formatCode.contains("&b");
    }

    public boolean color() {
        return !this.colorCode.isEmpty();
    }

    @NotNull
    public List<String> getFormatCode() {
        return this.formatCode;
    }

    public void setFormatCode(@NotNull final List<String> formatCode) {
        this.formatCode = formatCode;
        this.save();
    }

    @NotNull
    public UUID getUniqueId() {
        return this.uniqueId;
    }

    @NotNull
    public Optional<Player> getPlayer() {
        return Optional.ofNullable(Bukkit.getPlayer(this.uniqueId));
    }

}
