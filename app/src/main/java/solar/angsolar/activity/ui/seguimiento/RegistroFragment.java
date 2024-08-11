package solar.angsolar.activity.ui.seguimiento;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.util.Calendar;

import solar.angsolar.R;
import solar.angsolar.angsolar.db.DatabaseManager;

public class RegistroFragment extends Fragment {

    private EditText editFechaConsumo;
    private EditText editConsumoEnergia;
    private EditText editCostoEstimado;
    private EditText editTarifaKwh;
    private EditText editNotasAdicionales;

    private EditText editFechaFactura;
    private EditText editNumeroFactura;
    private EditText editMontoTotal;
    private EditText editFechaVencimiento;
    private Spinner spinnerMetodoPago;
    private RadioGroup radioEstadoPago;
    private EditText editNotasFactura;

    private DatabaseManager dbManager;


    private TextView textCantidadConsumos;
    private TextView textCantidadFacturas;

    
    private TextView consejoTextView;
    private TextView cantidadRegistrosTextView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_registro, container, false);

        dbManager = new DatabaseManager(getContext());

        // Inicializar campos
        editFechaConsumo = view.findViewById(R.id.editFechaConsumo);
        editConsumoEnergia = view.findViewById(R.id.editConsumoEnergia);
        editCostoEstimado = view.findViewById(R.id.editCostoEstimado);
        editTarifaKwh = view.findViewById(R.id.editTarifaKwh);
        editNotasAdicionales = view.findViewById(R.id.editNotasAdicionales);

        Button btnRegistrarConsumo = view.findViewById(R.id.btnRegistrarConsumo);

        editFechaFactura = view.findViewById(R.id.editFechaFactura);
        editNumeroFactura = view.findViewById(R.id.editNumeroFactura);
        editMontoTotal = view.findViewById(R.id.editMontoTotal);
        editFechaVencimiento = view.findViewById(R.id.editFechaVencimiento);
        spinnerMetodoPago = view.findViewById(R.id.spinnerMetodoPago);
        radioEstadoPago = view.findViewById(R.id.radioEstadoPago);
        editNotasFactura = view.findViewById(R.id.editNotasFactura);

        Button btnRegistrarFactura = view.findViewById(R.id.btnRegistrarFactura);


        textCantidadConsumos = view.findViewById(R.id.textCantidadConsumos);
        textCantidadFacturas = view.findViewById(R.id.textCantidadFacturas);


        actualizarCantidadesRegistros();


        btnRegistrarConsumo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    if (validarCamposConsumo()) {
                        String fecha = editFechaConsumo.getText().toString();
                        double consumo = Double.parseDouble(editConsumoEnergia.getText().toString());
                        double costo = Double.parseDouble(editCostoEstimado.getText().toString());
                        double tarifa = Double.parseDouble(editTarifaKwh.getText().toString());
                        String notas = editNotasAdicionales.getText().toString();

                        boolean resultado = dbManager.registrarConsumo(fecha, consumo, costo, tarifa, notas);
                        mostrarToast(resultado, "Registro de consumo agregado correctamente", "Error al agregar registro de consumo");

                        if (resultado) {
                            limpiarCamposConsumo();
                            actualizarCantidadesRegistros();
                        }
                    }
                } catch (Exception e) {
                    Toast.makeText(getContext(), "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });

        btnRegistrarFactura.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    if (validarCamposFactura()) {
                        String fechaFactura = editFechaFactura.getText().toString();
                        String numeroFactura = editNumeroFactura.getText().toString();
                        double montoTotal = Double.parseDouble(editMontoTotal.getText().toString());
                        String fechaVencimiento = editFechaVencimiento.getText().toString();
                        String metodoPago = spinnerMetodoPago.getSelectedItem().toString();
                        int estadoPago = obtenerEstadoPagoSeleccionado();
                        String notasFactura = editNotasFactura.getText().toString();

                        boolean resultado = dbManager.registrarFactura(fechaFactura, numeroFactura, montoTotal, fechaVencimiento, metodoPago, estadoPago, notasFactura);
                        mostrarToast(resultado, "Registro de factura agregado correctamente", "Error al agregar registro de factura");

                        if (resultado) {
                            limpiarCamposFactura();
                            actualizarCantidadesRegistros();
                        }
                    }
                } catch (Exception e) {
                    Toast.makeText(getContext(), "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });

        // Configurar date pickers
        configurarDatePicker(editFechaConsumo);
        configurarDatePicker(editFechaFactura);
        configurarDatePicker(editFechaVencimiento);



        dbManager = new DatabaseManager(getContext());
        consejoTextView = view.findViewById(R.id.consejoTextView);
        cantidadRegistrosTextView = view.findViewById(R.id.cantidadRegistrosTextView);

        // Mostrar consejo aleatorio
        String consejo = dbManager.obtenerConsejoAleatorio();
        consejoTextView.setText(consejo);

        // Mostrar cantidad de registros en la tabla de consumo
        int cantidadRegistros = dbManager.contarRegistros("consumo");
        cantidadRegistrosTextView.setText("Cantidad de registros: " + cantidadRegistros);

        return view;


    }

    // Actualizar la cantidad de registros
    private void actualizarCantidadesRegistros() {
        int cantidadConsumos = dbManager.obtenerCantidadRegistrosConsumo();
        int cantidadFacturas = dbManager.obtenerCantidadRegistrosFactura();
        textCantidadConsumos.setText("Registro N°|: " + cantidadConsumos);
        textCantidadFacturas.setText("Registro N°|: " + cantidadFacturas);
    }

    // Validar campos de consumo
    private boolean validarCamposConsumo() {
        if (editFechaConsumo.getText().toString().isEmpty() ||
                editConsumoEnergia.getText().toString().isEmpty() ||
                editCostoEstimado.getText().toString().isEmpty() ||
                editTarifaKwh.getText().toString().isEmpty()) {
            Toast.makeText(getContext(), "Por favor, complete todos los campos del registro de consumo", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    // Validar campos de factura
    private boolean validarCamposFactura() {
        if (editFechaFactura.getText().toString().isEmpty() ||
                editNumeroFactura.getText().toString().isEmpty() ||
                editMontoTotal.getText().toString().isEmpty() ||
                editFechaVencimiento.getText().toString().isEmpty() ||
                spinnerMetodoPago.getSelectedItem() == null ||
                radioEstadoPago.getCheckedRadioButtonId() == -1) {
            Toast.makeText(getContext(), "Por favor, complete todos los campos del registro de factura", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    // Limpiar campos de consumo
    private void limpiarCamposConsumo() {
        editFechaConsumo.setText("");
        editConsumoEnergia.setText("");
        editCostoEstimado.setText("");
        editTarifaKwh.setText("");
        editNotasAdicionales.setText("");
    }

    // Limpiar campos de factura
    private void limpiarCamposFactura() {
        editFechaFactura.setText("");
        editNumeroFactura.setText("");
        editMontoTotal.setText("");
        editFechaVencimiento.setText("");
        spinnerMetodoPago.setSelection(0);
        radioEstadoPago.clearCheck();
        editNotasFactura.setText("");
    }

    // Configurar DatePicker
    private void configurarDatePicker(final EditText editText) {
        editText.setFocusable(false);  // Evitar que se edite manualmente
        editText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar calendar = Calendar.getInstance();
                int year = calendar.get(Calendar.YEAR);
                int month = calendar.get(Calendar.MONTH);
                int day = calendar.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        // Formatear la fecha como 'YYYY-MM-DD' con ceros a la izquierda si es necesario
                        String fecha = String.format("%04d-%02d-%02d", year, month + 1, dayOfMonth);
                        editText.setText(fecha);
                    }
                }, year, month, day);
                datePickerDialog.show();
            }
        });
    }

    // Obtener estado de pago seleccionado
    private int obtenerEstadoPagoSeleccionado() {
        int selectedId = radioEstadoPago.getCheckedRadioButtonId();
        if (selectedId == R.id.radioPagado) {
            return 1;
        } else if (selectedId == R.id.radioPendiente) {
            return 0;
        }
        return -1;
    }

    // Mostrar mensaje Toast
    private void mostrarToast(boolean resultado, String mensajeExito, String mensajeError) {
        if (resultado) {
            Toast.makeText(getContext(), mensajeExito, Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(getContext(), mensajeError, Toast.LENGTH_SHORT).show();
        }
    }
}
