package com.battmobile.battmobilewarehouse.common;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.battmobile.battmobilewarehouse.R;
import com.battmobile.battmobilewarehouse.utility.SessionManager;
import com.battmobile.battmobilewarehouse.utility.UtilityConstraints;
import com.battmobile.battmobilewarehouse.utility.Utils;
import com.rw.velocity.Velocity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class SelectBrandActivity extends AppCompatActivity implements SelectBrandAdapter.MyClickListener {

    RecyclerView recyclerView;
    SelectBrandAdapter selectBrandAdapter;
    EditText etSearch;
    SessionManager sessionManager;
    private List<SelectBrandModel> listBrands;
    ProgressDialog progress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sessionManager = new SessionManager(this);
        setContentView(R.layout.activity_select_model);
        progress = new ProgressDialog(SelectBrandActivity.this);
        progress.setMessage(UtilityConstraints.Messages.LOADING);
        progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progress.setIndeterminate(true);
        progress.setCancelable(true);
        Velocity.initialize(3);
        DividerItemDecoration itemDecorator = new DividerItemDecoration(SelectBrandActivity.this, DividerItemDecoration.VERTICAL);
        itemDecorator.setDrawable(ContextCompat.getDrawable(SelectBrandActivity.this, R.drawable.divider));
        listBrands = new ArrayList<>();
        recyclerView = findViewById(R.id.recycle_view);
        etSearch = findViewById(R.id.et_search);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext(), RecyclerView.VERTICAL, false));
        recyclerView.addItemDecoration(new DividerItemDecoration(SelectBrandActivity.this,
                DividerItemDecoration.VERTICAL));

        if (getIntent().getStringExtra("for").equals("brand")) {
            Utils.setActionBarScreen("Select Brand", getSupportActionBar());
            brandAPI();
        } else if (getIntent().getStringExtra("for").equals("brand")) {
            Utils.setActionBarScreen("Select Brand", getSupportActionBar());
            brandAPI();
        } else if (getIntent().getStringExtra("for").equals("supplier")) {
            Utils.setActionBarScreen("Select Supplier", getSupportActionBar());
            supplierAPI();
        } else if (getIntent().getStringExtra("for").equals("technician")) {
            Utils.setActionBarScreen("Select Technician", getSupportActionBar());
            technicianAPI();
        } else if (getIntent().getStringExtra("for").equals("user")) {
            Utils.setActionBarScreen("Select Technician", getSupportActionBar());
            userAPI();
        } else if (getIntent().getStringExtra("for").equals("battery")) {
            etSearch.setHint("Search by SKU");
            Utils.setActionBarScreen("Select Battery", getSupportActionBar());
            batteryAPI();
        }


        etSearch.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence cs, int arg1, int arg2, int arg3) {
                // When user changed the Text

            }

            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
                                          int arg3) {
                // TODO Auto-generated method stub

            }

            @Override
            public void afterTextChanged(Editable arg) {
                // TODO Auto-generated method stub
                if (listBrands.size() > 0)
                    listBrands = selectBrandAdapter.filter(etSearch.getText().toString().trim().toLowerCase(Locale.getDefault()));
            }
        });
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
                            JSONObject data = new JSONObject(sd);
                            if (!data.getBoolean("error")) {

                                JSONArray brandsArray = data.getJSONArray("data");
                                for (int i = 0; i < brandsArray.length(); i++) {
                                    JSONObject jsonObject = brandsArray.getJSONObject(i);
                                    listBrands.add(new SelectBrandModel(
                                            jsonObject.getString("id"),
                                            jsonObject.getString("brand_name")));
                                }
                                selectBrandAdapter = new SelectBrandAdapter(SelectBrandActivity.this, listBrands, getIntent().getStringExtra("for"));
                                recyclerView.setAdapter(selectBrandAdapter);
                                selectBrandAdapter.setOnItemClickListener(SelectBrandActivity.this);
                            } else {
                                Toast.makeText(getApplicationContext(), data.getString("message"), Toast.LENGTH_SHORT).show();
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

    private void supplierAPI() {
        progress.show();
        Velocity.get(UtilityConstraints.UrlLocation.GET_SUPPLIER)
                .connect(new Velocity.ResponseListener() {
                    @Override
                    public void onVelocitySuccess(Velocity.Response response) {
                        progress.dismiss();
                        try {
                            String sd = new String(response.body.getBytes());
                            JSONObject data = new JSONObject(sd);
                            if (!data.getBoolean("error")) {

                                JSONArray brandsArray = data.getJSONArray("data");
                                for (int i = 0; i < brandsArray.length(); i++) {
                                    JSONObject jsonObject = brandsArray.getJSONObject(i);
                                    listBrands.add(new SelectBrandModel(
                                            jsonObject.getString("id"),
                                            jsonObject.getString("supplier_name"),
                                            jsonObject.getString("supplier_phone"),
                                            jsonObject.getString("supplier_address")));
                                }
                                selectBrandAdapter = new SelectBrandAdapter(SelectBrandActivity.this, listBrands, getIntent().getStringExtra("for"));
                                recyclerView.setAdapter(selectBrandAdapter);
                                selectBrandAdapter.setOnItemClickListener(SelectBrandActivity.this);
                            } else {
                                Toast.makeText(getApplicationContext(), data.getString("message"), Toast.LENGTH_SHORT).show();
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

    private void technicianAPI() {
        progress.show();
        Velocity.get(UtilityConstraints.UrlLocation.GET_TECHNICIAN)
                .withPathParam("type", "Technician")
                .connect(new Velocity.ResponseListener() {
                    @Override
                    public void onVelocitySuccess(Velocity.Response response) {
                        progress.dismiss();
                        try {
                            String sd = new String(response.body.getBytes());
                            JSONObject data = new JSONObject(sd);
                            if (!data.getBoolean("error")) {

                                JSONArray brandsArray = data.getJSONArray("data");
                                for (int i = 0; i < brandsArray.length(); i++) {
                                    JSONObject jsonObject = brandsArray.getJSONObject(i);
                                    listBrands.add(new SelectBrandModel(
                                            jsonObject.getString("id"),
                                            jsonObject.getString("name"),
                                            jsonObject.getString("phone"),
                                            jsonObject.getString("email")));
                                }
                                selectBrandAdapter = new SelectBrandAdapter(SelectBrandActivity.this, listBrands, getIntent().getStringExtra("for"));
                                recyclerView.setAdapter(selectBrandAdapter);
                                selectBrandAdapter.setOnItemClickListener(SelectBrandActivity.this);
                            } else {
                                Toast.makeText(getApplicationContext(), data.getString("message"), Toast.LENGTH_SHORT).show();
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

    private void userAPI() {
        progress.show();
        Velocity.get(UtilityConstraints.UrlLocation.GET_TECHNICIAN)
                .connect(new Velocity.ResponseListener() {
                    @Override
                    public void onVelocitySuccess(Velocity.Response response) {
                        progress.dismiss();
                        try {
                            String sd = new String(response.body.getBytes());
                            JSONObject data = new JSONObject(sd);
                            if (!data.getBoolean("error")) {

                                JSONArray brandsArray = data.getJSONArray("data");
                                for (int i = 0; i < brandsArray.length(); i++) {
                                    JSONObject jsonObject = brandsArray.getJSONObject(i);
                                    listBrands.add(new SelectBrandModel(
                                            jsonObject.getString("id"),
                                            jsonObject.getString("name"),
                                            jsonObject.getString("phone"),
                                            jsonObject.getString("email")));
                                }
                                selectBrandAdapter = new SelectBrandAdapter(SelectBrandActivity.this, listBrands, getIntent().getStringExtra("for"));
                                recyclerView.setAdapter(selectBrandAdapter);
                                selectBrandAdapter.setOnItemClickListener(SelectBrandActivity.this);
                            } else {
                                Toast.makeText(getApplicationContext(), data.getString("message"), Toast.LENGTH_SHORT).show();
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

    private void batteryAPI() {
        progress.show();
        Velocity.get(UtilityConstraints.UrlLocation.PRODUCT_LIST)
                .connect(new Velocity.ResponseListener() {
                    @Override
                    public void onVelocitySuccess(Velocity.Response response) {
                        progress.dismiss();
                        try {
                            String sd = new String(response.body.getBytes());
                            JSONObject data = new JSONObject(sd);
                            if (!data.getBoolean("error")) {

                                JSONArray brandsArray = data.getJSONArray("data");
                                for (int i = 0; i < brandsArray.length(); i++) {
                                    JSONObject jsonObject = brandsArray.getJSONObject(i);
                                    listBrands.add(new SelectBrandModel(
                                            jsonObject.getString("id"),
                                            jsonObject.getString("battery_sku"),
                                            jsonObject.getString("brand_name"),
                                            jsonObject.getString("battery_size")));
                                }
                                selectBrandAdapter = new SelectBrandAdapter(SelectBrandActivity.this, listBrands, getIntent().getStringExtra("for"));
                                recyclerView.setAdapter(selectBrandAdapter);
                                selectBrandAdapter.setOnItemClickListener(SelectBrandActivity.this);
                            } else {
                                Toast.makeText(getApplicationContext(), data.getString("message"), Toast.LENGTH_SHORT).show();
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

    @Override
    public void onItemClick(int position, View v) {
        Intent resultIntent = new Intent();
        resultIntent.putExtra("id", listBrands.get(position).getId());
        resultIntent.putExtra("name", listBrands.get(position).getTitle());
        resultIntent.putExtra("contact", listBrands.get(position).getMobile());
        resultIntent.putExtra("address", listBrands.get(position).getAddress());
        setResult(Activity.RESULT_OK, resultIntent);
        finish();
    }
}