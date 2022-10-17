package com.example.swiftide;

import org.fxmisc.richtext.model.StyleSpans;
import org.fxmisc.richtext.model.StyleSpansBuilder;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/*
SwiftKeywords handles the logic for syntax highlighting and stores the used Keywords
 */
public class SwiftKeywords {

    /*
    example keywords
     */
    private static final String[] KEYWORDS = new String[] {
            "break", "case", "catch", "continue", "default",
            "do", "for", "guard", "if", "in", "return",
            "switch", "while", "class", "init", "extension",
            "func", "let", "private", "public", "static",
            "struct", "var", "import"
    };

    /*
    Regex Patterns for Syntax highlighting
     */
    private static final String KEYWORD_PATTERN = "\\b(" + String.join("|", KEYWORDS) + ")\\b";
    private static final String PAREN_PATTERN = "\\(|\\)";
    private static final String BRACE_PATTERN = "\\{|\\}";
    private static final String BRACKET_PATTERN = "\\[|\\]";
    private static final String SEMICOLON_PATTERN = "\\;";
    private static final String STRING_PATTERN = "\"([^\"\\\\]|\\\\.)*\"";
    private static final String COMMENT_PATTERN = "//[^\n]*" + "|" + "/\\*(.|\\R)*?\\*/"   // for whole text processing (text blocks)
            + "|" + "/\\*[^\\v]*" + "|" + "^\\h*\\*([^\\v]*|/)";  // for visible paragraph processing (line by line)

    private static final Pattern PATTERN = Pattern.compile(
            "(?<KEYWORD>" + KEYWORD_PATTERN + ")"
                    + "|(?<PAREN>" + PAREN_PATTERN + ")"
                    + "|(?<BRACE>" + BRACE_PATTERN + ")"
                    + "|(?<BRACKET>" + BRACKET_PATTERN + ")"
                    + "|(?<SEMICOLON>" + SEMICOLON_PATTERN + ")"
                    + "|(?<STRING>" + STRING_PATTERN + ")"
                    + "|(?<COMMENT>" + COMMENT_PATTERN + ")"
    );


    /*
    StyleSpans is used to set color of a specific range of text
    function uses Pattern matching to parse the input string to a StyleSpan obj
     */
    public static StyleSpans<Collection<String>> computeHighlighting(String text) {
        Matcher matcher = PATTERN.matcher(text);
        int last = 0;
        StyleSpansBuilder<Collection<String>> spans = new StyleSpansBuilder<>();
        while (matcher.find()) {
            String styleClass = matcher.group("KEYWORD") != null ? "keyword" :
                    matcher.group("PAREN") != null ? "paren" :
                            matcher.group("BRACE") != null ? "brace" :
                                    matcher.group("BRACKET") != null ? "bracket" :
                                            matcher.group("SEMICOLON") != null ? "semicolon" :
                                                    matcher.group("STRING") != null ? "string" :
                                                            matcher.group("COMMENT") != null ? "comment" :
                                                                    null; /* never happens */ assert styleClass != null;
            // adds no styling to the part of text where no keyword or other matched regex is.
            spans.add(Collections.emptyList(), matcher.start() - last);
            // adds css styling to the part of text where a keyword or other match is located
            spans.add(Collections.singleton(styleClass), matcher.end() - matcher.start());
            last = matcher.end();
        }
        spans.add(Collections.emptyList(), text.length() - last);
        return spans.create();
    }
}
