package network.minter.bipwallet.addresses;

import android.app.Dialog;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.arellomobile.mvp.MvpView;

import dagger.Module;
import network.minter.my.models.AddressData;

/**
 * MinterWallet. 2018
 *
 * @author Eduard Maximovich <edward.vstock@gmail.com>
 */
@Module
public class AddressManageModule {

    public interface AddressItemView extends MvpView {

        void setAddress(String addressName);
        void setSecuredBy(String securedByVal);
        void setOnClickDelete(View.OnClickListener listener);
        void startRemoveDialog(CharSequence attention, CharSequence description, String yes, String no, Dialog.OnClickListener onYesListener);
        void finishWithResult(int resultCode);
        void showProgress(CharSequence text);
        void hideProgress();
    }

    public interface AddressListView extends MvpView {
        void setAdapter(RecyclerView.Adapter<?> adapter);
        void startAddressItem(int requestCode, AddressData address);
    }

}
