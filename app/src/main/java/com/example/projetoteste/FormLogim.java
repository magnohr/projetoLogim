package com.example.projetoteste;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class FormLogim extends AppCompatActivity {
    private TextView telaCadastro;

    private EditText email, senha;
    private Button entrar;
    private ProgressBar progressBar;
    private Button entrar1;
    private String[] mensagem = {"Preencha todos os campos", "logim realizado com sucesso", "Parabéns casdastro confirmado"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_form_logim);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });


        email = findViewById(R.id.email);
        senha = findViewById(R.id.senha);
        progressBar = findViewById(R.id.progre);
        entrar = findViewById(R.id.entrar3);
        telaCadastro = findViewById(R.id.telaCadastro);

        telaCadastro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent inte = new Intent(FormLogim.this, Formlogim1.class);
                startActivity(inte);
            }
        });

        entrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String emailDigitado = email.getText().toString();
                String senhaDigitada = senha.getText().toString();

                if (emailDigitado.isEmpty() || senhaDigitada.isEmpty()) {
                    Snackbar snackbar = Snackbar.make(view, mensagem[0], Snackbar.LENGTH_SHORT);
                    snackbar.setBackgroundTint(Color.BLACK);
                    snackbar.setTextColor(Color.WHITE);
                    snackbar.show();
                } else {
                    autenticarUsuario();
                }
            }
        });


    }



    public void autenticarUsuario() {
        String emailDigitado = email.getText().toString();
        String senhaDigitada = senha.getText().toString();
        FirebaseAuth.getInstance().signInWithEmailAndPassword(emailDigitado, senhaDigitada).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    progressBar.setVisibility((View.VISIBLE));

                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            telaPrincipal();
                        }
                    }, 3000);
                }
            }
        });
    }

    private void telaPrincipal(){
  Intent intent = new Intent(FormLogim.this,TelaPrincipal.class);
  startActivity(intent);
  finish();
    }

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseUser usuarioAtual = FirebaseAuth.getInstance().getCurrentUser();

        if (usuarioAtual != null) {
            new TelaPrincipal();

        }
    }


}
