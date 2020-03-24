package io.github.portlek.jsonmessage.util;

import java.io.File;
import java.io.IOException;
import org.jetbrains.annotations.NotNull;

public final class CreateStorage {

    @NotNull
    private final String path;

    @NotNull
    private final String fileName;

    public CreateStorage(@NotNull final String path, @NotNull final String fileName) {
        this.path = path;
        this.fileName = fileName;
    }

    @NotNull
    public File value() {
        final File storage = new File(this.path, this.fileName);
        if (!storage.exists()) {
            storage.getParentFile().mkdirs();
            try {
                storage.createNewFile();
            } catch (final IOException exception) {
                throw new IllegalStateException(exception);
            }
        }
        if (!storage.exists()) {
            throw new IllegalStateException(storage.getName() + " cannot be created, please check file permissions!");
        }
        return storage;
    }

}