package comgreedyai.github.quest;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class QuizQuestingActivity extends AppCompatActivity {

    @BindView(R.id.recycler_2)
    RecyclerView mainPageView;
    private RecyclerView.Adapter myAdapter;;
    private ArrayList<Question> questions = new ArrayList<Question>();
    private ArrayList<String> answers = new ArrayList<String>();
    private RecyclerView.LayoutManager layoutManager;
    private String user;
    private String curuser;
    private String quizID;
    private String quizName;
    private String newQuizName;
    private static Activity questActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz_questing);
        ButterKnife.bind(this);
        questActivity = this;
        user = getIntent().getStringExtra(getString(R.string.selected_quiz_owner));
        curuser = getIntent().getStringExtra(getString(R.string.user));
        quizID = getIntent().getStringExtra(getString(R.string.selected_quiz));
        quizName = getIntent().getStringExtra(getString(R.string.quiz_name));
        myAdapter = new QuestAdapter(questions);
        layoutManager = new LinearLayoutManager(this);
        mainPageView.setLayoutManager(layoutManager);
        mainPageView.setAdapter(myAdapter);
        setTitle(quizName);

        RedisServiceQuestion.getService().allKeys("QUESTION" + user + quizID + "*").enqueue(new Callback<RedisServiceQuestion.KeysResponse>() {
            @Override
            public void onResponse(Call<RedisServiceQuestion.KeysResponse> call, Response<RedisServiceQuestion.KeysResponse> response) {
                for (final String k : response.body().keys) {
                    RedisServiceQuestion.getService().getQuestion(k).enqueue(new Callback<RedisServiceQuestion.GetResponse>() {
                        @Override
                        public void onResponse(Call<RedisServiceQuestion.GetResponse> call, Response<RedisServiceQuestion.GetResponse> response) {
                            Question p = response.body().item;
                            questions.add(p);
                            answers.add(p.getAnswer());
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

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.myFAB2);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int j = 0;
                for (int i = 0; i < myAdapter.getItemCount(); i++) {
                    TextView tv = mainPageView.findViewHolderForAdapterPosition(i).itemView.findViewById(R.id.chosen_answer_2);
                    String answer = tv.getText().toString();
                    if (answer.equals(answers.get(i))) {
                        j++;
                    }
                }
                Intent intent = new Intent(questActivity, HomepageActivity.class);
                intent.putExtra(getString(R.string.user), curuser);
                intent.putExtra(getString(R.string.score), Integer.toString(j) + "/" + Integer.toString(myAdapter.getItemCount()));
                startActivityForResult(intent, 5);
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
            Intent intent = new Intent(questActivity, HomepageActivity.class);
            intent.putExtra(getString(R.string.user), curuser);
            intent.putExtra(getString(R.string.score), "");
            startActivityForResult(intent, 1);
            finish();
        } else if (id == R.id.take_quest) {
            Intent intent = new Intent(questActivity, QuizTakingActivity.class);
            intent.putExtra(getString(R.string.user), curuser);
            startActivityForResult(intent, 6);
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

}
