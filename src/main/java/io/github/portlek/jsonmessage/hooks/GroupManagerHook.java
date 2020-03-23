package io.github.portlek.jsonmessage.hooks;

import io.github.portlek.jsonmessage.Hook;
import io.github.portlek.jsonmessage.Wrapped;
import org.anjocaido.groupmanager.GroupManager;
import org.bukkit.Bukkit;
import org.jetbrains.annotations.NotNull;

public final class GroupManagerHook implements Hook {

    private GroupManager groupManager;

    @Override
    public boolean initiate() {
        return (this.groupManager = (GroupManager) Bukkit.getPluginManager().getPlugin("GroupManager")) != null;
    }

    @Override
    @NotNull
    public Wrapped create() {
        if (this.groupManager == null) {
            throw new IllegalStateException("GroupManager not initiated! Use GroupManagerHook#initiate method.");
        }
        return new GroupManagerWrapper(this.groupManager);
    }

}
