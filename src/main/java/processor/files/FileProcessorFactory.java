package processor.files;

public class FileProcessorFactory {

    public static FileProcessor createProcessor(String formatType) {
        return switch (formatType) {
            case "xml" -> new XMLFileProcessor();
            case "csv" -> new CSVFileProcessor();
            default -> throw new IllegalArgumentException("Unsupported format: " + formatType);
        };
    }
}
