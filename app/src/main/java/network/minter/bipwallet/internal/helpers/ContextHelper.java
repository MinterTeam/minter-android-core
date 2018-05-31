package network.minter.bipwallet.internal.helpers;

import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;

public class ContextHelper {

    public static Activity parseActivity(Context context) {
        if (context instanceof Activity)
            return (Activity)context;
        else if (context instanceof ContextWrapper)
            return parseActivity(((ContextWrapper)context).getBaseContext());

        return null;
    }
}
