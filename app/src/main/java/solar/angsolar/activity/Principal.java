package solar.angsolar.activity;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import solar.angsolar.R;
import solar.angsolar.angsolar.db.DatabaseManager;

public class Principal extends Fragment {

    private Button btnRegistrar, btnEstadisticas, btnBeneficios;
    private TextView consejoTextView;
    private TextView cantidadRegistrosTextView;
    private DatabaseManager dbManager;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_principal, container, false);

        btnRegistrar = root.findViewById(R.id.btnRegistrar);
        btnEstadisticas = root.findViewById(R.id.btnEstadisticas);
        btnBeneficios = root.findViewById(R.id.btnBeneficios);

        consejoTextView = root.findViewById(R.id.consejoTextView);
        cantidadRegistrosTextView = root.findViewById(R.id.cantidadRegistrosTextView);

        NavController navController = NavHostFragment.findNavController(this);

        btnRegistrar.setOnClickListener(v -> {
            navController.navigate(R.id.action_principalFragment_to_registroFragment);
        });

        btnEstadisticas.setOnClickListener(v -> {
            navController.navigate(R.id.action_principalFragment_to_estadisticasFragment);
        });

        btnBeneficios.setOnClickListener(v -> {
            navController.navigate(R.id.action_principalFragment_to_beneficiosFragment);
        });

        // Inicializar DatabaseManager después de inflar la vista
        dbManager = new DatabaseManager(getContext());

        // Mostrar consejo aleatorio
        String consejo = dbManager.obtenerConsejoAleatorio();
        consejoTextView.setText(consejo);

        // Mostrar cantidad de registros en la tabla de consumo
        int cantidadRegistros = dbManager.contarRegistros("consumo");
        cantidadRegistrosTextView.setText("Cantidad de registros: " + cantidadRegistros);

        return root;
    }
}
