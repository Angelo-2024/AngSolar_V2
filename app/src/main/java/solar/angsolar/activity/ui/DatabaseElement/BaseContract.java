package solar.angsolar.activity.ui.DatabaseElement;

public class BaseContract {

    public static final String TABLE_NAME = "categorias";

    public static class Columnas {
        public static final String ID = "_id";
        public static final String NOMBRE = "nombre";
        public static final String DESCRIPCION = "descripcion";
        public static final String FECHA_CREACION = "fechaCreacion";
        public static final String FECHA_ACTUALIZACION = "fechaActualizacion";
        public static final String ESTADO = "estado";
    }

    public static final String TABLE_NAME_FUNCIONES = "calculadora_energia";

    public static class Columnas_funciones_calculadora_energia {

        public static final String ID ="id";
        public static final String Potencia ="potencia";
        public static final String tiempo ="tiempo";
        public static final String paneles ="paneles";
        public static final String energia ="energia";

    }


}
