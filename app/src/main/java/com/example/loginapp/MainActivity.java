package com.example.loginapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.POST;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {
    EditText text;
    Button getbtn,postbtn;
    FirebaseDatabase database;
    DatabaseReference ref;
    Member member;
    long id = 0;
    private String txt;
    private JsonPlaceHolderApi jsonPlaceHolderApi;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        text = (EditText) findViewById(R.id.text);
        text.setMovementMethod(new ScrollingMovementMethod());
        getbtn = (Button) findViewById(R.id.getBtn);
        getbtn.setVisibility(View.INVISIBLE);
        postbtn = (Button)findViewById(R.id.postBtn);
        ref = database.getInstance().getReference().child("User");
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists())
                {
                    id  = (dataSnapshot.getChildrenCount());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        member=new Member();

        /*subbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                member.setInput(text.getText().toString());
                ref.child("User" + String.valueOf(id)).setValue(member);
                Toast.makeText(MainActivity.this,"Data inserted",Toast.LENGTH_LONG).show();
            }
        });*/

        Gson gson = new GsonBuilder()
                .setLenient()
                .create();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://71e74e96.ngrok.io/")
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        jsonPlaceHolderApi = retrofit.create(JsonPlaceHolderApi.class);

        final Call<List<Post>> call = jsonPlaceHolderApi.getPosts();

        getbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                call.enqueue(new Callback<List<Post>>() {
                    @Override
                    public void onResponse(Call<List<Post>> call, Response<List<Post>> response) {
                        if (!response.isSuccessful()) {//i.e the http code is 500 i.e data is not there
                            text.setText("Code: " + response.code());
                            Log.d("not well", response.message());
                            return;
                        }

                        List<Post> posts = response.body();
                        text.setText("");
                        for (Post post : posts) {
                            String content = "";
                            content += post.getText();
                            content += "\n\n";

                            text.append(content);
                        }
                        member.setOutput(text.getText().toString());
                        ref.child("User" + String.valueOf(id+1)).setValue(member);

                    }

                    @Override
                    public void onFailure(Call<List<Post>> call, Throwable t) {
                        text.setText(t.getMessage());
                        Log.d("done", t.getMessage());
                    }
                });

            }

        });


        /*final Call<List<Post>> call3 = jsonPlaceHolderApi.getPostAgain();
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                call3.enqueue(new Callback<List<Post>>() {
                    @Override
                    public void onResponse(Call<List<Post>> call3, Response<List<Post>> response) {
                        if (!response.isSuccessful()) {//i.e the http code is 500 i.e data is not there
                            text.setText("Code: " + response.code());
                            return;
                        }

                        List<Post> mila = response.body();
                        text.setText("");
                        for (Post post : mila) {
                            String content = "";
                            content += post.getText();
                            content += "\n\n";

                            text.append(content);
                        }
                    }
                    @Override
                    public void onFailure(Call<List<Post>> call3, Throwable t) {
                        text.setText(t.getMessage());
                    }
                });
            }
        });*/
        txt = text.getText().toString();
        Post post = new Post(txt);

        final Call<Post> call1 = jsonPlaceHolderApi.createPost(post);
        postbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                call1.enqueue(new Callback<Post>() {
                    @Override
                    public void onResponse(Call<Post> call1, Response<Post> response) {

                        if(!response.isSuccessful()){
                            text.setText("");
                            text.setText("Code: "+ response.body());
                            return;
                        }
                        member.setInput(text.getText().toString());
                        String content1 = "";
                        text.setText(content1);
                        Post postresponse = response.body();

                        content1 += postresponse.getText();

                        text.setText(content1);
                        Toast.makeText(MainActivity.this, "ho gaya connect nice", Toast.LENGTH_SHORT).show();
                        getbtn.setVisibility(View.VISIBLE);
                        postbtn.setVisibility(View.INVISIBLE);
                    }

                    @Override
                    public void onFailure(Call<Post> call, Throwable t) {
                        Toast.makeText(MainActivity.this, "fucked up", Toast.LENGTH_SHORT).show();

                    }
                });
            }
        });


    }
    public void logout(View view){
        FirebaseAuth.getInstance().signOut();
        startActivity(new Intent(getApplicationContext(),Login.class));
        finish();
    }

}
