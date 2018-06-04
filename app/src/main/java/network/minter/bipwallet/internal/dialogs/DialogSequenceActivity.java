package network.minter.bipwallet.internal.dialogs;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.WindowManager;

import network.minter.bipwallet.R;
import timber.log.Timber;

/**
 * MinterWallet. 2018
 *
 * @author Eduard Maximovich <edward.vstock@gmail.com>
 */
public class DialogSequenceActivity extends AppCompatActivity {

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Make us non-modal, so that others can receive touch events.
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL, WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL);

        // ...but notify us that it happened.
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH, WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH);
        setContentView(R.layout.fragment_dialog_sequence);

        DialogSequence dialogSequence = new DialogSequence();

        final WalletConfirmDialog.Builder mainBuilder = new WalletConfirmDialog.Builder(this, "Hello!")
                .setText("Some confirmation text")
                .setPositiveAction("Agreed", (d, w) -> {
                    Timber.d("Accepted confirm");
                })
                .setNegativeAction("Decline", (d, w) -> {
                    Timber.d("Declined confirm");
                });

        final WalletConfirmDialog.Builder nextBuilder = new WalletConfirmDialog.Builder(this, "Hello!")
                .setText("Some confirmation text 2")
                .setPositiveAction("Agreed 2", (d, w) -> {
                    Timber.d("Accepted confirm");
                })
                .setNegativeAction("Decline 2", (d, w) -> {
                    Timber.d("Declined confirm");
                });

        dialogSequence.setMain(0, mainBuilder, 1);
        dialogSequence.addDialog(1, nextBuilder, DialogSequence.NO_ACTION);

        dialogSequence.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                finish();
            }
        });
        dialogSequence.show();

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        // If we've received a touch notification that the user has touched
        // outside the app, finish the activity.
        if (MotionEvent.ACTION_OUTSIDE == event.getAction()) {
            finish();
            return true;
        }

        // Delegate everything else to Activity.
        return super.onTouchEvent(event);

    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }
}
