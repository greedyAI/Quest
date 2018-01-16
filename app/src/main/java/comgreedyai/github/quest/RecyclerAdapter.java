package comgreedyai.github.quest;

/**
 * Created by Waley on 2017/12/11.
 */

import android.Manifest;
import android.content.pm.PackageManager;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.QuestionViewHolder> {

    private ArrayList<Question> questions;

    public RecyclerAdapter(ArrayList<Question> questions) {
        this.questions = questions;
    }

    @Override
    public QuestionViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.question_item, parent, false);
        return new QuestionViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final QuestionViewHolder holder, int position) {
        final Question question = questions.get(position);
        String questionStatement = question.getQuestion();
        String choice1 = question.getChoices()[0];
        String choice2 = question.getChoices()[1];
        String choice3 = question.getChoices()[2];
        String choice4 = question.getChoices()[3];
        holder.qu.setText(questionStatement);
        holder.c1.setText(choice1);
        holder.c2.setText(choice2);
        holder.c3.setText(choice3);
        holder.c4.setText(choice4);
        // clicking the floating action button on the top right of every picture deletes it from the server and the app's display
        holder.picFAB.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                RedisServiceQuestion.getService().deleteQuestion("QUESTION" + question.getId().toString()).enqueue(new Callback<RedisServiceQuestion.DelResponse>() {
                    @Override
                    public void onResponse(Call<RedisServiceQuestion.DelResponse> call, Response<RedisServiceQuestion.DelResponse> response) {
                    }

                    @Override
                    public void onFailure(Call<RedisServiceQuestion.DelResponse> call, Throwable t) {
                    }
                });
                questions.remove(question);
                notifyDataSetChanged();
            }
        });
    }

    @Override
    public int getItemCount() {
        return questions.size();
    }

    public static class QuestionViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.question) TextView qu;
        public String key;
        @BindView(R.id.choice_1) TextView c1;
        @BindView(R.id.choice_2) TextView c2;
        @BindView(R.id.choice_3) TextView c3;
        @BindView(R.id.choice_4) TextView c4;
        @BindView(R.id.picFAB) FloatingActionButton picFAB;

        public QuestionViewHolder(View v) {
            super(v);
            ButterKnife.bind(this, v);
        }
    }

}
