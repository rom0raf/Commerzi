package com.commerzi.app;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

public class HomeActivity extends AppCompatActivity {
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_page);

        ViewPager2 pager = findViewById(R.id.home_viewpager);
        TabLayout tabManager = findViewById(R.id.tab_layout);

        pager.setAdapter(new FragmentAdapter(this)) ;

        String[] tabTitles = {getString(R.string.route_tab),
                getString(R.string.client_tab)};

        new TabLayoutMediator(tabManager, pager,
                (tab, position) -> tab.setText(tabTitles[position])
        ).attach();
    }
}
