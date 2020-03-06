package converter;


import static org.junit.Assert.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.ByteArrayOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;

@TestInstance(Lifecycle.PER_CLASS)
public class ConverterTest {

    private final ByteArrayOutputStream output = new ByteArrayOutputStream();

    @BeforeAll
    public void setUpStreams() {
        System.setOut(new PrintStream(output));
    }

    @AfterAll
    public void cleanUpStreams() {
        System.setOut(null);
    }

    @Test
    public void parseJSONRawTest() {

        //region
        String inputText = "<node>\n"
            + "    <child name = \"child_name1\" type = \"child_type1\">\n"
            + "        <subchild id = \"1\" auth=\"auth1\">Value1</subchild>\n"
            + "    </child>\n"
            + "    <child name = \"child_name2\" type = \"child_type2\">\n"
            + "        <subchild id = \"2\" auth=\"auth1\">Value2</subchild>\n"
            + "        <subchild id = \"3\" auth=\"auth2\">Value3</subchild>\n"
            + "        <subchild id = \"4\" auth=\"auth3\"></subchild>\n"
            + "        <subchild id = \"5\" auth=\"auth3\"/>\n"
            + "    </child>\n"
            + "</node>";
        String outputText = "Element:\n"
            + "path = node\n"
            + "\n"
            + "Element:\n"
            + "path = node, child\n"
            + "attributes:\n"
            + "name = \"child_name1\"\n"
            + "type = \"child_type1\"\n"
            + "\n"
            + "Element:\n"
            + "path = node, child, subchild\n"
            + "value = \"Value1\"\n"
            + "attributes:\n"
            + "id = \"1\"\n"
            + "auth = \"auth1\"\n"
            + "\n"
            + "Element:\n"
            + "path = node, child\n"
            + "attributes:\n"
            + "name = \"child_name2\"\n"
            + "type = \"child_type2\"\n"
            + "\n"
            + "Element:\n"
            + "path = node, child, subchild\n"
            + "value = \"Value2\"\n"
            + "attributes:\n"
            + "id = \"2\"\n"
            + "auth = \"auth1\"\n"
            + "\n"
            + "Element:\n"
            + "path = node, child, subchild\n"
            + "value = \"Value3\"\n"
            + "attributes:\n"
            + "id = \"3\"\n"
            + "auth = \"auth2\"\n"
            + "\n"
            + "Element:\n"
            + "path = node, child, subchild\n"
            + "value = \"\"\n"
            + "attributes:\n"
            + "id = \"4\"\n"
            + "auth = \"auth3\"\n"
            + "\n"
            + "Element:\n"
            + "path = node, child, subchild\n"
            + "value = null\n"
            + "attributes:\n"
            + "id = \"5\"\n"
            + "auth = \"auth3\"";
        //endregion
        try {
            FileWriter fileWriter= new FileWriter("test-converter.txt");
            fileWriter.write(inputText);
            fileWriter.close();
            Converter s = new Converter();
            s.start("test-converter.txt");
            assertEquals(outputText , output.toString().trim());
            Files.deleteIfExists(Paths.get("test-converter.txt"));
        } catch (IOException e){
            System.out.println("IO");
        }

    }

}