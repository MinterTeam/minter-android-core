/*
 * Copyright (C) 2018 by MinterTeam
 * @link https://github.com/MinterTeam
 *
 * The MIT License
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

package network.minter.bipwallet.addresses.adapters;

import android.arch.paging.PagedListAdapter;
import android.support.annotation.NonNull;
import android.support.v7.recyclerview.extensions.AsyncDifferConfig;
import android.support.v7.util.DiffUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import network.minter.bipwallet.R;
import network.minter.bipwallet.internal.helpers.ContextHelper;
import network.minter.my.models.AddressData;

/**
 * MinterWallet. 2018
 *
 * @author Eduard Maximovich <edward.vstock@gmail.com>
 */
public class AddressListAdapter extends PagedListAdapter<AddressData, AddressListAdapter.ViewHolder> {
    private LayoutInflater mInflater;
    private OnAddressClickListener mAddressClickListener;
    private final static DiffUtil.ItemCallback<AddressData> sDiffUtilCallback = new DiffUtil.ItemCallback<AddressData>() {
        @Override
        public boolean areItemsTheSame(AddressData oldItem, AddressData newItem) {
            return oldItem.id == newItem.id;
        }

        @Override
        public boolean areContentsTheSame(AddressData oldItem, AddressData newItem) {
            return oldItem.equals(newItem);
        }
    };
    private OnSetMainListener mOnSetMainListener;

    public AddressListAdapter() {
        super(sDiffUtilCallback);
    }

    private AddressListAdapter(@NonNull AsyncDifferConfig<AddressData> config) {
        super(config);
    }

    public void setOnSetMainListener(OnSetMainListener listener) {
        mOnSetMainListener = listener;
    }

    public void setData(final List<AddressData> data) {
//        mItems = new ArrayList<>(data);
        resort();
    }

    public void resort() {
//        Collections.sort(mItems, new Comparator<AddressData>() {
//            @Override
//            public int compare(AddressData o1, AddressData o2) {
//                if (o1.isMain && !o2.isMain) {
//                    return 1;
//                } else {
//                    noinspection ComparatorMethodParameterNotUsed
//                    return -1;
//                }
//            }
//        });
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (mInflater == null) {
            mInflater = LayoutInflater.from(parent.getContext());
        }

        View view = mInflater.inflate(R.layout.item_list_address, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        AddressData item = getItem(position);

        if (item.isMain || position == 0) {
            holder.addressTitle.setText("Main address");
        } else {
            holder.addressTitle.setText("Address #" + String.valueOf(position + 1));
        }

        holder.address.setText(item.address.toString());
        holder.address.setOnClickListener(v -> {
            if (mAddressClickListener != null) {
                mAddressClickListener.onClick(v, item);
            }
        });
        holder.actionCopy.setOnClickListener(v -> {
            ContextHelper.copyToClipboard(v.getContext(), item.address.toString());
        });
        holder.securedValue.setText(item.isServerSecured ? "Bip Wallet" : "You");
        holder.defSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//                if (isChecked) {
//                    notifyItemMoved(position, 0);
//                } else {
//                    notifyItemMoved(position, findByAddress(item.address));
//                }

                if (mOnSetMainListener != null) {
                    mOnSetMainListener.onSetMain(isChecked, item);
                }
            }
        });
    }

    public void setOnAddressClickListener(OnAddressClickListener clickListener) {
        mAddressClickListener = clickListener;
    }

//    private int findByAddress(MinterAddress address) {
//        int pos = 0;
//        for (AddressData d : mItems) {
//            if (d.address.equals(address)) {
//                return pos;
//            }
//            pos++;
//        }
//
//        return pos;
//    }

    public interface OnAddressClickListener {
        void onClick(View v, AddressData address);
    }

    public interface OnSetMainListener {
        void onSetMain(boolean isMain, AddressData data);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.address_title) TextView addressTitle;
        @BindView(R.id.address) TextView address;
        @BindView(R.id.action_copy) View actionCopy;
        @BindView(R.id.balance_value) TextView balanceValue;
        @BindView(R.id.secured_value) TextView securedValue;
        @BindView(R.id.default_switch) Switch defSwitch;
        @BindView(R.id.row_address) View rowAddress;
        @BindView(R.id.row_secured) View rowSecured;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }


}


