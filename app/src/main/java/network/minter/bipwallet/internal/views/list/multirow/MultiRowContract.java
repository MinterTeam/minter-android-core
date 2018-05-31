package network.minter.bipwallet.internal.views.list.multirow;

import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;

/**
 * Mds. 2017
 *
 * @author Eduard Maximovich <edward.vstock@gmail.com>
 */

public interface MultiRowContract {


    interface Row<T extends MultiRowAdapter.RowViewHolder> {
        int POSITION_FIRST = -1;
        int POSITION_LAST = 999;
        int POSITION_ORDERED = 0;

        /**
         * views ID
         *
         * @return int
         * @see MultiRowAdapter#makeHoldersCache()
         */
        @LayoutRes
        int getItemView();

        /**
         * views position index
         *
         * @return int
         * @see MultiRowAdapter
         * @see MultiRowContract.Row
         */
        @Deprecated
        int getRowPosition();

        /**
         * If view should be visible
         *
         * @return bool
         */
        boolean isVisible();

        /**
         * Вызывается когда адаптер биндит вьюху,
         * соответственно в этом методе заполняем ViewHolder
         *
         * @param viewHolder Row view holder
         * @see MultiRowAdapter#makeHoldersCache()
         */
        void onBindViewHolder(@NonNull T viewHolder);

        /**
         * Вызывается когда холдер отцепляется от окна
         *
         * @param viewHolder
         * @see RecyclerView#onDetachedFromWindow()
         */
        void onUnbindViewHolder(@NonNull T viewHolder);

        /**
         * Класс ViewHolder'а который отражает вьюху
         *
         * @return Class
         * @see MultiRowAdapter.RowViewHolder
         */
        @NonNull
        Class<T> getViewHolderClass();
    }
}
