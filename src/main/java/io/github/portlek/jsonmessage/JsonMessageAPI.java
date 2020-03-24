package io.github.portlek.jsonmessage;

import fr.minuskube.inv.InventoryManager;
import io.github.portlek.jsonmessage.file.*;
import io.github.portlek.jsonmessage.handle.Group;
import io.github.portlek.jsonmessage.handle.User;
import io.github.portlek.jsonmessage.util.ListenerBasic;
import java.util.Optional;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.AsyncPlayerChatEvent;
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
    public final FormatsFile formatsFile;

    @NotNull
    public final UsersFile usersFile;

    @NotNull
    public final MenuFile menuFile;

    public JsonMessageAPI(@NotNull final JsonMessage jsonMessage) {
        this.inventoryManager = new InventoryManager(jsonMessage);
        this.jsonMessage = jsonMessage;
        this.configFile = new ConfigFile();
        this.languageFile = new LanguageFile(this.configFile);
        this.formatsFile = new FormatsFile();
        this.usersFile = new UsersFile();
        this.menuFile = new MenuFile();
    }

    public void reloadPlugin(final boolean first) {
        this.languageFile.load();
        this.configFile.load();
        this.formatsFile.load();
        this.usersFile.load();
        this.menuFile.load();
        if (first) {
            this.inventoryManager.init();
            new ListenerBasic<>(
                AsyncPlayerChatEvent.class,
                EventPriority.MONITOR,
                event -> {
                    if (event.isCancelled()) {
                        return;
                    }
                    event.setCancelled(true);
                    final User user = this.usersFile.getOrCreateUser(event.getPlayer().getUniqueId());
                    final Optional<Group> opitional = this.formatsFile.findGroupByPlayer(event.getPlayer());
                    if (this.configFile.groups_enable && opitional.isPresent()) {
                        opitional.get().send(user, event.getMessage());
                        return;
                    }
                    FormatsFile.getByName("default_group").ifPresent(group ->
                        group.send(user, event.getMessage())
                    );
                }
            ).register(this.jsonMessage);
        }
    }

}
