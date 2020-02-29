package converter.builder;

import converter.components.Element;

import java.util.List;

public class XMLBuilder implements Builder {

    private String rawContent;
    private String xmlContent;
    private List<Element> elementList;

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
