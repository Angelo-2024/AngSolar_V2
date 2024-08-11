package solar.angsolar.activity;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;
import solar.angsolar.R;
import solar.angsolar.activity.ui.home.HomeFragment;

public class HomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        if (savedInstanceState == null) {
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            HomeFragment fragment = new HomeFragment();
            transaction.replace(R.id.fragment_container_home, fragment);
            transaction.commit();
        }
    }
}

