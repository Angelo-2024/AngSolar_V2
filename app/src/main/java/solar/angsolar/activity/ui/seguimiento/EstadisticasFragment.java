package solar.angsolar.activity.ui.seguimiento;

import android.graphics.Color;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.formatter.ValueFormatter;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import solar.angsolar.R;
import solar.angsolar.angsolar.db.DatabaseManager;

public class EstadisticasFragment extends Fragment {

    private DatabaseManager dbManager;
    private TextView promedioConsumoTextView, totalConsumoTextView, maxConsumoTextView, minConsumoTextView;
    private TextView totalFacturasTextView, maxFacturaTextView, minFacturaTextView, promedioFacturaTextView;
    private TextView mesFacturaMaximaTextView, mesFacturaMinimaTextView;
    private BarChart consumoChart, facturasChart;

    public EstadisticasFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_estadisticas, container, false);

        dbManager = new DatabaseManager(getActivity());

        // Inicializar TextViews para consumo
        promedioConsumoTextView = view.findViewById(R.id.promedio_consumo_text_view);
        totalConsumoTextView = view.findViewById(R.id.total_consumo_text_view);
        maxConsumoTextView = view.findViewById(R.id.max_consumo_text_view);
        minConsumoTextView = view.findViewById(R.id.min_consumo_text_view);

        // Inicializar TextViews para facturas
        totalFacturasTextView = view.findViewById(R.id.total_facturas_text_view);
        maxFacturaTextView = view.findViewById(R.id.max_factura_text_view);
        minFacturaTextView = view.findViewById(R.id.min_factura_text_view);
        promedioFacturaTextView = view.findViewById(R.id.promedio_factura_text_view);

        // Inicializar TextViews para meses de facturas
        mesFacturaMaximaTextView = view.findViewById(R.id.mes_factura_maxima_text_view);
        mesFacturaMinimaTextView = view.findViewById(R.id.mes_factura_minima_text_view);

        // Inicializar gráficos
        consumoChart = view.findViewById(R.id.consumoChart);
        facturasChart = view.findViewById(R.id.facturasChart);

        // Obtener y mostrar estadísticas de consumo
        double promedioConsumo = dbManager.obtenerPromedioConsumo();
        double totalConsumo = dbManager.obtenerConsumoTotal();
        double maxConsumo = dbManager.obtenerConsumoMaximo();
        double minConsumo = dbManager.obtenerConsumoMinimo();

        setTextWithStyle(promedioConsumoTextView, String.format("Promedio de Consumo: %.2f kWh", promedioConsumo));
        setTextWithStyle(totalConsumoTextView, String.format("Consumo Total: %.2f kWh", totalConsumo));
        setTextWithStyle(maxConsumoTextView, String.format("Consumo Máximo: %.2f kWh", maxConsumo));
        setTextWithStyle(minConsumoTextView, String.format("Consumo Mínimo: %.2f kWh", minConsumo));

        // Obtener y mostrar estadísticas de facturas
        double totalFacturas = dbManager.obtenerMontoTotalFacturas();
        double maxFactura = dbManager.obtenerFacturaMaxima();
        double minFactura = dbManager.obtenerFacturaMinima();
        double promedioFactura = dbManager.obtenerPromedioFacturas();
        String mesFacturaMaxima = dbManager.obtenerMesFacturaMaxima();
        String mesFacturaMinima = dbManager.obtenerMesFacturaMinima();

        setTextWithStyle(totalFacturasTextView, String.format("Monto Total Facturado: %.2f COP", totalFacturas));
        setTextWithStyle(maxFacturaTextView, String.format("Factura Máxima: %.2f COP", maxFactura));
        setTextWithStyle(minFacturaTextView, String.format("Factura Mínima: %.2f COP", minFactura));
        setTextWithStyle(promedioFacturaTextView, String.format("Promedio de Factura: %.2f COP", promedioFactura));

        // Mostrar meses de facturas
        mesFacturaMaximaTextView.setText(String.format("Mes de Factura Máxima: %s", mesFacturaMaxima != null ? mesFacturaMaxima : "No Disponible"));
        mesFacturaMinimaTextView.setText(String.format("Mes de Factura Mínima: %s", mesFacturaMinima != null ? mesFacturaMinima : "No Disponible"));

        // Configurar gráficos
        configurarGraficoConsumo();
        configurarGraficoFacturas();

        // Obtener datos de facturas y mostrarlos
        mostrarMontoTotalPorMes(view);
        mostrarConsumoPorMes(view);

        return view;
    }

    private void setTextWithStyle(TextView textView, String text) {
        SpannableString spannableString = new SpannableString(text);
        spannableString.setSpan(new StyleSpan(android.graphics.Typeface.BOLD), 0, text.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        spannableString.setSpan(new ForegroundColorSpan(ContextCompat.getColor(getContext(), R.color.dark_green)), 0, text.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        textView.setText(spannableString);
    }

    private void configurarGraficoConsumo() {
        List<BarEntry> entries = new ArrayList<>();
        Map<String, Double> consumoPorMes = dbManager.obtenerConsumoPorMes();

        int i = 0;
        List<String> meses = new ArrayList<>();
        for (Map.Entry<String, Double> entry : consumoPorMes.entrySet()) {
            entries.add(new BarEntry(i, entry.getValue().floatValue()));
            meses.add(formatoMes(entry.getKey()));
            i++;
        }

        BarDataSet dataSet = new BarDataSet(entries, "Consumo por Mes");
        dataSet.setColor(Color.BLUE);
        dataSet.setValueTextColor(Color.WHITE);
        dataSet.setValueFormatter(new ValueFormatter() {
            @Override
            public String getFormattedValue(float value) {
                return String.format("%.2f", value);
            }
        });

        BarData barData = new BarData(dataSet);
        consumoChart.setData(barData);

        // Configurar ejes
        XAxis xAxis = consumoChart.getXAxis();
        xAxis.setGranularity(1f);
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setValueFormatter(new IndexAxisValueFormatter(meses)); // Lista de meses

        YAxis leftAxis = consumoChart.getAxisLeft();
        leftAxis.setGranularity(1f);
        consumoChart.getAxisRight().setEnabled(false); // Desactivar eje derecho

        consumoChart.invalidate(); // Refresh chart
    }

    private void configurarGraficoFacturas() {
        List<BarEntry> entries = new ArrayList<>();
        Map<String, Double> facturasPorMes = dbManager.obtenerFacturasPorMes();

        int i = 0;
        List<String> meses = new ArrayList<>();
        for (Map.Entry<String, Double> entry : facturasPorMes.entrySet()) {
            entries.add(new BarEntry(i, entry.getValue().floatValue()));
            meses.add(formatoMes(entry.getKey()));
            i++;
        }

        BarDataSet dataSet = new BarDataSet(entries, "Facturas por Mes");
        dataSet.setColor(Color.GREEN);
        dataSet.setValueTextColor(Color.WHITE);
        dataSet.setValueFormatter(new ValueFormatter() {
            @Override
            public String getFormattedValue(float value) {
                return String.format("%.2f", value);
            }
        });

        BarData barData = new BarData(dataSet);
        facturasChart.setData(barData);

        // Configurar ejes
        XAxis xAxis = facturasChart.getXAxis();
        xAxis.setGranularity(1f);
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setValueFormatter(new IndexAxisValueFormatter(meses)); // Lista de meses

        YAxis leftAxis = facturasChart.getAxisLeft();
        leftAxis.setGranularity(1f);
        facturasChart.getAxisRight().setEnabled(false); // Desactivar eje derecho

        facturasChart.invalidate(); // Refresh chart
    }

    private String formatoMes(String fecha) {
        String[] partes = fecha.split("-");
        int mes = Integer.parseInt(partes[1]);
        String[] meses = {"Enero", "Febrero", "Marzo", "Abril", "Mayo", "Junio", "Julio", "Agosto", "Septiembre", "Octubre", "Noviembre", "Diciembre"};
        return meses[mes - 1];
    }

    private List<String> getMeses() {
        List<String> meses = new ArrayList<>();
        // Agregar nombres de los meses aquí
        meses.add("Enero");
        meses.add("Febrero");
        meses.add("Marzo");
        meses.add("Abril");
        meses.add("Mayo");
        meses.add("Junio");
        meses.add("Julio");
        meses.add("Agosto");
        meses.add("Septiembre");
        meses.add("Octubre");
        meses.add("Noviembre");
        meses.add("Diciembre");
        // ...
        return meses;
    }

    private void mostrarMontoTotalPorMes(View view) {
        TextView textView = view.findViewById(R.id.tvMontosPorMes);
        StringBuilder montosPorMesStr = new StringBuilder();

        LinkedHashMap<String, Double> montosPorMes = dbManager.obtenerMontoTotalPorMes();

        for (Map.Entry<String, Double> entry : montosPorMes.entrySet()) {
            String mes = entry.getKey();
            double monto = entry.getValue();
            montosPorMesStr.append("Mes: ").append(mes).append(" - Monto Total: ").append(String.format("%.2f", monto)).append("\n");
        }

        textView.setText(montosPorMesStr.toString());
    }

    private void mostrarConsumoPorMes(View view) {
        TextView textView = view.findViewById(R.id.tvConsumoPorMes);
        StringBuilder consumoPorMesStr = new StringBuilder();

        Map<String, Double> consumoPorMes = dbManager.obtenerConsumoMes();

        for (Map.Entry<String, Double> entry : consumoPorMes.entrySet()) {
            String mes = entry.getKey();
            double consumo = entry.getValue();
            consumoPorMesStr.append("Mes: ").append(mes).append(" - Consumo: ").append(String.format("%.2f", consumo)).append("\n");
        }

        textView.setText(consumoPorMesStr.toString());
    }



}

