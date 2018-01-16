In my original final project proposal, I was a little bit too ambitious, and intended on creating an app with much too many features than was required to get 8 points from the list of given features. As such, I was able to implement a complete app with most of the originally planned features, but with the following features omitted

1. Question type: My app currently only supports multiple-choice questions in quizzes, not a variety of types of questions as originally planned.

2. Entirely based on Redis: All of the server features are based on the Redis server given in class. Although my initial intention was to use SQLite for some of the database tables, I noted at the very end of my original final project proposal that if I was pressed for time and cannot implement all the planned features in time, I will ignore SQLite, which is exactly what I did.

3. No quiz selection for quiz taking: For my current app, anyone can take anyone else's quizzes, including their own quizzes. However, there's no feature to search for particular quizzes or narrow down the search by category/type of problems...etc.

4. Leaderboard: My app currently calculates the score of the most recent quiz submission and displays that on the user profile/homepage. However, there is no global leaderboard/XP feature or statistical analysis of the quiz-taker's performance.

Aprt from the above four omissions, the rest of my app is identical to my original proposal. However, even with these features omitted, my current app still includes 8 points from the list of given features, namely:

---Use of SharedPreferences (complements my RecyclerView feature)
---Use of ListView/RecyclerView with custom adapter (ListView for list of quizzes available to be taken, RecyclerView for adding questions to your own quizzes)
---Use of a spinner (for selecting which of the user's own quizzes he/she wants to edit upon logging in)
---Use of menu items (a menu for accessing a user's homepage and taking a quiz)
---Use of three or more Activities (six, to be precise)
---Use of an HTTP API (Redis server for networking & online data storage)
---Use of a hardware feature: camera (for the user profile picture feature)