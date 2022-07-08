package ru.kulikov.first_bot.service_classes;

import ru.kulikov.first_bot.Bot;

import java.sql.PreparedStatement;
import java.sql.SQLException;

public class CreateCategory {
    public static void createCategory(String[] not_command) throws SQLException {
        PreparedStatement preparedStatement =
                Bot.connection.prepareStatement("INSERT INTO sum VALUES(?, ?, ?)");
        preparedStatement.setString(1, not_command[0]);
        preparedStatement.setInt(2, Integer.parseInt(not_command[1]));
        preparedStatement.setLong(3, Bot.user_id);
        preparedStatement.executeUpdate();
    }
}
