package io.github.portlek.jsonmessage;

import org.jetbrains.annotations.NotNull;

public interface Hook {

    boolean initiate();

    @NotNull
    Wrapped create();

}
