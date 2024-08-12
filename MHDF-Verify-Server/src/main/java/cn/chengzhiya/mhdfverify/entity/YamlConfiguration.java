package cn.chengzhiya.mhdfverify.entity;

import lombok.Data;
import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.Yaml;

import java.io.*;
import java.nio.file.Files;
import java.util.*;

@Data
public final class YamlConfiguration {
    private Map<String, Object> data;

    public static YamlConfiguration loadConfiguration(File file) {
        YamlConfiguration config = new YamlConfiguration();
        Yaml yaml = new Yaml();
        try (InputStream in = Files.newInputStream(file.toPath())) {
            config.setData(yaml.load(in));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        if (config.getData() == null) {
            config.setData(new HashMap<>());
        }
        return config;
    }

    public void set(String path, Object value) {
        String[] keys = path.split("\\.");
        for (int i = 0; i < keys.length - 1; i++) {
            this.data = (Map<String, Object>) this.data.get(keys[i]);
        }
        this.data.put(keys[keys.length - 1], value);
    }

    public Object get(String path) {
        try {
            String[] keys = path.split("\\.");
            Map<String, Object> current = this.data;
            for (String key : keys) {
                Object value = current.get(key);
                if (!(value instanceof Map)) {
                    return value;
                }
                current = (Map<String, Object>) value;
            }
        } catch (Exception ignored) {
        }
        return null;
    }

    public void save(File file) {
        DumperOptions options = new DumperOptions();
        options.setDefaultFlowStyle(DumperOptions.FlowStyle.BLOCK);
        Yaml yaml = new Yaml(options);
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            yaml.dump(this.data, writer);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public YamlConfiguration getConfigurationSection(String path) {
        try {
            String[] keys = path.split("\\.");
            Map<String, Object> current = this.data;
            for (String key : keys) {
                current = (Map<String, Object>) current.get(key);
            }
            YamlConfiguration section = new YamlConfiguration();
            section.setData(current);
            return section;
        } catch (Exception ignored) {
        }
        return null;
    }

    public Set<String> getKeys() {
        return this.data.keySet();
    }

    public String getString(String path) {
        return (String) this.get(path);
    }

    public Integer getInt(String path) {
        return (Integer) this.get(path);
    }

    public boolean getBoolean(String path) {
        Object value = this.get(path);
        return value != null ? (Boolean) value : false;
    }

    public Double getDouble(String path) {
        return (Double) this.get(path);
    }

    public Long getLong(String path) {
        return Long.parseLong((String) Objects.requireNonNull(this.get(path)));
    }

    public List<?> getList(String path) {
        Object value = this.get(path);
        return value != null ? (List<?>) value : new ArrayList<>();
    }

    public List<String> getStringList(String path) {
        Object value = this.get(path);
        return value != null ? (List<String>) value : new ArrayList<>();
    }

    public List<Integer> getIntList(String path) {
        Object value = this.get(path);
        return value != null ? (List<Integer>) value : new ArrayList<>();
    }

    public List<Boolean> getBooleanList(String path) {
        Object value = this.get(path);
        return value != null ? (List<Boolean>) value : new ArrayList<>();
    }

    public List<Double> getDoubleList(String path) {
        Object value = this.get(path);
        return value != null ? (List<Double>) value : new ArrayList<>();
    }

    public List<Long> getLongList(String path) {
        Object value = this.get(path);
        return value != null ? (List<Long>) value : new ArrayList<>();
    }

    public boolean isString(String path) {
        return this.get(path) instanceof String;
    }

    public boolean isInt(String path) {
        return this.get(path) instanceof Integer;
    }

    public boolean isBoolean(String path) {
        return this.get(path) instanceof Boolean;
    }

    public boolean isDouble(String path) {
        return this.get(path) instanceof Double;
    }

    public boolean isLong(String path) {
        return this.get(path) instanceof Long;
    }

    public boolean isList(String path) {
        return this.get(path) instanceof List;
    }
}
