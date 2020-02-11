package com.battmobile.battmobilewarehouse.supplier;

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


public class SupplierAdapter extends RecyclerView.Adapter<SupplierAdapter.EventViewHolder> {

    Context context;
    SupplierActivity activity;
    private List<SupplierModel> list;
    SessionManager sessionManager;

    public SupplierAdapter(SupplierActivity activity, Context context, List<SupplierModel> list) {
        this.activity = activity;
        this.context = context;
        this.list = list;
        sessionManager = new SessionManager(context);
    }

    @NonNull
    @Override
    public EventViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new EventViewHolder(LayoutInflater.from(context).inflate(R.layout.list_item_supplier, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull EventViewHolder holder, int position) {

        final SupplierModel model = list.get(position);
        holder.setIsRecyclable(false);
        holder.tvName.setText(model.getName());
        holder.tvCompany.setText(model.getCompany());
        holder.tvAddress.setText(model.getAddress());
        holder.tvMobile.setText(model.getMobile());
        holder.tvEmail.setText(model.getEmail());
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
        TextView tvName, tvCompany, tvAddress, tvEmail, tvMobile;
        ImageView imgEdit;

        EventViewHolder(View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tv_name);
            tvCompany = itemView.findViewById(R.id.tv_company);
            tvAddress = itemView.findViewById(R.id.tv_address);
            tvEmail = itemView.findViewById(R.id.tv_email);
            tvMobile = itemView.findViewById(R.id.tv_mobile);
            imgEdit = itemView.findViewById(R.id.img_edit);
        }
    }
}