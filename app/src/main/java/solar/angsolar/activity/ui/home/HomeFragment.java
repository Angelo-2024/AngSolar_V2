package solar.angsolar.activity.ui.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import solar.angsolar.R;
import solar.angsolar.angsolar.db.DatabaseManager;
import solar.angsolar.databinding.FragmentHomeBinding;

public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        HomeViewModel homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        final Button buttonNavigateToFunction = binding.funcionButtom;
        buttonNavigateToFunction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(v).navigate(R.id.screenFragment);
            }
        });

        // Configurar el botón de navegación
        final Button consejoButton = binding.consejoButtom;
        consejoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(v).navigate(R.id.beneficiosFragment); // verifica layout
            }
        });

        // Botón para navegar a fragment_estadisticas.xml

        final Button estadisticaButton = binding.estadisticaButtom;
        estadisticaButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(v).navigate(R.id.estadisticasFragment);
            }
        });

        // Conectar el botón de registro a RegistroFragment

        final Button buttonNavigateToRegistro = binding.registroButtom;
        buttonNavigateToRegistro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(v).navigate(R.id.registroFragment);
            }
        });

        // Obtener el TextView para mostrar el contador de registros

        final TextView registroContador = binding.registroContador;

        // Obtener los contadores de registros

        DatabaseManager databaseManager = new DatabaseManager(getContext());
        int cantidadConsumos = databaseManager.obtenerCantidadRegistrosConsumo();
        int cantidadFacturas = databaseManager.obtenerCantidadRegistrosFactura();

        // Mostrar los contadores en el TextView

        String textoContador = "RC: " + cantidadConsumos + "\nRF: " + cantidadFacturas;
        registroContador.setText(textoContador);


        return root;
    }



    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
