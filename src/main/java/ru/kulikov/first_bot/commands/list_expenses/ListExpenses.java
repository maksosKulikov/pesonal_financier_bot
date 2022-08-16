package ru.kulikov.first_bot.commands.list_expenses;

import ru.kulikov.first_bot.Bot;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ListExpenses {

    public static String listExpenses() throws SQLException {
        StringBuilder answer = new StringBuilder();
        int sum = 0;
        PreparedStatement preparedStatement =
                Bot.connection.prepareStatement("SELECT * FROM sum WHERE chat_id=? ORDER BY local_sum");
        preparedStatement.setLong(1, Bot.user_id);
        ResultSet resultSet = preparedStatement.executeQuery();
        while (resultSet.next()) {
            String category = resultSet.getString("category");
            int local_sum = resultSet.getInt("local_sum");
            sum += local_sum;
            answer.append(category).append(" - ").append(local_sum).append("\n");
        }
        answer.append("\n<b>ВСЕГО - ").append(sum).append("</b>");
        return answer.toString();
    }
}
