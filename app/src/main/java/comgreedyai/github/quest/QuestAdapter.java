package comgreedyai.github.quest;

import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Waley on 2017/12/13.
 */

public class QuestAdapter extends RecyclerView.Adapter<QuestAdapter.QuestViewHolder> {
    private ArrayList<Question> questions;

    public QuestAdapter(ArrayList<Question> questions) {
        this.questions = questions;
    }

    @Override
    public QuestAdapter.QuestViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.quest_item, parent, false);
        return new QuestAdapter.QuestViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final QuestAdapter.QuestViewHolder holder, int position) {
        final Question question = questions.get(position);
        String questionStatement = question.getQuestion();
        final String choice1 = question.getChoices()[0];
        final String choice2 = question.getChoices()[1];
        final String choice3 = question.getChoices()[2];
        final String choice4 = question.getChoices()[3];
        holder.qu.setText(questionStatement);
        holder.c1.setText(choice1);
        holder.c2.setText(choice2);
        holder.c3.setText(choice3);
        holder.c4.setText(choice4);
        holder.b1.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                holder.answer.setText(choice1);
            }
        });
        holder.b2.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                holder.answer.setText(choice2);
            }
        });
        holder.b3.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                holder.answer.setText(choice3);
            }
        });
        holder.b4.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                holder.answer.setText(choice4);
            }
        });
    }

    @Override
    public int getItemCount() {
        return questions.size();
    }

    public static class QuestViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.question)
        TextView qu;
        public String key;
        @BindView(R.id.choice_1) TextView c1;
        @BindView(R.id.choice_2) TextView c2;
        @BindView(R.id.choice_3) TextView c3;
        @BindView(R.id.choice_4) TextView c4;
        @BindView(R.id.chosen_answer_2) TextView answer;
        @BindView(R.id.choice_a) Button b1;
        @BindView(R.id.choice_b) Button b2;
        @BindView(R.id.choice_c) Button b3;
        @BindView(R.id.choice_d) Button b4;

        public QuestViewHolder(View v) {
            super(v);
            ButterKnife.bind(this, v);
        }
    }

}
