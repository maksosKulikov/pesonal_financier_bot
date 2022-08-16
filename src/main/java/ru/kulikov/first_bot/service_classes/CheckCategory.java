package ru.kulikov.first_bot.service_classes;

import ru.kulikov.first_bot.Bot;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class CheckCategory {
    public static int checkCategory(String category) throws SQLException {
        int old_local_sum = -1;
        PreparedStatement preparedStatement =
                Bot.connection.prepareStatement("SELECT category, local_sum FROM sum WHERE chat_id=?");
        preparedStatement.setLong(1, Bot.user_id);
        ResultSet resultSet = preparedStatement.executeQuery();
        while (resultSet.next() && old_local_sum == -1) {
            if (resultSet.getString("category").equals(category)) {
                old_local_sum = resultSet.getInt("local_sum");
            }
        }
        return old_local_sum;
    }
}
