package com.ecom.ecommerceapp.utility;

import android.content.Context;

import com.ecom.ecommerceapp.model.Product;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class DemoData {


    public static List<Product> getProductsList(Context context,String query)
    {
        List<Product> productList = new ArrayList<>();

        try {
            JSONObject obj = new JSONObject(loadJSONFromAsset(context));
            JSONArray m_jArry = obj.getJSONArray(query);

            for (int i = 0; i < m_jArry.length(); i++) {

                Product product = new Product();
                JSONObject jobject = m_jArry.getJSONObject(i);
                product.setName(jobject.getString("name"));
                product.setImgurl(jobject.getString("imgurl"));
                product.setDesc(jobject.getString("desc"));
                product.setPrice(jobject.getString("price"));
                product.setCategory(jobject.getString("category"));
                //Add your values in your `ArrayList` as below:

                productList.add(product);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return productList;

    }

    public static String loadJSONFromAsset(Context context) {
        String json = null;
        try {
            InputStream is = context.getAssets().open("products.json");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return json;
    }

}
