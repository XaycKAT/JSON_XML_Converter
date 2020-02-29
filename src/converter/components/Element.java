package converter.components;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Map;

public class Element {

    private Map<String, String> attributes;
    private String value;
    private Deque<String> path;

    public Element(Map<String, String> attributes, String value, Deque<String> path) {
        this.attributes = attributes;
        this.value = value;
        this.path = ((ArrayDeque) path).clone();
    }

    public Element(Map<String, String> attributes, Deque<String> path) {
        this.attributes = attributes;
        this.path = ((ArrayDeque) path).clone();
    }

    public void removeLastPath(){
        path.removeLast();
    }

    public void addPath(String path) {
        this.path.addLast(path);
    }


    private String toStringAttributes() {
        if (attributes.size() == 0) {
            return "";
        } else {
            String str = "attributes:\n";
            for (Map.Entry<String, String> entry : this.attributes.entrySet()) {
                str += entry.getKey() + " = \"" + entry.getValue() + "\"\n";
            }
            return str;
        }
    }

    private String toStringPath() {
        StringBuilder stringBuilder = new StringBuilder();
        for (String s : this.path) {
            stringBuilder.append(s);
            if (!path.getLast().equals(s)) {
                stringBuilder.append(", ");
            }
        }
        return path.size() == 0 ? "" : "path = " + stringBuilder.toString() + "\n";
    }

    private String toStringValue() {
        if (this.value == null) {
            return "";
        } else if (this.value.equals("")) {
            return "value = \"\"" + "\n";
        } else if (this.value.equals("null")) {
            return "value = null\n";
        } else {
            return "value = \"" + value + "\"\n";
        }
    }

    @Override
    public String toString() {
        return "Element:\n"
                + this.toStringPath()
                + this.toStringValue()
                + this.toStringAttributes();
    }
}