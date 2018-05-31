package network.minter.bipwallet.auth.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityOptionsCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.Observable;
import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import network.minter.bipwallet.R;
import network.minter.bipwallet.home.ui.HomeActivity;
import network.minter.bipwallet.internal.BaseInjectFragment;
import network.minter.bipwallet.internal.BaseMvpInjectActivity;
import network.minter.bipwallet.internal.auth.AuthSession;
import timber.log.Timber;

/**
 * MinterWallet. 2018
 *
 * @author Eduard Maximovich <edward.vstock@gmail.com>
 */
public class SplashFragment extends BaseInjectFragment {

    @Inject AuthSession session;
    @BindView(R.id.logo) ImageView logo;
    private AuthSwitchActivity mAuthSwitchActivity;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_splash, container, false);
        ButterKnife.bind(this, view);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Observable.timer(1, TimeUnit.SECONDS)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(res->{
                    if(mAuthSwitchActivity == null) {
                        return;
                    }

                    if(!session.isLoggedIn(true)) {
                        mAuthSwitchActivity.showAuth(logo);
                        return;
                    }

                    ((BaseMvpInjectActivity) getActivity()).startActivityClearTop(getActivity(), HomeActivity.class);
                    getActivity().finish();
                });


    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        if (!(context instanceof AuthSwitchActivity)) {
            throw new IllegalStateException("Activity must implement AuthSwitchActivity");
        }

        mAuthSwitchActivity = ((AuthSwitchActivity) context);

    }

    @Override
    public void onDetach() {
        super.onDetach();
        mAuthSwitchActivity = null;
    }

    public interface AuthSwitchActivity {
        void showAuth(View sharedView);
    }
}
