package io.github.portlek.jsonmessage.file;

import io.github.portlek.configs.BukkitManaged;
import io.github.portlek.configs.annotations.Config;
import io.github.portlek.configs.annotations.Instance;
import io.github.portlek.configs.annotations.Section;
import io.github.portlek.configs.util.FileType;
import io.github.portlek.jsonmessage.handle.User;
import java.util.*;
import org.jetbrains.annotations.NotNull;

@Config(
    name = "users",
    type = FileType.JSON,
    location = "%basedir%/JsonMessage"
)
public final class UsersFile extends BukkitManaged {

    private static final Map<UUID, User> DATA = new HashMap<>();

    @Instance
    public final UsersFile.Users users = new UsersFile.Users();

    @NotNull
    public User getOrCreateUser(@NotNull final UUID uuid) {
        return Optional.ofNullable(UsersFile.DATA.get(uuid)).orElseGet(() -> {
            final User user = new User(
                uuid,
                "",
                new ArrayList<>(),
                false
            );
            UsersFile.DATA.put(uuid, user);
            user.save();
            return user;
        });
    }

    @Override
    public void load() {
        super.load();
        this.setAutoSave(true);
        this.getOrCreateSection("users").ifPresent(section ->
            section.getKeys(false).forEach(s -> {
                final UUID uuid = UUID.fromString(s);
                UsersFile.DATA.put(
                    uuid,
                    new User(
                        uuid,
                        this.getOrSet("users." + s + ".color-code", ""),
                        this.getOrSet("users." + s + ".format-code", new ArrayList<>()),
                        this.getOrSet("users." + s + ".rainbow", false)
                    )
                );
            })
        );
    }

    @Section(path = "users")
    public static final class Users {

    }

}
