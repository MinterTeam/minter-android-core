package network.minter.bipwallet.coins;

import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.arellomobile.mvp.MvpView;

import dagger.Module;
import network.minter.bipwallet.coins.views.rows.ListWithButtonRow;
import network.minter.bipwallet.internal.views.list.multirow.MultiRowAdapter;

/**
 * MinterWallet. 2018
 *
 * @author Eduard Maximovich <edward.vstock@gmail.com>
 */
@Module
public interface CoinsTabModule {

    interface CoinsTabView extends MvpView {
        void setAvatar(String url);
        void setUsername(CharSequence name);
        void setBalance(long decimals, long points, CharSequence coinName);
        void setAdapter(RecyclerView.Adapter<?> adapter);
        void setOnAvatarClick(View.OnClickListener listener);
    }
}
