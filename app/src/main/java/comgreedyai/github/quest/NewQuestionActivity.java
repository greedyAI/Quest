package comgreedyai.github.quest;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NewQuestionActivity extends AppCompatActivity {

    @BindView(R.id.description_text) EditText description;
    @BindView(R.id.answer_1) EditText answer_1;
    @BindView(R.id.answer_2) EditText answer_2;
    @BindView(R.id.answer_3) EditText answer_3;
    @BindView(R.id.answer_4) EditText answer_4;
    @BindView(R.id.correct_answer) EditText correct_answer;
    @BindView(R.id.cancel) Button cancel;
    @BindView(R.id.save) Button save;
    private static final int REQUEST_TAKE_PHOTO = 1;
    private static final int REQUEST_CANCELLED = 2;
    private String mCurrentPhotoPath;
    private String mCurrentPhotoName;
    private String user;
    private String quizID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_question);
        ButterKnife.bind(this);
        user = getIntent().getStringExtra(getString(R.string.user));
        quizID = getIntent().getStringExtra(getString(R.string.quizID));
        final Activity captureActivity = this;

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Date date = Calendar.getInstance().getTime();
                final Question newQuestion = new Question(user + quizID + date.toString(), "multiple_choice", description.getText().toString(), answer_1.getText().toString(),
                        answer_2.getText().toString(), answer_3.getText().toString(), answer_4.getText().toString(), correct_answer.getText().toString(), user);
                final String name = "QUESTION" + user  + quizID + date.toString();

                RedisServiceQuestion.getService().addQuestion(name, newQuestion).enqueue(new Callback<RedisServiceQuestion.SetResponse>() {
                    @Override
                    public void onResponse(Call<RedisServiceQuestion.SetResponse> call, Response<RedisServiceQuestion.SetResponse> response) {
                        Intent intent = new Intent();
                        intent.putExtra(getString(R.string.id), user + date.toString());
                        intent.putExtra(getString(R.string.question), description.getText().toString());
                        intent.putExtra(getString(R.string.type), "multiple_choice");
                        intent.putExtra(getString(R.string.choice1), answer_1.getText().toString());
                        intent.putExtra(getString(R.string.choice2), answer_2.getText().toString());
                        intent.putExtra(getString(R.string.choice3), answer_3.getText().toString());
                        intent.putExtra(getString(R.string.choice4), answer_4.getText().toString());
                        intent.putExtra(getString(R.string.answer), correct_answer.getText().toString());
                        intent.putExtra(getString(R.string.author), user);
                        setResult(4, intent);
                        finish();
                    }

                    @Override
                    public void onFailure(Call<RedisServiceQuestion.SetResponse> call, Throwable t) {
                        Toast.makeText(captureActivity, t.toString(), Toast.LENGTH_LONG).show();
                    }
                });
            }
        });

    }

}
