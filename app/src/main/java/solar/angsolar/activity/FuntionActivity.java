package solar.angsolar.activity;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.FragmentTransaction;

import solar.angsolar.R;
import solar.angsolar.activity.ui.function.FunctionFragment;
import solar.angsolar.activity.ui.home.HomeFragment;


public class FuntionActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_funtion);

        if (savedInstanceState == null) {
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            FunctionFragment fragment = new FunctionFragment();
            transaction.replace(R.id.fragment_container, fragment);

            transaction.commit();
        }
    }
}