package com.battmobile.battmobilewarehouse.scrap;

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
import com.squareup.picasso.Picasso;

import java.util.List;


public class ScrapAdapter extends RecyclerView.Adapter<ScrapAdapter.EventViewHolder> {

    Context context;
    private List<ScrapModel> list;
    SessionManager sessionManager;

    public ScrapAdapter(Context context, List<ScrapModel> list) {
        this.context = context;
        this.list = list;
        sessionManager = new SessionManager(context);
    }

    @NonNull
    @Override
    public EventViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new EventViewHolder(LayoutInflater.from(context).inflate(R.layout.list_item_scrap, parent, false));
    }

    @Override
    public void onBindViewHolder(final @NonNull EventViewHolder holder, int position) {

        ScrapModel model = list.get(position);
        holder.setIsRecyclable(false);
        holder.tvBrand.setText(model.getBrand());
        holder.tvSize.setText(model.getSize());
        holder.tvSku.setText(model.getSku());
        Picasso.with(context).load(model.getImage()).placeholder(R.drawable.default_dp).into(holder.imgBattery);
        holder.imgMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu popup = new PopupMenu(context, holder.imgMenu);
                //Inflating the Popup using xml file
                popup.getMenuInflater().inflate(R.menu.menu_scrap, popup.getMenu());

                //registering popup with OnMenuItemClickListener
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    public boolean onMenuItemClick(MenuItem item) {
                        Toast.makeText(context, "You Clicked : " + item.getTitle(), Toast.LENGTH_SHORT).show();
                        return true;
                    }
                });

                popup.show();//showing popup menu
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class EventViewHolder extends RecyclerView.ViewHolder {
        TextView tvBrand, tvSize, tvSku;
        ImageView imgBattery, imgMenu;

        EventViewHolder(View itemView) {
            super(itemView);
            tvSku = itemView.findViewById(R.id.tv_sku);
            tvBrand = itemView.findViewById(R.id.tv_brand);
            tvSize = itemView.findViewById(R.id.tv_size);
            imgBattery = itemView.findViewById(R.id.img_battery);
            imgMenu = itemView.findViewById(R.id.img_menu);
        }
    }
}