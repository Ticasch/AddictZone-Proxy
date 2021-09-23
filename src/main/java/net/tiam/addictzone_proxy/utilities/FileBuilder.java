package net.tiam.addictzone_proxy.utilities;

import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.config.ConfigurationProvider;
import net.md_5.bungee.config.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Set;

public class FileBuilder {
    private File f;
    private Configuration c;
    public FileBuilder(String FilePath, String Filename) throws IOException {
        this.f = new File(FilePath, Filename);
        this.c = ConfigurationProvider.getProvider(YamlConfiguration.class).load(this.f);
    }
    public FileBuilder(String FilePath) throws IOException {
        this.f = new File(FilePath);
        this.c = ConfigurationProvider.getProvider(YamlConfiguration.class).load(this.f);
    }
    public FileBuilder setValue(String ValuePath, Object Value) {
        this.c.set(ValuePath, Value);
        return this;
    }
    public boolean exist() {
        return this.f.exists();
    }
    public Object getObject(String ValuePth) {
        return this.c.get(ValuePth);
    }

    public Integer getInt(String ValuePth) {
        return Integer.valueOf(this.c.getInt(ValuePth));
    }

    public String getString(String ValuePth) {
        return this.c.getString(ValuePth);
    }

    public boolean getBoolean(String ValuePth) {
        return this.c.getBoolean(ValuePth);
    }

    public long getLong(String ValuePath) {
        return this.c.getLong(ValuePath);
    }

    public List<String> getStringList(String ValuePath) {
        return this.c.getStringList(ValuePath);
    }

    public double getDouble(String ValuePath) {
        return this.c.getDouble(ValuePath);
    }

    public List<?> getList(String ValuePath) {
        return this.c.getList(ValuePath);
    }

    public void deleteFile() {
        this.f.delete();
    }

    public FileBuilder save () {
        try {
            ConfigurationProvider.getProvider(YamlConfiguration.class).save(this.c, this.f);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return this;
    }
}
