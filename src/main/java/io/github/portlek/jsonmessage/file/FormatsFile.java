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

    @Instance
    public final FormatsFile.Template template = new FormatsFile.Template();

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
        this.createDefaultValues(defaultPrefix, defaultPlayer, defaultSuffix, defaultMessage);
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
                    this.createDefaultValues(prefix, player, suffix, message);
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

    public void createDefaultValues(@NotNull final FormatFile prefix, @NotNull final FormatFile player, @NotNull final FormatFile suffix,
                                    @NotNull final FormatFile message) {
        if (!prefix.getManaged().getSection("components").isPresent()) {
            final Managed managed = prefix.getManaged();
            this.getOrCreateSection("template.prefix").ifPresent(section ->
                section.getValues(true).forEach(managed::set)
            );
        }
        if (!player.getManaged().getSection("components").isPresent()) {
            final Managed managed = player.getManaged();
            this.getOrCreateSection("template.player").ifPresent(section ->
                section.getValues(true).forEach(managed::set)
            );
        }
        if (!suffix.getManaged().getSection("components").isPresent()) {
            final Managed managed = suffix.getManaged();
            this.getOrCreateSection("template.suffix").ifPresent(section ->
                section.getValues(true).forEach(managed::set)
            );
        }
        if (!message.getManaged().getSection("components").isPresent()) {
            final Managed managed = message.getManaged();
            this.getOrCreateSection("template.message").ifPresent(section ->
                section.getValues(true).forEach(managed::set)
            );
        }
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

    @Section(path = "template")
    public static final class Template {

        @Instance
        public final FormatsFile.Template.Prefix prefix = new FormatsFile.Template.Prefix();

        @Instance
        public final FormatsFile.Template.Player player = new FormatsFile.Template.Player();

        @Instance
        public final FormatsFile.Template.Suffix suffix = new FormatsFile.Template.Suffix();

        @Instance
        public final FormatsFile.Template.Message message = new FormatsFile.Template.Message();

        @Section(path = "prefix")
        public static final class Prefix {

        }

        @Section(path = "player")
        public static final class Player {

        }

        @Section(path = "suffix")
        public static final class Suffix {

        }

        @Section(path = "message")
        public static final class Message {

        }

    }

    @Section(path = "default_group")
    public static final class Default {

        @Value
        public String prefix = "default_group_prefix";

        @Value
        public String player = "default_group_player";

        @Value
        public String suffix = "default_group_suffix";

        @Value
        public String message = "default_group_message";

    }

    @Section(path = "groups")
    public static final class Groups {

    }

}
