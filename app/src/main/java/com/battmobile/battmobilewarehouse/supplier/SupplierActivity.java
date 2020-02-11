package com.battmobile.battmobilewarehouse.supplier;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.battmobile.battmobilewarehouse.R;
import com.battmobile.battmobilewarehouse.stock.StockActivity;
import com.battmobile.battmobilewarehouse.utility.PaginationScrollListener;
import com.battmobile.battmobilewarehouse.utility.SessionManager;
import com.battmobile.battmobilewarehouse.utility.UtilityConstraints;
import com.battmobile.battmobilewarehouse.utility.Utils;
import com.rw.velocity.Velocity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import timber.log.Timber;

public class SupplierActivity extends AppCompatActivity {

    RecyclerView rvMyTransactions;
    List<SupplierModel> list;
    SessionManager sessionManager;
    SupplierAdapter supplierAdapter;
    RelativeLayout rlEmptyList;
    RelativeLayout rlHeader;
    RelativeLayout loader;
    Menu menu;
    int ADD_REQUEST_CODE = 101, EDIT_REQUEST_CODE = 102;
    LinearLayoutManager linearLayoutManager;
    ProgressBar progressBarMore;
    int total_count = 0, scroll_count = 0, page_no = 1;
    private boolean isLoading = false;
    String TAG = StockActivity.class.getSimpleName();
    private boolean isLastPage = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_supplier);
        Utils.setActionBarScreen("Supplier", getSupportActionBar());
        Velocity.initialize(3);
        loader = findViewById(R.id.loader);
        rvMyTransactions = findViewById(R.id.rv_my_transactions);
        list = new ArrayList<>();
        rlHeader = findViewById(R.id.rl_my_wallet);
        rlEmptyList = findViewById(R.id.rl_list_empty);
        sessionManager = new SessionManager(SupplierActivity.this);
        linearLayoutManager = new LinearLayoutManager(getApplicationContext(), RecyclerView.VERTICAL, false);
        rvMyTransactions.setLayoutManager(linearLayoutManager);
        progressBarMore = findViewById(R.id.more_progress);
        supplierAdapter = new SupplierAdapter(this, this, list);
        rvMyTransactions.setAdapter(supplierAdapter);
        rvMyTransactions.setNestedScrollingEnabled(false);
        getSupplierAPI(true);
        implementScrollListener();
    }

    private void implementScrollListener() {
        rvMyTransactions.addOnScrollListener(new PaginationScrollListener(linearLayoutManager) {
            @Override
            protected void loadMoreItems() {
                isLoading = true;

                // mocking network delay for API call
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (scroll_count < total_count) {
                            Log.e(TAG, "run: call page no = " + page_no);
                            progressBarMore.setVisibility(View.VISIBLE);
                            getSupplierAPI(false);


                        }
                    }
                }, 1000);
            }

            @Override
            public int getTotalPageCount() {
                return 1;
            }

            @Override
            public boolean isLastPage() {
                return isLastPage;
            }

            @Override
            public boolean isLoading() {
                return isLoading;
            }
        });
    }


    public void editProduct(SupplierModel model) {
        startActivityForResult(new Intent(SupplierActivity.this, EditSupplierActivity.class)
                .putExtra("id", model.getId())
                .putExtra("name", model.getName())
                .putExtra("email", model.getEmail())
                .putExtra("mobile", model.getMobile())
                .putExtra("company", model.getCompany())
                .putExtra("address", model.getAddress()), EDIT_REQUEST_CODE);
    }

    private void showAlert(String message) {
        new AlertDialog.Builder(SupplierActivity.this)
                .setMessage(message)
                .setTitle("Alert!")
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                })
                .setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialog) {
                    }
                }).create().show();
    }

    private void getSupplierAPI(Boolean isFirstTimeCall) {
        if (isFirstTimeCall) {
            loader.setVisibility(View.VISIBLE);
            total_count = 0;
            scroll_count = 0;
            page_no = 1;
            list.clear();
        }

        Velocity.get(UtilityConstraints.UrlLocation.SUPPLIER_LIST)
                .withPathParam("page", "" + page_no)
                .connect(new Velocity.ResponseListener() {
                    @Override
                    public void onVelocitySuccess(Velocity.Response response) {
                        loader.setVisibility(View.GONE);
                        try {
                            String sd = new String(response.body.getBytes());
                            JSONObject mainObject = new JSONObject(sd);
                            Timber.d("onVelocitySuccess: " + mainObject);
                            if (!mainObject.getBoolean("error")) {
                                JSONArray data = mainObject.getJSONArray("data");
                                for (int i = 0; i < data.length(); i++) {
                                    JSONObject object = data.getJSONObject(i);
                                    list.add(new SupplierModel(
                                            object.getString("id"),
                                            object.getString("supplier_name"),
                                            object.getString("supplier_email"),
                                            object.getString("supplier_phone"),
                                            object.getString("supplier_company"),
                                            object.getString("supplier_address")));
                                }
                                supplierAdapter.notifyDataSetChanged();
                                progressBarMore.setVisibility(View.GONE);
                                total_count = mainObject.getInt("total_records");
                                scroll_count = scroll_count + mainObject.getInt("count");
                                page_no++;
                                isLoading = false;

                            } else {
                                if (page_no != 1) {
                                    progressBarMore.setVisibility(View.GONE);
                                    Toast.makeText(getApplicationContext(), "Unable to load more data", Toast.LENGTH_SHORT).show();
                                } else {
                                    showAlert(mainObject.getString("message"));
                                }
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onVelocityFailed(Velocity.Response error) {
                        loader.setVisibility(View.GONE);
                        Timber.e("onVelocityFailed: " + error.body);
                        try {
                            JSONObject mainObject = new JSONObject(error.body);
                            Toast.makeText(SupplierActivity.this, mainObject.getString("message"), Toast.LENGTH_SHORT).show();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        this.menu = menu;
        getMenuInflater().inflate(R.menu.menu_add, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.menu_item_add) {
            if (Utils.isNetworkConnected(SupplierActivity.this)) {
                startActivityForResult(new Intent(SupplierActivity.this, AddSupplierActivity.class), ADD_REQUEST_CODE);
            } else
                Toast.makeText(getApplicationContext(), "No Internet Connection", Toast.LENGTH_SHORT).show();

            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // check if the request code is same as what is passed  here it is 2

        if (requestCode == EDIT_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            getSupplierAPI(true);

        } else if (requestCode == ADD_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            getSupplierAPI(true);

        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}