package Systems;

import java.sql.Timestamp;

public class TimeConverter {
    public static String diffHours(Timestamp lastTimestamp, Timestamp finalTimestamp) {
        long lastTime = lastTimestamp.getTime();
        long finalTime = finalTimestamp.getTime();
        long diffTime = finalTime - lastTime;
        int seconds = 59 - (int) ((diffTime / 1000) % 60);
        int minutes = 59 - (int) ((diffTime / (1000 * 60)) % 60);
        int hours = 24 - (int) ((diffTime / (1000 * 60 * 60)) % 24);
        return hours + " hrs " + minutes + " min " + seconds + " sec";
    }
}
