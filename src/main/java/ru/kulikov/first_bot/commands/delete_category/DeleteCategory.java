package ru.kulikov.first_bot.commands.delete_category;

import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import ru.kulikov.first_bot.Bot;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DeleteCategory {
    public static List<List<InlineKeyboardButton>> deleteCategory() {
        try {
            List<List<InlineKeyboardButton>> buttons = new ArrayList<>();
            PreparedStatement preparedStatement =
                    Bot.connection.prepareStatement("SELECT category, local_sum FROM sum WHERE chat_id=?");
            preparedStatement.setLong(1, Bot.user_id);
            ResultSet resultSet = preparedStatement.executeQuery();
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
