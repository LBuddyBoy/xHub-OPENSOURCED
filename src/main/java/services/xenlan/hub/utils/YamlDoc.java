package services.xenlan.hub.utils;

import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;

import java.io.*;

public class YamlDoc {

    protected File file;
    protected YamlConfiguration config;
    private String configName;

    public YamlDoc(File folder, String configName) {
        this.configName = configName;
        file = new File(folder, configName);
    }


    public void init() throws IOException {
        if (!file.exists()) {
            file.createNewFile();
            loadDefaults();
        }

        config = YamlConfiguration.loadConfiguration(file);
    }

    public void loadDefaults() throws IOException {
        InputStream is = getClass().getResourceAsStream("/" + configName);
        BufferedWriter writer = new BufferedWriter(new FileWriter(file));
        writer.write(readFile(is));
        writer.close();
    }


    public String readFile(InputStream inputStream) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        String content = "";
        String line;

        while ((line = reader.readLine()) != null) {
            content += line + "\n";
        }

        reader.close();
        return content.trim();
    }


    public void save() throws IOException {
        if (!file.exists())
            file.createNewFile();

        config.save(file);
    }


    public YamlConfiguration getConfig() {
        return config;
    }

    public void reloadConfig() throws IOException {
        init();
    }

}
