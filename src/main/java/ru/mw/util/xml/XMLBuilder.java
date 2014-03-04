package ru.mw.util.xml;


import java.text.DateFormat;
import java.util.Date;
import java.util.Stack;

public class XMLBuilder {

    public static final String DEFAULT_VERSION = "1.0";

    public static final String DEFAULT_ENCODING = "utf-8";

    private String version;

    private String encoding;

    private Stack<String> tags = new Stack<String>();

    public DateFormat dateFormat = null;

    public boolean inOpenTag = false;

    public boolean inText = false;

    public boolean hasContent = false;

    private StringBuilder r = new StringBuilder();

    public XMLBuilder() {
        this(DEFAULT_VERSION, DEFAULT_ENCODING);
    }

    public XMLBuilder(String version, String encoding) {
        this.version = version;
        this.encoding = encoding;
        putProlog();
    }

    public XMLBuilder(boolean useProlog) {
        if (useProlog) {
            this.version = DEFAULT_VERSION;
            this.encoding = DEFAULT_ENCODING;
            putProlog();
        }
    }

    /**
     * Inserts XML header.
     */
    private void putProlog() {
        r.append(String.format("<?xml version=\"%s\" encoding=\"%s\"?>", version, encoding));
    }

    public XMLBuilder tag(String name) {
        closeOpenTag();
        nextLine();
        fillPadding();
        r.append("<").append(name);
        inOpenTag = true;
        hasContent = false;
        tags.push(name);
        return this;
    }

    private void closeOpenTag() {
        if (inOpenTag) {
            r.append(">");
            inOpenTag = false;
        }
    }

    private void closeOpenTagCompact() {
        if (inOpenTag && !hasContent) {
            r.append(" />");
            inOpenTag = false;
        }
    }

    /**
     * Throws exception if value is null or blank
     */
    public XMLBuilder requiredTag(String name, String value) {
        closeOpenTag();
        if (value == null || value.equals("")) {
            throw new NullPointerException("Tag is required, but value is blank: " + name);
        }
        return this;
    }

    /**
     * Doesn't append tag into result if values is null or empty
     */
    public XMLBuilder optionalTag(String name, String value) {
        closeOpenTag();
        if (value != null && !value.equals("")) {
            return tag(name, value);
        }
        return this;
    }

    public XMLBuilder tag(String name, String value) {
        closeOpenTag();
        return tag(name).text(value).up();
    }

    public XMLBuilder tag(String name, int value) {
        return tag(name, Integer.toString(value));
    }

    public XMLBuilder tag(String name, long value) {
        return tag(name, Long.toString(value));
    }

    public XMLBuilder tag(String name, Date value) {
        if (value == null) {
            return this;
        }
        if (dateFormat == null) {
            throw new NullPointerException("Date format is not set");
        }
        return tag(name, dateFormat.format(value));
    }

    private XMLBuilder closeTag(String name) {
        if (!hasContent) {
            closeOpenTagCompact();
        } else {
            closeOpenTag();
            if (!inText) {
                nextLine();
                fillPadding();
            }
            r.append(String.format("</%s>", name));
        }
        inText = false;
        hasContent = true;
        return this;
    }

    public XMLBuilder attr(String name, String value) {
        if (!inOpenTag) {
            throw new IllegalStateException(
                    "Opening tag is closed with '<'. Can't append attribute");
        }
        r.append(String.format(" %s=\"%s\"", name, normalizeXML(value)));
        return this;
    }

    public XMLBuilder attr(String name, int value) {
        attr(name, Integer.toString(value));
        return this;
    }

    public XMLBuilder attr(String name, long value) {
        attr(name, Long.toString(value));
        return this;
    }

    public XMLBuilder text(String text) {
        closeOpenTag();
        inText = true;
        hasContent = true;
        r.append(normalizeXML(text));
        return this;
    }

    /**
     * Inserts a chunk of XML without escaping.
     */
    public XMLBuilder chunk(String xml) {
        r.append(xml);
        return this;
    }

    public XMLBuilder up() {
        String lastTag = tags.pop();
        return closeTag(lastTag);
    }
	
    public XMLBuilder close() {
        while(tags.size() > 0){
            up();
        }
        return this;
    }

    @Override
    public String toString() {
        return r.toString();
    }

    private void nextLine() {
        r.append("\n");
    }

    private void fillPadding() {
        for (int i = 0; i < tags.size(); i++) {
            r.append("\t");
        }
    }

    //
    // ABBREVIATIONS
    //

    public XMLBuilder e(String name) {
        return this.tag(name);
    }

    public XMLBuilder a(String name, String value) {
        return this.attr(name, value);
    }

    public XMLBuilder a(String name, long value) {
        return this.attr(name, value);
    }

    public XMLBuilder a(String name, int value) {
        return this.attr(name, value);
    }

    public XMLBuilder ct(String name) {
        return closeTag(name);
    }

    public XMLBuilder e(String name, String value) {
        return tag(name, value);
    }

    public XMLBuilder e(String name, int value) {
        return tag(name, value);
    }

    public XMLBuilder e(String name, long value) {
        return tag(name, value);
    }

    public XMLBuilder t(String text) {
        return text(text);
    }

    public XMLBuilder u() {
        return up();
    }

    public void setDateFormat(DateFormat dateFormat) {
        this.dateFormat = dateFormat;
    }

    //
    //  XML TEXT NORMALIZATION
    //

    private static String normalizeXML(String source) {
        if (null == source) {
            return "";
        }
        // String src = source;
        String src = source.replaceAll("[^\\p{Graph}\\p{Space}а-яА-Я]", "");
        StringBuilder sb = new StringBuilder();
        for (char ch : src.toCharArray()) {
            switch (ch) {
                case '\n':
                case '\r':
                case '\t':
                case '\0':
                    sb.append(" ");
                    break;
                case '<':
                    sb.append("&lt;");
                    break;
                case '>':
                    sb.append("&gt;");
                    break;
                case '&':
                    sb.append("&amp;");
                    break;
                case '"':
                    sb.append("&quot;");
                    break;
                default:
                    sb.append(ch);
            }
        }

        return sb.toString();
    }
}
 
