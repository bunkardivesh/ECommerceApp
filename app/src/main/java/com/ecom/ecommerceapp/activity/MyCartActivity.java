package com.ecom.ecommerceapp.activity;


import android.app.Dialog;

import android.content.Intent;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.os.Bundle;
import androidx.cardview.widget.CardView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.View;
import android.widget.Button;

import com.ecom.ecommerceapp.R;
import com.ecom.ecommerceapp.adapter.MyCartAdapter;
import com.ecom.ecommerceapp.model.CartData;
import com.ecom.ecommerceapp.utility.SQLiteHandler;
import java.util.ArrayList;


public class MyCartActivity extends AppCompatActivity {

    private Toolbar mToolbar;
    Button btnSaveOrder;
    RecyclerView cartrv;
    SQLiteHandler handler;
    ArrayList<CartData> list;
    double TotalAmount=0;
    CardView card2;
    ConstraintLayout mycartlayout;
    Button btnfinish;
    public final int CATEGORY_ID =0;
    Dialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_cart);

        initViews();
        getdata();

        btnSaveOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }

    public void initViews() {
        handler = new SQLiteHandler(this);
        mToolbar = (Toolbar) findViewById(R.id.home_toolbar);
        setSupportActionBar(mToolbar);
        mToolbar.setNavigationIcon(R.drawable.ic_chevron_left);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        btnSaveOrder = findViewById(R.id.btnSaveOrder);
        cartrv = findViewById(R.id.cart_items_rv);
        setUpRecyclerView();

        card2 = findViewById(R.id.cardView7);
        mycartlayout = findViewById(R.id.mycartlayout);
        btnfinish = findViewById(R.id.btnfinish);


    }

    public void setUpRecyclerView()
    {
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL,false);
        cartrv.setLayoutManager(layoutManager);
        cartrv.setNestedScrollingEnabled(false);
    }

    public void getdata()
    {

        list = new ArrayList<>();
        list = handler.getAllData();
        if (list.size()!=0) {
            MyCartAdapter adapter = new MyCartAdapter(this, list, new MyCartAdapter.ClickListener() {
                @Override
                public void onclick(int position, double amount) {
                    TotalAmount = TotalAmount - amount;
                    setTotalAmount(TotalAmount);
                    if (list != null) {
                        String qty = list.get(position).getQuantity();
                        if (amount < 0) {
                            list.get(position).setQuantity(String.valueOf(Integer.valueOf(qty) + 1));
                        } else {
                            list.get(position).setQuantity(String.valueOf(Integer.valueOf(qty) - 1));
                        }
                    }else {
                        setResult(RESULT_OK);
                        finish();
                    }
                }
            });
            cartrv.setAdapter(adapter);

            for (CartData data : list) {
                double qty = Double.valueOf(data.getQuantity());
                double price = Double.valueOf(data.getPrice());
                double total = qty * price;
                TotalAmount += total;
            }
            setTotalAmount(TotalAmount);
        }else {
            btnSaveOrder.setVisibility(View.GONE);
            card2.setVisibility(View.GONE);
            btnfinish.setVisibility(View.VISIBLE);
            mycartlayout.setBackground(getResources().getDrawable(R.drawable.emptycart_bg));

            btnfinish.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    setResult(RESULT_OK);
                    finish();
                }
            });
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        setResult(RESULT_CANCELED);
        finish();
    }
    public void setTotalAmount(double amount)
    {
        btnSaveOrder.setText("Checkout( \u20B9 "+ amount +")");
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode==RESULT_CANCELED){

        }else {
            setResult(RESULT_OK);
            finish();
        }
    }

}
