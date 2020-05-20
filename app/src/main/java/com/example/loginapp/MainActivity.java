package com.example.loginapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


import com.google.firebase.auth.FirebaseUser;
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
    Button getbtn,postbtn,subbtn;
    FirebaseDatabase database;
    DatabaseReference ref;
    FirebaseUser firebaseUser;
    Member member;
    long id = 0;
    private JsonPlaceHolderApi jsonPlaceHolderApi;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
            String currentuser = FirebaseAuth.getInstance().getCurrentUser().getUid();
            text = (EditText) findViewById(R.id.text);
            text.setMovementMethod(new ScrollingMovementMethod());
            getbtn = (Button) findViewById(R.id.getBtn);
            getbtn.setVisibility(View.INVISIBLE);
            postbtn = (Button) findViewById(R.id.postBtn);
            Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
            setSupportActionBar(myToolbar);
            ref = database.getInstance().getReference().child("User").child(currentuser);
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

        Gson gson = new GsonBuilder()
                .setLenient()
                .create();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://b2be3591.ngrok.io")
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
                        ref.child(String.valueOf(id+1)).setValue(member);

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
        //Post post = new Post(txt);
        String txt = "Jeff Bezos has a carrot and stick method to “motivate” his employees.\n" +
                "\n" +
                "Base salaries are slightly below par than competitors such as Google, Microsoft and Apple (in general, unless you have a very rare skill).\n" +
                "\n" +
                "The largest portion of your total compensation is offered in terms of RSUs (restricted stock units). It is not uncommon to provide over a $100,000 in Amazon RSUs to a freshly minted MBA.\n" +
                "\n" +
                "That’s where the evil takes over. Unlike Microsoft where the vesting schedule is even, at Amazon only 5% of your stock vests after year 1. The majority vests at the end of 4 years, which is why you will find many Amazonians quitting soon after their 4 year anniversary.\n" +
                "\n" +
                "Amazon also keeps a lookout on your “total compensation”. Let’s say the aMZN stock has done really well and you have a good pay year. Your stock grant that year is going to be a lot less simply because your total comp was high.\n" +
                "\n" +
                "It is not a fair company to work for.";
        //String txt = text.getText().toString().trim();
        Post post = new Post(txt);
        member.setInput(txt);
        final Call<Post> call1 = jsonPlaceHolderApi.createPost(post);
        postbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getbtn.setVisibility(View.VISIBLE);
                call1.enqueue(new Callback<Post>() {
                    @Override
                    public void onResponse(Call<Post> call1, Response<Post> response) {

                        if(!response.isSuccessful()){
                            text.setText("");
                            text.setText("Code: "+ response.body());
                            return;
                        }

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

                    }
                });
            }
        });

        /*subbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String txt = text.getText().toString().trim();
                Toast.makeText(MainActivity.this, txt, Toast.LENGTH_SHORT).show();
            }
        });*/

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
         super.onCreateOptionsMenu(menu);
         getMenuInflater().inflate(R.menu.options_menu,menu);
         return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
         super.onOptionsItemSelected(item);
         if(item.getItemId() == R.id.recent)
         {
             SendUsertoRecentActivity();
         }
         if(item.getItemId()==R.id.logout)
         {
             FirebaseAuth.getInstance().signOut();
             startActivity(new Intent(getApplicationContext(),Login.class));
             finish();
         }
         return true;
    }

    private void SendUsertoRecentActivity() {
        Intent recentIntent = new Intent(MainActivity.this,RecentActivity.class);
        startActivity(recentIntent);
        overridePendingTransition(R.anim.slide_from_left,R.anim.slideout_from_right);
    }
}
