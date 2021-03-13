package com.ecom.ecommerceapp.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.ecom.ecommerceapp.R;
import com.ecom.ecommerceapp.activity.MyCartActivity;
import com.ecom.ecommerceapp.model.CartData;
import com.ecom.ecommerceapp.utility.SQLiteHandler;

import java.util.List;

public class MyCartAdapter extends RecyclerView.Adapter<MyCartAdapter.ItemHolder>
{
    private Context context;
    private List<CartData> mList;
    private static int n=1;
    SQLiteHandler sqLiteHandler;
    ConstraintLayout mLayout;
    static int QTY;
    private int quantity;

    public interface ClickListener{
        void onclick(int position,double amount);
    }

    ClickListener listener;

    public MyCartAdapter(Context context, List<CartData> mList,ClickListener listener) {
        this.context = context;
        this.mList = mList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public MyCartAdapter.ItemHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
       View view = LayoutInflater.from(context).inflate(R.layout.cart_item_layout,parent,false);

        return new ItemHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemHolder holder, final int position) {
        final int pos = position;

        final ImageView img = holder.productImage;
        final TextView textName = holder.productName;
        final TextView price = holder.price;
        ImageButton btnremove = holder.btnremove;
        final TextView qtytext = holder.qtytv;
        ImageButton plus = holder.plus;
        final ImageButton minus = holder.minus;

        final CartData items = mList.get(position);

        byte[] image = items.getPimage();
        Bitmap bmp = BitmapFactory.decodeByteArray(image, 0, image.length);
        Glide.with(context).load(bmp).apply(options).into(img);

           Double totalamnt = Double.valueOf(items.getPrice()) * Double.valueOf(items.getQuantity());

            textName.setText(items.getItemname());
            price.setText(context.getString(R.string.rs)+" "+items.getPrice());
            holder.totalAmnt.setText(context.getString(R.string.rs)+" "+totalamnt);
            qtytext.setText(items.getQuantity());
            holder.QTYTV.setText(items.getQuantity());

            //*********************for quantity selection
            quantity = Integer.valueOf(items.getQuantity());
            if (quantity == 1)
                minus.setEnabled(false);
            final double mPrice = Double.valueOf(items.getPrice());
            plus.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    quantity = Integer.valueOf(items.getQuantity());
                    minus.setEnabled(true);
                    quantity++;
                    qtytext.setText(String.valueOf(quantity));
                    listener.onclick(position, (mPrice * (-1)));
                    items.setQuantity(String.valueOf(quantity));
                    sqLiteHandler = new SQLiteHandler(context);
                    sqLiteHandler.UpdateQty(textName.getText().toString(), String.valueOf(quantity));
                }

            });
            minus.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    quantity = Integer.valueOf(items.getQuantity());
                    quantity--;
                    qtytext.setText(String.valueOf(quantity));
                    listener.onclick(position, (mPrice));
                    items.setQuantity(String.valueOf(quantity));
                    sqLiteHandler = new SQLiteHandler(context);
                    sqLiteHandler.UpdateQty(textName.getText().toString(), String.valueOf(quantity));
                    if (quantity == 1)
                        minus.setEnabled(false);
                }
            });
//**********************


            QTY = Integer.valueOf(items.getQuantity());


            btnremove.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    sqLiteHandler = new SQLiteHandler(context);
                    String value = sqLiteHandler.DeleteData(items.getItemname());
                    notifyDataSetChanged();
                    double qty = Double.valueOf(qtytext.getText().toString());
                    double price = Double.valueOf(items.getPrice());
                    listener.onclick(position, (qty * price));
                    mList.remove(pos);

                    if (mList.size()==0){
                        ((MyCartActivity)context).getdata();
                    }
                }
            });
        }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public class ItemHolder extends RecyclerView.ViewHolder {
        ImageView productImage;
        TextView productName,price,qtytv,totalAmnt;
        ImageButton btnremove,plus,minus;
        TextView QTYTV;

        private ItemHolder(View itemView) {
            super(itemView);
            mLayout = itemView.findViewById(R.id.cartLayout);
            productImage = (ImageView) itemView.findViewById(R.id.cart_product_image);//
            productName = (TextView) itemView.findViewById(R.id.cart_product_name);//
            btnremove = (ImageButton) itemView.findViewById(R.id.btn_removefromcart);//
            qtytv = (TextView) itemView.findViewById(R.id.cart_select_quantity);//
            plus = (ImageButton) itemView.findViewById(R.id.cart_btnplus);//
            minus = (ImageButton) itemView.findViewById(R.id.cart_btnminus);//
            QTYTV = (TextView) itemView.findViewById(R.id.cart_quantity);//
            price = (TextView) itemView.findViewById(R.id.cart_price);//
            totalAmnt = (TextView) itemView.findViewById(R.id.cart_amount);

        }
    }

    private RequestOptions options = new RequestOptions()
            .centerCrop()
            .placeholder(R.drawable.noimageavailable)
            .error(R.drawable.noimageavailable)
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .priority(Priority.HIGH);
}
