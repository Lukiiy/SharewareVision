package me.lukiiy.shareware;

import me.lukiiy.shareware.platform.Services;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Properties;

public class Config {
    private final Properties properties = new Properties();
    private final Path path;
    private final String modName;

    public Config(String fileName, String modName) {
        Path confDir = Services.PLATFORM.configDir();

        try {
            Files.createDirectories(confDir);
        } catch (IOException e) {
            Constants.LOG.error(e.getMessage());
        }

        this.path = confDir.resolve(fileName + ".properties");
        this.modName = modName;

        load();
    }

    public void load() {
        if (!Files.exists(path)) {
            save();
            return;
        }

        try (BufferedReader reader = Files.newBufferedReader(path, StandardCharsets.UTF_8)) {
            properties.load(reader);
        } catch (IOException e) {
            Constants.LOG.error(e.getMessage());
        }
    }

    public void save() {
        try (BufferedWriter writer = Files.newBufferedWriter(path, StandardCharsets.UTF_8)) {
            properties.store(writer, modName + " Config");
        } catch (IOException e) {
            Constants.LOG.error(e.getMessage());
        }
    }

    public boolean has(String key) {
        return properties.getProperty(key) != null;
    }

    public <T> void set(String key, T value) {
        if (value == null) properties.remove(key); else properties.setProperty(key, String.valueOf(value));

        save();
    }

    public <T> void setIfAbsent(String key, T defaultValue) {
        if (!has(key)) set(key, defaultValue);
    }

    @SuppressWarnings("unchecked")
    public <T> T get(String key, T fallback) {
        String stored = properties.getProperty(key);
        if (stored == null) return fallback;

        try {
            Object parsed = switch (fallback) { // neverending
                case Integer _ -> Integer.parseInt(stored.trim());
                case Long _ -> Long.parseLong(stored.trim());
                case Float _ -> Float.parseFloat(stored.trim());
                case Double _ -> Double.parseDouble(stored.trim());
                case Boolean _ -> Boolean.parseBoolean(stored.trim());
                default -> stored;
            };

            return (T) parsed;
        } catch (NumberFormatException e) {
            return fallback;
        }
    }

    public <T> Key<T> key(String name, T defaultValue) {
        return new Key<>(name, defaultValue);
    }

    public final class Key<T> {
        public final String key;
        public final T defaultValue;
        public boolean reloadable;

        private Key(String key, T defaultValue) {
            this.key = key;
            this.defaultValue = defaultValue;

            Config.this.setIfAbsent(key, defaultValue);
        }

        public String name() {
            return key;
        }

        public T defaultValue() {
            return defaultValue;
        }

        public T get() {
            return Config.this.get(key, defaultValue);
        }

        public void set(T value) {
            Config.this.set(key, value);
        }

        public Key<T> setReloadable() {
            this.reloadable = true;
            return this;
        }
    }
}