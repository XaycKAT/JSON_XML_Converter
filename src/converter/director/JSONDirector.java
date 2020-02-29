package converter.director;

import converter.builder.JSONBuilder;

public class JSONDirector implements Director {

    private String content;
    private Boolean isRaw;

    public JSONDirector(String content, Boolean isRaw) {
        this.content = content;
        this.isRaw = isRaw;
    }

    @Override
    public void startConversion() {
        JSONBuilder jsonBuilder = new JSONBuilder();

    }

}
