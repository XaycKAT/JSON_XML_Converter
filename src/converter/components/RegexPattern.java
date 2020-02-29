package converter.components;

public enum RegexPattern {
    XML_OPEN_TAG("<[\\w\\s=\"]*>"),
    XML_CLOSE_TAG("</\\w+>"),
    XML_NO_VALUE("<[^<>]+></\\w+>"),
    XML_WITH_VALUE("<[^<>]+>[^<>]+</[^<>]+>"),
    XML_WITH_NODES("<[^<>]+><.*?></\\w+>"),
    XML_NULL_VALUE("<[^<>]+/>"),
    XML_NAME("<\\w+", "[<>]"),
    XML_VALUE(">.*?<", "[><]"),
    XML_ATTRIBUTES("\\w+\\s*=\\s*\"\\w+\"", "[\"=\\s]", "="),

    JSON_NAME("\"\\w+\"\\s*:", "[\":\\s]");

    RegexPattern(String match, String replace) {
        this.match = match;
        this.replace = replace;
    }

    RegexPattern(String match) {
        this.match = match;
    }

    RegexPattern(String match, String replace, String split) {
        this.match = match;
        this.replace = replace;
        this.split = split;
    }

    private String match;
    private String replace = "";
    private String split = "";

    public String getMatch() {
        return match;
    }

    public String getReplace() {
        return replace;
    }

    public String getSplit() {
        return split;
    }

}