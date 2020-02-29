package converter.director;

import converter.builder.XMLBuilder;

public class XMLDirector implements Director {

    private String content;
    private Boolean isRaw;

    public XMLDirector(String content, Boolean isRaw) {
        this.content = content;
        this.isRaw = isRaw;
    }

    @Override
    public void startConversion() {
        XMLBuilder xmlBuilder = new XMLBuilder(content);
        xmlBuilder.read();
        if (isRaw)
            xmlBuilder.printRaw();
        else {
            xmlBuilder.parse();
            xmlBuilder.print();
        }
    }

}
