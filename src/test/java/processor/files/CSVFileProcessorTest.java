package processor.files;

import model.Sentence;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import processor.files.CSVFileProcessor;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.LinkedHashMap;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class CSVFileProcessorTest {

    private CSVFileProcessor processor;
    private Path tempFile;

    @BeforeEach
    void setUp() throws IOException {
        processor = new CSVFileProcessor();
        tempFile = Files.createTempFile("test", ".csv");
    }

    @AfterEach
    void tearDown() throws IOException {
        Files.deleteIfExists(tempFile);
    }

    @Test
    @DisplayName("should create correct CSV string for multiple sentences")
    void testCreateStringForCsv_MultipleSentences() {
        LinkedHashMap<Sentence, List<String>> sentenceMap = new LinkedHashMap<>();
        sentenceMap.put(new Sentence("This is a test."), List.of("This", "is", "a", "test"));
        sentenceMap.put(new Sentence("Another one."), List.of("Another", "one"));

        String csv = processor.createStringForCsv(sentenceMap);

        String expectedStart = ",Word 1,Word 2,Word 3,Word 4,\n" +
                "Sentence 1,This,is,a,test,\n" +
                "Sentence 2,Another,one,\n";

        assertEquals(expectedStart, csv);
    }

    @Test
    @DisplayName("should create correct CSV file from sentence map")
    void testProcess_CreatesCorrectFile() throws IOException {
        LinkedHashMap<Sentence, List<String>> sentenceMap = new LinkedHashMap<>();
        sentenceMap.put(new Sentence("Sample."), List.of("Sample"));

        processor.process(sentenceMap, tempFile.toString());

        String outputContent = Files.readString(Path.of(tempFile.toString().replace(".csv", ".csv")));

        String expectedContent = ",Word 1,\n" +
                "Sentence 1,Sample,\n";

        assertEquals(expectedContent, outputContent);
    }

    @Test
    @DisplayName("should create correct CSV string for empty sentence map")
    void testCreateStringForCsv_EmptySentenceMap() {
        LinkedHashMap<Sentence, List<String>> sentenceMap = new LinkedHashMap<>();

        String csv = processor.createStringForCsv(sentenceMap);

        assertEquals(",\n", csv);
    }

    @Test
    @DisplayName("should return correct format type")
    void testGetFormatType() {
        assertEquals("csv", processor.getFormatType());
    }

    @Test
    @DisplayName("should process input file and match expected output")
    void endToEndTest_withInputAndExpectedFiles() throws IOException {
        Path inputPath = Path.of("src/main/resources/small.in");
        Path expectedOutputPath = Path.of("src/main/resources/small.csv");

        List<String> inputLines = Files.readAllLines(inputPath);
        LinkedHashMap<Sentence, List<String>> sentenceMap = new LinkedHashMap<>();

        for (String line : inputLines) {
            List<String> words = List.of(line.split("\\s+"));
            sentenceMap.put(new Sentence(line), words);
        }

        processor.process(sentenceMap, inputPath.toString());

        Path actualOutputPath = Path.of(inputPath.toString().replace(".in", ".csv"));

        String expectedContent = Files.readString(expectedOutputPath);
        String actualContent = Files.readString(actualOutputPath);

        assertEquals(expectedContent.trim(), actualContent.trim());
    }
}
