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
            Intent loginIntent = new Intent(MainActivity.this,Login.class);
            startActivity(loginIntent);
            String currentuser = FirebaseAuth.getInstance().getCurrentUser().getUid();
            text = (EditText) findViewById(R.id.text);
            text.setMovementMethod(new ScrollingMovementMethod());
            getbtn = (Button) findViewById(R.id.getBtn);
            /*getbtn.setVisibility(View.INVISIBLE);*/
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
                .baseUrl("http://dc4d7956.ngrok.io")
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
        String txt = "Those Who Are Resilient Stay In The Game Longer\n" +
                "\t“On the mountains of truth you can never climb in vain: either you will reach a point higher up today, or you will be training your powers so that you will be able to climb higher tomorrow.” — Friedrich Nietzsche\n" +
                "\tChallenges and setbacks are not meant to defeat you, but promote you. However, I realise after many years of defeats, it can crush your spirit and it is easier to give up than risk further setbacks and disappointments. Have you experienced this before? To be honest, I don’t have the answers. I can’t tell you what the right course of action is; only you will know. However, it’s important not to be discouraged by failure when pursuing a goal or a dream, since failure itself means different things to different people. To a person with a Fixed Mindset failure is a blow to their self-esteem, yet to a person with a Growth Mindset, it’s an opportunity to improve and find new ways to overcome their obstacles. Same failure, yet different responses. Who is right and who is wrong? Neither. Each person has a different mindset that decides their outcome. Those who are resilient stay in the game longer and draw on their inner means to succeed.\n" +
                "\tI’ve coached mummy and mom clients who gave up after many years toiling away at their respective goal or dream. It was at that point their biggest breakthrough came. Perhaps all those years of perseverance finally paid off. It was the 19th Century’s minister Henry Ward Beecher who once said: “One’s best success comes after their greatest disappointments.” No one knows what the future holds, so your only guide is whether you can endure repeated defeats and disappointments and still pursue your dream. Consider the advice from the American academic and psychologist Angela Duckworth who writes in Grit: The Power of Passion and Perseverance: “Many of us, it seems, quit what we start far too early and far too often. Even more than the effort a gritty person puts in on a single day, what matters is that they wake up the next day, and the next, ready to get on that treadmill and keep going.”\n" +
                "\tI know one thing for certain: don’t settle for less than what you’re capable of, but strive for something bigger. Some of you reading this might identify with this message because it resonates with you on a deeper level. For others, at the end of their tether the message might be nothing more than a trivial pep talk. What I wish to convey irrespective of where you are in your journey is: NEVER settle for less. If you settle for less, you will receive less than you deserve and convince yourself you are justified to receive it.\n" +
                "\t“Two people on a precipice over Yosemite Valley” by Nathan Shipps on Unsplash\n" +
                "\tDevelop A Powerful Vision Of What You Want\n" +
                "\t“Your problem is to bridge the gap which exists between where you are now and the goal you intend to reach.” — Earl Nightingale\n" +
                "\tI recall a passage my father often used growing up in 1990s: “Don’t tell me your problems unless you’ve spent weeks trying to solve them yourself.” That advice has echoed in my mind for decades and became my motivator. Don’t leave it to other people or outside circumstances to motivate you because you will be let down every time. It must come from within you. Gnaw away at your problems until you solve them or find a solution. Problems are not stop signs, they are advising you that more work is required to overcome them. Most times, problems help you gain a skill or develop the resources to succeed later. So embrace your challenges and develop the grit to push past them instead of retreat in resignation. Where are you settling in your life right now? Could you be you playing for bigger stakes than you are? Are you willing to play bigger even if it means repeated failures and setbacks? You should ask yourself these questions to decide whether you’re willing to put yourself on the line or settle for less. And that’s fine if you’re content to receive less, as long as you’re not regretful later.\n" +
                "\tIf you have not achieved the success you deserve and are considering giving up, will you regret it in a few years or decades from now? Only you can answer that, but you should carve out time to discover your motivation for pursuing your goals. It’s a fact, if you don’t know what you want you’ll get what life hands you and it may not be in your best interest, affirms author Larry Weidel: “Winners know that if you don’t figure out what you want, you’ll get whatever life hands you.” The key is to develop a powerful vision of what you want and hold that image in your mind. Nurture it daily and give it life by taking purposeful action towards it.\n" +
                "\tVision + desire + dedication + patience + daily action leads to astonishing success. Are you willing to commit to this way of life or jump ship at the first sign of failure? I’m amused when I read questions written by millennials on Quora who ask how they can become rich and famous or the next Elon Musk. Success is a fickle and long game with highs and lows. Similarly, there are no assurances even if you’re an overnight sensation, to sustain it for long, particularly if you don’t have the mental and emotional means to endure it. This means you must rely on the one true constant in your favour: your personal development. The more you grow, the more you gain in terms of financial resources, status, success — simple. If you leave it to outside conditions to dictate your circumstances, you are rolling the dice on your future.\n" +
                "\tSo become intentional on what you want out of life. Commit to it. Nurture your dreams. Focus on your development and if you want to give up, know what’s involved before you take the plunge. Because I assure you, someone out there right now is working harder than you, reading more books, sleeping less and sacrificing all they have to realise their dreams and it may contest with yours. Don’t leave your dreams to chance.\n" +
                "\t";

        Post post = new Post(txt);
        member.setInput(txt);
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
