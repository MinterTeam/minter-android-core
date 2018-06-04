package network.minter.bipwallet.coins.ui;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.arellomobile.mvp.presenter.InjectPresenter;
import com.arellomobile.mvp.presenter.ProvidePresenter;

import javax.inject.Inject;
import javax.inject.Provider;

import butterknife.BindView;
import butterknife.ButterKnife;
import network.minter.bipwallet.R;
import network.minter.bipwallet.coins.CoinsTabModule;
import network.minter.bipwallet.coins.views.CoinsTabPresenter;
import network.minter.bipwallet.home.HomeModule;
import network.minter.bipwallet.home.HomeTabFragment;
import network.minter.bipwallet.internal.views.widgets.BipCircleImageView;

/**
 * MinterWallet. 2018
 *
 * @author Eduard Maximovich <edward.vstock@gmail.com>
 */
public class CoinsTabFragment extends HomeTabFragment implements CoinsTabModule.CoinsTabView {

    @Inject Provider<CoinsTabPresenter> presenterProvider;
    @InjectPresenter CoinsTabPresenter presenter;
    @BindView(R.id.userAvatar) BipCircleImageView avatar;
    @BindView(R.id.username) TextView username;
    @BindView(R.id.balanceDecs) TextView balanceDecs;
    @BindView(R.id.balancePoints) TextView balancePoints;
    @BindView(R.id.balanceCoinName) TextView balanceCoinName;
    @BindView(R.id.list) RecyclerView list;

    @Override
    public void onAttach(Context context) {
        HomeModule.getComponent().inject(this);
        super.onAttach(context);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tab_coins, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        HomeModule.getComponent().inject(this);
        super.onCreate(savedInstanceState);
    }

    @Override
    public void setAvatar(String url) {
        if (url == null) {
            return;
        }

        avatar.setImageUrl(url);
    }

    @Override
    public void setUsername(CharSequence name) {
        username.setText(name);
    }

    @Override
    public void setBalance(long decimals, long points, CharSequence coinName) {
        balanceDecs.setText(String.valueOf(decimals));
        balancePoints.setText(String.valueOf(points));
        balanceCoinName.setText(coinName);
    }

    @Override
    public void setAdapter(RecyclerView.Adapter<?> adapter) {
        list.setLayoutManager(new LinearLayoutManager(getActivity()));
        list.setAdapter(adapter);
    }

    @Override
    public void setOnAvatarClick(View.OnClickListener listener) {
        avatar.setOnClickListener(listener);
    }

    @ProvidePresenter
    CoinsTabPresenter providePresenter() {
        return presenterProvider.get();
    }
}
