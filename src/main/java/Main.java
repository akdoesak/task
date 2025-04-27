import model.Sentence;
import processor.files.FileProcessor;
import processor.files.FileProcessorFactory;
import processor.text.TextProcessor;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.List;

public class Main {

    // two arguments: file path and format type
    public static void main(String[] args) {
        if (args.length != 2) {
            System.out.println("Usage: java -jar task.jar <path to file>");
        }

        String filePath = args[0];
        String formatType = args[1];

        FileProcessor processor = FileProcessorFactory.createProcessor(formatType);

        String inputText = processor.readFile(filePath);
        LinkedHashMap<Sentence, List<String>> sentenceMap = new TextProcessor().process(inputText);

        processor.process(sentenceMap, filePath);
    }
}
