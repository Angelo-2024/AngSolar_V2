package solar.angsolar.activity.ui.function;

import androidx.lifecycle.ViewModelProvider;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import solar.angsolar.R;

public class FunctionFragment extends Fragment {

    private FunctionViewModel mViewModel;
    private EditText potenciaEditText, tiempoEditText, panelesEditText;
    private TextView resultadoTextSolicitadoView;
    private TableLayout resultTable;
    private Button calcularButton;

    public static FunctionFragment newInstance() {
        return new FunctionFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_function, container, false);

        potenciaEditText = root.findViewById(R.id.potenciaEditText);
        tiempoEditText = root.findViewById(R.id.tiempoEditText);
        panelesEditText = root.findViewById(R.id.panelesEditText);
        resultadoTextSolicitadoView = root.findViewById(R.id.resultadoTextSolicitadoView);
        resultTable = root.findViewById(R.id.resultTable);
        calcularButton = root.findViewById(R.id.calcularButton);

        calcularButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calcularEnergia();
            }
        });

        return root;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(FunctionViewModel.class);
    }

    private void calcularEnergia() {
        // Verificar campos vacíos
        if (potenciaEditText.getText().toString().isEmpty() ||
                panelesEditText.getText().toString().isEmpty() ||
                tiempoEditText.getText().toString().isEmpty()) {
            Toast.makeText(getContext(), "Por favor complete los campos", Toast.LENGTH_SHORT).show();
            return;
        }

        try {
            int potencia = Integer.parseInt(potenciaEditText.getText().toString());
            int paneles = Integer.parseInt(panelesEditText.getText().toString());
            int tiempo = Integer.parseInt(tiempoEditText.getText().toString());

            int[] tiemposHoras = {24, 36, 72, 96, 720, 2160, 4320, 6480, 8640, 26298}; // en horas, añadir para mas resultados
            String[] tiemposDescripcion = {"24 Horas", "36 H", "72 H", "96 H", "1 Mes (720 H)",
                    "3 Meses (2160 H)", "6 Meses (4320 H)", "9 Meses (6480 H)",
                    "12 Meses (8640 H)", "3 Años (26298 H)"};

            resultTable.removeViews(1, resultTable.getChildCount() - 1); // Eliminar filas no utilizadas

            for (int i = 0; i < tiemposHoras.length; i++) {
                double energia = potencia * tiemposHoras[i] * paneles;
                String energiaTexto;
                if (tiemposHoras[i] >= 24
                ) {
                    energia /= 1000; // Convertir datos de Wh a kWh
                    energiaTexto = energia + " kWh";
                } else {
                    energiaTexto = energia + " Wh";
                }

                TableRow row = new TableRow(getContext());
                TextView tcTextView = new TextView(getContext());
                TextView egTextView = new TextView(getContext());

                tcTextView.setText(tiemposDescripcion[i]);
                egTextView.setText(energiaTexto);

                // Configuración de  bordes y padding para las celdas de la tabla

                tcTextView.setPadding(8, 8, 8, 8);
                egTextView.setPadding(8, 8, 8, 8);

                tcTextView.setBackgroundResource(R.drawable.table_cell_border);
                egTextView.setBackgroundResource(R.drawable.table_cell_border);

                tcTextView.setGravity(Gravity.CENTER);
                egTextView.setGravity(Gravity.CENTER);

                row.addView(tcTextView);
                row.addView(egTextView);

                resultTable.addView(row);
            }

            double energiasolicitada = potencia * tiempo * paneles;
            resultadoTextSolicitadoView.setText("Energía generada solicitada: " + energiasolicitada + " Wh");

            Toast.makeText(getContext(), "Resultado Obtenido", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Toast.makeText(getContext(), "No se genera resultado", Toast.LENGTH_SHORT).show();
        }
    }
}
