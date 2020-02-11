package com.battmobile.battmobilewarehouse.stock;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.battmobile.battmobilewarehouse.R;
import com.battmobile.battmobilewarehouse.utility.SessionManager;

import java.util.List;


public class StockAdapter extends RecyclerView.Adapter<StockAdapter.EventViewHolder> {

    Context context;
    private List<StockModel> list;
    private static MyClickListener myClickListener;

    public StockAdapter(Context context, List<StockModel> list) {
        this.context = context;
        this.list = list;
    }

    public void setOnItemClickListener(MyClickListener myClickListener) {
        this.myClickListener = myClickListener;
    }

    @NonNull
    @Override
    public EventViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new EventViewHolder(LayoutInflater.from(context).inflate(R.layout.list_item_stock, parent, false));
    }

    @Override
    public void onBindViewHolder(final @NonNull EventViewHolder holder, int position) {

        StockModel model = list.get(position);
        holder.setIsRecyclable(false);
        holder.tvBrand.setText(model.getBrand());
        holder.tvSize.setText(model.getSize());
        holder.tvQuantity.setText(model.getQuantity());
        holder.tvCost.setText(model.getCost_price());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public void addItems(List<StockModel> items) {
        this.list.addAll(items);
        notifyDataSetChanged();
    }

    class EventViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView tvBrand, tvSize, tvQuantity, tvCost;

        EventViewHolder(View itemView) {
            super(itemView);
            tvBrand = itemView.findViewById(R.id.tv_brand);
            tvSize = itemView.findViewById(R.id.tv_size);
            tvQuantity = itemView.findViewById(R.id.tv_quantity);
            tvCost = itemView.findViewById(R.id.tv_cost);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            try {
                if (null != myClickListener) {
                    myClickListener.onItemClick(getLayoutPosition(), view);
                } else {
                    Toast.makeText(view.getContext(), "Click Event Null hai", Toast.LENGTH_SHORT).show();
                }
            } catch (NullPointerException e) {
                e.printStackTrace();
                Toast.makeText(view.getContext(), "Click Event Null Ex", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public interface MyClickListener {
        void onItemClick(int position, View v);
    }
}