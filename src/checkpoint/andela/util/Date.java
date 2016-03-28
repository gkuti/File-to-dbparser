package checkpoint.andela.util;

import java.text.SimpleDateFormat;

public class Date {

    public String getDate() {
        java.util.Date date = new java.util.Date();
        SimpleDateFormat sd = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return sd.format(date);
    }
}
