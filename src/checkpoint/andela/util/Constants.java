package checkpoint.andela.util;

/**
 * Constants class
 */
public enum Constants {

    DATABASE_URL("jdbc:mysql://localhost:3306/reactiondb"),
    USER("root"),
    PASSWORD(""),
    TABLE("reactions"),
    DATA_FILENAME("files/reactions.dat"),
    LOG_FILENAME("files/reactions.txt");

    String value;

    Constants(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}