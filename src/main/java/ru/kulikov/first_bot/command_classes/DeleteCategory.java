package ru.kulikov.first_bot.command_classes;

import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import ru.kulikov.first_bot.Bot;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class DeleteCategory {
    public static List<List<InlineKeyboardButton>> deleteCategory() {
        try {
            List<List<InlineKeyboardButton>> buttons = new ArrayList<>();
            Statement statement = Bot.connection.createStatement();
            String SQL = "SELECT category, local_sum FROM sum WHERE chat_id=" + Bot.user_id;
            ResultSet resultSet = statement.executeQuery(SQL);
            while (resultSet.next()) {
                buttons.add(List.of(InlineKeyboardButton.builder()
                        .text(resultSet.getString("category"))
                        .callbackData(resultSet.getString("category"))
                        .build()));
            }
            return buttons;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
