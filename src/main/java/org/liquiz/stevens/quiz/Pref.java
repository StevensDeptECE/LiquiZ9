package org.liquiz.stevens.quiz;

import java.text.DateFormat;
import java.text.FieldPosition;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Pref {
    private static SimpleDateFormat df;

    static {
        df = new SimpleDateFormat("yyyy.MM.dd");
    }

    public static DateFormat getDateFormat() {
        return df;
    }
}
