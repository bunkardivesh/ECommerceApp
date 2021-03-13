package com.ecom.ecommerceapp.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.ecom.ecommerceapp.R;
import com.ecom.ecommerceapp.activity.ItemDetailActivity;
import com.ecom.ecommerceapp.model.Product;
import com.ecom.ecommerceapp.utility.MakeSnakBar;
import com.ecom.ecommerceapp.utility.RecyclerViewClickListener;
import com.ecom.ecommerceapp.utility.SQLiteHandler;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.util.List;

public class AllProductsAdapter extends RecyclerView.Adapter<AllProductsAdapter.ItemHolder>
{
    private Context context;
    private List<Product> mList;
    RecyclerViewClickListener listener;
    private int n=1;
    SQLiteHandler sqLiteHandler;
    View mLayout;
    int quantity;

    public AllProductsAdapter(Context context, List<Product> mList,RecyclerViewClickListener listener,View mLayout) {
        this.context = context;
        this.mList = mList;
        this.listener = listener;
        this.mLayout = mLayout;
    }

    @NonNull
    @Override
    public AllProductsAdapter.ItemHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
       View view = LayoutInflater.from(context).inflate(R.layout.single_item_grid_layout,parent,false);

        return new ItemHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemHolder holder, final int position) {
        final int pos = position;

        final ImageView img = holder.productImage;
        final TextView textName = holder.productName;
        final Button addToCart = holder.btnAddToCart;
        final TextView qtytext = holder.qtytv;
        final TextView productPrice = holder.price;
        final ImageButton minus = holder.minus;

        final Product items = mList.get(position);

        textName.setText(items.getName());
        productPrice.setText(context.getString(R.string.rs)+" "+items.getPrice());
        if (quantity==1)
            minus.setEnabled(false);


        String url = items.getImgurl();
        if (!url.isEmpty()){
            Picasso.get().load(url).placeholder(R.drawable.noimageavailable)
                    .error(R.drawable.noimageavailable)
                    .into(img);
        }



        addToCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sqLiteHandler = new SQLiteHandler(context);
                String saveitem = textName.getText().toString();
                Boolean as = sqLiteHandler.CheckIfNameExist(saveitem);

                if (as){
                    MakeSnakBar.makeSnakbar("Item Already Exists in your Cart",R.color.color_red,mLayout,context);
                }else {

                    String itemName = items.getName();
                    String price = String.valueOf(items.getPrice());
                    String qty = qtytext.getText().toString();
                    String desc = items.getDesc();

                    Bitmap src = ((BitmapDrawable) img.getDrawable()).getBitmap();
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();

                    src.compress(Bitmap.CompressFormat.PNG, 100, baos);
                    byte[] pimage = baos.toByteArray();
                    sqLiteHandler = new SQLiteHandler(context);
                    boolean isInserted = sqLiteHandler.addtoCart(itemName,price,qty,desc,pimage);
                    if (isInserted == true) {
                        MakeSnakBar.makeSnakbar("Item Added to Cart",R.color.color_green,mLayout,context);

                        String id = String.valueOf(sqLiteHandler.getProfilesCount());
                        Log.i("Name", id);
                        listener.OnClick(view,position);
                    }
                }

            }
        });

        holder.productImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ItemDetailActivity.class);
                intent.putExtra("object",items);
                context.startActivity(intent);
            }
        });


    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public class ItemHolder extends RecyclerView.ViewHolder {
        ImageView productImage;
        TextView productName,price,qtytv;
        ImageButton plus,minus;
        Button btnAddToCart;

        private ItemHolder(View itemView) {
            super(itemView);
            productImage = (ImageView) itemView.findViewById(R.id.single_item_image);
            productName = (TextView) itemView.findViewById(R.id.single_item_name);
            price = (TextView) itemView.findViewById(R.id.single_item_price);
            btnAddToCart = (Button) itemView.findViewById(R.id.single_button_add);
            qtytv = (TextView) itemView.findViewById(R.id.cart_select_quantity);
            plus = (ImageButton) itemView.findViewById(R.id.cart_btnplus);
            minus = (ImageButton) itemView.findViewById(R.id.btnminus);

            quantity =Integer.valueOf(qtytv.getText().toString());
            plus.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    quantity =Integer.valueOf(qtytv.getText().toString());
                    minus.setEnabled(true);
                    quantity++;
                    qtytv.setText(String.valueOf(quantity));
                }

            });
            minus.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    quantity =Integer.valueOf(qtytv.getText().toString());
                    quantity--;
                    qtytv.setText(String.valueOf(quantity));
                    if (quantity==1)
                        minus.setEnabled(false);
                }
            });

        }

    }
}
