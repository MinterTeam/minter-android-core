package network.minter.bipwallet.internal.helpers;

import org.joda.time.Duration;

/**
 * Minter. 2018
 *
 * @author Eduard Maximovich <edward.vstock@gmail.com>
 */
public class Plurals {
    private static final String[] seconds = new String[]{"second", "seconds", "seconds"};
    private static final String[] minutes = new String[]{"minute", "minutes", "minutes"};
    private static final String[] hours = new String[]{"hour", "hours", "hours"};


    public static String seconds(Long n) {
        return plurals(n, seconds);
    }

    public static String minutes(Long n) {
        return plurals(n, minutes);
    }

    public static String hours(Long n) {
        return plurals(n, hours);
    }

    public static String countdown(Long seconds) {
        return countdown(new Duration(seconds * 1000L /*accepts milliseconds only*/));
    }

    public static String countdown(Duration duration) {
        StringBuilder out = new StringBuilder();

        if (duration.getStandardHours() > 0) {
            out.append(String.format("%d %s", duration.getStandardHours(), hours(duration.getStandardHours())));
        }

        if (duration.getStandardMinutes() > 0) {
            out.append(String.format("%d %s", duration.getStandardMinutes(), minutes(duration.getStandardMinutes())));
        }

        if (duration.getStandardSeconds() > 0) {
            out.append(String.format("%d %s", duration.getStandardSeconds(), seconds(duration.getStandardSeconds())));
        } else {
            out.append("0 seconds");
        }

        return out.toString();
    }



    private static String plurals(Long n, String[] words) {
        if (n == null) {
            return words[2];
        }

        n = n % 100;
        if (n > 19) {
            n = n % 10;
        }

        if (n == 1) {
            return words[0];
        } else if (n == 2L || n == 3L || n == 4L) {
            return words[1];
        } else {
            return words[2];
        }
    }
}
