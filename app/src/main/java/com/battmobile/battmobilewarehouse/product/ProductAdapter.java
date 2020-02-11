package com.battmobile.battmobilewarehouse.product;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.battmobile.battmobilewarehouse.R;
import com.battmobile.battmobilewarehouse.utility.SessionManager;

import java.util.List;


public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.EventViewHolder> {

    Context context;
    ProductActivity activity;
    private List<ProductModel> list;
    SessionManager sessionManager;

    public ProductAdapter(ProductActivity activity, Context context, List<ProductModel> list) {
        this.activity = activity;
        this.context = context;
        this.list = list;
        sessionManager = new SessionManager(context);
    }

    @NonNull
    @Override
    public EventViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new EventViewHolder(LayoutInflater.from(context).inflate(R.layout.list_item_product, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull EventViewHolder holder, int position) {

        final ProductModel model = list.get(position);
        holder.setIsRecyclable(false);
        holder.tvBrand.setText(model.getBrand_title());
        holder.tvSize.setText(model.getSize());
        holder.tvSKU.setText(model.getSku());
        holder.imgEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.editProduct(model);
            }
        });

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class EventViewHolder extends RecyclerView.ViewHolder {
        TextView tvBrand, tvSize, tvSKU;
        ImageView imgEdit;

        EventViewHolder(View itemView) {
            super(itemView);
            tvBrand = itemView.findViewById(R.id.tv_brand);
            tvSize = itemView.findViewById(R.id.tv_size);
            tvSKU = itemView.findViewById(R.id.tv_sku);
            imgEdit = itemView.findViewById(R.id.img_edit);
        }
    }
}