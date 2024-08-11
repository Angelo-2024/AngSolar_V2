package solar.angsolar.activity.ui.seguimiento;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import solar.angsolar.R;
import solar.angsolar.angsolar.db.DatabaseManager;

public class BeneficiosFragment extends Fragment {
    private DatabaseManager dbManager;
    private TextView consejoTextView;
    private TextView cantidadRegistrosTextView;

    public BeneficiosFragment() {
        // constructor Vacio
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_beneficios, container, false);

        dbManager = new DatabaseManager(getContext());
        consejoTextView = view.findViewById(R.id.consejoTextView);
        cantidadRegistrosTextView = view.findViewById(R.id.cantidadRegistrosTextView);

        // consejo aleatorio
        String consejo = dbManager.obtenerConsejoAleatorio();
        consejoTextView.setText(consejo);

        // Mostrar cantidad de registros en la tabla de consumo  , manejo de aleatorios verificar función
        int cantidadRegistros = dbManager.contarRegistros("consumo");
        cantidadRegistrosTextView.setText("Cantidad de registros: " + cantidadRegistros);

        return view;
    }
}
