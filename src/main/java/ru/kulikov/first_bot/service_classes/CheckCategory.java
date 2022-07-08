package ru.kulikov.first_bot.service_classes;

import ru.kulikov.first_bot.Bot;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class CheckCategory {
    public static int checkCategory(String category) throws SQLException {
        int old_local_sum = -1;
        Statement statement = Bot.connection.createStatement();
        String SQL_categories = "SELECT category, local_sum FROM sum WHERE chat_id=" + Bot.user_id;
        ResultSet resultSet = statement.executeQuery(SQL_categories);
        while (resultSet.next() && old_local_sum == -1) {
            if (resultSet.getString("category").equals(category)) {
                old_local_sum = resultSet.getInt("local_sum");
            }
        }
        return old_local_sum;
    }
}
