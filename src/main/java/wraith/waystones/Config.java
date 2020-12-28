package wraith.waystones;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

public class Config {

    public static JsonObject loadConfig() {
        String defaultConfig =
                        "{\n" +
                        "  \"global_discover\": false,\n" +
                        "  \"cost_type\": \"xp\",\n" +
                        "  \"cost_item\": \"minecraft:ender_pearl\",\n" +
                        "  \"cost_amount\": 1,\n" +
                        "  \"village_generation\": true\n" +
                        "}";
        String path = "config/waystones/config.json";
        Config.createFile(path, defaultConfig, false);
        return getJsonObject(readFile(new File(path)));
    }

    public static JsonObject loadRecipe() {
        String defaultRecipe =
                "{\n" +
                        "  \"type\": \"minecraft:crafting_shaped\",\n" +
                        "  \"pattern\": [\n" +
                        "    \"BEB\",\n" +
                        "    \"BDB\",\n" +
                        "    \"BOB\"\n" +
                        "  ],\n" +
                        "  \"key\": {\n" +
                        "    \"B\": {\n" +
                        "      \"item\": \"minecraft:stone_bricks\"\n" +
                        "    },\n" +
                        "    \"E\": {\n" +
                        "      \"item\": \"minecraft:eye\"\n" +
                        "    }\n" +
                        "    \"D\": {\n" +
                        "      \"item\": \"minecraft:diamond\"\n" +
                        "    }\n" +
                        "    \"O\": {\n" +
                        "      \"item\": \"minecraft:obsidian\"\n" +
                        "    }\n" +
                        "  },\n" +
                        "  \"result\": {\n" +
                        "    \"item\": \"waystones:waystone\",\n" +
                        "    \"count\": 1\n" +
                        "  }\n" +
                        "}\n" +
                        "\n";
        String path = "config/waystones/recipe.json";
        Config.createFile(path, defaultRecipe, false);

        return getJsonObject(readFile(new File(path)));
    }


    public static void createFile(String path, String contents, boolean overwrite) {
        File file = new File(path);
        if (file.exists() && !overwrite) {
            return;
        }
        file.getParentFile().mkdirs();
        try {
            file.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        file.setReadable(true);
        file.setWritable(true);
        file.setExecutable(true);
        if (contents == null || "".equals(contents)) {
            return;
        }
        try {
            FileWriter writer = new FileWriter(file);
            writer.write(contents);
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String readFile(File file) {
        String output = "";
        try {
            Scanner scanner = new Scanner(file);
            scanner.useDelimiter("\\Z");
            output = scanner.next();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return output;
    }

    public static JsonObject getJsonObject(String json) {
        return new JsonParser().parse(json).getAsJsonObject();
    }

}
