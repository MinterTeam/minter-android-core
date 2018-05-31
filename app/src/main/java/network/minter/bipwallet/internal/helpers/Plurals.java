package network.minter.bipwallet.internal.helpers;

/**
 * Dogsy. 2018
 *
 * @author Eduard Maximovich <edward.vstock@gmail.com>
 */

public class Plurals {
    private static final String[] ordersWords = new String[]{"заказ", "заказа", "заказов"};
    private static final String[] repeatedWords = new String[]{"повторный", "повторных", "повторных"};
    private static final String[] reviewsWords = new String[]{"отзыв", "отзыва", "отзывов"};
    private static final String[] timesWords = new String[]{"раз", "раза", "раз"};
    private static final String[] dogsWords = new String[]{"собака", "собаки", "собак"};
    private static final String[] yearsWords = new String[]{"год", "года", "лет"};
    private static final String[] foundWords = new String[]{"найден", "найдено", "найдено"};
    private static final String[] sitterWords = new String[]{"догситтер", "догситтера", "догситтеров"};
    private static final String[] sitterShortWords = new String[]{"ситтер", "ситтера", "ситтеров"};
    private static final String[] daysWords = new String[]{"день", "дня", "дней"};
    private static final String[] daysFullWords = new String[]{"сутки", "суток", "суток"};
    private static final String[] rubbleWords = new String[]{"рубль", "рубля", "рублей"};
    private static final String[] messageWords = new String[]{"сообщение", "сообщения", "сообщений"};


    public static String busy(long n) {
        if (n == 1) {
            return "занятый";
        }

        return "занятые";
    }

    public static String available(long n) {
        if (n == 1) {
            return "доступный";
        }

        return "доступные";
    }


    public static String message(int n) {
        return message(Long.valueOf(n));
    }

    public static String message(Long n) {
        return plurals(n, messageWords);
    }


    public static String rubble(Long n) {
        return plurals(n, rubbleWords);
    }

    public static String daysFull(long n) {
        return plurals(n, daysFullWords);
    }

    public static String orders(long n) {
        return plurals(n, ordersWords);
    }

    public static String days(long n) {
        return plurals(n, daysWords);
    }

    public static String repeatedOrders(long n) {
        return String.format("%s %s", plurals(n, repeatedWords), plurals(n, ordersWords));
    }

    public static String founds(long n) {
        return plurals(n, foundWords);
    }

    public static String sitters(long n) {
        return plurals(n, sitterWords);
    }

    public static String sittersShort(long n) {
        return plurals(n, sitterShortWords);
    }

    public static String reviews(long n) {
        return plurals(n, reviewsWords);
    }

    public static String times(long n) {
        return plurals(n, timesWords);
    }

    public static String years(long n) {
        return plurals(n, yearsWords);
    }

    public static String dogs(long countGuestDogs) {
        return plurals(countGuestDogs, dogsWords);
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
