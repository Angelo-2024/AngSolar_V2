package solar.angsolar;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import solar.angsolar.activity.MenuActivity;
import solar.angsolar.angsolar.db.DatabaseManager;

public class MainActivity_Register extends AppCompatActivity {
    private EditText etCedula;
    private EditText etNombre;
    private EditText etEmail;
    private EditText etUsuario;
    private EditText etContrasena;
    private EditText etRepetirContrasena;
    private Button btnRegistrar;
    private DatabaseManager databaseManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_registre);

        etCedula = findViewById(R.id.et_cedula);
        etNombre = findViewById(R.id.et_nombre_usuario);
        etEmail = findViewById(R.id.et_email);
        etUsuario = findViewById(R.id.et_usuario);
        etContrasena = findViewById(R.id.et_contrasena);
        etRepetirContrasena = findViewById(R.id.et_repetir_contrasena);
        btnRegistrar = findViewById(R.id.btn_start);
        databaseManager = new DatabaseManager(this);

        TextView textViewInicio = findViewById(R.id.Navegacion_inicio);
        textViewInicio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cerrarRegistroYAbrirLogin();
            }
        });

        Animation blinkAnimation = AnimationUtils.loadAnimation(this, R.anim.blink);
        textViewInicio.startAnimation(blinkAnimation);


        etRepetirContrasena.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                verificarContrasenas();
                verificarCamposCompletos();
            }
        });

        btnRegistrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int cedula = Integer.parseInt(etCedula.getText().toString());
                String nombre = etNombre.getText().toString();
                String email = etEmail.getText().toString();
                String usuario = etUsuario.getText().toString();
                String contrasena = etContrasena.getText().toString();

                if (databaseManager.isCedulaExists(cedula)) {
                    Toast.makeText(MainActivity_Register.this, "El ID de cédula ya está registrado", Toast.LENGTH_SHORT).show();
                } else {
                    boolean success = databaseManager.registrarUsuario(cedula, nombre, email, usuario, contrasena);
                    if (success) {
                        Toast.makeText(MainActivity_Register.this, "Usuario registrado con éxito", Toast.LENGTH_SHORT).show();
                        cerrarRegistroYAbrirLogin();
                    } else {
                        Toast.makeText(MainActivity_Register.this, "Verifica tus datos", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        verificarCamposCompletos();
    }

    private boolean registrarUsuario(int cedula, String nombre, String email, String usuario, String contrasena) {
        return databaseManager.registrarUsuario(cedula, nombre, email, usuario, contrasena);
    }

    private void verificarContrasenas() {
        String contrasena = etContrasena.getText().toString();
        String repetirContrasena = etRepetirContrasena.getText().toString();

        if (contrasena.equals(repetirContrasena)) {
            etRepetirContrasena.setTextColor(getResources().getColor(android.R.color.holo_green_dark));
        } else {
            etRepetirContrasena.setTextColor(getResources().getColor(android.R.color.holo_red_dark));
        }
    }

    private void verificarCamposCompletos() {
        String cedula = etCedula.getText().toString();
        String nombre = etNombre.getText().toString();
        String email = etEmail.getText().toString();
        String usuario = etUsuario.getText().toString();
        String contrasena = etContrasena.getText().toString();
        String repetirContrasena = etRepetirContrasena.getText().toString();

        btnRegistrar.setEnabled(!cedula.isEmpty() && !nombre.isEmpty() && !email.isEmpty() && !usuario.isEmpty() &&
                !contrasena.isEmpty() && contrasena.equals(repetirContrasena));
    }

    private void cerrarRegistroYAbrirLogin() {
        Intent intent = new Intent(MainActivity_Register.this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // No cerramos la conexión aquí para que pueda reutilizarse en MainActivity
    }
}
