package solar.angsolar.activity.ui.function;

import static solar.angsolar.R.id.action_screenFragment_to_calcFragment;

import androidx.lifecycle.ViewModelProvider;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import androidx.navigation.Navigation;
import solar.angsolar.R;

public class ScreenFragment extends Fragment {

    private ScreenViewModel mViewModel;

    public static ScreenFragment newInstance() {
        return new ScreenFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_screen, container, false);

        Button navigateButton = root.findViewById(R.id.button_navigate_to_function);
        navigateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(v).navigate(R.id.action_screenFragment_to_functionFragment);
            }
        });


        Button navigateButtonDos = root.findViewById(R.id.button_navigate_to_consumer);
        navigateButtonDos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(v).navigate(action_screenFragment_to_calcFragment);
            }
        });

        return root;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(ScreenViewModel.class);
    }
}
