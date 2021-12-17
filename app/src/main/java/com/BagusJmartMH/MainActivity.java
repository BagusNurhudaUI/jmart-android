package com.BagusJmartMH;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.viewpager.widget.ViewPager;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.BagusJmartMH.model.Account;
import com.BagusJmartMH.model.Payment;
import com.BagusJmartMH.model.Product;
import com.BagusJmartMH.model.ProductCategory;
import com.BagusJmartMH.request.FilterRequest;
import com.BagusJmartMH.request.PaymentRequest;
import com.BagusJmartMH.request.RequestFactory;
import com.google.android.material.tabs.TabLayout;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.tabs.TabLayout;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * merupakan activity home page awal pada aplikasi setelah login
 */
public class MainActivity extends AppCompatActivity {

    //inisialisasi
    public static List<Product> PList = new ArrayList<>();
    private static final Gson gson = new Gson();
    private ListView listView;
    private Button prevbtn;
    private Button nextbtn;
    private Button gobtn;
    private EditText edtfilterproduct;
    int page = 0;
    int pageSize = 2;
    boolean applyFilter = false;
    private Spinner spinner;
    private EditText filterName;
    private EditText filterlowestprice;
    private EditText filterhighestprice;
    private CheckBox checkBoxnew;
    private CheckBox checkBoxused;
    private Button apllybtn;
    private Button clearbtn;
    private TabLayout tabLayout;
    private CardView productCardview, filterCardview;
    public int pageNumber;
    private Button buynowbtn, cancelbelibtn, belibtn;
    private Payment payment;
    private LinearLayout pembelian, detprod;
    double discount ;
    public static Account account = LoginActivity.getLoggedAccount();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        prevbtn = findViewById(R.id.prev_button);
        nextbtn = findViewById(R.id.next_button);
        gobtn = findViewById(R.id.go_button);
        edtfilterproduct = findViewById(R.id.edt_filter_product);
        listView = findViewById(R.id.list_product);
        spinner = findViewById(R.id.id_spinner_product_category);
        filterName = findViewById(R.id.id_filter_name);
        filterlowestprice = findViewById(R.id.id_low_price);
        filterhighestprice = findViewById(R.id.id_highest_price);
        checkBoxnew = findViewById(R.id.id_checkbox_new);
        checkBoxused = findViewById(R.id.id_checkbox_used);
        apllybtn = findViewById(R.id.id_apply);
        clearbtn = findViewById(R.id.id_clear);
        tabLayout = findViewById(R.id.tablayout);
        productCardview = findViewById(R.id.ProductCardView);
        filterCardview = findViewById(R.id.filterCardView);


        ArrayAdapter<CharSequence> adapter1 = ArrayAdapter.createFromResource(MainActivity.this, R.array.category, android.R.layout.simple_spinner_item);
        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter1);


        /**
         *
         */
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                final Dialog dialog = new Dialog(MainActivity.this);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setCancelable(true);
                dialog.setContentView(R.layout.detail_product);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

                final TextView productdetailsName = dialog.findViewById(R.id.nama_produk);
                final TextView productdetailsWeight = dialog.findViewById(R.id.weight_dp);
                final TextView productdetailsPrice = dialog.findViewById(R.id.price_dp);
                final TextView productdetailsDiscount = dialog.findViewById(R.id.discount_dp);
                final TextView productdetailsConditionUsed = dialog.findViewById(R.id.condition_dp);
                final TextView productdetailsCategory = dialog.findViewById(R.id.category_dp);
                final TextView productdetailsShipmentPlan = dialog.findViewById(R.id.shipmentplan_dp);

                final TextView productdetailsName1 = dialog.findViewById(R.id.nama_produk1);
                final TextView productdetailsWeight1 = dialog.findViewById(R.id.weight_dp1);
                final TextView productdetailsPrice1 = dialog.findViewById(R.id.price_dp1);
                final TextView productdetailsDiscount1 = dialog.findViewById(R.id.discount_dp1);
                final TextView productdetailsConditionUsed1 = dialog.findViewById(R.id.condition_dp1);
                final TextView productdetailsCategory1 = dialog.findViewById(R.id.category_dp1);
                final TextView productdetailsShipmentPlan1 = dialog.findViewById(R.id.shipmentplan_dp1);

                final TextView productdetailsTotalPrice = dialog.findViewById(R.id.totalPrice);
                final EditText productdetailsQuantity = dialog.findViewById(R.id.quantity_beli);
                final EditText productdetailsAddress = dialog.findViewById(R.id.address_beli);
                buynowbtn = dialog.findViewById(R.id.buy_now_dp);
                detprod = dialog.findViewById(R.id.detail_prod);
                pembelian = dialog.findViewById(R.id.pembelian);
                cancelbelibtn = dialog.findViewById(R.id.cancel_beli_btn);
                belibtn = dialog.findViewById(R.id.beli_btn);
                Product product = PList.get(position);

                //Button Buy Now pada product detail4/product/page?page=0&pageSize=5
                buynowbtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        detprod.setVisibility(v.GONE);
                        pembelian.setVisibility(v.VISIBLE);
                        Toast.makeText(getApplicationContext(), "Buy Now clicked", Toast.LENGTH_SHORT).show();
                    }
                });

                //Cancel button di click
                cancelbelibtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        detprod.setVisibility(v.VISIBLE);
                        pembelian.setVisibility(v.GONE);
                        Toast.makeText(getApplicationContext(), "Cancel Clicked", Toast.LENGTH_SHORT).show();
                    }
                });

                productdetailsQuantity.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                        if (productdetailsQuantity.getText().toString().equals("")) {

                        } else {
                            if (product.discount == 0) {
                                productdetailsTotalPrice.setText("Rp. " + ((product.price * Integer.valueOf(productdetailsQuantity.getText().toString()))));
                            }
                            discount = product.price * (product.discount / (100));
                            productdetailsTotalPrice.setText("Rp. " + ((product.price - discount) * Integer.valueOf(productdetailsQuantity.getText().toString())));
                        }
                    }

                    @Override
                    public void afterTextChanged(Editable s) {

                    }
                });


                String ShipmentPlans = "REGULER";
                if (product.shipmentPlans == 1) {
                    ShipmentPlans = "INSTANT";
                } else if (product.shipmentPlans == 2) {
                    ShipmentPlans = "SAME DAY";
                } else if (product.shipmentPlans == 4) {
                    ShipmentPlans = "NEXT DAY";
                } else if (product.shipmentPlans == 8) {
                    ShipmentPlans = "REGULER";
                } else if (product.shipmentPlans == 16) {
                    ShipmentPlans = "KARGO";
                }

                String condition;
                if (product.conditionUsed) { //kondisi true
                    condition = "NEW";
                } else {
                    condition = "USED";
                }
                productdetailsTotalPrice.setText("Rp. " + product.price);
                productdetailsName.setText(product.name);
                productdetailsWeight.setText(product.weight + " Kg");
                productdetailsPrice.setText("Rp. " + product.price);
                productdetailsDiscount.setText(product.discount + " %");
                productdetailsConditionUsed.setText(condition);
                productdetailsCategory.setText(product.category + "");
                productdetailsShipmentPlan.setText(ShipmentPlans);

                productdetailsTotalPrice.setText("Rp. " + product.price);
                productdetailsName1.setText(product.name);
                productdetailsWeight1.setText(product.weight + " Kg");
                productdetailsPrice1.setText("Rp. " + product.price);
                productdetailsDiscount1.setText(product.discount + " %");
                productdetailsConditionUsed1.setText(condition);
                productdetailsCategory1.setText(product.category + "");
                productdetailsShipmentPlan1.setText(ShipmentPlans);

                dialog.show();

                //Cancel button di click dialog kedua
                belibtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        boolean isEmptyFields = false;
                        if (TextUtils.isEmpty(productdetailsAddress.getText().toString())) {
                            isEmptyFields = true;
                            productdetailsAddress.setError("Alamat harus diisi!");
                        }
                        if (TextUtils.isEmpty(productdetailsQuantity.getText().toString())) {
                            isEmptyFields = true;
                            productdetailsQuantity.setError("harus Input jumlah!");
                        }
                        if (!isEmptyFields) {
                            double totalprice = Double.valueOf(productdetailsTotalPrice.getText().toString().substring(3));
                            if (account.balance < totalprice) {
                                Toast.makeText(MainActivity.this, "Balance tidak cukup", Toast.LENGTH_SHORT).show();
                            } else if (account.balance >= totalprice) {
                                Response.Listener<String> listenerCreatePayment = new Response.Listener<String>() {
                                    @Override
                                    public void onResponse(String response) {
                                        JSONObject object = null;
                                        try {
                                            object = new JSONObject(response);
                                            if (object != null) {
                                                Toast.makeText(MainActivity.this, "Payment Berhasil!", Toast.LENGTH_SHORT).show();
                                                refreshBalance();
                                                dialog.dismiss();
                                            }
                                            payment = gson.fromJson(response, Payment.class);
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                };

                                Response.ErrorListener errorListenerCreatePayment = new Response.ErrorListener() {
                                    @Override
                                    public void onErrorResponse(VolleyError error) {
                                        Toast.makeText(MainActivity.this, "Payment gagal!", Toast.LENGTH_SHORT).show();
                                        Log.d("ERROR", error.toString());
                                    }
                                };
                                PaymentRequest createPaymentRequest = new PaymentRequest(account.id, product.id, Integer.parseInt(productdetailsQuantity.getText().toString()), productdetailsAddress.getText().toString(), product.shipmentPlans, product.accountId, listenerCreatePayment, errorListenerCreatePayment);
                                RequestQueue queue = Volley.newRequestQueue(MainActivity.this);
                                queue.add(createPaymentRequest);
                            }

                        }
                    }
                });
            }
        });






        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                switch (tab.getPosition()) {
                    case 0:
                        productCardview.setVisibility(View.VISIBLE);
                        filterCardview.setVisibility(View.GONE);
                        break;
                    case 1:
                        filterCardview.setVisibility(View.VISIBLE);
                        productCardview.setVisibility(View.GONE);
                        break;
                    default:
                        break;
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });


        GetshowProductList(page, 2);


        gobtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                page = Integer.valueOf(edtfilterproduct.getText().toString());
                page--;
                if (applyFilter) {
                    GetShowFilterProductList(page, 2);
                } else {
                    GetshowProductList(page, 2);
                }
            }
        });


        prevbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (page == 0) {
                    Toast.makeText(MainActivity.this, "Anda sudah berada pada halaman pertama!", Toast.LENGTH_SHORT).show();
                } else if (page >= 1) {
                    page--;
                    if (applyFilter) {
                        GetShowFilterProductList(page, 2);
                    } else {
                        GetshowProductList(page, 2);
                    }
                }
            }
        });

        nextbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                page++;
                if (applyFilter) {
                    GetShowFilterProductList(page, 2);
                } else {
                    GetshowProductList(page, 2);
                }
            }
        });


        apllybtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GetShowFilterProductList(page, 2);
                applyFilter = true;
                edtfilterproduct.setText("" + 1);
                Toast.makeText(MainActivity.this, "Filter Sukses", Toast.LENGTH_SHORT).show();
            }
        });

        clearbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GetshowProductList(page, 2);
                edtfilterproduct.setText("" + 1);
                applyFilter = false;
                Toast.makeText(MainActivity.this, "Filter berhasil dihapus", Toast.LENGTH_SHORT).show();
            }
        });

    }


    public void GetshowProductList(int lastPage, int pageSize) {
        Response.Listener<String> stringListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    PList.clear();
                    JSONArray jsonArray = new JSONArray(response);
                    Type productlistType = new TypeToken<ArrayList<Product>>() {
                    }.getType();
                    PList = gson.fromJson(response, productlistType);
                    if (PList.isEmpty()) {
//                        Toast.makeText(MainActivity.this, "Error! Halaman kosong!", Toast.LENGTH_SHORT).show();
//                        page--;
                    } else {
                        List<String> productnameList = new ArrayList<>();
                        for (Product product : PList) {
                            productnameList.add(product.name);
                        }
                        ArrayAdapter adapter = new ArrayAdapter<String>(MainActivity.this, R.layout.layoutitemproduct, productnameList);
                        listView.setAdapter(adapter);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };

        Response.ErrorListener errorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(MainActivity.this, "Sudah berada pada halaman terahkir", Toast.LENGTH_SHORT).show();
                page--;
            }
        };
        RequestQueue queue = Volley.newRequestQueue(MainActivity.this);
        queue.add(RequestFactory.getPage("product", lastPage, pageSize, stringListener, errorListener));
    }

    public void GetShowFilterProductList(int Lastpage, int pageSize) {
        String filteredname = filterName.getText().toString();
        int minP;
        int maxP;
        if (filterlowestprice.getText().toString().equals("")) {
            minP = 0;
        } else {
            minP = Integer.valueOf(filterlowestprice.getText().toString());
        }

        if (filterhighestprice.getText().toString().equals("")) {
            maxP = 0;
        } else {
            maxP = Integer.valueOf(filterhighestprice.getText().toString());
        }

        ProductCategory category = getProductCategory(spinner);
        String productcategory = spinner.getSelectedItem().toString();
        for (ProductCategory p : ProductCategory.values()) {
            if (p.toString().equals(productcategory)) {
                category = p;
            }
        }

        Response.Listener<String> listener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    PList.clear();
                    JSONArray jsonArray = new JSONArray(response);
                    Type PListType = new TypeToken<ArrayList<Product>>() {
                    }.getType();
                    PList = gson.fromJson(response, PListType);
                    if (PList.isEmpty()) {
//                        Toast.makeText(MainActivity.this, "Error! TIdak ada produk pada halaman ini", Toast.LENGTH_SHORT).show();
//                        page--;
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                List<String> productnameList = new ArrayList<>();
                for (Product product : PList) {
                    productnameList.add(product.name);
                }
                ArrayAdapter adapter = new ArrayAdapter<String>(MainActivity.this, R.layout.layoutitemproduct, productnameList);
                listView.setAdapter(adapter);
            }
        };
        Response.ErrorListener errorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(MainActivity.this, "Error! TIdak ada produk pada halaman ini", Toast.LENGTH_SHORT).show();
                page--;
            }
        };
        FilterRequest filterRequest;
        if (maxP == 0 && minP == 0) {
            filterRequest = new FilterRequest(Lastpage, pageSize, account.id, filteredname, category, listener, errorListener);
        } else if (maxP == 0) {
            filterRequest = new FilterRequest(Lastpage, pageSize, account.id, minP, filteredname, category, listener, errorListener);
        } else if (minP == 0) {
            filterRequest = new FilterRequest(filteredname, page, account.id, maxP, category, listener, errorListener);
        } else {
            filterRequest = new FilterRequest(Lastpage, account.id, filteredname, minP, maxP, category, listener, errorListener);
        }
        RequestQueue queue = Volley.newRequestQueue(MainActivity.this);
        queue.add(filterRequest);
    }


    @Override
    public void onResume() {
        super.onResume();
        List<String> productnameList = new ArrayList<>();
        for (Product product : PList) {
            productnameList.add(product.name);
        }
        ArrayAdapter adapter = new ArrayAdapter<String>(MainActivity.this, R.layout.layoutitemproduct, productnameList);
        listView.setAdapter(adapter);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.jmart_android_menu, menu);

        MenuItem addMenu = menu.findItem(R.id.addbox);
        if (account.store == null) {
            addMenu.setVisible(false);
        }

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        setMode(item.getItemId());
        return super.onOptionsItemSelected(item);
    }

    public void setMode(int selectedMode) {
        switch (selectedMode) {
            case R.id.search:
                break;
            case R.id.addbox:
                Intent i = new Intent(MainActivity.this, CreateProductActivity.class);
                startActivity(i);
                break;
            case R.id.person:
                Intent x = new Intent(MainActivity.this, AboutMeActivity.class);
                startActivity(x);
                break;
        }
    }

    /**
     * menampilkan urutan produk spinner
     * @param spinner parameter spinner
     * @return
     */
    public ProductCategory getProductCategory(Spinner spinner) {
        ProductCategory category = ProductCategory.BOOK;
        String productCategory = spinner.getSelectedItem().toString();
        for (ProductCategory pc : ProductCategory.values()) {
            if (pc.toString().equals(productCategory)) {
                category = pc;
            }
        }
        return category;
    }

    //mengambil balance ketika sudah membayar
    public void refreshBalance() {
        //Ketika menerima response
        Response.Listener<String> listener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject object = new JSONObject(response);
                    account = gson.fromJson(response, Account.class);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };

    }
}