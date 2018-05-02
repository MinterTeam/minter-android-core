package network.minter.bipwallet.repo;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.junit.Before;

import network.minter.mintercore.MinterApi;
import timber.log.Timber;

/**
 * MinterWallet. 2018
 *
 * @author Eduard Maximovich <edward.vstock@gmail.com>
 */
public class BaseApiTest {

    static {
        MinterApi.initialize(true);
        //noinspection ConstantConditions
        Timber.plant(new Timber.DebugTree() {
            @Override
            protected void log(int priority, String tag, String message, Throwable t) {
                if (message.length() < 4000) {
                    if (priority == Log.ASSERT) {
                        System.err.println(String.format("[%d]%s: %s", priority, tag, message));
                    } else {
                        System.out.println(String.format("[%d]%s: %s", priority, tag, message));
                    }
                    return;
                }

                // Split by line, then ensure each line can fit into Log's maximum length.
                for (int i = 0, length = message.length(); i < length; i++) {
                    int newline = message.indexOf('\n', i);
                    newline = newline != -1 ? newline : length;
                    do {
                        int end = Math.min(newline, i + 4000);
                        String part = message.substring(i, end);
                        if (priority == Log.ASSERT) {
                            System.err.println(String.format("[%d]%s: %s", priority, tag, part));
                        } else {
                            System.out.println(String.format("[%d]%s: %s", priority, tag, part));
                        }
                        i = end;
                    } while (i < newline);
                }
            }
        });
    }

    private Gson mGson;

    @Before
    public void setUp() {
        mGson = new GsonBuilder().setPrettyPrinting().create();
    }


    public String toJson(Object o) {
        return mGson.toJson(o);
    }
}
