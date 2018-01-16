package comgreedyai.github.quest;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Environment;
import android.os.Parcelable;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

import static android.R.attr.permission;

public class QuizCreationActivity extends AppCompatActivity {

    @BindView(R.id.recycler)
    RecyclerView mainPageView;
    private RecyclerView.Adapter myAdapter;;
    private ArrayList<Question> questions = new ArrayList<Question>();
    private RecyclerView.LayoutManager layoutManager;
    private String mCurrentPhotoPath;
    private String mCurrentPhotoName;
    private String user;
    private String quizID;
    private String quizName;
    private String newQuizName;
    private static Activity mainActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz_creation);
        ButterKnife.bind(this);
        mainActivity = this;
        user = getIntent().getStringExtra(getString(R.string.user));
        quizID = getIntent().getStringExtra(getString(R.string.quizID));
        quizName = getIntent().getStringExtra(getString(R.string.quiz_name));
        myAdapter = new RecyclerAdapter(questions);
        layoutManager = new LinearLayoutManager(this);
        mainPageView.setLayoutManager(layoutManager);
        mainPageView.setAdapter(myAdapter);
        setTitle(quizName);
        System.out.println("-----------------------"+"QUESTION" + user + quizID+"--------------------------");

        RedisServiceQuestion.getService().allKeys("QUESTION" + user + quizID + "*").enqueue(new Callback<RedisServiceQuestion.KeysResponse>() {
            @Override
            public void onResponse(Call<RedisServiceQuestion.KeysResponse> call, Response<RedisServiceQuestion.KeysResponse> response) {
                for (final String k : response.body().keys) {
                    RedisServiceQuestion.getService().getQuestion(k).enqueue(new Callback<RedisServiceQuestion.GetResponse>() {
                        @Override
                        public void onResponse(Call<RedisServiceQuestion.GetResponse> call, Response<RedisServiceQuestion.GetResponse> response) {
                            Question p = response.body().item;
                            questions.add(p);
                            myAdapter.notifyDataSetChanged();
                        }

                        @Override
                        public void onFailure(Call<RedisServiceQuestion.GetResponse> call, Throwable t) {
                        }
                    });
                }
            }

            @Override
            public void onFailure(Call<RedisServiceQuestion.KeysResponse> call, Throwable t) {
            }
        });

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.myFAB);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mainActivity, NewQuestionActivity.class);
                intent.putExtra(getString(R.string.user), user);
                intent.putExtra(getString(R.string.quizID), quizID);
                startActivityForResult(intent, 2);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present
        getMenuInflater().inflate(R.menu.quiz_creation, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks (new game and leaderboard)
        int id = item.getItemId();
        if (id == R.id.profile) {
            Intent intent = new Intent(mainActivity, HomepageActivity.class);
            intent.putExtra(getString(R.string.user), user);
            intent.putExtra(getString(R.string.score), "");
            startActivityForResult(intent, 1);
            finish();
        } else if (id == R.id.take_quest) {
            Intent intent = new Intent(mainActivity, QuizTakingActivity.class);
            intent.putExtra(getString(R.string.user), user);
            startActivityForResult(intent, 6);
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
            if (resultCode == 4) {
                Bundle extra = data.getExtras();
                Question newQuestion = new Question((String) extra.get(getString(R.string.id)),
                        (String) extra.get(getString(R.string.type)), (String) extra.get(getString(R.string.question)),
                        (String) extra.get(getString(R.string.choice1)), (String) extra.get(getString(R.string.choice2)),
                        (String) extra.get(getString(R.string.choice3)), (String) extra.get(getString(R.string.choice4)),
                        (String) extra.get(getString(R.string.answer)), (String) extra.get(getString(R.string.author)));
                questions.add(newQuestion);
                myAdapter.notifyDataSetChanged();
            }
    }
}
