package com.battmobile.battmobilewarehouse.product;

import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.battmobile.battmobilewarehouse.R;
import com.battmobile.battmobilewarehouse.common.SelectBrandModel;

import java.util.List;

public class FilterBrandsAdapter extends RecyclerView.Adapter<FilterBrandsAdapter.ViewHolder> {

    private List<SelectBrandModel> list;
    private Context context;
    private MyClickListener myClickListener;

    public FilterBrandsAdapter(Context contextl, List<SelectBrandModel> list, MyClickListener myClickListener) {
        this.context = contextl;
        this.list = list;
        this.myClickListener = myClickListener;
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.list_item_filter_brands, parent, false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        SelectBrandModel model = list.get(position);
        if (model.isSelected()) {
            ((GradientDrawable) holder.llHeader.getBackground()).setStroke(2, context.getResources().getColor(R.color.colorPrimary));
        } else
            ((GradientDrawable) holder.llHeader.getBackground()).setStroke(2, context.getResources().getColor(R.color.light_grey_2));

        holder.tvTitle.setText(model.getTitle());
        holder.llHeader.setVisibility(View.VISIBLE);

    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView tvTitle;
        LinearLayout llHeader;

        private ViewHolder(View itemView) {
            super(itemView);
            setIsRecyclable(false);
            tvTitle = itemView.findViewById(R.id.tv_sub_name);
            llHeader = itemView.findViewById(R.id.ll_header);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            try {
                if (null != myClickListener) {
                    myClickListener.onItemClick(list.get(getAdapterPosition()), getAdapterPosition(), view);
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
        void onItemClick(SelectBrandModel model, int position, View v);
    }
}