package io.github.portlek.jsonmessage.hooks;

import io.github.portlek.jsonmessage.Hook;
import org.bukkit.Bukkit;
import org.jetbrains.annotations.NotNull;
import ru.tehkode.permissions.bukkit.PermissionsEx;

public final class PermissionsExHook implements Hook {

    private PermissionsEx permissionsEx;

    @Override
    public boolean initiate() {
        return (this.permissionsEx = (PermissionsEx) Bukkit.getPluginManager().getPlugin("PermissionsEx")) != null;
    }

    @Override
    @NotNull
    public PermissionsExWrapper create() {
        if (this.permissionsEx == null) {
            throw new IllegalStateException("PermissionsEx not initiated! Use PermissionsExHook#initiate method.");
        }
        return new PermissionsExWrapper(this.permissionsEx);
    }

}
