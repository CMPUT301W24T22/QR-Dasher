package com.example.qr_dasher;

import android.os.Bundle;
import android.view.View;

import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

public class ManageEvent extends FragmentActivity {
    //contains the event details
   /* private ViewPager2 viewPager;
    private FragmentStateAdapter pagerAdapter;
    public Event clickedEvent;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_event);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            clickedEvent = getIntent().getParcelableExtra("event");
        }




        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        viewPager = findViewById(R.id.event_view_pager);
        Event fake = new Event(null, null, null, null);
        pagerAdapter = new EventPagerAdapter(this, clickedEvent);
        viewPager.setAdapter(pagerAdapter);

        TabLayout tabLayout = findViewById(R.id.tab_layout);
        new TabLayoutMediator(tabLayout, viewPager, (tab, position) -> {
            switch (position) {
                case 0:
                    tab.setText("Overview");
                    break;
                case 1:
                    tab.setText("Announcements");
                    break;
                case 2:
                    tab.setText("Map");
                    break;
            }
        }).attach();



    }
}*/

}
