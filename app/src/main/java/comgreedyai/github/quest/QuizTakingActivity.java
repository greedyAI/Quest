package comgreedyai.github.quest;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.SearchManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class QuizTakingActivity extends AppCompatActivity {

    private ArrayList<Quiz> quizzes = new ArrayList<Quiz>();
    private static Activity quizTakingActivity;
    private String user;
    private String newQuizName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz_taking);
        user = getIntent().getStringExtra(getString(R.string.user));
        quizTakingActivity = this;
        RedisServiceQuiz.getService().allKeys("QUIZ*").enqueue(new Callback<RedisServiceQuiz.KeysResponse>() {
            @Override
            public void onResponse(Call<RedisServiceQuiz.KeysResponse> call, Response<RedisServiceQuiz.KeysResponse> response) {
                for (final String k : response.body().keys) {
                    RedisServiceQuiz.getService().getQuiz(k).enqueue(new Callback<RedisServiceQuiz.GetResponse>() {
                        @Override
                        public void onResponse(Call<RedisServiceQuiz.GetResponse> call, Response<RedisServiceQuiz.GetResponse> response) {
                            Quiz p = response.body().item;
                            quizzes.add(p);
                            setAdapter();
                        }
                        @Override
                        public void onFailure(Call<RedisServiceQuiz.GetResponse> call, Throwable t) {
                        }
                    });
                }
            }
            @Override
            public void onFailure(Call<RedisServiceQuiz.KeysResponse> call, Throwable t) {
            }
        });
    }

    public void setAdapter() {
        ListView listView = (ListView) findViewById(R.id.list_view);
        QuizTakingAdapter adapter = new QuizTakingAdapter(this, quizzes);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new ListClickHandler());
    }

    public class ListClickHandler implements AdapterView.OnItemClickListener {

        @Override
        public void onItemClick(AdapterView<?> adapter, View view, int position, long arg3) {
            TextView listText = (TextView) view.findViewById(R.id.one_author);
            String text = listText.getText().toString();
            String text1 = text.split("\n")[0];
            String text2 = text.split("\n")[1];
            text = text1.substring(8) + "\t"+ text2.substring(9);
            System.out.println("------------------------------"+text+"-------------------------");
            Intent intent = new Intent(QuizTakingActivity.quizTakingActivity, QuizQuestingActivity.class);
            intent.putExtra(getString(R.string.selected_quiz), text);
            intent.putExtra(getString(R.string.selected_quiz_owner), text1.substring(8));
            intent.putExtra(getString(R.string.user), user);
            startActivity(intent);
        }
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
            Intent intent = new Intent(quizTakingActivity, HomepageActivity.class);
            intent.putExtra(getString(R.string.user), user);
            intent.putExtra(getString(R.string.score), "");
            startActivityForResult(intent, 1);
            finish();
        } else if (id == R.id.take_quest) {

        }
        return super.onOptionsItemSelected(item);
    }
}