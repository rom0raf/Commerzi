package com.commerzi.app;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

public class HomeActivity extends AppCompatActivity {
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_page);

        ViewPager2 pager = findViewById(R.id.home_viewpager);
        TabLayout tabLayout = findViewById(R.id.tab_layout);

        pager.setAdapter(new FragmentAdapter(this)) ;

        new TabLayoutMediator(tabLayout, pager, (tab, position) -> {
            View customView = LayoutInflater.from(this).inflate(R.layout.custom_tab, null);

            ImageView icon = customView.findViewById(R.id.tab_icon);
            TextView title = customView.findViewById(R.id.tab_title);

            switch (position) {
                case 0:
                    icon.setImageResource(R.drawable.ic_route);
                    title.setText(getString(R.string.routes));
                    break;
                case 1:
                    icon.setImageResource(R.drawable.ic_company);
                    title.setText(getString(R.string.customers));
                    break;
                case 2:
                    icon.setImageResource(R.drawable.ic_avatar);
                    title.setText(getString(R.string.profile));
                    break;
            }

            tab.setCustomView(customView);
        }).attach();
    }
}
