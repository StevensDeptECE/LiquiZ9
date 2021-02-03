public class OpCode extends FillIn {
  private String opcodeStyle = "opcode"; // Style for opcode questions
  private static long defaultLen = 5;

  public final String getStyle() {
    return opcodeStyle;
  }

  public final long getDefaultLen() {
    return defaultLen;
  }
}