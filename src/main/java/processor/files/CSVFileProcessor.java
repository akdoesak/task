package processor.files;

import model.Sentence;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.LinkedHashMap;
import java.util.List;

public class CSVFileProcessor extends FileProcessor {

    @Override
    public void process(LinkedHashMap<Sentence, List<String>> sentenceMap, String inputFilePath) {
        String outputFilePath = generateOutputFilePath(inputFilePath);

        try {
            Files.writeString(Path.of(outputFilePath), createStringForCsv(sentenceMap));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public String getFormatType() {
        return "csv";
    }

    protected String createStringForCsv(LinkedHashMap<Sentence, List<String>> sentenceMap) {
        StringBuilder builder = new StringBuilder();

        int maxWords = sentenceMap.values().stream()
                .mapToInt(List::size)
                .max()
                .orElse(0);

        builder.append(",");
        for (int i = 1; i <= maxWords; i++) {
            builder.append("Word ").append(i).append(",");
        }
        builder.append("\n");

        int sentenceNumber = 1;
        for (var entry : sentenceMap.entrySet()) {
            builder.append("Sentence ").append(sentenceNumber++).append(",");
            List<String> words = entry.getValue();
            for (String word : words) {
                builder.append(word).append(",");
            }
            builder.append("\n");
        }

        return builder.toString();
    }

}
