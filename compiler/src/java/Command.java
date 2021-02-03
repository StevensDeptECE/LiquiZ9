public class Command extends FillIn {
  private static String commandStyle = "command"; // Style for command line questions, longer by default
  private static long defaultLen = 20;

  public final String getStyle() {
    return commandStyle;
  }

  public final long getDefaultLen() {
    return defaultLen;
  }
}