package processor.text;

import model.Sentence;

import java.util.*;
import java.util.regex.Pattern;

public class TextProcessor {
    private static final Pattern WORD_SPLIT = Pattern.compile("[,\\s]+");
    private static final Pattern ABBREVIATION_PATTERN = Pattern.compile("^[A-Za-z]{1,3}\\.$");

    //Can be provided as a file parameter which can be updated.
    private static final Set<String> ABBREVIATIONS = Set.of(
            "Mr.", "Mrs.", "Ms.", "Dr.", "Prof.", "Sr.", "Jr.", "St.",
            "np.", "itp.", "itd.", "etc.", "e.g.", "i.e.", "z.B."
    );

        public LinkedHashMap<Sentence, List<String>> process(String text) {
            LinkedHashMap<Sentence, List<String>> map = new LinkedHashMap<>();

            List<String> rawSentences = smartSplitIntoSentences(text.trim());

            for (String rawSentence : rawSentences) {
                if (!rawSentence.isBlank()) {
                    Sentence sentence = new Sentence(rawSentence.trim());
                    List<String> words = splitWords(rawSentence);
                    List<String> sortedWords = words.stream().sorted(String.CASE_INSENSITIVE_ORDER).toList();
                    if (!sentence.isEmpty()) {
                        map.put(sentence, sortedWords);
                    }
                }
            }

            return map;
        }

        private List<String> smartSplitIntoSentences(String text) {
            List<String> sentences = new ArrayList<>();
            StringBuilder current = new StringBuilder();

            String[] tokens = text.split(" ");
            for (String token : tokens) {
                current.append(token).append(" ");

                if (endsWithSentenceBoundary(token)) {
                    sentences.add(current.toString().trim());
                    current.setLength(0);
                }
            }

            if (!current.isEmpty()) {
                sentences.add(current.toString().trim());
            }

            return sentences;
        }

        private boolean endsWithSentenceBoundary(String word) {
            String clean = word.trim();
            if (clean.isEmpty()) {
                return false;
            }
            if (isAbbreviation(clean)) {
                return false;
            }
            return clean.endsWith(".") && !clean.matches(".*\\d\\..*");
        }

        private boolean isAbbreviation(String word) {
            return ABBREVIATION_PATTERN.matcher(word).matches();
        }

        private List<String> splitWords(String sentence) {
            List<String> words = new ArrayList<>();
            for (String word : WORD_SPLIT.split(sentence.trim())) {
                String cleaned = cleanWord(word);
                if (!cleaned.isBlank()) {
                    words.add(cleaned);
                }
            }
            return words;
        }

    private String cleanWord(String word) {
        if (isAbbreviation(word)) {
            return word;
        }
        return word.replaceAll("^\\p{Punct}+|\\p{Punct}+$", "");
    }
}