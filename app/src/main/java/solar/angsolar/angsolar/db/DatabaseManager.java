package solar.angsolar.angsolar.db;

import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.widget.Toast;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

public class DatabaseManager {
    private DatabaseHelper dbHelper;
    private SQLiteDatabase db;
    private Context context;

    private static String userCedula;
    private static String userEmail;

    public DatabaseManager(Context context) {
        dbHelper = new DatabaseHelper(context);
        this.context = context;
    }

    // Método para verificar credenciales y extraer cédula y correo electrónico
    public boolean verificarCredenciales(String usuario, String contrasena) {
        db = dbHelper.getWritableDatabase();
        String query = "SELECT * FROM usuarios WHERE usuario = ? AND contrasena = ?";
        Cursor cursor = db.rawQuery(query, new String[]{usuario, contrasena});

        if (cursor != null && cursor.getCount() > 0) {
            cursor.moveToFirst();
            // Extraer cédula y correo electrónico del cursor
            int cedulaIndex = cursor.getColumnIndex("cedula");
            int emailIndex = cursor.getColumnIndex("email");

            String cedula = cursor.getString(cedulaIndex);
            String email = cursor.getString(emailIndex);

            // Almacenar cédula y correo electrónico en variables globales
            SharedPreferences sharedPreferences = context.getSharedPreferences("USER_PREFS", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString("cedula", cedula);
            editor.putString("email", email);
            editor.apply();

            cursor.close();
            showToast("Credenciales verificadas correctamente");
            db.close();
            return true;
        } else {
            showToast("Credenciales incorrectas");
            db.close();
            return false;
        }
    }



    // Método para registrar un nuevo usuario
    public boolean registrarUsuario(int cedula, String nombre, String email, String usuario, String contrasena) {
        db = dbHelper.getWritableDatabase();

        // Verificar si el usuario ya existe
        String query = "SELECT * FROM usuarios WHERE cedula = ?";
        Cursor cursor = db.rawQuery(query, new String[]{String.valueOf(cedula)});

        if (cursor != null && cursor.getCount() > 0) {
            cursor.close();
            showToast("El usuario ya existe, no se pudo registrar");
            db.close();
            return false; // Usuario ya existe
        }

        // Insertar nuevo usuario
        ContentValues values = new ContentValues();
        values.put("cedula", cedula);
        values.put("nombre", nombre);
        values.put("email", email);
        values.put("usuario", usuario);
        values.put("contrasena", contrasena);

        long result = db.insert("usuarios", null, values);
        db.close();

        if (result != -1) {
            showToast("Usuario registrado correctamente");
            return true; // Usuario registrado exitosamente
        } else {
            showToast("Error al registrar usuario");
            return false;
        }
    }

    // Método para verificar si el ID de cédula ya existe en la base de datos
    public boolean isCedulaExists(int cedula) {
        db = dbHelper.getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT 1 FROM usuarios WHERE cedula = ?", new String[]{String.valueOf(cedula)});
        boolean exists = (cursor.getCount() > 0);
        cursor.close();
        db.close();
        return exists;
    }

    // Método para insertar registro de consumo
    public boolean registrarConsumo(String fecha, double consumo, double costo, double tarifa, String notas) {
        db = dbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("fecha_consumo", fecha);
        values.put("consumo_energia", consumo);
        values.put("costo_estimado", costo);
        values.put("tarifa_kwh", tarifa);
        values.put("notas_adicionales", notas);

        long result = db.insert("consumo", null, values);
        db.close();

        if (result != -1) {
            showToast("Registro de consumo agregado correctamente");
            return true;
        } else {
            showToast("Error al agregar registro de consumo");
            return false;
        }
    }

    // Método para insertar factura
    public boolean registrarFactura(String fechaFactura, String numeroFactura, double montoTotal, String fechaVencimiento, String metodoPago, int estadoPago, String notas) {
        db = dbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("fecha_factura", fechaFactura);
        values.put("numero_factura", numeroFactura);
        values.put("monto_total", montoTotal);
        values.put("fecha_vencimiento", fechaVencimiento);
        values.put("metodo_pago", metodoPago);
        values.put("estado_pago", estadoPago);
        values.put("notas_adicionales", notas);

        long result = db.insert("facturas", null, values);
        db.close();

        if (result != -1) {
            showToast("Factura agregada correctamente");
            return true;
        } else {
            showToast("Error al agregar factura");
            return false;
        }
    }

    // Método para contar los registros en la tabla de consumos
    public int obtenerCantidadRegistrosConsumo() {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT COUNT(*) FROM consumo", null);
        int cantidad = 0;
        if (cursor.moveToFirst()) {
            cantidad = cursor.getInt(0);
        }
        cursor.close();
        return cantidad;
    }

    // Método para contar los registros en la tabla de facturas
    public int obtenerCantidadRegistrosFactura() {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT COUNT(*) FROM facturas", null);
        int cantidad = 0;
        if (cursor.moveToFirst()) {
            cantidad = cursor.getInt(0);
        }
        cursor.close();
        return cantidad;
    }

    // Método para obtener todos los registros de consumo
    public Cursor obtenerRegistrosConsumo() {
        db = dbHelper.getReadableDatabase();
        return db.rawQuery("SELECT * FROM consumo", null);
    }

    // Método para obtener todas las facturas
    public Cursor obtenerFacturas() {
        db = dbHelper.getReadableDatabase();
        return db.rawQuery("SELECT * FROM facturas", null);
    }

    // Método para obtener estadísticas de consumo
    public double obtenerPromedioConsumo() {
        db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT AVG(consumo_energia) FROM consumo", null);
        double promedio = 0;
        if (cursor.moveToFirst()) {
            promedio = cursor.getDouble(0);
        }
        cursor.close();
        return promedio;
    }

    // Método para obtener consumo total
    public double obtenerConsumoTotal() {
        db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT SUM(consumo_energia) FROM consumo", null);
        double total = 0;
        if (cursor.moveToFirst()) {
            total = cursor.getDouble(0);
        }
        cursor.close();
        return total;
    }

    // Método para obtener consumo maximo
    public double obtenerConsumoMaximo() {
        db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT MAX(consumo_energia) FROM consumo", null);
        double maximo = 0;
        if (cursor.moveToFirst()) {
            maximo = cursor.getDouble(0);
        }
        cursor.close();
        return maximo;
    }

    // Método para obtener consumo minimo

    public double obtenerConsumoMinimo() {
        db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT MIN(consumo_energia) FROM consumo", null);
        double minimo = 0;
        if (cursor.moveToFirst()) {
            minimo = cursor.getDouble(0);
        }
        cursor.close();
        return minimo;
    }

    // Método para obtener estadísticas de facturas
    public double obtenerMontoTotalFacturas() {
        db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT SUM(monto_total) FROM facturas", null);
        double total = 0;
        if (cursor.moveToFirst()) {
            total = cursor.getDouble(0);
        }
        cursor.close();
        return total;
    }

    // Método para obtener factura maxima
    public double obtenerFacturaMaxima() {
        db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT MAX(monto_total) FROM facturas", null);
        double maximo = 0;
        if (cursor.moveToFirst()) {
            maximo = cursor.getDouble(0);
        }
        cursor.close();
        return maximo;
    }

    // Método para obtener factura minima
    public double obtenerFacturaMinima() {
        db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT MIN(monto_total) FROM facturas", null);
        double minimo = 0;
        if (cursor.moveToFirst()) {
            minimo = cursor.getDouble(0);
        }
        cursor.close();
        return minimo;
    }

    // Método para obtener promedio factura
    public double obtenerPromedioFacturas() {
        db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT AVG(monto_total) FROM facturas", null);
        double promedio = 0;
        if (cursor.moveToFirst()) {
            promedio = cursor.getDouble(0);
        }
        cursor.close();
        return promedio;
    }

    // Método para obtener el mes de factura maxima
    public String obtenerMesFacturaMaxima() {
        db = dbHelper.getReadableDatabase();
        String mes = "Desconocido";
        Cursor cursor = null;
        try {
            cursor = db.rawQuery(
                    "SELECT strftime('%Y-%m', fecha_factura) AS mes " +
                            "FROM facturas " +
                            "WHERE monto_total = (SELECT MAX(monto_total) FROM facturas)",
                    null
            );

            if (cursor != null && cursor.moveToFirst()) {
                mes = cursor.getString(0);
            }
        } catch (Exception e) {
            showToast("Error al obtener mes de factura máxima: " + e.getMessage());
        } finally {
            if (cursor != null) {
                cursor.close();
            }
            db.close();
        }
        return mes;
    }

    // Método para obtener el mes de factura minima
    public String obtenerMesFacturaMinima() {
        db = dbHelper.getReadableDatabase();
        String mes = "Desconocido";
        Cursor cursor = null;
        try {
            cursor = db.rawQuery(
                    "SELECT strftime('%Y-%m', fecha_factura) AS mes " +
                            "FROM facturas " +
                            "WHERE monto_total = (SELECT MIN(monto_total) FROM facturas)",
                    null
            );

            if (cursor != null && cursor.moveToFirst()) {
                mes = cursor.getString(0);
            }
        } catch (Exception e) {
            showToast("Error al obtener mes de factura mínima: " + e.getMessage());
        } finally {
            if (cursor != null) {
                cursor.close();
            }
            db.close();
        }
        return mes;
    }

    // Método para contar registros en una tabla
    public int contarRegistros(String tabla) {
        db = dbHelper.getWritableDatabase();
        String query = "SELECT COUNT(*) FROM " + tabla;
        Cursor cursor = db.rawQuery(query, null);

        int count = 0;
        if (cursor.moveToFirst()) {
            count = cursor.getInt(0);
        }
        cursor.close();
        db.close();
        return count;
    }

    // Método para obtener consejos aleatorios
    public String obtenerConsejoAleatorio() {
        db = dbHelper.getWritableDatabase();
        String query = "SELECT descripcion FROM consejos ORDER BY RANDOM() LIMIT 1";
        Cursor cursor = db.rawQuery(query, null);

        String consejo = null;
        if (cursor.moveToFirst()) {
            consejo = cursor.getString(0);
        }
        cursor.close();
        db.close();
        return consejo;
    }

    // Método para obtener datos de consumo agrupados por mes
    public Map<String, Double> obtenerConsumoPorMes() {
        db = dbHelper.getReadableDatabase();
        String query = "SELECT strftime('%Y-%m', fecha_consumo) AS mes, SUM(consumo_energia) AS total_consumo " +
                "FROM consumo GROUP BY mes ORDER BY strftime('%Y-%m', fecha_consumo)";
        Cursor cursor = db.rawQuery(query, null);

        Map<String, Double> consumoPorMes = new LinkedHashMap<>();
        if (cursor.moveToFirst()) {
            do {
                String mes = cursor.getString(0);
                double totalConsumo = cursor.getDouble(1);
                consumoPorMes.put(mes, totalConsumo);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return consumoPorMes;
    }

    // Método para obtener factura por mes
    public Map<String, Double> obtenerFacturasPorMes() {
        db = dbHelper.getReadableDatabase();
        String query = "SELECT strftime('%Y-%m', fecha_factura) AS mes, SUM(monto_total) AS total_factura " +
                "FROM facturas GROUP BY mes ORDER BY strftime('%Y-%m', fecha_factura)";
        Cursor cursor = db.rawQuery(query, null);

        Map<String, Double> facturasPorMes = new LinkedHashMap<>();
        if (cursor.moveToFirst()) {
            do {
                String mes = cursor.getString(0);
                double totalFactura = cursor.getDouble(1);
                facturasPorMes.put(mes, totalFactura);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return facturasPorMes;
    }

    // Método para obtener monto total mes

    public LinkedHashMap<String, Double> obtenerMontoTotalPorMes() {
        db = dbHelper.getReadableDatabase();
        LinkedHashMap<String, Double> resultados = new LinkedHashMap<>();

        Cursor cursor = db.rawQuery(
                "SELECT strftime('%Y-%m', fecha_factura) AS mes, SUM(monto_total) AS monto " +
                        "FROM facturas " +
                        "GROUP BY mes",
                null
        );

        if (cursor != null) {
            while (cursor.moveToNext()) {
                String mes = cursor.getString(cursor.getColumnIndex("mes"));
                double monto = cursor.getDouble(cursor.getColumnIndex("monto"));
                resultados.put(mes, monto);
            }
            cursor.close();
        }

        return resultados;
    }



    // Método para obtener datos de consumo por mes

    public Map<String, Double> obtenerConsumoMes() {
        db = dbHelper.getReadableDatabase();
        String query = "SELECT strftime('%Y-%m', fecha_consumo) AS mes, consumo_energia " +
                "FROM consumo ORDER BY strftime('%Y-%m', fecha_consumo)";
        Cursor cursor = db.rawQuery(query, null);

        Map<String, Double> consumoMes = new LinkedHashMap<>();
        if (cursor.moveToFirst()) {
            do {
                String mes = cursor.getString(0);
                double consumo = cursor.getDouble(1);
                consumoMes.put(mes, consumo);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return consumoMes;
    }



    // Método para mostrar mensajes de Toast
    private void showToast(String message) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }


}
