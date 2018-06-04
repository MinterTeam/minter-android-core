package network.minter.bipwallet.addresses.adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import network.minter.bipwallet.R;
import network.minter.mintercore.crypto.MinterAddress;
import network.minter.my.models.AddressData;

/**
 * MinterWallet. 2018
 *
 * @author Eduard Maximovich <edward.vstock@gmail.com>
 */
public class AddressListAdapter extends RecyclerView.Adapter<AddressListAdapter.ViewHolder> {
    private LayoutInflater mInflater;
    private List<AddressData> mItems = new ArrayList<>(0);
    private OnAddressClickListener mAddressClickListener;

    public void setData(final List<AddressData> data) {
        mItems = new ArrayList<>(data);
        resort();
    }

    public void resort() {
        Collections.sort(mItems, new Comparator<AddressData>() {
            @Override
            public int compare(AddressData o1, AddressData o2) {
                if (o1.isMain && !o2.isMain) {
                    return 1;
                } else {
                    //noinspection ComparatorMethodParameterNotUsed
                    return -1;
                }
            }
        });
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
        AddressData item = mItems.get(position);

        if (item.isMain) {
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
            //@TODO
        });
        holder.securedValue.setText(item.isServerSecured ? "Bip Wallet" : "You");
        holder.defSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    notifyItemMoved(position, 0);
                } else {
                    notifyItemMoved(position, findByAddress(item.address));
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }

    public void setOnAddressClickListener(OnAddressClickListener clickListener) {
        mAddressClickListener = clickListener;
    }

    public void clear() {
        mItems.clear();
        notifyDataSetChanged();
    }

    private int findByAddress(MinterAddress address) {
        int pos = 0;
        for (AddressData d : mItems) {
            if (d.address.equals(address)) {
                return pos;
            }
            pos++;
        }

        return pos;
    }

    public interface OnAddressClickListener {
        void onClick(View v, AddressData address);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.address_title) TextView addressTitle;
        @BindView(R.id.address) TextView address;
        @BindView(R.id.action_copy) TextView actionCopy;
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


