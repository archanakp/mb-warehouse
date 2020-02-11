package com.battmobile.battmobilewarehouse.product;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.battmobile.battmobilewarehouse.R;
import com.battmobile.battmobilewarehouse.common.SelectBrandModel;
import com.battmobile.battmobilewarehouse.utility.UtilityConstraints;
import com.battmobile.battmobilewarehouse.utility.Utils;
import com.rw.velocity.Velocity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class FilterActivity extends AppCompatActivity {

    ProgressDialog progress;
    RecyclerView recyclerViewBrands;
    List<SelectBrandModel> listBrands;
    FilterBrandsAdapter adapterBrands;
    JSONObject data;
    LinearLayout llReset, llApply;
    Bundle bundle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filter);
        bundle = getIntent().getExtras();
        Velocity.initialize(3);
        Utils.setActionBarScreen("Filter", getSupportActionBar());
        llReset = findViewById(R.id.linear_reset);
        llApply = findViewById(R.id.linear_apply);

        progress = new ProgressDialog(FilterActivity.this);
        progress.setMessage(UtilityConstraints.Messages.LOADING);
        progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progress.setIndeterminate(true);
        progress.setCancelable(true);

        recyclerViewBrands = findViewById(R.id.recycle_view_brand);
        listBrands = new ArrayList<>();
        recyclerViewBrands.setLayoutManager(new GridLayoutManager(FilterActivity.this, 3));
        adapterBrands = new FilterBrandsAdapter(FilterActivity.this, listBrands, new FilterBrandsAdapter.MyClickListener() {
            @Override
            public void onItemClick(SelectBrandModel model, int position, View v) {
                model.setSelected(!model.isSelected());
                adapterBrands.notifyDataSetChanged();
            }
        });
        recyclerViewBrands.setAdapter(adapterBrands);


        llReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                for (SelectBrandModel model : listBrands) {
                    model.setSelected(false);
                }
                adapterBrands.notifyDataSetChanged();
            }
        });
        llApply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<String> brands = new ArrayList<>();
                for (SelectBrandModel model : listBrands) {
                    if (model.isSelected()) {
                        brands.add(model.getId());
                    }
                }
                Intent intent = new Intent();
                Bundle bundle = new Bundle();
                bundle.putString("brands", brands.size() == 0 ? "" : brands.toString());
                intent.putExtras(bundle);
                setResult(Activity.RESULT_OK, intent);
                finish();
            }
        });

        brandAPI();
    }

    private void brandAPI() {
        progress.show();
        Velocity.get(UtilityConstraints.UrlLocation.SEARCH_BRAND_LIST)
                .connect(new Velocity.ResponseListener() {
                    @Override
                    public void onVelocitySuccess(Velocity.Response response) {
                        progress.dismiss();
                        try {
                            String sd = new String(response.body.getBytes());
                            JSONObject mainObject = new JSONObject(sd);
                            if (!mainObject.getBoolean("error")) {

                                data = mainObject;
                                JSONArray brandsArray = data.getJSONArray("data");
                                for (int i = 0; i < brandsArray.length(); i++) {
                                    JSONObject jsonObject = brandsArray.getJSONObject(i);
                                    listBrands.add(new SelectBrandModel(
                                            jsonObject.getString("id"),
                                            jsonObject.getString("brand_name")));
                                }

                                if (bundle != null) {
                                    if (bundle.getString("brands").length() != 0) {
                                        JSONArray brandsObject = new JSONArray(bundle.getString("brands"));
                                        for (int a = 0; a < brandsObject.length(); a++) {
                                            for (int b = 0; b < listBrands.size(); b++) {
                                                if (brandsObject.get(a).toString().equals(listBrands.get(b).getId())) {
                                                    listBrands.get(b).setSelected(true);
                                                }
                                            }
                                        }
                                    }
                                } else {
                                }
                                adapterBrands.notifyDataSetChanged();
                            } else {
                                Toast.makeText(getApplicationContext(), mainObject.getString("message"), Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onVelocityFailed(Velocity.Response error) {
                        progress.dismiss();
                        try {
                            JSONObject mainObject = new JSONObject(error.body);
                            Toast.makeText(getApplicationContext(), mainObject.getString("message"), Toast.LENGTH_SHORT).show();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}