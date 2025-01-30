package com.example.projetoteste;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class Formlogim1 extends AppCompatActivity {
    private EditText nomeC, emailC, senhaC;
    private Button confirmarC;
    private String[] mensagem = {"Faça seu cadastro", "Cadastro realizado com sucesso", "Parabéns casdastro confirmado"};
    String UsuarioID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_formlogim1);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        nomeC = findViewById(R.id.nomeC);
        emailC = findViewById(R.id.emailC);
        senhaC = findViewById(R.id.senhaC);
        confirmarC = findViewById(R.id.confirmarC);


        confirmarC.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                // Obtendo o texto dos campos
                String nomeCC = nomeC.getText().toString();
                String emailCC = emailC.getText().toString();
                String senhaCC = senhaC.getText().toString();

                // Verificando se algum campo está vazio
                if (nomeCC.isEmpty() || emailCC.isEmpty() || senhaCC.isEmpty()) {
                    // Exibindo o Snackbar com uma mensagem de erro
                    Snackbar snackbar = Snackbar.make(view, mensagem[0], Snackbar.LENGTH_LONG);
                    snackbar.setBackgroundTint(Color.BLACK);
                    snackbar.setTextColor(Color.WHITE);
                    snackbar.show();
                } else {


                    cadastrarUsuario(view);
                }
            }
        });

    }


    public void cadastrarUsuario(View v) {
        String emailCC = emailC.getText().toString();
        String senhaCC = senhaC.getText().toString();


        FirebaseAuth.getInstance().createUserWithEmailAndPassword(emailCC, senhaCC).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {

                    SalvarDadosUsuario();
                    Snackbar snackbar = Snackbar.make(v, mensagem[1], Snackbar.LENGTH_SHORT);
                    snackbar.setBackgroundTint(Color.BLACK);
                    snackbar.setTextColor(Color.WHITE);
                    snackbar.show();

                } else {
                    String erro;
                    try {
                        throw task.getException();

                    } catch (FirebaseAuthWeakPasswordException e) {

                        erro = "Digite uma senha com no minimo 6 caracteres";

                    } catch (FirebaseAuthUserCollisionException e) {
                        erro = "Essa conta ja foi cadastrada";
                    } catch (FirebaseAuthInvalidCredentialsException e) {
                        erro = "Email invalida";


                    } catch (Exception e) {
                        erro = "Erro Ao cadastrar o Usuario";

                    }

                    Snackbar snackbar = Snackbar.make(v, erro, Snackbar.LENGTH_SHORT);
                    snackbar.setBackgroundTint(Color.BLACK);
                    snackbar.setTextColor(Color.WHITE);
                    snackbar.show();
                }
            }
        });


    }

    public void SalvarDadosUsuario() {
        String nomeCC = nomeC.getText().toString().trim(); // Remove espaços extras

        if (nomeCC.isEmpty()) {
            Log.e("SalvarDadosUsuario", "O nome está vazio");
            return;
        }

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        Map<String, String> usuarios = new HashMap<>();
        usuarios.put("nome", nomeCC);  // Usando "nome" em vez de "nome7"

        String usuarioID = FirebaseAuth.getInstance().getCurrentUser().getUid();

        DocumentReference documentoreference = db.collection("usuarios").document(usuarioID);
        documentoreference.set(usuarios).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Log.d("FirestoreSuccess", "Sucesso ao salvar os dados");
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.e("FirestoreError", "Erro ao salvar os dados: " + e.getMessage());
            }
        });

    }
}