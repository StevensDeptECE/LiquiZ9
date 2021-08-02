public class Audio extends DisplayElement {
    private String audioURL;
    public void writeHTML(StringBuilder b, boolean isQuestion) {
        b.append("<audio controls>\n<source src='")
                .append(audioURL).append("' ")
                .append("type='audio/").append(audioURL.substring(audioURL.length()-3))
                .append("'>Your browser does not support the audio element. </audio>");
    }

    public Audio(String audioURL){
        this.audioURL = audioURL;
    }
}
