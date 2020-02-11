package com.battmobile.battmobilewarehouse.wallet;

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
import com.battmobile.battmobilewarehouse.utility.Utils;

import java.util.List;


public class WalletAdapter extends RecyclerView.Adapter<WalletAdapter.EventViewHolder> {

    Context context;
    private List<WalletModel> list;
    SessionManager sessionManager;

    public WalletAdapter(Context context, List<WalletModel> list) {
        this.context = context;
        this.list = list;
        sessionManager = new SessionManager(context);
    }

    @NonNull
    @Override
    public EventViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new EventViewHolder(LayoutInflater.from(context).inflate(R.layout.list_item_wallet, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull EventViewHolder holder, int position) {

        WalletModel model = list.get(position);
        holder.setIsRecyclable(false);
        holder.tvDate.setText(model.getDate());
        if (model.getTransferred_to().equalsIgnoreCase(sessionManager.getWAREHOUSE_ID())) {
            holder.tvAmount.setText("+AED " + model.getAmount());
            holder.tvComment.setText("Cash Received from " + model.getAgent_name());
            holder.tvAmount.setTextColor(context.getResources().getColor(R.color.colorPrimary));
            holder.imgType.setImageDrawable(context.getResources().getDrawable(R.drawable.icon_credit));
        } else {
            holder.tvAmount.setText("-AED " + model.getAmount());
            holder.tvComment.setText("Cash Transfered to " + model.getTranferred_to_agent());
            holder.tvAmount.setTextColor(context.getResources().getColor(R.color.black));
            holder.imgType.setImageDrawable(context.getResources().getDrawable(R.drawable.icon_debit));
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class EventViewHolder extends RecyclerView.ViewHolder {
        TextView tvRefNo, tvComment, tvAmount, tvDate;
        ImageView imgType;

        EventViewHolder(View itemView) {
            super(itemView);
            tvRefNo = itemView.findViewById(R.id.tv_reference_no);
            tvComment = itemView.findViewById(R.id.tv_comment);
            tvAmount = itemView.findViewById(R.id.tv_amount);
            tvDate = itemView.findViewById(R.id.tv_date);
            imgType = itemView.findViewById(R.id.img_type);
        }
    }
}