package hms.ewon.sample.m2websample;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class Dashboard extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
    }

    private void initLayout(){
        FragmentManager fm = getSupportFragmentManager();
        fm.beginTransaction()
                .replace(R.id.dashboard_container, new MainDashboard())
                .commit();
    }
}
