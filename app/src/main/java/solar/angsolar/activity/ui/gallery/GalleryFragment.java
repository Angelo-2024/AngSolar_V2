package solar.angsolar.activity.ui.gallery;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import solar.angsolar.R;
import solar.angsolar.activity.ui.DatabaseElement.BaseContract;
import solar.angsolar.activity.ui.DatabaseElement.BaseDbHelper;

public class GalleryFragment extends Fragment {

    private BaseDbHelper baseDbHelper;
    private EditText etNombre, etDescripcion, etConsultarNombre, etConsultarID, etEditarNombre, etEditarEstado, etEditarDescripcion;
    private TextView tvResultados;
    private Button btnEditar, btnEliminar;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_gallery, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        baseDbHelper = new BaseDbHelper(getContext());

        etNombre = view.findViewById(R.id.et_nombre);
        etDescripcion = view.findViewById(R.id.et_descripcion);
        etConsultarNombre = view.findViewById(R.id.et_consultar_nombre);
        etConsultarID = view.findViewById(R.id.et_consultar_id);
        etEditarNombre = view.findViewById(R.id.et_editar_nombre);
        etEditarEstado = view.findViewById(R.id.et_editar_estado);
        etEditarDescripcion = view.findViewById(R.id.et_editar_descripcion);
        tvResultados = view.findViewById(R.id.tv_resultados);
        btnEditar = view.findViewById(R.id.btn_editar);
        btnEliminar = view.findViewById(R.id.btn_eliminar);

        etEditarNombre.setEnabled(false);
        etEditarDescripcion.setEnabled(false);
        etEditarEstado.setEnabled(false);
        btnEditar.setEnabled(false);
        btnEliminar.setEnabled(false);

        Button btnInsertar = view.findViewById(R.id.btn_insertar);
        Button btnConsultar = view.findViewById(R.id.btn_consultar);

        btnInsertar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                insertarCategoria();
            }
        });

        btnConsultar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                consultarCategoria();
            }
        });

        btnEditar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editarCategoria();
            }
        });

        btnEliminar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                eliminarCategoria();
            }
        });
    }

    private void insertarCategoria() {
        String nombre = etNombre.getText().toString().trim();
        String descripcion = etDescripcion.getText().toString().trim();

        if (!nombre.isEmpty() && !descripcion.isEmpty()) {
            SQLiteDatabase db = baseDbHelper.getWritableDatabase();

            ContentValues values = new ContentValues();
            values.put(BaseContract.Columnas.NOMBRE, nombre);
            values.put(BaseContract.Columnas.DESCRIPCION, descripcion);
            long newRowId = db.insert(BaseContract.TABLE_NAME, null, values);

            db.close();

            if (newRowId != -1) {
                Toast.makeText(getContext(), "Registro guardado", Toast.LENGTH_SHORT).show();
                etNombre.setText("");
                etDescripcion.setText("");
            } else {
                Toast.makeText(getContext(), "Error al guardar el registro", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(getContext(), "Por favor, complete todos los campos", Toast.LENGTH_SHORT).show();
        }
    }

    private void consultarCategoria() {
        String nombre = etConsultarNombre.getText().toString().trim();
        String id = etConsultarID.getText().toString().trim();

        SQLiteDatabase db = baseDbHelper.getReadableDatabase();

        String[] projection = {
                BaseContract.Columnas.ID,
                BaseContract.Columnas.NOMBRE,
                BaseContract.Columnas.DESCRIPCION,
                BaseContract.Columnas.FECHA_CREACION,
                BaseContract.Columnas.FECHA_ACTUALIZACION,
                BaseContract.Columnas.ESTADO
        };

        String selection = null;
        String[] selectionArgs = null;

        if (!TextUtils.isEmpty(nombre)) {
            selection = BaseContract.Columnas.NOMBRE + " = ?";
            selectionArgs = new String[]{nombre};
        } else if (!TextUtils.isEmpty(id)) {
            selection = BaseContract.Columnas.ID + " = ?";
            selectionArgs = new String[]{id};
        } else {
            Toast.makeText(getContext(), "Por favor, ingrese un nombre o ID para consultar", Toast.LENGTH_SHORT).show();
            return;
        }

        Cursor cursor = db.query(
                BaseContract.TABLE_NAME,
                projection,
                selection,
                selectionArgs,
                null,
                null,
                null
        );

        if (cursor != null && cursor.moveToFirst()) {
            int idResultado = cursor.getInt(cursor.getColumnIndexOrThrow(BaseContract.Columnas.ID));
            String nombreResultado = cursor.getString(cursor.getColumnIndexOrThrow(BaseContract.Columnas.NOMBRE));
            String descripcionResultado = cursor.getString(cursor.getColumnIndexOrThrow(BaseContract.Columnas.DESCRIPCION));
            String fechaCreacion = cursor.getString(cursor.getColumnIndexOrThrow(BaseContract.Columnas.FECHA_CREACION));
            String fechaActualizacion = cursor.getString(cursor.getColumnIndexOrThrow(BaseContract.Columnas.FECHA_ACTUALIZACION));
            int estado = cursor.getInt(cursor.getColumnIndexOrThrow(BaseContract.Columnas.ESTADO));

            tvResultados.setText("ID: " + idResultado +
                    "\nNombre: " + nombreResultado +
                    "\nDescripción: " + descripcionResultado +
                    "\nFecha de Creación: " + fechaCreacion +
                    "\nFecha de Actualización: " + fechaActualizacion +
                    "\nEstado: " + estado);

            // Habilitar campos de edición y botones
            etEditarNombre.setEnabled(true);
            etEditarDescripcion.setEnabled(true);
            etEditarEstado.setEnabled(true);
            btnEditar.setEnabled(true);
            btnEliminar.setEnabled(true);

            Toast.makeText(getContext(), "Búsqueda Exitosa", Toast.LENGTH_SHORT).show();
        } else {
            tvResultados.setText("Registro no encontrado");
            Toast.makeText(getContext(), "Registro no encontrado", Toast.LENGTH_SHORT).show();
        }

        if (cursor != null) {
            cursor.close();
        }
        db.close();
    }

    private void editarCategoria() {
        String nombreConsulta = etConsultarNombre.getText().toString().trim();
        String idConsulta = etConsultarID.getText().toString().trim();
        String nuevoNombre = etEditarNombre.getText().toString().trim();
        String nuevoEstado = etEditarEstado.getText().toString().trim();
        String nuevaDescripcion = etEditarDescripcion.getText().toString().trim();

        if (TextUtils.isEmpty(nombreConsulta) && TextUtils.isEmpty(idConsulta)) {
            Toast.makeText(getContext(), "Por favor, ingrese un nombre o ID para consultar", Toast.LENGTH_SHORT).show();
            return;
        }

        if (TextUtils.isEmpty(nuevoNombre) && TextUtils.isEmpty(nuevoEstado) && TextUtils.isEmpty(nuevaDescripcion)) {
            Toast.makeText(getContext(), "Por favor, ingrese un nuevo nombre, estado o descripción para editar", Toast.LENGTH_SHORT).show();
            return;
        }

        SQLiteDatabase db = baseDbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        if (!TextUtils.isEmpty(nuevoNombre)) {
            values.put(BaseContract.Columnas.NOMBRE, nuevoNombre);
        }
        if (!TextUtils.isEmpty(nuevoEstado)) {
            try {
                int estado = Integer.parseInt(nuevoEstado);
                values.put(BaseContract.Columnas.ESTADO, estado);
            } catch (NumberFormatException e) {
                Toast.makeText(getContext(), "Estado debe ser un número", Toast.LENGTH_SHORT).show();
                return;
            }
        }
        if (!TextUtils.isEmpty(nuevaDescripcion)) {
            values.put(BaseContract.Columnas.DESCRIPCION, nuevaDescripcion);
        }

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd HH:mm:ss.SSS", Locale.getDefault());
        String fechaActualizacion = dateFormat.format(new Date());
        values.put(BaseContract.Columnas.FECHA_ACTUALIZACION, fechaActualizacion);

        String selection = null;
        String[] selectionArgs = null;

        if (!TextUtils.isEmpty(nombreConsulta)) {
            selection = BaseContract.Columnas.NOMBRE + " = ?";
            selectionArgs = new String[]{nombreConsulta};
        } else if (!TextUtils.isEmpty(idConsulta)) {
            selection = BaseContract.Columnas.ID + " = ?";
            selectionArgs = new String[]{idConsulta};
        }

        int count = db.update(
                BaseContract.TABLE_NAME,
                values,
                selection,
                selectionArgs
        );

        db.close();

        if (count > 0) {
            Toast.makeText(getContext(), "Categoría actualizada", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(getContext(), "Error al actualizar la categoría", Toast.LENGTH_SHORT).show();
        }
    }

    private void eliminarCategoria() {
        String nombre = etConsultarNombre.getText().toString().trim();
        String id = etConsultarID.getText().toString().trim();

        if (TextUtils.isEmpty(nombre) && TextUtils.isEmpty(id)) {
            Toast.makeText(getContext(), "Por favor, ingrese un nombre o ID para eliminar", Toast.LENGTH_SHORT).show();
            return;
        }

        SQLiteDatabase db = baseDbHelper.getWritableDatabase();

        String selection = null;
        String[] selectionArgs = null;

        if (!TextUtils.isEmpty(nombre)) {
            selection = BaseContract.Columnas.NOMBRE + " = ?";
            selectionArgs = new String[]{nombre};
        } else if (!TextUtils.isEmpty(id)) {
            selection = BaseContract.Columnas.ID + " = ?";
            selectionArgs = new String[]{id};
        }

        int deletedRows = db.delete(BaseContract.TABLE_NAME, selection, selectionArgs);

        db.close();

        if (deletedRows > 0) {
            Toast.makeText(getContext(), "Categoría eliminada", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(getContext(), "Error al eliminar la categoría", Toast.LENGTH_SHORT).show();
        }
    }
}
