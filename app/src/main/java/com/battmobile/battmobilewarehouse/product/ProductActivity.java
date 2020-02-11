package com.battmobile.battmobilewarehouse.product;

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
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.battmobile.battmobilewarehouse.R;
import com.battmobile.battmobilewarehouse.stock.StockActivity;
import com.battmobile.battmobilewarehouse.utility.PaginationScrollListener;
import com.battmobile.battmobilewarehouse.utility.SessionManager;
import com.battmobile.battmobilewarehouse.utility.UtilityConstraints;
import com.battmobile.battmobilewarehouse.utility.Utils;
import com.flipboard.bottomsheet.BottomSheetLayout;
import com.rw.velocity.Velocity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import timber.log.Timber;

public class ProductActivity extends AppCompatActivity {

    RecyclerView rvMyTransactions;
    List<ProductModel> list;
    SessionManager sessionManager;
    ProductAdapter productAdapter;
    RelativeLayout rlEmptyList;
    RelativeLayout rlHeader;
    RelativeLayout loader;
    LinearLayout linearLayoutSort, linearLayoutFilter;
    LinearLayout linearLayoutSortFilter;
    BottomSheetLayout bottomSheetMenu;
    RadioGroup radioGroupSort;
    RadioButton rbDefault, rbSkuAZ, rbSkuZA, rbBrandNameAZ, rbBrandNameZA;
    Menu menu;
    int i = 1;
    String sort_input = UtilityConstraints.Constraints.UsedVehicle.SortInput.DEFAULT,
            sort_type = UtilityConstraints.Constraints.UsedVehicle.SortType.DEFAULT;
    String filter_by_brands = "";
    int ADD_REQUEST_CODE = 101, EDIT_REQUEST_CODE = 102, FILTER_REQUEST_CODE = 103;
    LinearLayoutManager linearLayoutManager;
    ProgressBar progressBarMore;
    int total_count = 0, scroll_count = 0, page_no = 1;
    private boolean isLoading = false;
    String TAG = StockActivity.class.getSimpleName();
    private boolean isLastPage = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product);
        Utils.setActionBarScreen("Products", getSupportActionBar());
        Velocity.initialize(3);
        loader = findViewById(R.id.loader);
        rvMyTransactions = findViewById(R.id.rv_my_transactions);
        list = new ArrayList<>();
        rlHeader = findViewById(R.id.rl_my_wallet);
        rlEmptyList = findViewById(R.id.rl_list_empty);
        linearLayoutSort = findViewById(R.id.linear_sort);
        linearLayoutFilter = findViewById(R.id.linear_filter);
        bottomSheetMenu = findViewById(R.id.bottomsheet);
        linearLayoutSortFilter = findViewById(R.id.linear_sort_filtert);
        sessionManager = new SessionManager(ProductActivity.this);
        linearLayoutManager = new LinearLayoutManager(getApplicationContext(), RecyclerView.VERTICAL, false);
        rvMyTransactions.setLayoutManager(linearLayoutManager);
        progressBarMore = findViewById(R.id.more_progress);
        productAdapter = new ProductAdapter(this, this, list);
        rvMyTransactions.setAdapter(productAdapter);
        rvMyTransactions.setNestedScrollingEnabled(false);
        getProductAPI(true);
        implementScrollListener();
        linearLayoutFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putString("brands", filter_by_brands);
                Intent intent = new Intent(ProductActivity.this, FilterActivity.class);
                intent.putExtras(bundle);
                startActivityForResult(intent, FILTER_REQUEST_CODE);
            }
        });
        linearLayoutSort.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bottomSheetMenu.showWithSheetView(LayoutInflater.from(getApplicationContext()).inflate(R.layout.bottomsheet_sort, bottomSheetMenu, false));

                radioGroupSort = bottomSheetMenu.findViewById(R.id.radioGroup);
                rbDefault = findViewById(R.id.rb_default);
                rbSkuAZ = findViewById(R.id.rb_sku_a_to_z);
                rbSkuZA = findViewById(R.id.rb_sku_z_to_z);
                rbBrandNameAZ = findViewById(R.id.rb_brand_name_a_to_z);
                rbBrandNameZA = findViewById(R.id.rb_brand_name_z_to_a);
                switch (i) {
                    case 2:
                        rbDefault.setChecked(false);
                        rbSkuAZ.setChecked(true);
                        rbSkuZA.setChecked(false);
                        rbBrandNameAZ.setChecked(false);
                        rbBrandNameZA.setChecked(false);
                        break;
                    case 3:
                        rbDefault.setChecked(false);
                        rbSkuAZ.setChecked(false);
                        rbSkuZA.setChecked(true);
                        rbBrandNameAZ.setChecked(false);
                        rbBrandNameZA.setChecked(false);
                        break;
                    case 4:
                        rbDefault.setChecked(false);
                        rbSkuAZ.setChecked(false);
                        rbSkuZA.setChecked(false);
                        rbBrandNameAZ.setChecked(true);
                        rbBrandNameZA.setChecked(false);
                        break;
                    case 5:
                        rbDefault.setChecked(false);
                        rbSkuAZ.setChecked(false);
                        rbSkuZA.setChecked(false);
                        rbBrandNameAZ.setChecked(false);
                        rbBrandNameZA.setChecked(true);
                        break;
                    default:
                        rbDefault.setChecked(true);
                        rbSkuAZ.setChecked(false);
                        rbSkuZA.setChecked(false);
                        rbBrandNameAZ.setChecked(false);
                        rbBrandNameZA.setChecked(false);
                        break;
                }

                rbDefault.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        i = 1;
                        bottomSheetMenu.dismissSheet();
                        sort_input = UtilityConstraints.Constraints.UsedVehicle.SortInput.DEFAULT;
                        sort_type = UtilityConstraints.Constraints.UsedVehicle.SortType.DEFAULT;
                        getProductAPI(true);


                    }
                });
                rbSkuAZ.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        i = 3;
                        bottomSheetMenu.dismissSheet();
                        sort_input = UtilityConstraints.Constraints.UsedVehicle.SortInput.SKU;
                        sort_type = UtilityConstraints.Constraints.UsedVehicle.SortType.AZ;
                        getProductAPI(true);

                    }
                });
                rbSkuZA.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        i = 4;
                        bottomSheetMenu.dismissSheet();
                        sort_input = UtilityConstraints.Constraints.UsedVehicle.SortInput.SKU;
                        sort_type = UtilityConstraints.Constraints.UsedVehicle.SortType.ZA;
                        getProductAPI(true);

                    }
                });
                rbBrandNameAZ.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        i = 5;
                        bottomSheetMenu.dismissSheet();
                        sort_input = UtilityConstraints.Constraints.UsedVehicle.SortInput.BRAND_NAME;
                        sort_type = UtilityConstraints.Constraints.UsedVehicle.SortType.AZ;
                        getProductAPI(true);

                    }
                });
                rbBrandNameZA.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        i = 6;
                        bottomSheetMenu.dismissSheet();
                        sort_input = UtilityConstraints.Constraints.UsedVehicle.SortInput.BRAND_NAME;
                        sort_type = UtilityConstraints.Constraints.UsedVehicle.SortType.ZA;
                        getProductAPI(true);

                    }
                });
            }
        });
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
                            getProductAPI(false);


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


    public void editProduct(ProductModel model) {
        startActivityForResult(new Intent(ProductActivity.this, EditProductActivity.class)
                .putExtra("id", model.getId())
                .putExtra("sku", model.getSku())
                .putExtra("size", model.getSize())
                .putExtra("brand_id", model.getBrand_id())
                .putExtra("brand_title", model.getBrand_title())
                .putExtra("image", model.getImage()), EDIT_REQUEST_CODE);
    }

    private void showAlert(String message) {
        new AlertDialog.Builder(ProductActivity.this)
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

    private void getProductAPI(Boolean isFirstTimeCall) {
        if (isFirstTimeCall) {
            loader.setVisibility(View.VISIBLE);
            total_count = 0;
            scroll_count = 0;
            page_no = 1;
            list.clear();

        }

        Velocity.get(UtilityConstraints.UrlLocation.PRODUCT_LIST)
                .withPathParam("page", "" + page_no)
                .withPathParam("type", "New")
                .withPathParam("brand_id", filter_by_brands)
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
                                    list.add(new ProductModel(
                                            object.getString("id"),
                                            object.getString("battery_sku"),
                                            object.getString("battery_size"),
                                            object.getString("brand_id"),
                                            object.getString("brand_name"),
                                            object.getString("battery_image")));
                                }
                                productAdapter.notifyDataSetChanged();
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
                            Toast.makeText(ProductActivity.this, mainObject.getString("message"), Toast.LENGTH_SHORT).show();
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
            if (Utils.isNetworkConnected(ProductActivity.this)) {
                startActivityForResult(new Intent(ProductActivity.this, AddProductActivity.class), ADD_REQUEST_CODE);
            } else
                Toast.makeText(getApplicationContext(), "No Internet Connection", Toast.LENGTH_SHORT).show();

            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // check if the request code is same as what is passed  here it is 2

        if (requestCode == FILTER_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            Bundle bundle = data.getExtras();
            if (bundle != null) {
                filter_by_brands = bundle.getString("brands");
                getProductAPI(true);

            }
        } else if (requestCode == EDIT_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            getProductAPI(true);

        } else if (requestCode == ADD_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            getProductAPI(true);

        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}