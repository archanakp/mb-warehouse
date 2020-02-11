package com.battmobile.battmobilewarehouse.home;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.PopupMenu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.battmobile.battmobilewarehouse.R;
import com.battmobile.battmobilewarehouse.brands.BrandsActivity;
import com.battmobile.battmobilewarehouse.product.ProductActivity;
import com.battmobile.battmobilewarehouse.stock.StockActivity;
import com.battmobile.battmobilewarehouse.supplier.SupplierActivity;
import com.battmobile.battmobilewarehouse.utility.SessionManager;
import com.battmobile.battmobilewarehouse.utility.Utils;
import com.battmobile.battmobilewarehouse.wallet.WalletActivity;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class HomeActivity extends Activity {
    LinearLayout llStock, llAllocation, llReport, llScrap, llWallet, llSupplier, llBrands;
    SessionManager sessionManager;
    TextView tvName, tvEmail;
    ImageView imgMenu;
    CircleImageView profileImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        sessionManager = new SessionManager(HomeActivity.this);
        profileImage = findViewById(R.id.profile_image);
        imgMenu = findViewById(R.id.img_menu);
        tvName = findViewById(R.id.tv_name);
        tvEmail = findViewById(R.id.tv_email);
        llStock = findViewById(R.id.ll_stock);
        llAllocation = findViewById(R.id.ll_allocation);
        llReport = findViewById(R.id.ll_report);
        llScrap = findViewById(R.id.ll_scrap);
        llWallet = findViewById(R.id.ll_wallet);
        llSupplier = findViewById(R.id.ll_supplier);
        llBrands = findViewById(R.id.ll_brands);

        imgMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu popup = new PopupMenu(HomeActivity.this, imgMenu);
                //Inflating the Popup using xml file
                popup.getMenuInflater().inflate(R.menu.menu_home, popup.getMenu());

                //registering popup with OnMenuItemClickListener
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    public boolean onMenuItemClick(MenuItem item) {
                        int id = item.getItemId();
                        if (id == R.id.menu_item_profile) {
                            if (Utils.isNetworkConnected(HomeActivity.this)) {
                                startActivity(new Intent(HomeActivity.this, ProfileActivity.class));
                            } else
                                Toast.makeText(getApplicationContext(), "No Internet Connection", Toast.LENGTH_SHORT).show();

                        } else if (id == R.id.menu_item_logout) {
                            sessionManager.logoutUser();

                        }
                        return true;
                    }
                });

                popup.show();//showing popup menu
            }
        });

        llStock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(HomeActivity.this, StockActivity.class)
                        .putExtra("battery_type", "New"));
            }
        });
        llAllocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(HomeActivity.this, ProductActivity.class));
            }
        });
        llReport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });
        llScrap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(HomeActivity.this, StockActivity.class)
                        .putExtra("battery_type", "Scrap"));
            }
        });
        llWallet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(HomeActivity.this, WalletActivity.class));
            }
        });
        llSupplier.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(HomeActivity.this, SupplierActivity.class));
            }
        });
        llBrands.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(HomeActivity.this, BrandsActivity.class));
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        tvName.setText(sessionManager.getFIRST_NAME() + " " + sessionManager.getLAST_NAME());
        tvEmail.setText(sessionManager.getEMAIL());
        Picasso.with(HomeActivity.this).load(sessionManager.getPROFILE_IMAGE()).placeholder(R.drawable.profile_pic).into(profileImage);
    }
}
