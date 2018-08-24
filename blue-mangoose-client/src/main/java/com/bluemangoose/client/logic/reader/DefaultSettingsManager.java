package com.bluemangoose.client.logic.reader;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @Author
 * Karol Meksuła
 * 24-07-2018
 * */

public class DefaultSettingsManager implements SettingReader, SettingWriter {
    private final String CURRENT_PATH = System.getProperty("user.dir");

    @Override
    public Map<String, String> loadSettings() {
        try {
            return fillSettingMap(readCustomSettingsFile());
        } catch (FileNotFoundException ex) {
            InputStream input = this.getClass().getClassLoader().getResourceAsStream("config.mng");
            return fillSettingMap(readFile(input));
        }
    }

    private String readFile(InputStream input) {
        StringBuilder textBuilder = new StringBuilder();
        try (Reader reader = new BufferedReader(new InputStreamReader(input, Charset.forName(StandardCharsets.UTF_8.name())))) {
            int c;
            while ((c = reader.read()) != -1) {
                textBuilder.append((char) c);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return textBuilder.toString();
    }

    private Map<String, String> fillSettingMap(String input) {
        final String PROPERTY_PATTERN = "=.+;";
        Pattern pattern = Pattern.compile(PROPERTY_PATTERN);
        Matcher matcher = pattern.matcher(input);

        Map<String, String> settings = new LinkedHashMap<>();

        int counter = 0;
        while (matcher.find()) {
            String property = matcher.group();
            settings.put("prop" + counter, property.substring(1, property.length() - 1));
            counter++;
        }

        return settings;
    }

    @Override
    public void updateSettings(SettingsProperties properties, String value) {
        String output = "";

        try {
            InputStream existed = new FileInputStream(CURRENT_PATH + "/config.mng");
            String outputExist = readFile(existed);
            if (!outputExist.isEmpty()) {
                output = outputExist;
            }
        } catch (FileNotFoundException e) {
            InputStream input = this.getClass().getClassLoader().getResourceAsStream("config.mng");
            output = readFile(input);
        }

        String[] parts = output.split("\n");
        parts[properties.index()] = properties.property() + value + ";";

        StringBuilder stringBuilder = new StringBuilder();

        for (String part : parts) {
            stringBuilder.append(part).append("\n");
        }

        BufferedWriter writer;
        try {
            writer = new BufferedWriter(new FileWriter(CURRENT_PATH + "/config.mng"));
            writer.write(stringBuilder.toString());
            writer.close();

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public String readCustomSettingsFile() throws FileNotFoundException {
        final File file = new File("config.mng");
        BufferedReader reader = new BufferedReader(new FileReader(file));

        return reader.lines()
                .reduce("", (s1, s2) -> s1.concat(s2 + "\n"));
    }
}
