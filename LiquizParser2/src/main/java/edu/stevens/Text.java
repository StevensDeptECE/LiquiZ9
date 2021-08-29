package edu.stevens;

public class Text extends DisplayElement {
    private String text;
    public void writeHTML(StringBuilder b, boolean isQuestion) {
        //TODO: what if your text contains funky characters? Do we escape them? & < >   &amp;  &lt;
        b.append("<div>").append(text).append("</div>");
    }
    public Text(String t) {
        text = t;
    }
}
