package ru.kulikov.first_bot.service_classes;

import org.telegram.telegrambots.meta.api.objects.Update;
import ru.kulikov.first_bot.Bot;

import java.sql.PreparedStatement;
import java.sql.SQLException;

public class PressButton {
    public static void pressButton(Update update) throws SQLException {
        String category = update.getCallbackQuery().getData();
        PreparedStatement preparedStatement = Bot.connection.prepareStatement("DELETE FROM sum WHERE category=? and chat_id=?");
        preparedStatement.setString(1, category);
        preparedStatement.setLong(2, Bot.user_id);
        preparedStatement.executeUpdate();
    }
}
