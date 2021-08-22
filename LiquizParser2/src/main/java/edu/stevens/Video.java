package edu.stevens;

public class Video extends DisplayElement {
    private String videoURL;
    public void writeHTML(StringBuilder b, boolean isQuestion) {
        b.append("\n<div><video controls width='320' height='240'><source src='media/")
            .append(videoURL)
            .append("' type='video/mp4'></video></div>\n");
    }
    public Video(String videoURL) {
        this.videoURL = videoURL;
    }
}
