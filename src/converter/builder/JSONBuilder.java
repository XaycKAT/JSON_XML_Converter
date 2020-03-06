package converter.builder;

import converter.components.Element;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static converter.components.Functions.*;
import static converter.components.Functions.getSubXml;
import static converter.components.RegexPattern.*;
import static converter.components.RegexPattern.XML_OPEN_TAG;

public class JSONBuilder implements Builder {

    private String rawContent;
    private String jsonContent;
    private List<Element> elementList = new ArrayList<>();
    private Deque<String> path = new ArrayDeque<>();

    public JSONBuilder(String rawContent) {
        this.rawContent = parseToLine(rawContent);
    }

    @Override
    public void read() {
        findNestedNodes(rawContent);
    }



    private void findNestedNodes(String parseLine) {

        switch (checkXmlLine(parseLine)) {
            case 1: {
                addElement.accept(findNameValue(XML_VALUE, parseLine), parseLine);
                break;
            }
            case 2: {
                addElement.accept("", parseLine);
                break;
            }
            case 3: {
                addElement.accept("null", parseLine);
                break;
            }
            default: {
                finder.accept(parseLine);
            }
        }
    }

    BiConsumer<String, String> addElement = (value, line) -> {
        String node = findNameValue(XML_NAME, line);
        path.addLast(node);
        elementList
            .add(new Element(findAllAttributes(XML_ATTRIBUTES, line), value, path));
        path.removeLast();
    };

    Consumer<String> finder = (parseLine) -> {
        while (parseLine.length() > 0) {
            String tag = findNameValue(XML_NAME, parseLine);
            Matcher matcher = Pattern.compile(xmlNullValueRegexWrapper(tag) + "|" + xmlDefaultRegexWrapper(tag))
                    .matcher(parseLine);
            while (matcher.find()) {
                String xml = matcher.group();
                if (checkXmlLine(xml) != 0) {
                    findNestedNodes(xml);
                } else {
                    path.addLast(tag);
                    elementList.add(new Element(
                            findAllAttributes(XML_ATTRIBUTES,
                                    findNameValue(XML_OPEN_TAG, parseLine)), path));
                    findNestedNodes(getSubXml(xml));
                    path.removeLast();
                }
                parseLine = parseLine.replaceAll(xml, "");
            }
        }
    };

    @Override
    public void parse() {

    }

    @Override
    public void printRaw() {
        elementList.stream().map(x -> x.toString()).forEach(System.out::println);
    }

    @Override
    public void print() {
        System.out.println(jsonContent);
    }
}
