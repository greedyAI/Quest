package comgreedyai.github.quest;

/**
 * Created by Waley on 2017/12/11.
 */

public class Question {
    private String id;
    private String type;
    private String question;
    private String choice1;
    private String choice2;
    private String choice3;
    private String choice4;
    private String answer;
    private String author;

    public Question(String id, String type, String question, String choice1, String choice2, String choice3, String choice4, String answer, String author) {
        this.id = id;
        this.type = type;
        this.question = question;
        this.choice1 = choice1;
        this.choice2 = choice2;
        this.choice3 = choice3;
        this.choice4 = choice4;
        this.answer = answer;
        this.author= author;
    }

    public String getId() {
        return id;
    }
    public String getType() {
        return type;
    }

    public String getQuestion() {
        return question;
    }

    public String[] getChoices() {
        String[] array = {choice1, choice2, choice3, choice4};
        return array;
    }

    public String getAnswer() {
        return answer;
    }

    public String getAuthor() {
        return author;
    }

}
