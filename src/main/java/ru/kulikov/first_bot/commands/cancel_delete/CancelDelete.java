package ru.kulikov.first_bot.commands.cancel_delete;

import ru.kulikov.first_bot.Bot;
import ru.kulikov.first_bot.service_classes.CreateCategory;

import java.sql.PreparedStatement;
import java.sql.SQLException;

public class CancelDelete {
    public static void deleteCategory() throws SQLException {
        CreateCategory.createCategory(Bot.lastDelete);

    }
    public static void newLocalSum(int old_local_sum, String[] not_command) throws SQLException {
        PreparedStatement preparedStatement =
                Bot.connection.prepareStatement("UPDATE sum SET local_sum=? WHERE category=? and chat_id=?");
        preparedStatement.setInt(1, old_local_sum);
        preparedStatement.setString(2, not_command[0]);
        preparedStatement.setLong(3, Bot.user_id);
        preparedStatement.executeUpdate();
    }
    public static void newCategory(String category) throws SQLException {
        PreparedStatement preparedStatement = Bot.connection.prepareStatement("DELETE FROM sum WHERE category=? and chat_id=?");
        preparedStatement.setString(1, category);
        preparedStatement.setLong(2, Bot.user_id);
        preparedStatement.executeUpdate();
    }
}
