package comgreedyai.github.quest;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Waley on 2017/12/13.
 */

public class QuizTakingAdapter extends ArrayAdapter<Quiz> {

    LayoutInflater inflator;
    public QuizTakingAdapter(Context context, ArrayList<Quiz> players) {
        super(context, R.layout.quiz_item, players);
        inflator = LayoutInflater.from(context);
    }

    @Override
    @NonNull
    public View getView(int position, View view, @NonNull ViewGroup parent) {
        if (view == null) {
            view = inflator.inflate(R.layout.quiz_item, parent, false);
        }
        Quiz quiz = getItem(position);
        String text = quiz.getName();
        ((TextView) view.findViewById(R.id.one_quiz)).setText(text);
        String text2 = "Author: " + quiz.getId().split("\t")[0] + "\n" + "Created: " + quiz.getId().split("\t")[1];
        ((TextView) view.findViewById(R.id.one_author)).setText(text2);
        return view;
    }

}
