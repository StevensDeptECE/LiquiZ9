public class Hex extends FillIn {
  private static String hexStyle = "hex"; // Style for hex questions
  private static long defaultLen = 8;

  public final String getStyle() {
    return hexStyle;
  }

  public final long getDefaultLen() {
    return defaultLen;
  }
}