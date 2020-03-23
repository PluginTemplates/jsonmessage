package io.github.portlek.jsonmessage.file;

import io.github.portlek.configs.BukkitManaged;
import io.github.portlek.configs.annotations.Config;
import io.github.portlek.configs.annotations.Instance;
import io.github.portlek.configs.annotations.Section;
import io.github.portlek.configs.annotations.Value;
import io.github.portlek.configs.util.ColorUtil;
import io.github.portlek.configs.util.Replaceable;
import io.github.portlek.jsonmessage.Hook;
import io.github.portlek.jsonmessage.Wrapped;
import io.github.portlek.jsonmessage.hooks.GroupManagerHook;
import io.github.portlek.jsonmessage.hooks.LuckPermsHook;
import io.github.portlek.jsonmessage.hooks.PermissionsExHook;
import io.github.portlek.jsonmessage.hooks.PlaceholderAPIHook;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.BooleanSupplier;
import org.bukkit.Bukkit;
import org.jetbrains.annotations.NotNull;

@Config(
    name = "config",
    location = "%basedir%/JsonMessage"
)
public final class ConfigFile extends BukkitManaged {

    private static final String HOOKS_PATH = "hooks.";

    private static final String PLACEHOLDER_API = "PlaceholderAPI";

    private static final String GROUP_MANAGER = "GroupManager";

    private static final String LUCK_PERMS = "LuckPerms";

    private static final String PERMISSIONS_EX = "PermissionsEx";

    @Instance
    public final ConfigFile.Hooks hooks = new ConfigFile.Hooks();

    private final Map<String, Wrapped> wrapped = new HashMap<>();

    @Value
    public Replaceable<String> plugin_prefix = Replaceable.of("&6[&eJsonMessage&6]")
        .map(ColorUtil::colored);

    @Value
    public String plugin_language = "en";

    @Value
    public boolean check_for_update = true;

    @Override
    public void load() {
        super.load();
        this.loadWrapped();
        this.setAutoSave(true);
    }

    @NotNull
    @SuppressWarnings("unchecked")
    public <T extends Wrapped> Optional<T> getWrapped(@NotNull final String wrappedId) {
        return Optional.ofNullable(
            (T) this.wrapped.get(wrappedId)
        );
    }

    private void loadWrapped() {
        this.hookLittle(this.hooks.LuckPerms, new LuckPermsHook(), ConfigFile.LUCK_PERMS,
            () -> {
                this.hooks.LuckPerms = true;
                this.set(ConfigFile.HOOKS_PATH + ConfigFile.LUCK_PERMS, true);
            },
            () -> {
                this.hooks.LuckPerms = false;
                this.set(ConfigFile.HOOKS_PATH + ConfigFile.LUCK_PERMS, false);
            });

        this.hookLittle(this.hooks.PlaceholderAPI, new PlaceholderAPIHook(), ConfigFile.PLACEHOLDER_API,
            () -> {
                this.hooks.PlaceholderAPI = true;
                this.set(ConfigFile.HOOKS_PATH + ConfigFile.PLACEHOLDER_API, true);
            },
            () -> {
                this.hooks.PlaceholderAPI = false;
                this.set(ConfigFile.HOOKS_PATH + ConfigFile.PLACEHOLDER_API, false);
            });
        this.hookLittle(this.hooks.GroupManager, new GroupManagerHook(), ConfigFile.GROUP_MANAGER, () ->
                !this.wrapped.containsKey(ConfigFile.PERMISSIONS_EX) && !this.wrapped.containsKey(ConfigFile.LUCK_PERMS),
            () -> {
                this.hooks.GroupManager = true;
                this.set(ConfigFile.HOOKS_PATH + ConfigFile.GROUP_MANAGER, true);
            },
            () -> {
                this.hooks.GroupManager = false;
                this.set(ConfigFile.HOOKS_PATH + ConfigFile.GROUP_MANAGER, false);
            });
        this.hookLittle(this.hooks.PermissionsEX, new PermissionsExHook(), ConfigFile.PERMISSIONS_EX, () ->
                !this.wrapped.containsKey(ConfigFile.GROUP_MANAGER) && !this.wrapped.containsKey(ConfigFile.LUCK_PERMS),
            () -> {
                this.hooks.PermissionsEX = true;
                this.set(ConfigFile.HOOKS_PATH + ConfigFile.PERMISSIONS_EX, true);
            },
            () -> {
                this.hooks.PermissionsEX = false;
                this.set(ConfigFile.HOOKS_PATH + ConfigFile.PERMISSIONS_EX, false);
            });
    }

    private void hookLittle(final boolean force, @NotNull final Hook hook, @NotNull final String path, @NotNull final Runnable succeed,
                            @NotNull final Runnable failed) {
        this.hookLittle(force, hook, path, () -> true, succeed, failed);
    }

    private void hookLittle(final boolean force, @NotNull final Hook hook, @NotNull final String path,
                            @NotNull final BooleanSupplier supplier, @NotNull final Runnable succeed, @NotNull final Runnable failed) {
        if ((this.hooks.auto_detect || force) && hook.initiate() && supplier.getAsBoolean()) {
            this.wrapped.put(path, hook.create());
            this.sendHookNotify(path);
            this.set(ConfigFile.HOOKS_PATH + path, true);
            succeed.run();
        } else {
            this.set(ConfigFile.HOOKS_PATH + path, false);
            failed.run();
        }
    }

    private void sendHookNotify(@NotNull final String path) {
        Bukkit.getConsoleSender().sendMessage(
            ColorUtil.colored(
                this.plugin_prefix.build() + " &r>> &a" + path + " is hooking"
            )
        );
    }

    @Section(path = "hooks")
    public static final class Hooks {

        @Value
        public boolean auto_detect = true;

        @Value
        public boolean PlaceholderAPI = false;

        @Value
        public boolean GroupManager = false;

        @Value
        public boolean LuckPerms = false;

        @Value
        public boolean PermissionsEX = false;

        @Value
        public boolean Vault = false;

    }

}
