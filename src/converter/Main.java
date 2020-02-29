package converter;

import converter.components.Element;
import converter.components.RegexPattern;

import java.io.File;
import java.io.FileNotFoundException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static converter.components.RegexPattern.*;

public class Main {

    private List<Element> elementList = new ArrayList<>();
    private Deque<String> path = new ArrayDeque<>();

    public static String parseToLine(Scanner scanner) {
        StringBuilder answer = new StringBuilder("");
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine().trim().replaceAll("<\\s+", "<")
                    .replaceAll("\\s+>", ">")
                    .replaceAll("\\s+/>", "/>")
                    .replaceAll("</\\s+", "</")
                    .replaceAll("\\s*:\\s*", ":");
            answer.append(line);
        }
        scanner.close();
        return answer.toString();
    }

    public void printElements() {
        elementList.stream().forEach(element -> System.out.println(element.toString()));
    }


    public static Map<String, String> findAllAttributes(RegexPattern regexLines,
                                                        String line) {
        Map<String, String> attributesMap = new LinkedHashMap<>();
        Pattern pattern = Pattern.compile(regexLines.getMatch());
        Matcher matcher = pattern.matcher(line);
        while (matcher.find()) {
            String[] buf = matcher.group().split(regexLines.getSplit());
            if (attributesMap.containsKey(buf[0])) {
                continue;
            } else {
                attributesMap
                        .put(buf[0].trim(), buf[1].replaceAll(regexLines.getReplace(), "").trim());
            }
        }
        return attributesMap;

    }

    public static String findNameValue(RegexPattern regexPattern, String parseLine) {
        Matcher matcher = Pattern.compile(regexPattern.getMatch()).matcher(parseLine);
        if (matcher.find()) {
            return matcher.group().replaceAll(regexPattern.getReplace(), "").trim();
        } else {
            return "";
        }
    }

    public static String findNameValue(String regex, String parseLine) {
        Matcher matcher = Pattern.compile(regex).matcher(parseLine);
        if (matcher.find()) {
            return matcher.group().trim();
        } else {
            return "";
        }
    }

    public static Integer checkXmlLine(String xmlLine) {
        String line = xmlLine.trim();
        if (line.matches(XML_WITH_VALUE.getMatch())) {
            return 1;
        } else if (line.matches(XML_NO_VALUE.getMatch())) {
            return 2;
        } else if (line.matches(XML_NULL_VALUE.getMatch())) {
            return 3;
        } else {
            return 0;
        }
    }

    public static String getSubXml(String xml) {
        return xml.substring(xml.indexOf(">") + 1, xml.lastIndexOf("</"));
    }


    private void findNestedNodes(String parseLine) {
        BiConsumer<String, String> addElement = (value, line) -> {
            String node = findNameValue(XML_NAME, line);
            path.addLast(node);
            elementList
                    .add(new Element(findAllAttributes(XML_ATTRIBUTES, line), value, path));
            path.removeLast();
        };
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

    public Consumer<String> finder = (parseLine) -> {
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

    public static String xmlDefaultRegexWrapper(String tag) {
        return "<" + tag + ".*?</" + tag + ">";
    }

    public static String xmlNullValueRegexWrapper(String tag) {
        return "<" + tag + "[^><]*/>";
    }

    public void readElements(Scanner scanner) {
        findNestedNodes(parseToLine(scanner));
    }

//    public static void main(String[] args) throws FileNotFoundException {
//        Main main = new Main();
//        main.readElements(new Scanner(new File("test.txt")));
//        main.printElements();
//    }

    public static void main(String[] args) throws Exception {
        String fileName = "test.txt";
        new Converter().start(fileName);

    }
}