package com.ecom.ecommerceapp.activity;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.ecom.ecommerceapp.R;
import com.ecom.ecommerceapp.model.Product;
import com.ecom.ecommerceapp.utility.BadgeCount;
import com.ecom.ecommerceapp.utility.MakeSnakBar;
import com.ecom.ecommerceapp.utility.SQLiteHandler;
import com.squareup.picasso.Picasso;


import java.io.ByteArrayOutputStream;

public class ItemDetailActivity extends AppCompatActivity{

    private Toolbar mToolbar;
    Product product;
    TextView productName, price, description, qtyTextTV;
    Button addtocart;
    ImageButton plus,minus;
    ImageView productImage;
    public Menu menu;
    SQLiteHandler handler;
    private static int n=1;
    ConstraintLayout mLayout;
    int quantity;
    public final int CATEGORY_ID =0;
    Dialog dialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_details);
        initViews();
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        product = bundle.getParcelable("object");
        setValues(product);



        quantity =Integer.valueOf(qtyTextTV.getText().toString());
        plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                quantity =Integer.valueOf(qtyTextTV.getText().toString());
                minus.setEnabled(true);
                quantity++;
                qtyTextTV.setText(String.valueOf(quantity));
            }

        });
        minus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                quantity =Integer.valueOf(qtyTextTV.getText().toString());
                quantity--;
                qtyTextTV.setText(String.valueOf(quantity));
                if (quantity==1)
                    minus.setEnabled(false);
            }
        });


        addtocart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (view.getId()){

                    case R.id.btnaddtocart:
                        handler = new SQLiteHandler(ItemDetailActivity.this);
                        String saveitem = productName.getText().toString();
                        Boolean as = handler.CheckIfNameExist(saveitem);

                        if (as){
                            MakeSnakBar.makeSnakbar("Item Already Exists in your Cart",R.color.color_red,mLayout,ItemDetailActivity.this);
                        }else {

                            String itemName = product.getName();
                            String price = String.valueOf(product.getPrice());
                            String desc = product.getDesc();
                            String qty = qtyTextTV.getText().toString();

                            Bitmap src = ((BitmapDrawable) productImage.getDrawable()).getBitmap();
                            ByteArrayOutputStream baos = new ByteArrayOutputStream();

                            src.compress(Bitmap.CompressFormat.PNG, 100, baos);
                            byte[] pimage = baos.toByteArray();
                            handler = new SQLiteHandler(ItemDetailActivity.this);
                            boolean isInserted = handler.addtoCart(itemName,price,qty,desc,pimage);
                            if (isInserted == true) {
                                MakeSnakBar.makeSnakbar("Item Added to Cart",R.color.color_green,mLayout,ItemDetailActivity.this);
                                String id = String.valueOf(handler.getProfilesCount());
                                Log.i("Name", id);
                                BadgeCount.setCount(ItemDetailActivity.this,id,menu);
                            }
                        }

                        break;
                }
            }
        });
    }
    public void initViews()
    {
        mLayout = findViewById(R.id.content_layout);
        handler = new SQLiteHandler(this);
        mToolbar = findViewById(R.id.home_toolbar);
        setSupportActionBar(mToolbar);
        mToolbar.setNavigationIcon(R.drawable.ic_chevron_left);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        productImage = findViewById(R.id.itemdetail_image);
        productName = findViewById(R.id.itemdetail_name);
        price = findViewById(R.id.itemdetail_price);
        description = findViewById(R.id.itemdetail_desc);
        addtocart = findViewById(R.id.btnaddtocart);
        qtyTextTV = findViewById(R.id.cart_select_quantity);
        plus = findViewById(R.id.cart_btnplus);
        minus = findViewById(R.id.btnminus);


    }
    public void setValues(Product product)
    {
        productName.setText(product.getName());
        price.setText(getString(R.string.rs)+" "+String.valueOf(product.getPrice()));
        description.setText(product.getDesc());

        String url = product.getImgurl();
        Picasso.get().load(url).placeholder(R.drawable.noimageavailable)
                .error(R.drawable.noimageavailable)
                .into(productImage);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        this.menu = menu;
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId())
        {
            case R.id.navigation_cart:
                Intent intent = new Intent(ItemDetailActivity.this, MyCartActivity.class);
                startActivityForResult(intent,11);
                break;
        }

        return true;
    }
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        BadgeCount.setCount(ItemDetailActivity.this,String.valueOf(handler.getProfilesCount()),menu);
        return true;
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
    }
}
