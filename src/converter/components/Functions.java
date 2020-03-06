package converter.components;

import converter.builder.Builder;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static converter.components.RegexPattern.*;

public class Functions {

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


    public static String parseToLine(String string) {
        return string.replaceAll("\\s+|\t|\n|\r"," ")
                .replaceAll("> <", "><")
                .replaceAll("\\s/>","/>")
                .strip();

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


    public static String xmlDefaultRegexWrapper(String tag) {
        return "<" + tag + ".*?</" + tag + ">";
    }

    public static String xmlNullValueRegexWrapper(String tag) {
        return "<" + tag + "[^><]*/>";
    }

}
