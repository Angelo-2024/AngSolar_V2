package solar.angsolar;

import android.content.Intent;
import android.content.SharedPreferences;  // Importar SharedPreferences
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import solar.angsolar.activity.FuntionActivity;
import solar.angsolar.activity.MenuActivity;
import solar.angsolar.MainActivity_Register; // Añadir la importación para MainActivity_Register
import solar.angsolar.angsolar.db.DatabaseManager;

public class MainActivity extends AppCompatActivity {
    private EditText etUsuario;
    private EditText etContrasena;
    private Button btnIniciarSesion;
    private DatabaseManager databaseManager;
    private TextView textViewMail; // Añadir la declaración para el TextView

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        etUsuario = findViewById(R.id.et_usuario);
        etContrasena = findViewById(R.id.et_contrasena);
        btnIniciarSesion = findViewById(R.id.btn_start);
        databaseManager = new DatabaseManager(this);

        textViewMail = findViewById(R.id.mail); // Inicializar el TextView

        btnIniciarSesion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String usuario = etUsuario.getText().toString();
                String contrasena = etContrasena.getText().toString();

                if (databaseManager.verificarCredenciales(usuario, contrasena)) {
                    // Si las credenciales son correctas, mostrar un mensaje de éxito
                    Toast.makeText(MainActivity.this, "Inicio de sesión exitoso", Toast.LENGTH_SHORT).show();

                    // Obtener cédula y correo electrónico almacenados en SharedPreferences
                    SharedPreferences sharedPreferences = getSharedPreferences("USER_PREFS", MODE_PRIVATE);
                    String cedula = sharedPreferences.getString("cedula", "No disponible");
                    String email = sharedPreferences.getString("email", "No disponible");

                    // Mostrar el correo electrónico en el TextView
                    textViewMail.setText(email);

                    etUsuario.setText("");
                    etContrasena.setText("");
                    finish();
                    Intent intent = new Intent(MainActivity.this, MenuActivity.class);
                    startActivity(intent);
                } else {
                    Toast.makeText(MainActivity.this, "Error en las credenciales de acceso", Toast.LENGTH_SHORT).show();
                }
            }
        });

        TextView textViewRegistrar = findViewById(R.id.text_nuevo_register);
        textViewRegistrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, MainActivity_Register.class);
                startActivity(intent);
                Toast.makeText(MainActivity.this, "Ingreso a Registro exitoso", Toast.LENGTH_SHORT).show();
            }
        });

        Animation blinkAnimation = AnimationUtils.loadAnimation(this, R.anim.blink);
        textViewRegistrar.startAnimation(blinkAnimation);

        TextView textViewInvitado = findViewById(R.id.text_invitado);
        textViewInvitado.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, FuntionActivity.class);
                startActivity(intent);
                Toast.makeText(MainActivity.this, "Ingreso como Invitado, acceso limitado", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
