package com.ecom.ecommerceapp;

import android.app.Dialog;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.PersistableBundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.ecom.ecommerceapp.activity.AllProductsActivity;
import com.ecom.ecommerceapp.activity.ItemDetailActivity;
import com.ecom.ecommerceapp.activity.MyCartActivity;
import com.ecom.ecommerceapp.adapter.CategoryAdapter;
import com.ecom.ecommerceapp.adapter.MyPagerAdapter;
import com.ecom.ecommerceapp.adapter.PopularItemAdapter;
import com.ecom.ecommerceapp.model.BannerList;
import com.ecom.ecommerceapp.model.Category;
import com.ecom.ecommerceapp.model.Product;
import com.ecom.ecommerceapp.utility.BadgeCount;
import com.ecom.ecommerceapp.utility.DemoData;
import com.ecom.ecommerceapp.utility.RecyclerViewClickListener;
import com.ecom.ecommerceapp.utility.SQLiteHandler;
import com.ecom.ecommerceapp.utility.Utility;
import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;


public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private ViewPager viewPager;
    private MyPagerAdapter myPagerAdapter;
    private int currentPage = 0;
    private Toolbar mToolbar;
    public DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private View headerView;
    private CircleImageView profile;
    private TextView guestname;
    private ActionBarDrawerToggle actionBarDrawerToggle;

    private List<Category> categoryList;
    private TextView seeAll;
    private RecyclerView popularItems,categoryrv;
    private PopularItemAdapter popularItemAdapter;
    private ProgressBar progressBar,progressBar2;
    private CategoryAdapter categoryAdapter;
    private SQLiteHandler handler;
    public Menu menu;
    private List<Product> productList;
    private SearchView searchView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initviews();
        setUpRecyclerView();
        setBanner();
        setCategoryItems();
        getPopularItems();
        seeAll.setOnClickListener(this);

    }
    public void initviews()
    {
        handler = new SQLiteHandler(this);
        viewPager = findViewById(R.id.banner_display);
        mToolbar = (Toolbar) findViewById(R.id.home_toolbar);
        setUpToolBar();
        drawerLayout = findViewById(R.id.drawer);
        navigationView = findViewById(R.id.side_navigation);
        progressBar = findViewById(R.id.progressBar);
        progressBar2 = findViewById(R.id.progressBar2);

        headerView = navigationView.getHeaderView(0);
        profile = headerView.findViewById(R.id.profile);
        guestname = headerView.findViewById(R.id.hname);

        popularItems = findViewById(R.id.all_products);
        popularItems.setNestedScrollingEnabled(false);
        categoryrv = findViewById(R.id.category_rv);
        categoryrv.setNestedScrollingEnabled(false);

        seeAll = findViewById(R.id.seealltext);

        actionBarDrawerToggle = new ActionBarDrawerToggle(this,drawerLayout,mToolbar,R.string.drawer_open,R.string.drawer_close);
        actionBarDrawerToggle.setDrawerIndicatorEnabled(true);
        actionBarDrawerToggle.syncState();
        actionBarDrawerToggle.getDrawerArrowDrawable().setColor(getResources().getColor(R.color.white));

        searchView = (SearchView) findViewById(R.id.search_view_home);
        searchView.setIconifiedByDefault(false);

    }
    public void setUpToolBar()
    {
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void setCategoryItems() {

        prepareCategoryList();
        categoryAdapter = new CategoryAdapter(MainActivity.this, categoryList, new RecyclerViewClickListener() {
            @Override
            public void OnClick(View view, int position) {
              //  openDialog(categoryList,position);
                String category = categoryList.get(position).getCategory_id();
                Intent in = new Intent(MainActivity.this, AllProductsActivity.class);
                in.putExtra("category",category);
                startActivityForResult(in,11);
            }
        });
        categoryrv.setAdapter(categoryAdapter);
        progressBar2.setVisibility(View.GONE);

    }

    private void prepareCategoryList() {

        categoryList = new ArrayList<>();
        categoryList.add(new Category("Electronics","file:///android_asset/Electronics.jpg","1"));
        categoryList.add(new Category("Ayurveda","file:///android_asset/Ayurveda_1.png","2"));
        categoryList.add(new Category("Grocery","file:///android_asset/grocery.png","3"));
        categoryList.add(new Category("Furniture","file:///android_asset/furniture.png","4"));
        categoryList.add(new Category("Clothing","file:///android_asset/fashion.jpg","5"));
        categoryList.add(new Category("Chocolates","file:///android_asset/chocolate.png","6"));

    }


    public void setUpRecyclerView()
    {
        int nuOfColumns = Utility.calculateNoOfColumns(this,216);
        RecyclerView.LayoutManager ItemLayoutOne = new GridLayoutManager(this, nuOfColumns);
        popularItems.setLayoutManager(ItemLayoutOne);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL,false);
        categoryrv.setLayoutManager(layoutManager);

    }
    public void setBanner()     //set slider
    {
       new SetBannerTask().execute();
    }


    public void getPopularItems()       //get popular items to show
    {
        progressBar.setVisibility(View.VISIBLE);

        productList = new ArrayList<>();
        productList = DemoData.getProductsList(this,"popularItems");

        popularItemAdapter = new PopularItemAdapter(MainActivity.this, productList, new RecyclerViewClickListener() {
            @Override
            public void OnClick(View view, int position) {
                Product product = productList.get(position);
                Intent intent = new Intent(MainActivity.this, ItemDetailActivity.class);
                intent.putExtra("object",product);
                startActivityForResult(intent,11);
            }
        });
        popularItems.setAdapter(popularItemAdapter);

        progressBar.setVisibility(View.GONE);
    }


    @Override
    public void onClick(View view) {
        switch (view.getId())
        {
            case R.id.seealltext:
               /* Intent in = new Intent(MainActivity.this, AllProductsActivity.class);
                in.putExtra("category","General");
                startActivityForResult(in,11);*/
                break;
        }
    }
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        BadgeCount.setCount(MainActivity.this,String.valueOf(handler.getProfilesCount()),menu);
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode==11)
        {
            invalidateOptionsMenu();
        }
    }

    private class SetBannerTask extends AsyncTask<Call,Void,List<BannerList>>{

            List<BannerList> list;
            @Override
            protected List<BannerList> doInBackground(Call... calls) {

                list = new ArrayList<>();
                list.add(new BannerList(("file:///android_asset/banner_1.png")));
                list.add(new BannerList(("file:///android_asset/banner_2.png")));
                list.add(new BannerList(("file:///android_asset/banner_3.png")));
                list.add(new BannerList(("file:///android_asset/banner_4.png")));
                list.add(new BannerList(("file:///android_asset/banner_5.png")));


                //below code is for calling api to get data from server
/*                Call<List<BannerList>> call = mService.banners();
                try {
                   list =  call.execute().body();
                } catch (IOException e) {
                    e.printStackTrace();
                }*/

                return list;
            }

            @Override
            protected void onPostExecute(final List<BannerList> list) {
                super.onPostExecute(list);

                if (list!=null)
                {
                    myPagerAdapter = new MyPagerAdapter(MainActivity.this, list);
                    viewPager.setAdapter(myPagerAdapter);
                    viewPager.setCurrentItem(0);
                    final Handler handler = new Handler();
                    final Runnable Update = new Runnable() {
                        public void run() {
                            if (currentPage == list.size()) {
                                currentPage = 0;
                            }
                            viewPager.setCurrentItem(currentPage++, true);
                        }
                    };
                    Timer swipeTimer = new Timer();
                    swipeTimer.schedule(new TimerTask() {
                        @Override
                        public void run() {
                            handler.post(Update);
                        }
                    }, 2000, 4000);

                }
            }
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
        // ActionBarDrawerToggle will take care of this.
        if (actionBarDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        switch (item.getItemId())
        {
            case R.id.navigation_cart:
                Intent intent = new Intent(MainActivity.this, MyCartActivity.class);
                startActivityForResult(intent,11);
                break;
        }

        return true;
    }
    @Override
    public void onPostCreate(@Nullable Bundle savedInstanceState, @Nullable PersistableBundle persistentState) {
        super.onPostCreate(savedInstanceState, persistentState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        if (actionBarDrawerToggle != null) {
            actionBarDrawerToggle.syncState();
        }
    }
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        actionBarDrawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    protected void onResume() {
        super.onResume();
        searchView.clearFocus();
    }
}
