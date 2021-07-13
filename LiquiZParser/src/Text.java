public class Text extends DisplayElement {
    private String text;
    public void writeHTML(StringBuilder b) {
        //TODO: what if your text contains funky characters? Do we escape them? & < >   &amp;  &lt;
        b.append(text);
    }
    public Text(String text) {

    }
}
