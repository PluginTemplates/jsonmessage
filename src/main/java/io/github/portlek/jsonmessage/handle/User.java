package io.github.portlek.jsonmessage.handle;

import io.github.portlek.configs.Managed;
import io.github.portlek.jsonmessage.JsonMessage;
import java.util.UUID;
import org.jetbrains.annotations.NotNull;

public final class User {

    @NotNull
    private final UUID uniqueId;

    private String colorCode;

    private String formatCode;

    public User(@NotNull final UUID uniqueId, final String colorCode, final String formatCode) {
        this.uniqueId = uniqueId;
        this.colorCode = colorCode;
        this.formatCode = formatCode;
    }

    @NotNull
    public String getColorCode() {
        return this.colorCode;
    }

    public void setColorCode(@NotNull final String colorCode) {
        this.colorCode = colorCode;
        this.save();
    }

    public void save() {
        final Managed managed = JsonMessage.getAPI().usersFile;
        final String path = "users." + this.uniqueId.toString() + '.';
        managed.set(path + "color-code", this.colorCode);
        managed.set(path + "format-code", this.formatCode);
    }

    @NotNull
    public String getFormatCode() {
        return this.formatCode;
    }

    public void setFormatCode(@NotNull final String formatCode) {
        this.formatCode = formatCode;
        this.save();
    }

    @NotNull
    public UUID getUniqueId() {
        return this.uniqueId;
    }

}
