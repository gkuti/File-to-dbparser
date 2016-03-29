package checkpoint.andela.util;

import java.text.SimpleDateFormat;

/**
 * Date class
 */
public class Date {

    /**
     * returns date current date is the format of "yyyy-MM-dd HH:mm:ss"
     *
     * @return String of date
     */
    public String getDate() {
        java.util.Date date = new java.util.Date();
        SimpleDateFormat sd = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return sd.format(date);
    }
}
