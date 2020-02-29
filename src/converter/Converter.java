package converter;

import converter.director.JSONDirector;
import converter.director.XMLDirector;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

class Converter {
    private String content;

    private void parseXML() {
        JSONDirector director = new JSONDirector(content, true);
        director.startConversion();
    }

    private void parseJSON() {
        XMLDirector director = new XMLDirector(content, true);
        director.startConversion();
    }

    void start(String file) {
        try {
            Path path = Paths.get(file);
            content = Files
                    .readString(path, StandardCharsets.UTF_8)
                    .trim();
            long c = System.currentTimeMillis();
            if (content.startsWith("<")) {
                parseXML();
            } else {
                parseJSON();
            }
            long d = System.currentTimeMillis() - c;
        } catch (IOException e) {
            System.out.println("Error while reading file occurred.");
            System.out.println(e.getMessage());
        }
    }
}