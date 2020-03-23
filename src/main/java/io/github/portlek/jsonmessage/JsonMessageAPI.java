package io.github.portlek.jsonmessage;

import fr.minuskube.inv.InventoryManager;
import io.github.portlek.jsonmessage.file.ConfigFile;
import io.github.portlek.jsonmessage.file.LanguageFile;
import io.github.portlek.jsonmessage.file.UsersFile;
import org.jetbrains.annotations.NotNull;

public final class JsonMessageAPI {

    @NotNull
    public final InventoryManager inventoryManager;

    @NotNull
    public final JsonMessage jsonMessage;

    @NotNull
    public final ConfigFile configFile;

    @NotNull
    public final LanguageFile languageFile;

    @NotNull
    public final UsersFile usersFile;

    public JsonMessageAPI(@NotNull final JsonMessage jsonMessage) {
        this.inventoryManager = new InventoryManager(jsonMessage);
        this.jsonMessage = jsonMessage;
        this.configFile = new ConfigFile();
        this.languageFile = new LanguageFile(this.configFile);
        this.usersFile = new UsersFile();
    }

    public void reloadPlugin(final boolean first) {
        this.languageFile.load();
        this.configFile.load();
        if (first) {
            this.inventoryManager.init();
        }
    }

}
