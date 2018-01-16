package comgreedyai.github.quest;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomepageActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private int position = -1;
    private String user;
    private static Activity homepageActivity;
    private ArrayList<Quiz> quizzes = new ArrayList<Quiz>();
    private ArrayList<String> quiznames = new ArrayList<String>();
    private String newQuizName;
    private String score;
    private static final int MY_PERMISSIONS_REQUEST_APP = 1;
    @BindView(R.id.new_name) EditText newName;
    @BindView(R.id.myFAB4) FloatingActionButton myFab;
    @BindView(R.id.image) ImageView image;
    private static final int REQUEST_TAKE_PHOTO = 1;
    private static final int REQUEST_CANCELLED = 2;
    private String mCurrentPhotoPath;
    private String mCurrentPhotoName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_homepage);
        homepageActivity = this;
        ButterKnife.bind(this);
        Bundle extra = getIntent().getExtras();
        user = (String) extra.get(getString(R.string.user));
        score = (String) extra.get(getString(R.string.score));
        TextView title = (TextView)findViewById(R.id.profile_header);
        title.setText("Welcome, " + user);
        TextView result = (TextView)findViewById(R.id.xp);
        if (!score.equals("") && score != null) {
            result.setText("Most recent quiz score: " + score);
        } else {
            result.setVisibility(View.INVISIBLE);
        }

        String url = "http://ec2-54-210-25-34.compute-1.amazonaws.com/94b37c98a64e0a85b8c69a34a7b97e59c0/GET/" + "PHOTO" + user + ".jpg";
        Picasso.with(image.getContext()).load(url).into(image);

        RedisServiceQuiz.getService().allKeys("QUIZ" + user + "*").enqueue(new Callback<RedisServiceQuiz.KeysResponse>() {
            @Override
            public void onResponse(Call<RedisServiceQuiz.KeysResponse> call, Response<RedisServiceQuiz.KeysResponse> response) {
                for (final String k : response.body().keys) {
                    RedisServiceQuiz.getService().getQuiz(k).enqueue(new Callback<RedisServiceQuiz.GetResponse>() {
                        @Override
                        public void onResponse(Call<RedisServiceQuiz.GetResponse> call, Response<RedisServiceQuiz.GetResponse> response) {
                            Quiz p = response.body().item;
                            quizzes.add(p);
                            quiznames.add(p.getName());
                            System.out.println(p.getName());
                            setupSpinner(quizzes, quiznames);
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

        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(this,
                Manifest.permission.INTERNET)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.INTERNET},
                    MY_PERMISSIONS_REQUEST_APP);
        }

        myFab.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if ((ContextCompat.checkSelfPermission(homepageActivity,
                        Manifest.permission.CAMERA)
                        == PackageManager.PERMISSION_GRANTED) && (ContextCompat.checkSelfPermission(homepageActivity,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        == PackageManager.PERMISSION_GRANTED) && (ContextCompat.checkSelfPermission(homepageActivity,
                        Manifest.permission.INTERNET) == PackageManager.PERMISSION_GRANTED)) {
                    dispatchTakePictureIntent();
                }
            }
        });

        Button btn = (Button) findViewById(R.id.create_quiz);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (newName.getText().toString().equals("")) {
                    if (position != -1) {
                        Intent intent = new Intent(homepageActivity, QuizCreationActivity.class);
                        intent.putExtra(getString(R.string.user), user);
                        intent.putExtra(getString(R.string.quizID), quizzes.get(position).getId());
                        intent.putExtra(getString(R.string.quiz_name), quizzes.get(position).getName());
                        startActivityForResult(intent, 4);
                        finish();
                    } else {
                        AlertDialog.Builder builder;
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                            builder = new AlertDialog.Builder(homepageActivity, android.R.style.Theme_Material_Dialog_Alert);
                        } else {
                            builder = new AlertDialog.Builder(homepageActivity);
                        }
                        builder.setTitle("Invalid Name")
                                .setMessage("Quiz names cannot be blank!")
                                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                    }
                                })
                                .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                    }
                                })
                                .setIcon(android.R.drawable.ic_dialog_alert)
                                .show();
                    }

                } else {
                    newQuizName = newName.getText().toString();
                    final Date date = Calendar.getInstance().getTime();
                    final String name = "QUIZ" + user + date.toString();
                    final Quiz newQuiz = new Quiz(user + "\t" + date.toString(), newQuizName);
                    RedisServiceQuiz.getService().addQuiz(name, newQuiz).enqueue(new Callback<RedisServiceQuiz.SetResponse>() {
                        @Override
                        public void onResponse(Call<RedisServiceQuiz.SetResponse> call, Response<RedisServiceQuiz.SetResponse> response) {
                            Intent intent = new Intent(homepageActivity, QuizCreationActivity.class);
                            intent.putExtra(getString(R.string.user), user);
                            intent.putExtra(getString(R.string.quizID), user + "\t"+ date.toString());
                            intent.putExtra(getString(R.string.quiz_name), newQuizName);
                            startActivityForResult(intent, 4);
                            finish();
                        }

                        @Override
                        public void onFailure(Call<RedisServiceQuiz.SetResponse> call, Throwable t) {
                            Toast.makeText(homepageActivity, t.toString(), Toast.LENGTH_LONG).show();
                        }
                    });
                }
            }
        });
    }

    public void setupSpinner(ArrayList<Quiz> quizzes, ArrayList<String> quiznames) {
        Spinner spinner = (Spinner) findViewById(R.id.edit_quiz_spinner);
        List<String> actualnames = quiznames.subList(0, quiznames.size());
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, actualnames);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);
    }

    public void onItemSelected(AdapterView<?> parent, View view,
                               int pos, long id) {
        this.position = pos;
    }

    public void onNothingSelected(AdapterView<?> parent) {
        // Another interface callback
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

        } else if (id == R.id.take_quest) {
            Intent intent = new Intent(homepageActivity, QuizTakingActivity.class);
            intent.putExtra(getString(R.string.user), user);
            startActivityForResult(intent, 6);
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_APP: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                } else {
                    Toast.makeText(this, R.string.persistent_toast, Toast.LENGTH_LONG).show();
                    return;
                }
                return;
            }
        }
    }

    private File createImageFile() throws IOException {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );
        mCurrentPhotoPath = image.getAbsolutePath();
        return image;
    }

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
            }
            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(this, "com.example.android.fileprovider", photoFile);
                mCurrentPhotoName = photoURI.toString();
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_TAKE_PHOTO) {
            if (resultCode == REQUEST_CANCELLED) {
                Toast.makeText(this, "Cancelled!", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Success!", Toast.LENGTH_SHORT).show();
                File imgFile = new File(mCurrentPhotoPath);
                if (imgFile.exists()) {
                    Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
                    image.setImageBitmap(myBitmap);
                    final File newImgFile = new File(mCurrentPhotoPath);
                    RequestBody reqFile = RequestBody.create(MediaType.parse("image/*"), newImgFile);
                    RedisServiceLogin.getService().postImage("PHOTO" + user, reqFile).enqueue(new Callback<RedisServiceLogin.SetResponse>() {
                        @Override
                        public void onResponse(Call<RedisServiceLogin.SetResponse> call, Response<RedisServiceLogin.SetResponse> response) {
                        }

                        @Override
                        public void onFailure(Call<RedisServiceLogin.SetResponse> call, Throwable t) {
                            Toast.makeText(homepageActivity, t.toString(), Toast.LENGTH_LONG).show();
                        }
                    });
                }
            }
        }
    }
}
