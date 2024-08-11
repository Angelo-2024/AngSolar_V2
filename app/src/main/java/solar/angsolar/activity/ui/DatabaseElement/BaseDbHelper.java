package solar.angsolar.activity.ui.DatabaseElement;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class BaseDbHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "base.db";
    private static final int DATABASE_VERSION = 1;

    public BaseDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_TABLE = "CREATE TABLE " + BaseContract.TABLE_NAME + " (" +
                BaseContract.Columnas.ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                BaseContract.Columnas.NOMBRE + " TEXT, " +
                BaseContract.Columnas.DESCRIPCION + " TEXT, " +
                BaseContract.Columnas.FECHA_CREACION + " DATETIME DEFAULT CURRENT_TIMESTAMP, " +
                BaseContract.Columnas.FECHA_ACTUALIZACION + " DATETIME DEFAULT CURRENT_TIMESTAMP, " +
                BaseContract.Columnas.ESTADO + " INTEGER)";
        db.execSQL(CREATE_TABLE);

        String CREATE_TABLE_FUNCTION = "CREATE TABLE " + BaseContract.TABLE_NAME_FUNCIONES + " (" +
                BaseContract.Columnas_funciones_calculadora_energia.ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                BaseContract.Columnas_funciones_calculadora_energia.Potencia + " INTEGER, " +
                BaseContract.Columnas_funciones_calculadora_energia.tiempo + " INTEGER, " +
                BaseContract.Columnas_funciones_calculadora_energia.paneles+ " INTEGER, " +
                BaseContract.Columnas_funciones_calculadora_energia.energia + " DOUBLE)";
        db.execSQL(CREATE_TABLE_FUNCTION);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}