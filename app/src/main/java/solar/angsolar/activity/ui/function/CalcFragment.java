package solar.angsolar.activity.ui.function;

import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.HashMap;

import solar.angsolar.R;

public class CalcFragment extends Fragment {

    private CalcViewModel mViewModel;
    private Spinner spinnerAparato;
    private EditText editTextConsumo;
    private EditText editTextHoras;
    private EditText editTextEnergiaDia;
    private EditText editTextEnergiaMes;
    private EditText editTextEnergiaAnio;
    private Button buttonCalcular;
    private Button buttonReset;

    private HashMap<String, Double> aparatosConsumo = new HashMap<>();

    public static CalcFragment newInstance() {
        return new CalcFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_calc, container, false);

        spinnerAparato = root.findViewById(R.id.spinnerAparato);
        editTextConsumo = root.findViewById(R.id.editTextConsumo);
        editTextHoras = root.findViewById(R.id.editTextHoras);
        editTextEnergiaDia = root.findViewById(R.id.editTextEnergiaDia);
        editTextEnergiaMes = root.findViewById(R.id.editTextEnergiaMes);
        editTextEnergiaAnio = root.findViewById(R.id.editTextEnergiaAnio);
        buttonCalcular = root.findViewById(R.id.buttonCalcular);
        buttonReset = root.findViewById(R.id.buttonReset);

        // Inicializamos el mapa con los valores
        aparatosConsumo.put("Otros", 0.0);
        aparatosConsumo.put("Aire acondicionado", 600.0);
        aparatosConsumo.put("Secadora de Ropa", 3000.0);
        aparatosConsumo.put("Plancha de Ropa", 2400.0);
        aparatosConsumo.put("Estufa Electrica", 2000.0);
        aparatosConsumo.put("Calentador", 2000.0);
        aparatosConsumo.put("Horno", 800.0);
        aparatosConsumo.put("Desktop", 100.0);
        aparatosConsumo.put("Nevera", 200.0);
        aparatosConsumo.put("Aspiradora", 1600.0);
        aparatosConsumo.put("Lavadora", 2000.0);
        aparatosConsumo.put("Lámpara", 2000.0);
        aparatosConsumo.put("Televisor", 70.0);
        aparatosConsumo.put("Portatil", 50.0);

        // ...

        // Adaptador para el spinner
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, getResources().getStringArray(R.array.aparatos));
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerAparato.setAdapter(adapter);

        // listener para el spinner
        spinnerAparato.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                resetCampos();
                String aparatoSeleccionado = parent.getItemAtPosition(position).toString();
                Double consumo = aparatosConsumo.get(aparatoSeleccionado);

                if (consumo != null) {
                    editTextConsumo.setText(consumo.toString());
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        buttonCalcular.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calcularConsumo();
            }
        });

        buttonReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetCampos();
            }
        });

        return root;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(CalcViewModel.class);
        // TODO: Use the ViewModel
    }

    private void calcularConsumo() {
        try {
            double consumo = Double.parseDouble(editTextConsumo.getText().toString());
            double horas = Double.parseDouble(editTextHoras.getText().toString());

            double energiaDia = (consumo * horas) / 1000;
            double energiaMes = energiaDia * 30;
            double energiaAnio = energiaDia * 365;

            editTextEnergiaDia.setText(String.format("%.2f", energiaDia));
            editTextEnergiaMes.setText(String.format("%.2f", energiaMes));
            editTextEnergiaAnio.setText(String.format("%.2f", energiaAnio));
        } catch (NumberFormatException e) {
            Toast.makeText(getContext(), "Ingrese valores numéricos válidos.", Toast.LENGTH_SHORT).show();
        }
    }

    private void resetCampos() {
        editTextConsumo.setText("");
        editTextHoras.setText("");
        editTextEnergiaDia.setText("");
        editTextEnergiaMes.setText("");
        editTextEnergiaAnio.setText("");
    }
}