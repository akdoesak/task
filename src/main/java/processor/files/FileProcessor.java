package processor.files;

import model.Sentence;

import java.io.*;
import java.util.LinkedHashMap;
import java.util.List;

public abstract class FileProcessor {

    public String readFile(String filePath) {
        StringBuilder content = new StringBuilder();

        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                content.append(line).append("\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return content.toString();
    }

    public abstract void process(LinkedHashMap<Sentence, List<String>> sentenceMap, String inputFilePath);
    public abstract String getFormatType();

    protected String generateOutputFilePath(String inputFilePath) {
        int lastDotIndex = inputFilePath.lastIndexOf('.');
        String baseName = (lastDotIndex == -1) ? inputFilePath : inputFilePath.substring(0, lastDotIndex);
        return baseName + "." + getFormatType();
    }
}
