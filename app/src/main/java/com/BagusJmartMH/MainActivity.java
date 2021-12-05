package com.BagusJmartMH;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.BagusJmartMH.model.Account;
import com.BagusJmartMH.model.Product;
import com.BagusJmartMH.model.ProductCategory;
import com.BagusJmartMH.request.FilterRequest;
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

public class MainActivity extends AppCompatActivity {

    private TextView textView;
    public static ViewPager viewPager;
    private VPAdapter vpAdapter;
    public static List<Product> PList = new ArrayList<>();
    private static final Gson gson = new Gson();
    private ListView listView;
    private Button prevbtn;
    private Button nextbtn;
    private Button gobtn;
    private EditText edtfilterproduct;
    int page = 0;
    int pageSize = 2;
    boolean SignFilter = false;
    private Spinner spinner;
    private EditText filterName;
    private EditText filterlowestprice;
    private EditText filterhighestprice;
    private CheckBox checkBoxnew;
    private CheckBox checkBoxused;
    private Button apllybtn;
    private Button clearebtn;
    private TabLayout tabLayout;
    private CardView productCardview, filterCardview;
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
        clearebtn = findViewById(R.id.id_clear);
        tabLayout = findViewById(R.id.tablayout);
        productCardview = findViewById(R.id.ProductCardView);
        filterCardview = findViewById(R.id.filterCardView);

        ArrayAdapter<CharSequence> adapter1 = ArrayAdapter.createFromResource(MainActivity.this, R.array.category, android.R.layout.simple_spinner_item);
        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter1);

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                switch (tab.getPosition()){
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
                if(SignFilter){
                    GetShowFilterProductList(page, 1);
                }
                else {
                    GetshowProductList(page, 1);
                }
            }
        });

        prevbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (page == 0){
                    Toast.makeText(MainActivity.this, "Halaman sudah Mentok", Toast.LENGTH_SHORT).show();
                }
                else if (page >= 1){
                    page--;
                    if(SignFilter){
                        GetShowFilterProductList(page, 1);
                    }
                    else {
                        GetshowProductList(page, 1);
                    }
                }
            }
        });

        nextbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                page++;
                if(SignFilter){
                    GetShowFilterProductList(page, 1);
                }
                else {
                    GetshowProductList(page, 1);
                }
            }
        });

        apllybtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GetShowFilterProductList(page, 1);
                SignFilter = true;
                edtfilterproduct.setText("" + 1);
                Toast.makeText(MainActivity.this, "Filter Sukses", Toast.LENGTH_SHORT).show();
            }
        });

        clearebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GetshowProductList(page, 1);
                edtfilterproduct.setText("" + 1);
                SignFilter = false;
                Toast.makeText(MainActivity.this, "Filter clear", Toast.LENGTH_SHORT).show();
            }
        });

    }

    public void GetshowProductList(int page, int pageSize){
        Response.Listener<String> stringListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    PList.clear();
                    JSONArray jsonArray = new JSONArray(response);
                    Type productlistType = new TypeToken<ArrayList<Product>>(){}.getType();
                    PList = gson.fromJson(response, productlistType);
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
                Toast.makeText(MainActivity.this, error.toString(), Toast.LENGTH_SHORT).show();
            }
        };
        RequestQueue queue = Volley.newRequestQueue(MainActivity.this);
        queue.add(RequestFactory.getPage("product", page, pageSize, stringListener, errorListener));
    }

    public void GetShowFilterProductList(int page, int pageSize){
        String filteredname = filterName.getText().toString();
        int minP;
        int maxP;
        if (filterlowestprice.getText().toString().equals("")){
            minP = 0;
        }
        else {
            minP = Integer.valueOf(filterlowestprice.getText().toString());
        }

        if (filterhighestprice.getText().toString().equals("")){
            maxP = 0;
        }
        else {
            maxP = Integer.valueOf(filterhighestprice.getText().toString());
        }

        ProductCategory category = ProductCategory.AUTOMOTIVE;
        String productcategory = spinner.getSelectedItem().toString();
        for (ProductCategory p : ProductCategory.values()){
            if (p.toString().equals(productcategory)){
                category = p;
            }
        }

        Response.Listener<String> listener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    PList.clear();
                    JSONArray jsonArray = new JSONArray(response);
                    Type PListType = new TypeToken<ArrayList<Product>>(){}.getType();
                    PList = gson.fromJson(response, PListType);
                }catch (JSONException e){
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
                Toast.makeText(MainActivity.this, "ERROR", Toast.LENGTH_SHORT).show();
            }
        };
        FilterRequest filterRequest;
        if (maxP == 0 && minP == 0){
            filterRequest = new FilterRequest(page, pageSize, account.id, filteredname, category, listener, errorListener);
        }
        else if(maxP == 0){
            filterRequest = new FilterRequest(page, pageSize, account.id, minP, filteredname, category, listener, errorListener);
        }
        else if(minP == 0){
            filterRequest = new FilterRequest(filteredname, page, account.id, maxP, category, listener, errorListener);
        }
        else {
            filterRequest = new FilterRequest(page, account.id, filteredname, minP, maxP, category, listener, errorListener);
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
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.jmart_android_menu, menu);
//        return super.onCreateOptionsMenu(menu);
//        MenuItem search = menu.findItem(R.id.search);
        MenuItem addMenu = menu.findItem(R.id.addbox);
        if(account.store == null){
            addMenu.setVisible(false);
        }

//        SearchView searchView = (SearchView) search.getActionView();
//        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
//            @Override
//            public boolean onQueryTextSubmit(String query) {
//                return false;
//            }
//
//            @Override
//            public boolean onQueryTextChange(String newText) {
////                customAdapter.getFilter().filter(newText);
//                return true;
//            }
//        });
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        setMode(item.getItemId());
        return super.onOptionsItemSelected(item);
    }

    public void setMode(int selectedMode){
        switch (selectedMode){
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
}