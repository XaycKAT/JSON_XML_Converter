package converter;

import org.junit.jupiter.api.Test;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Scanner;


import static converter.Main.*;
import static converter.Main.getSubXml;
import static converter.components.RegexPattern.*;
import static converter.components.RegexPattern.XML_ATTRIBUTES;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class MainTest {

//    @Test
//    public void checkXmlLineTest() {
//        assertEquals(1, checkXmlLine("<transaction>f43feds</transaction>"));
//        assertEquals(2, checkXmlLine("  <body font=\"Verdana\"></body>\n"));
//        assertEquals(2, checkXmlLine("  <body></body>\n"));
//        assertEquals(3, checkXmlLine("<nonattr />\n"));
//        assertEquals(3, checkXmlLine("<nonattr sad=\"231\" />\n"));
////        assertEquals(4, checkXmlLine("    <child name = \"child_name1\" type = \"child_type1\">"
////            + "<subchild id = \"1\" auth=\"auth1\">Value1</subchild></child>"));
//        assertEquals(0, checkXmlLine("><id>6753322</id>\n"));
////        assertEquals(4, checkXmlLine("<child name=\"child_name1\" type=\"child_type1\"><subchild id=\"1\" auth=\"auth1\">Value1</subchild></child><child name=\"child_name2\" "
////            + "type=\"child_type2\"><subchild id=\"2\" auth=\"auth1\">Value2</subchild><subchild id=\"3\" auth=\"auth2\">Value3</subchild><subchild id=\"4\" auth=\"auth3\"/><subchild "
////            + "id=\"5\" auth=\"auth3\"/><subchild></subchild></child>"));
//        assertEquals(0, checkXmlLine("</transaction>\n"));
//        assertEquals(0, checkXmlLine("<child name=\"child_name1\" type=\"child_type1\"><subchild id=\"1\" auth=\"auth1\">Value1</subchild></child><child "
//                + "name=\"child_name2\" type=\"child_type2\"><subchild id=\"2\" auth=\"auth1\">Value2</subchild><subchild id=\"3\" auth=\"auth2\">Value3</subchild>"
//                + "<subchild id=\"4\" auth=\"auth3\"></subchild><subchild id=\"5\" auth=\"auth3\"/></child>"));
//
//    }

    @Test
    public void xmlName() {
        assertEquals(findNameValue(XML_NAME, "<transaction>\n"), "transaction");
        assertEquals(findNameValue(XML_NAME, "    <id>6753322</id>\n"), "id");
        assertEquals(findNameValue(XML_NAME, "    <nonattr />\n"), "nonattr");
        assertEquals(findNameValue(XML_NAME, "    <nonattr></nonattr>\n"),
                "nonattr");
        assertEquals("body",
                findNameValue(XML_NAME, "         <body font=\"Verdana\">Body message</body>\n"));

    }

    @Test
    public void xmlValue() {
        assertEquals(findNameValue(XML_VALUE, "<transaction>\n"), "");
        assertEquals(findNameValue(XML_VALUE, "    <id>6753322</id>\n"), "6753322");
        assertEquals(findNameValue(XML_VALUE, "    <nonattr />\n"), "");
        assertEquals(findNameValue(XML_VALUE, "    <nonattr></nonattr>\n"),
                "");
        assertEquals(findNameValue(XML_VALUE, "  </email>\n"), "");
        assertEquals(findNameValue(XML_VALUE, "   <attr id=\"3\">text</attr>\n"), "text");
        assertEquals(findNameValue(XML_VALUE, "   <attr id=\"2\"></attr>\n"), "");
        assertEquals(findNameValue(XML_VALUE, "  <date day=\"12\" month=\"12\" year=\"2018\"/>\n"),
                "");
        assertEquals(findNameValue(XML_VALUE, "         <from>from_example@gmail.com</from>\n"),
                "from_example@gmail.com");
        assertEquals(
                findNameValue(XML_VALUE, "         <body font=\"Verdana\">Body message</body>\n"),
                "Body message");
    }

    @Test
    public void checkXmlOpenTag() {
        assertEquals("<child name=\"child_name1\" type=\"child_type1\">",
                findNameValue(XML_OPEN_TAG,
                        "<child name=\"child_name1\" type=\"child_type1\"><subchild id=\"1\" auth=\"auth1\">Value1</subchild>\n"
                                + "</child><child name=\"child_name2\" type=\"child_type2\"><subchild id=\"2\" auth=\"auth1\">Value2</subchild><subchild id=\"3\" auth=\"auth2\">Value3</subchild><subchild id=\"4\" auth=\"auth3\"/><subchild id=\"5\" auth=\"auth3\"/><subchild></subchild></child>"));

        assertEquals("<subchild id = \"2\" auth=\"auth1\">", findNameValue(XML_OPEN_TAG,
                "        <subchild id = \"2\" auth=\"auth1\">Value2</subchild>"));

        assertEquals("<subchild id = \"2\" auth=\"auth1\">",
                findNameValue(XML_OPEN_TAG, "        <subchild id = \"2\" auth=\"auth1\"></subchild>"));

        assertEquals("",
                findNameValue(XML_OPEN_TAG, "        <subchild id = \"2\" auth=\"auth1\" />"));

        assertEquals("", findNameValue(XML_OPEN_TAG, "</subchild>"));
    }

    @Test
    public void checkXmlCloseTag() {

        assertEquals("</subchild>", findNameValue(XML_CLOSE_TAG,
                "        <subchild id = \"2\" auth=\"auth1\">Value2</subchild>"));

        assertEquals("</subchild>", findNameValue(XML_CLOSE_TAG,
                "        <subchild id = \"2\" auth=\"auth1\"></subchild>"));

        assertEquals("",
                findNameValue(XML_CLOSE_TAG, "        <subchild id = \"2\" auth=\"auth1\" />"));
    }


    @Test
    public void jsonName() {
        assertEquals(findNameValue(JSON_NAME, " { "), "");
        assertEquals(findNameValue(JSON_NAME, " {\"host\":\"127.0.0.1\"}\n "), "host");
        assertEquals(findNameValue(JSON_NAME, " {\"jdk\" : \"1.8.9\"}\n "), "jdk");
        assertEquals(findNameValue(JSON_NAME, " {\n \"success\": "), "success");

    }
    
    @Test
    public void parseToLineTest() {
        String text = "<transaction>\n"
                + "\t<id>6753322</id>\n"
                + "\t<number region=\"Russia\">8-900-000-00-00</number>\n"
                + "\t<nonattr />\n"
                + "\t<nonattr/>\n"
                + "\t<nonattr>text</nonattr>\n"
                + "\t<attr id=\"1\" />\n"
                + "\t<attr id=\"2\"/>\n"
                + "\t<attr id=\"3\">text</attr>\n"
                + "\t<email>\n"
                + "\t\t<to>to_example@gmail.com</to>\n"
                + "\t\t<from>from_example@gmail.com</from>\n"
                + "\t\t<subject>Project discussion</subject>\n"
                + "\t\t<body font=\"Verdana\">Body message</body>\n"
                + "\t\t<date day=\"12\" month=\"12\" year=\"2018\"/>\n"
                + "\t</email>\n"
                + "</transaction>\n";
        assertEquals(
                "<transaction><id>6753322</id><number region=\"Russia\">8-900-000-00-00</number><nonattr/><nonattr/><nonattr>text</nonattr>"
                        + "<attr id=\"1\"/><attr id=\"2\"/><attr id=\"3\">text</attr><email><to>to_example@gmail.com</to><from>from_example@gmail.com</from><subject>Project discussion</subject>"
                        + "<body font=\"Verdana\">Body message</body><date day=\"12\" month=\"12\" year=\"2018\"/></email></transaction>",
                parseToLine(new Scanner(text)));

        text = "{\n"
                + "    \"person\": {\n"
                + "        \"@rate\": 1,\n"
                + "        \"@name\": \"Torvalds\",\n"
                + "        \"#person\": null\n"
                + "    }\n"
                + "}";
        assertEquals("{\"person\":{\"@rate\":1,\"@name\":\"Torvalds\",\"#person\":null}}",
                parseToLine(new Scanner(text)));

        text =
                "<node><child name=\"child_name1\" type=\"child_type1\"><subchild id=\"1\" auth=\"auth1\">Value1</subchild></child><child name=\"child_name2\" type=\"child_type2\">"
                        + "<subchild id=\"2\" auth=\"auth1\">Value2</subchild><subchild id=\"3\" auth=\"auth2\">Value3</subchild><subchild id=\"4\" auth=\"auth3\"></subchild><subchild id=\"5\" auth=\"auth3\"/></child></node>";
        assertEquals(text, parseToLine(new Scanner(text)));
    }

    @Test
    public void findAllAttributesTest() {
        String text = "    <child name = \"child_name1\" type = \"child_type1\" id=\"2\" />";
        Map<String, String> actualMap = findAllAttributes(XML_ATTRIBUTES, text);
        Map<String, String> expectMap = new LinkedHashMap<>();
        expectMap.put("name", "child_name1");
        expectMap.put("type", "child_type1");
        expectMap.put("id", "2");
        assertEquals(actualMap, expectMap);

        text = "<child />";
        actualMap = findAllAttributes(XML_ATTRIBUTES, text);
        expectMap = new LinkedHashMap<>();
        assertEquals(actualMap, expectMap);
    }
    @Test
    public void getSubXmlTest() {
        String text = "<child name = \"child_name1\" type = \"child_type1\"><subchild id = \"1\" auth=\"auth1\">Value1</subchild></child>";
        assertEquals("<subchild id = \"1\" auth=\"auth1\">Value1</subchild>", getSubXml(text));
        text = "<node><child name=\"child_name1\" type=\"child_type1\"><subchild id=\"1\" auth=\"auth1\">Value1</subchild></child>"
                + "<child name=\"child_name2\" type=\"child_type2\"><subchild id=\"2\" auth=\"auth1\">Value2</subchild><subchild id=\"3\" auth=\"auth2\">Value3"
                + "</subchild><subchild id=\"4\" auth=\"auth3\"></subchild><subchild id=\"5\" auth=\"auth3\"/></child></node>\n";
        assertEquals("<child name=\"child_name1\" type=\"child_type1\"><subchild id=\"1\" auth=\"auth1\">Value1</subchild></child>"
                +"<child name=\"child_name2\" type=\"child_type2\"><subchild id=\"2\" auth=\"auth1\">Value2</subchild><subchild id=\"3\" auth=\"auth2\">Value3"
                +"</subchild><subchild id=\"4\" auth=\"auth3\"></subchild><subchild id=\"5\" auth=\"auth3\"/></child>", getSubXml(text));
    }

//    @Test
//    public void findNestedNodesTest() {
//        String text = "<transaction>\n"
//            + "    <id>6753322</id>\n"
//            + "    <number region=\"Russia\">8-900-000-00-00</number>\n"
//            + "    <nonattr />\n"
//            + "    <nonattr></nonattr>\n"
//            + "    <nonattr>text</nonattr>\n"
//            + "    <attr id=\"1\" />\n"
//            + "    <attr id=\"2\"></attr>\n"
//            + "    <attr id=\"3\">text</attr>\n"
//            + "    <email>\n"
//            + "        <to>to_example@gmail.com</to>\n"
//            + "        <from>from_example@gmail.com</from>\n"
//            + "        <subject>Project discussion</subject>\n"
//            + "        <body font=\"Verdana\">Body message</body>\n"
//            + "        <date day=\"12\" month=\"12\" year=\"2018\"/>\n"
//            + "    </email>\n"
//            + "</transaction>";
//        converter.Converter converter = new Converter();
//        converter.readElements(text);
//        assertEquals(15, converter.getElementList().size());
//    }

}