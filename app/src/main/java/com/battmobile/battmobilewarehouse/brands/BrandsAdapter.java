package com.battmobile.battmobilewarehouse.brands;

import android.content.Context;
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


public class BrandsAdapter extends RecyclerView.Adapter<BrandsAdapter.EventViewHolder> {

    Context context;
    BrandsActivity activity;
    private List<BrandsModel> list;
    SessionManager sessionManager;

    public BrandsAdapter(BrandsActivity activity, Context context, List<BrandsModel> list) {
        this.activity = activity;
        this.context = context;
        this.list = list;
        sessionManager = new SessionManager(context);
    }

    @NonNull
    @Override
    public EventViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new EventViewHolder(LayoutInflater.from(context).inflate(R.layout.list_item_brands, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull EventViewHolder holder, int position) {

        final BrandsModel model = list.get(position);
        holder.setIsRecyclable(false);
        holder.tvName.setText(model.getName());
        holder.tvStatus.setText(model.isStatus() ? "Active" : "Inactive");
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
        TextView tvName, tvStatus;
        ImageView imgEdit;

        EventViewHolder(View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tv_brand);
            tvStatus = itemView.findViewById(R.id.tv_status);
            imgEdit = itemView.findViewById(R.id.img_edit);
        }
    }
}