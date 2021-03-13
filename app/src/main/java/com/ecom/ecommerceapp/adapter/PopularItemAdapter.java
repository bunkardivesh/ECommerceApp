package com.ecom.ecommerceapp.adapter;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.ecom.ecommerceapp.R;
import com.ecom.ecommerceapp.model.Product;
import com.ecom.ecommerceapp.utility.RecyclerViewClickListener;
import com.squareup.picasso.Picasso;

import java.util.List;

public class PopularItemAdapter extends RecyclerView.Adapter<PopularItemAdapter.ItemHolder>
{
    private Context context;
    private List<Product> mList;
    RecyclerViewClickListener listener;

    public PopularItemAdapter(Context context, List<Product> mList,RecyclerViewClickListener listener) {
        this.context = context;
        this.mList = mList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public PopularItemAdapter.ItemHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
       View view = LayoutInflater.from(context).inflate(R.layout.category_items_layout,parent,false);

        return new ItemHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemHolder holder, final int position) {

        ImageView img = holder.productImage;
        TextView textName = holder.productName;

        Product item = mList.get(position);

        textName.setText(item.getName());

        String url = item.getImgurl();
        if (!url.isEmpty()){
            Picasso.get().load(url).placeholder(R.drawable.noimageavailable)
            .error(R.drawable.noimageavailable)
            .into(img);
        }

        img.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.OnClick(view,position);
            }
        });

    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public class ItemHolder extends RecyclerView.ViewHolder implements OnClickListener {
        ImageView productImage;
        TextView productName;

        private ItemHolder(View itemView) {
            super(itemView);

            productImage = (ImageView) itemView.findViewById(R.id.productImage);
            productName = (TextView) itemView.findViewById(R.id.productName);

        }

        @Override
        public void onClick(View view) {

        }
    }


}
