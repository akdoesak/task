package processor.text;

import model.Sentence;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.LinkedHashMap;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class TextProcessorTest {

    private TextProcessor textProcessor;


    @BeforeEach
    public void setUp() {
        textProcessor = new TextProcessor();
    }

    @Test
    @DisplayName("Text processor success")
    public void testProcessSuccess() throws Exception {
        String exampleText = "This is a test of Mr. and Mrs. Smith. This is another test.";
        String[] sentences = new String[]{"This is a test of Mr. and Mrs. Smith.", "This is another test."};

        LinkedHashMap<Sentence, List<String>> result = textProcessor.process(exampleText);

        assertNotNull(result);
        assertEquals(2, result.size());

        Sentence firstSentence = result.keySet().stream().findFirst().get();
        List<String> tokens = result.get(firstSentence);

        assertTrue(tokens.contains("This"));
        assertTrue(tokens.contains("is"));
        assertTrue(tokens.contains("a"));
        assertTrue(tokens.contains("test"));
        assertTrue(tokens.contains("of"));
        assertTrue(tokens.contains("Mr."));
        assertTrue(tokens.contains("and"));
        assertTrue(tokens.contains("Mrs."));
        assertTrue(tokens.contains("Smith"));
    }
}
