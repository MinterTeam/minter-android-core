package network.minter.bipwallet.home.views;

import android.view.MenuItem;

import com.arellomobile.mvp.InjectViewState;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import network.minter.bipwallet.R;
import network.minter.bipwallet.home.HomeModule;
import network.minter.bipwallet.home.HomeTabFragment;
import network.minter.bipwallet.home.HomeTabsClasses;
import network.minter.bipwallet.internal.mvp.MvpBasePresenter;

/**
 * MinterWallet. 2018
 *
 * @author Eduard Maximovich <edward.vstock@gmail.com>
 */
@InjectViewState
public class HomePresenter extends MvpBasePresenter<HomeModule.HomeView> {

    private final HashMap<Integer, Integer> mClientBottomIdPositionMap = new HashMap<Integer, Integer>() {{
        put(R.id.bottom_coins, 0);
        put(R.id.bottom_send, 1);
        put(R.id.bottom_receive, 2);
        put(R.id.bottom_settings, 3);
    }};

    @Inject @HomeTabsClasses
    List<Class<? extends network.minter.bipwallet.home.HomeTabFragment>> tabsClasses;

    private int mLastPosition = 0;

    @Inject
    public HomePresenter() {

    }

    public void onPageSelected(int position) {
        mLastPosition = position;
    }

    public int getBottomIdByPosition(int position) {
        for (Map.Entry<Integer, Integer> item : mClientBottomIdPositionMap.entrySet()) {
            if (item.getValue() == position) {
                return item.getKey();
            }
        }

        return 0;
    }

    public int getTabPosition(String name) {
        int position = 0;
        for (Class<? extends HomeTabFragment> cls : tabsClasses) {
            if (cls.getName().equals(name)) {
                break;
            }
            position++;
        }

        return position;
    }

    public void onNavigationBottomItemSelected(MenuItem item) {

    }

    public int getBottomPositionById(int itemId) {
        return mClientBottomIdPositionMap.get(itemId);
    }
}
