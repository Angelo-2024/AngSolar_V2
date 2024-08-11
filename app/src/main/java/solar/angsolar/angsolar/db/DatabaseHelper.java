package solar.angsolar.angsolar.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "mydatabase.db";
    private static final int DATABASE_VERSION = 4; //  versión 4 , verificar si se actualiza

    // Tabla usuarios
    private static final String TABLE_USUARIOS = "usuarios";
    private static final String COLUMN_CEDULA = "cedula";
    private static final String COLUMN_NOMBRE = "nombre";
    private static final String COLUMN_EMAIL = "email";
    private static final String COLUMN_USUARIO = "usuario";
    private static final String COLUMN_CONTRASENA = "contrasena";

    // Crear tabla usuarios
    private static final String CREATE_TABLE_USUARIOS = "CREATE TABLE " + TABLE_USUARIOS + " (" +
            COLUMN_CEDULA + " INTEGER PRIMARY KEY, " +
            COLUMN_NOMBRE + " TEXT, " +
            COLUMN_EMAIL + " TEXT, " +
            COLUMN_USUARIO + " TEXT, " +
            COLUMN_CONTRASENA + " TEXT);";





    // Tabla registros de consumo
    private static final String TABLE_CONSUMO = "consumo";
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_FECHA_CONSUMO = "fecha_consumo";
    private static final String COLUMN_CONSUMO_ENERGIA = "consumo_energia";
    private static final String COLUMN_COSTO_ESTIMADO = "costo_estimado";
    private static final String COLUMN_TARIFA_KWH = "tarifa_kwh";
    private static final String COLUMN_NOTAS_ADICIONALES = "notas_adicionales";

    // Crear tabla consumo
    private static final String CREATE_TABLE_CONSUMO = "CREATE TABLE " + TABLE_CONSUMO + " (" +
            COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            COLUMN_FECHA_CONSUMO + " TEXT, " +
            COLUMN_CONSUMO_ENERGIA + " REAL, " +
            COLUMN_COSTO_ESTIMADO + " REAL, " +
            COLUMN_TARIFA_KWH + " REAL, " +
            COLUMN_NOTAS_ADICIONALES + " TEXT);";

    // Tabla facturas
    private static final String TABLE_FACTURAS = "facturas";
    private static final String COLUMN_FECHA_FACTURA = "fecha_factura";
    private static final String COLUMN_NUMERO_FACTURA = "numero_factura";
    private static final String COLUMN_MONTO_TOTAL = "monto_total";
    private static final String COLUMN_FECHA_VENCIMIENTO = "fecha_vencimiento";
    private static final String COLUMN_METODO_PAGO = "metodo_pago";
    private static final String COLUMN_ESTADO_PAGO = "estado_pago";

    // Crear tabla facturas
    private static final String CREATE_TABLE_FACTURAS = "CREATE TABLE " + TABLE_FACTURAS + " (" +
            COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            COLUMN_FECHA_FACTURA + " TEXT, " +
            COLUMN_NUMERO_FACTURA + " TEXT, " +
            COLUMN_MONTO_TOTAL + " REAL, " +
            COLUMN_FECHA_VENCIMIENTO + " TEXT, " +
            COLUMN_METODO_PAGO + " TEXT, " +
            COLUMN_ESTADO_PAGO + " TEXT, " +
            COLUMN_NOTAS_ADICIONALES + " TEXT);";

    // Nueva tabla consejos
    private static final String TABLE_CONSEJOS = "consejos";
    private static final String COLUMN_CONSEJO_ID = "id";
    private static final String COLUMN_DESCRIPCION = "descripcion";

    // Crear tabla consejos
    private static final String CREATE_TABLE_CONSEJOS = "CREATE TABLE " + TABLE_CONSEJOS + " (" +
            COLUMN_CONSEJO_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            COLUMN_DESCRIPCION + " TEXT);";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Crear tablas
        db.execSQL(CREATE_TABLE_USUARIOS);
        db.execSQL(CREATE_TABLE_CONSUMO);
        db.execSQL(CREATE_TABLE_FACTURAS);
        db.execSQL(CREATE_TABLE_CONSEJOS);

        // Insertar registro de administrador si no existe
        db.execSQL("INSERT OR IGNORE INTO " + TABLE_USUARIOS + " (" + COLUMN_CEDULA + ", " + COLUMN_NOMBRE + ", " + COLUMN_EMAIL + ", " + COLUMN_USUARIO + ", " + COLUMN_CONTRASENA + ") VALUES (1, 'admin', 'admin@example.com', 'admin', '123')");

        // Insertar consejos iniciales
        insertarConsejosIniciales(db);

    }

    private void insertarConsejosIniciales(SQLiteDatabase db) {
        // Array con 20 consejos
        String[] consejos = {
                "Ahorra energía apagando las luces que no necesitas.",
                "Utiliza bombillas LED para reducir el consumo eléctrico.",
                "Desconecta los electrodomésticos que no estás utilizando.",
                "Mantén tu refrigerador a una temperatura adecuada.",
                "Usa programas de lavado en frío para tu ropa.",
                "Instala paneles solares para aprovechar la energía solar.",
                "Aísla tu casa para mantener la temperatura adecuada.",
                "Utiliza cortinas para regular la temperatura interior.",
                "Aprovecha la luz natural en lugar de la artificial.",
                "Realiza mantenimiento regular a tus electrodomésticos.",
                "Usa ventiladores en lugar de aire acondicionado.",
                "Instala un termostato programable.",
                "Utiliza la lavadora y el lavavajillas con carga completa.",
                "Reduce el uso de calentadores de agua eléctricos.",
                "Descongela tu congelador regularmente.",
                "Cierra las puertas y ventanas cuando el aire acondicionado esté en uso.",
                "Reduce el brillo de tus dispositivos electrónicos.",
                "Utiliza regletas para desconectar varios dispositivos a la vez.",
                "Invierte en electrodomésticos de bajo consumo energético.",
                "Informa a tu familia sobre hábitos de ahorro energético."
        };

        // Insertar consejos en la tabla
        for (String consejo : consejos) {
            ContentValues values = new ContentValues();
            values.put(COLUMN_DESCRIPCION, consejo);
            db.insert(TABLE_CONSEJOS, null, values);
        }
    }



    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Actualizar estructura de la base de datos
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CONSUMO);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_FACTURAS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USUARIOS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CONSEJOS);
        onCreate(db);
    }
}
