import java.io.InputStream;

public class Main {
    public static void main(String[] args) {
        try {
            if(args.length < 2) {
                LiquizCompiler L = new LiquizCompiler("output/");
                L.generateQuiz("demo.lq");
            } else {
                System.setProperty("user.dir", args[1]); //change directory in java
                for(int i = 2; i < args.length; i++) {
                    LiquizCompiler L = new LiquizCompiler("output/");
                    L.generateQuiz(args[i]);
                }
            }
        } catch(Exception e) {
            e.printStackTrace();
            System.out.println(e);
        }            
    }
}