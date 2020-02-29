package converter.builder;

import converter.components.Element;

import java.util.Deque;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static converter.components.Functions.*;
import static converter.components.RegexPattern.*;

public class XMLBuilder implements Builder {

    private String rawContent;
    private String xmlContent;
    private List<Element> elementList;
    private Deque<String> path;

    public XMLBuilder(String rawContent) {
        this.rawContent = rawContent;
    }

    @Override
    public void read() {
    }


    @Override
    public void parse() {

    }

    @Override
    public void printRaw() {
        elementList.stream().map(x -> x.toString()).forEach(System.out::println);
    }

    @Override
    public void print() {
        System.out.println(xmlContent);
    }


}
