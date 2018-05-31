package network.minter.bipwallet.receiving.ui;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;

import network.minter.bipwallet.home.HomeModule;
import network.minter.bipwallet.home.HomeTabFragment;

/**
 * MinterWallet. 2018
 *
 * @author Eduard Maximovich <edward.vstock@gmail.com>
 */
public class ReceivingTabFragment extends HomeTabFragment {

    @Override
    public void onAttach(Context context) {
        HomeModule.getComponent().inject(this);
        super.onAttach(context);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        HomeModule.getComponent().inject(this);
        super.onCreate(savedInstanceState);
    }

}
