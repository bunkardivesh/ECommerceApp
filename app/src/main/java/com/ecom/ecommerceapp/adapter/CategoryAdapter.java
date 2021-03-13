package com.ecom.ecommerceapp.adapter;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.ecom.ecommerceapp.R;
import com.ecom.ecommerceapp.model.Category;
import com.ecom.ecommerceapp.utility.RecyclerViewClickListener;
import com.squareup.picasso.Picasso;

import java.util.List;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.ItemHolder> {

    private Context context;
    private List<Category> categoryList;
    RecyclerViewClickListener listener;

    public CategoryAdapter(Context context, List<Category> categoryList,RecyclerViewClickListener listener) {
        this.context = context;
        this.categoryList = categoryList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public CategoryAdapter.ItemHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.category_items_layout,parent,false);
        return new ItemHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemHolder holder, final int position) {
        ImageView img = holder.productImage;
        TextView textName = holder.productName;

        Category category = categoryList.get(position);

        textName.setText(category.getCategory_name());

        img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.OnClick(view,position);
            }
        });

        Picasso.get().load(category.getImage()).placeholder(R.drawable.noimageavailable)
                .error(R.drawable.noimageavailable)
                .into(img);
    }

    @Override
    public int getItemCount() {
        return categoryList.size();
    }

    public class ItemHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
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
