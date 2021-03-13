package com.ecom.ecommerceapp.activity;

import android.annotation.SuppressLint;
import android.content.Intent;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.os.Bundle;

import androidx.recyclerview.widget.GridLayoutManager;

import androidx.recyclerview.widget.RecyclerView;


import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import com.ecom.ecommerceapp.R;
import com.ecom.ecommerceapp.adapter.AllProductsAdapter;
import com.ecom.ecommerceapp.model.DialogCalling;
import com.ecom.ecommerceapp.model.Product;
import com.ecom.ecommerceapp.utility.BadgeCount;
import com.ecom.ecommerceapp.utility.DemoData;
import com.ecom.ecommerceapp.utility.RecyclerViewClickListener;
import com.ecom.ecommerceapp.utility.SQLiteHandler;
import com.ecom.ecommerceapp.utility.Utility;

import java.util.ArrayList;

import java.util.List;

import dmax.dialog.SpotsDialog;

public class AllProductsActivity extends AppCompatActivity {

    private RecyclerView AllItemsRv;
    private Toolbar mToolbar;
    AllProductsAdapter adapter;
    public Menu menu;
    SpotsDialog progressBar;
    SQLiteHandler handler;
    ConstraintLayout mLayout;
    private int dataRow = 0;
    private GridLayoutManager gridLayoutManager;
    String searchquery;

    @SuppressLint("NewApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_products);

        initviews();
        setUpRecyclerView();
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        searchquery = bundle.getString("category");

        getAllProducts(searchquery);
    }

    public void initviews()
    {
        mLayout = findViewById(R.id.mLayout);
        handler = new SQLiteHandler(this);
        AllItemsRv = findViewById(R.id.allItems_rv);
        mToolbar = (Toolbar) findViewById(R.id.home_toolbar);
        progressBar = new SpotsDialog(this,R.style.Custom);

        setUpToolBar();
    }
    public void setUpToolBar()
    {

        setSupportActionBar(mToolbar);
        mToolbar.setNavigationIcon(R.drawable.ic_chevron_left);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

    }

    public void setUpRecyclerView()
    {
        int nuOfColumns = Utility.calculateNoOfColumns(this,216);
        gridLayoutManager = new GridLayoutManager(this, nuOfColumns);
        AllItemsRv.setLayoutManager(gridLayoutManager);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        this.menu = menu;
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        BadgeCount.setCount(AllProductsActivity.this,String.valueOf(handler.getProfilesCount()),menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId())
        {
            case R.id.navigation_cart:
                Intent intent = new Intent(AllProductsActivity.this, MyCartActivity.class);
                startActivityForResult(intent,11);
                break;
        }

        return true;
    }

    public void getAllProducts(String query)
    {
            List<Product> productList = new ArrayList<>();
            List<Product> allProductsList = DemoData.getProductsList(this,"products");

            for (Product product :allProductsList)
            {
                if (product.getCategory().equals(query))
                {
                    productList.add(product);
                }
            }

            if (productList != null)
            {
                adapter = new AllProductsAdapter(AllProductsActivity.this, productList, new RecyclerViewClickListener() {
                    @Override
                    public void OnClick(View view, int position) {
                        BadgeCount.setCount(AllProductsActivity.this, String.valueOf(handler.getProfilesCount()), menu);
                    }
                }, mLayout);
                AllItemsRv.setAdapter(adapter);
                progressBar.hide();
            }else {
                progressBar.hide();
                DialogCalling.showFailedAlertBoxWithFinish(AllProductsActivity.this,"Server Error!");

            }


    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        setResult(RESULT_OK);
        finish();
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode==11)
        {
            invalidateOptionsMenu();
        }
        if (resultCode==RESULT_OK){
            finish();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        invalidateOptionsMenu();
    }
}
