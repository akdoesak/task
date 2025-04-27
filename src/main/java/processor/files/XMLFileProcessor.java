package processor.files;

import model.Sentence;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import java.io.IOException;
import java.io.StringWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class XMLFileProcessor extends FileProcessor {
    @Override
    public void process(LinkedHashMap<Sentence, List<String>> sentenceMap, String inputFilePath) {

        String outputFilePath = generateOutputFilePath(inputFilePath);

        try {
            Files.writeString(Path.of(outputFilePath), createStringForXml(sentenceMap));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public String getFormatType() {
        return "xml";
    }

    protected String createStringForXml(Map<Sentence, List<String>> sentenceTokensMap) {
        try {
            DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            Document doc = builder.newDocument();

            Element root = doc.createElement("text");
            doc.appendChild(root);

            sentenceTokensMap.forEach((_, tokens) -> {
                Element sentenceElement = doc.createElement("sentence");
                root.appendChild(sentenceElement);

                tokens.forEach(token -> {
                    Element wordElement = doc.createElement("word");
                    wordElement.appendChild(doc.createTextNode(token));
                    sentenceElement.appendChild(wordElement);
                });
            });

            return transformXMLToString(doc);
        } catch (TransformerException | ParserConfigurationException e) {
            throw new RuntimeException(e);
        }
    }

    private String transformXMLToString(Document doc) throws TransformerException {
        Transformer transformer = TransformerFactory.newInstance().newTransformer();
        transformer.setOutputProperty(OutputKeys.INDENT, "yes");
        transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");

        StringWriter writer = new StringWriter();
        transformer.transform(new DOMSource(doc), new StreamResult(writer));

        return writer.toString();
    }

}