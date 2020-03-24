package io.github.portlek.jsonmessage.file;

import io.github.portlek.configs.BukkitManaged;
import io.github.portlek.configs.Managed;
import io.github.portlek.configs.annotations.Config;
import io.github.portlek.configs.annotations.Instance;
import io.github.portlek.configs.annotations.Section;
import io.github.portlek.configs.annotations.Value;
import io.github.portlek.configs.util.FileType;
import io.github.portlek.jsonmessage.JsonMessage;
import io.github.portlek.jsonmessage.Wrapped;
import io.github.portlek.jsonmessage.handle.Format;
import io.github.portlek.jsonmessage.handle.FormatFile;
import io.github.portlek.jsonmessage.handle.Group;
import io.github.portlek.jsonmessage.hooks.GroupManagerWrapper;
import io.github.portlek.jsonmessage.hooks.LuckPermsWrapper;
import io.github.portlek.jsonmessage.hooks.PermissionsExWrapper;
import io.github.portlek.jsonmessage.util.CreateStorage;
import java.io.File;
import java.util.*;
import java.util.stream.Collectors;
import org.bukkit.entity.Player;
import org.cactoos.list.Joined;
import org.jetbrains.annotations.NotNull;
import ru.tehkode.permissions.PermissionGroup;

@Config(
    name = "formats",
    type = FileType.JSON,
    location = "%basedir%/JsonMessage"
)
public final class FormatsFile extends BukkitManaged {

    private static final Map<String, Group> GROUPS = new HashMap<>();

    @Instance
    public final FormatsFile.Default def = new FormatsFile.Default();

    @Instance
    public final FormatsFile.Groups groups = new FormatsFile.Groups();

    @NotNull
    public static Optional<Group> getByName(@NotNull final String name) {
        return Optional.ofNullable(FormatsFile.GROUPS.get(name));
    }

    @Override
    public void load() {
        super.load();
        this.setAutoSave(true);
        final FormatFile defaultPrefix = new FormatFile(this.createManaged(this.def.prefix));
        final FormatFile defaultPlayer = new FormatFile(this.createManaged(this.def.player));
        final FormatFile defaultSuffix = new FormatFile(this.createManaged(this.def.suffix));
        final FormatFile defaultMessage = new FormatFile(this.createManaged(this.def.message));
        if (!defaultPrefix.getManaged().getSection("components").isPresent()) {
            final Managed managed = defaultPrefix.getManaged();
            managed.set("components.0.text", "[");
            managed.set("components.0.color", "gold");
            managed.set("components.0.hover.action", "SHOW_TEXT");
            managed.set("components.0.hover.value", "This is prefix");
            managed.set("components.1.text", "Guest");
            managed.set("components.1.hover.action", "SHOW_TEXT");
            managed.set("components.1.hover.value", "This is prefix");
            managed.set("components.2.text", "] ");
            managed.set("components.2.color", "gold");
            managed.set("components.2.hover.action", "SHOW_TEXT");
            managed.set("components.2.hover.value", "This is suffix");
        }
        if (!defaultPlayer.getManaged().getSection("components").isPresent()) {
            final Managed managed = defaultPlayer.getManaged();
            managed.set("components.0.text", "%player_name% ");
            managed.set("components.0.color", "gray");
            managed.set("components.0.hover.action", "SHOW_TEXT");
            managed.set("components.0.hover.value", "&7Player name > &e%player_name%\n&7Player money > &e%vault_eco_balance%");
            managed.set("components.0.click.action", "RUN_CONSUMER");
            managed.set("components.0.click.commands.0.as-player", false);
            managed.set("components.0.click.commands.0.placeholderapi-mode", "clicker");
            managed.set("components.0.click.commands.0.values", Arrays.asList(
                "give %player_name% apple",
                "msg %player_name% You clicked the message of %writer% and gain rewards"
            ));
            managed.set("components.0.click.commands.1.as-player", false);
            managed.set("components.0.click.commands.1.placeholderapi-mode", "writer");
            managed.set("components.0.click.commands.1.values", Arrays.asList(
                "give %player_name% apple",
                "msg %player_name% %clicker% clicked your message and gain rewards"
            ));
        }
        if (!defaultSuffix.getManaged().getSection("components").isPresent()) {
            final Managed managed = defaultSuffix.getManaged();
            managed.set("components.0.text", "> ");
            managed.set("components.0.color", "reset");
        }
        if (!defaultMessage.getManaged().getSection("components").isPresent()) {
            final Managed managed = defaultMessage.getManaged();
            managed.set("components.0.text", "%message%");
            managed.set("components.0.color", "yellow");
            managed.set("components.0.hover.action", "SHOW_TEXT");
            managed.set("components.0.hover.value", "&7Player message > &e%message%");
            managed.set("components.0.hover.value", "&7Player message > &e%message%");
            managed.set("components.0.click.action", "RUN_CONSUMER");
            managed.set("components.0.click.remove-after-click", true);
            managed.set("components.0.click.commands.0.as-player", false);
            managed.set("components.0.click.commands.0.placeholderapi-mode", "writer");
            managed.set("components.0.click.commands.0.values", Collections.singletonList(
                "msg %player_name% %clicker% clicked your message!"
            ));
        }
        FormatsFile.GROUPS.put(
            "default_group",
            new Group(
                "default_group",
                new Format(
                    new Joined<>(
                        defaultPrefix.loadMessages(),
                        defaultPlayer.loadMessages(),
                        defaultSuffix.loadMessages(),
                        defaultMessage.loadMessages(true)
                    )
                )
            )
        );
        this.getOrCreateSection("groups").ifPresent(section ->
            section.getKeys(false).forEach(s -> {
                final FormatFile prefix = new FormatFile(this.createManaged(this.getOrSet("groups." + s + ".prefix", s + "_prefix")));
                final FormatFile player = new FormatFile(this.createManaged(this.getOrSet("groups." + s + ".player", s + "_player")));
                final FormatFile suffix = new FormatFile(this.createManaged(this.getOrSet("groups." + s + ".suffix", s + "_suffix")));
                final FormatFile message = new FormatFile(this.createManaged(this.getOrSet("groups." + s + ".message", s + "_message")));
                FormatsFile.GROUPS.put(
                    s,
                    new Group(
                        s,
                        new Format(
                            new Joined<>(
                                prefix.loadMessages(),
                                player.loadMessages(),
                                suffix.loadMessages(),
                                message.loadMessages(true)
                            )
                        )
                    )
                );
            })
        );
    }

    @NotNull
    public BukkitManaged createManaged(@NotNull final String filename) {
        final BukkitManaged managed = new BukkitManaged() {
        };
        final File file = new CreateStorage(
            JsonMessage.getInstance().getDataFolder().getAbsolutePath() + File.separator + "formats",
            filename + ".json"
        ).value();
        managed.setup(
            file,
            FileType.JSON.load(file)
        );
        managed.save();
        managed.setAutoSave(true);
        return managed;
    }

    @NotNull
    public Optional<Group> findGroupByPlayer(@NotNull final Player player) {
        final Optional<Wrapped> groupManager = JsonMessage.getAPI().configFile.getWrapped("GroupManager");
        final Optional<Wrapped> luckPerms = JsonMessage.getAPI().configFile.getWrapped("LuckPerms");
        final Optional<Wrapped> permissionsEx = JsonMessage.getAPI().configFile.getWrapped("PermissionsEx");
        return groupManager.map(wrapped ->
            Collections.singletonList(((GroupManagerWrapper) wrapped).getGroup(player))
        ).orElseGet(() ->
            luckPerms.map(wrapped ->
                Collections.singletonList(((LuckPermsWrapper) wrapped).getGroup(player))
            ).orElseGet(() ->
                permissionsEx.map(wrapped ->
                    ((PermissionsExWrapper) wrapped).getGroup(player).stream()
                        .map(PermissionGroup::getName)
                        .collect(Collectors.toList())
                ).orElseGet(ArrayList::new)
            )
        ).stream()
            .filter(FormatsFile.GROUPS::containsKey)
            .map(FormatsFile.GROUPS::get)
            .findFirst();
    }

    @Section(path = "default_sroup")
    public static final class Default {

        @Value
        public String prefix = "default_prefix";

        @Value
        public String player = "default_player";

        @Value
        public String suffix = "default_suffix";

        @Value
        public String message = "default_message";

    }

    @Section(path = "groups")
    public static final class Groups {

    }

}
