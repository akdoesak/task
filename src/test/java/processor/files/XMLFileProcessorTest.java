package processor.files;

import model.Sentence;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.xmlunit.builder.DiffBuilder;
import org.xmlunit.diff.Diff;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.LinkedHashMap;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

class XMLFileProcessorTest {

    private XMLFileProcessor processor;
    private Path tempFile;

    @BeforeEach
    void setUp() throws IOException {
        processor = new XMLFileProcessor();
        tempFile = Files.createTempFile("test", ".xml");
    }

    @AfterEach
    void tearDown() throws IOException {
        Files.deleteIfExists(tempFile);
    }

    @Test
    @DisplayName("should create correct XML string for multiple sentences")
    void testCreateStringForXml_MultipleSentences() {
        LinkedHashMap<Sentence, List<String>> sentenceMap = new LinkedHashMap<>();
        sentenceMap.put(new Sentence("This is a test."), List.of("This", "is", "a", "test"));
        sentenceMap.put(new Sentence("Another one."), List.of("Another", "one"));

        String xml = processor.createStringForXml(sentenceMap);

        String expectedXml =
                """
                <text>
                  <sentence>
                    <word>This</word>
                    <word>is</word>
                    <word>a</word>
                    <word>test</word>
                  </sentence>
                  <sentence>
                    <word>Another</word>
                    <word>one</word>
                  </sentence>
                </text>
                """;

        Diff diff = DiffBuilder.compare(expectedXml)
                .withTest(xml)
                .ignoreWhitespace()
                .checkForSimilar()
                .build();

        assertFalse(diff.hasDifferences(), "Generated XML differs from expected XML: " + diff.toString());
    }

    @Test
    @DisplayName("should create correct XML file from sentence map")
    void testProcess_CreatesCorrectFile() throws IOException {
        LinkedHashMap<Sentence, List<String>> sentenceMap = new LinkedHashMap<>();
        sentenceMap.put(new Sentence("Sample."), List.of("Sample"));

        processor.process(sentenceMap, tempFile.toString());

        String outputContent = Files.readString(Path.of(tempFile.toString().replace(".xml", ".xml")));

        String expectedContent =
                """
                <text>
                  <sentence>
                    <word>Sample</word>
                  </sentence>
                </text>
                """;

        Diff diff = DiffBuilder.compare(expectedContent)
                .withTest(outputContent)
                .ignoreWhitespace()
                .checkForSimilar()
                .build();

        assertFalse(diff.hasDifferences(), "Generated XML file differs from expected content: " + diff.toString());
    }

    @Test
    @DisplayName("should create correct XML string for empty sentence map")
    void testCreateStringForXml_EmptySentenceMap() {
        LinkedHashMap<Sentence, List<String>> sentenceMap = new LinkedHashMap<>();

        String xml = processor.createStringForXml(sentenceMap);

        String expectedXml = "<text/>";

        Diff diff = DiffBuilder.compare(expectedXml)
                .withTest(xml)
                .ignoreWhitespace()
                .checkForSimilar()
                .build();

        assertFalse(diff.hasDifferences(), "Generated XML for empty map differs from expected: " + diff.toString());
    }

    @Test
    @DisplayName("should return correct format type")
    void testGetFormatType() {
        assertEquals("xml", processor.getFormatType());
    }

    @Test
    @DisplayName("should process input file and match expected output")
    void endToEndTest_withInputAndExpectedFiles() throws IOException {
        Path inputPath = Path.of("src/main/resources/small.in");
        Path expectedOutputPath = Path.of("src/main/resources/small.xml");

        List<String> inputLines = Files.readAllLines(inputPath);
        LinkedHashMap<Sentence, List<String>> sentenceMap = new LinkedHashMap<>();

        for (String line : inputLines) {
            List<String> words = List.of(line.split("\\s+"));
            sentenceMap.put(new Sentence(line), words);
        }

        processor.process(sentenceMap, inputPath.toString());

        Path actualOutputPath = Path.of(inputPath.toString().replace(".in", ".xml"));

        String expectedContent = Files.readString(expectedOutputPath);
        String actualContent = Files.readString(actualOutputPath);

        Diff diff = DiffBuilder.compare(expectedContent)
                .withTest(actualContent)
                .ignoreWhitespace()
                .checkForSimilar()
                .build();

        assertFalse(diff.hasDifferences(), "Actual XML differs from expected XML: " + diff.toString());
    }
}
