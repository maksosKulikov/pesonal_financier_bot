package ru.kulikov.first_bot.service_classes;

import ru.kulikov.first_bot.Bot;

import java.sql.PreparedStatement;
import java.sql.SQLException;

public class AddToCategory {
    public static void addToCategory(int old_local_sum, String[] not_command) throws SQLException {
        PreparedStatement preparedStatement =
                Bot.connection.prepareStatement("UPDATE sum SET local_sum=? WHERE category=? and chat_id=?");
        preparedStatement.setInt(1, old_local_sum + Integer.parseInt(not_command[1]));
        preparedStatement.setString(2, not_command[0]);
        preparedStatement.setLong(3, Bot.user_id);
        preparedStatement.executeUpdate();
    }
}
