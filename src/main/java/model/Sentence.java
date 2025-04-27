package model;

public record Sentence (String sentence){
    public boolean isEmpty() {
        return sentence.isBlank();
    }
}