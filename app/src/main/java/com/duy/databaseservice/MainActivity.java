package com.duy.databaseservice;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import com.duy.databaseservice.fragment.SectionsPagerAdapter;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        SectionsPagerAdapter sectionsPagerAdapter =
                new SectionsPagerAdapter(getSupportFragmentManager(), this);
        ViewPager viewPager = (ViewPager) findViewById(R.id.viewpager);
        viewPager.setOffscreenPageLimit(sectionsPagerAdapter.getCount());
        viewPager.setAdapter(sectionsPagerAdapter);
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tab);
        tabLayout.setupWithViewPager(viewPager);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

    }

/*

    private void syncPin() {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                for (int i = 3; i < 53; i++) {
                    String cmd = Protocol.GET + Protocol.PIN + i;
                    sendCommand(cmd);
                    SystemClock.sleep(100);
                }
            }
        });
        thread.start();
    }

*/


}
