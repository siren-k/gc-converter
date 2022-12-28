package kr.co.songeul.converter.nexacro;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Node {

    private static final Pattern PATTERN_TYPE = Pattern.compile("(?<=<)(.*?)(?=[ >])");

    private Node parent;
    private List<Node> children = new ArrayList<>();

    // sibling
    private Node prev;
    private Node next;

    private StringBuilder content;
    private String trimContent;
    private String type;
    private int level;

    private boolean closed;

    private String id;
    private String text;
    private String top;
    private String left;
    private String height;
    private String width;

    public Node(String content) {
        this.content = new StringBuilder(content);
        this.trimContent = this.content.toString().trim();
        // extract type of node
        Matcher patternTypeMatcher = PATTERN_TYPE.matcher(this.trimContent);
        if (patternTypeMatcher.find()) {
            if (patternTypeMatcher.group().length() > 0) {
                this.type = patternTypeMatcher.group();
            }
        }
        // extract tag is closed or not
        if (this.trimContent.startsWith("<") && this.trimContent.endsWith("/>")) {
            this.closed = true;
        }
        if (this.trimContent.startsWith("<") && this.trimContent.endsWith("</" + this.type + ">")) {
            this.closed = true;
        }
        // extract current values
        this.id = getAttribute("id");
        this.text = getAttribute("text");
        this.top = getAttribute("top");
        this.left = getAttribute("left");
        this.height = getAttribute("height");
        this.width = getAttribute("width");
    }

    public String getContent() {
        if (this.type == null || this.type.equals("Script")) {
            return this.content.toString();
        }

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < level; i++) {
            sb.append("  ");
        }
        sb.append(this.trimContent);

        return sb.toString();
    }

    public String getChildrendContents() {
        StringBuilder sb = new StringBuilder();

        sb.append(getContent());
        sb.append("\n");
        if (getChildren().size() > 0) {
            for (Node child : getChildren()) {
                sb.append(child.getChildrendContents());
            }
        }
        if (!isClosed()) {
            sb.append(getCloseTag());
            sb.append("\n");
        }

        return sb.toString();
    }

    public void addContent(String content) {
        if (this.type.equals("Script")) {
            this.content.append("\n");
            this.content.append(content);
        }
    }

    public String getType() {
        return this.type;
    }

    public String getCloseTag() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < level; i++) {
            sb.append("  ");
        }
        sb.append(this.type.startsWith("/") ? "<" : "</");
        sb.append(this.type);
        sb.append(">");

        return sb.toString();
    }

    public boolean isClosed() {
        return this.closed;
    }

    public void putNext(Node node) {
        this.next = node;
    }

    public Node getNext() {
        return this.next;
    }

    public void putPrev(Node node) {
        this.prev = node;
    }

    public Node getPrev() {
        return this.prev;
    }

    public void putParent(Node node) {
        this.parent = node;
        if (node != null) {
            this.level = node.level + 1;
        }
    }

    public Node getParent() {
        return this.parent;
    }

    public void putChild(Node node) {
        this.children.add(node);
    }

    public List<Node> getChildren() {
        return this.children;
    }

    private String getAttribute(String attribute) {
        if (this.type == null || this.type.equals("Script")) {
            return null;
        }

        Pattern pattern = Pattern.compile("(?<=" + attribute + "=\")(.*?)(?=\")");
        Matcher matcher = pattern.matcher(this.trimContent);
        if (matcher.find()) {
            return matcher.group();
        }
        return null;
    }

    private void setAttribute(String attribute, String value) {

    }

    @Override
    public String toString() {
        return "Node"
                + "[id=" + id + "] "
                + "[text=" + text + "] "
                + "[top=" + top + "] "
                + "[left=" + left + "] "
                + "[height=" + height + "] "
                + "[width=" + width + "]"
                + "[content=" + content + "] "
                ;
    }

}
