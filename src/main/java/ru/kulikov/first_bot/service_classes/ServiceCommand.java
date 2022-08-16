package ru.kulikov.first_bot.service_classes;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageReplyMarkup;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.commands.BotCommand;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import ru.kulikov.first_bot.commands.delete_category.DeleteCategory;

import java.util.List;

import static org.telegram.telegrambots.meta.api.methods.ParseMode.HTML;

public class ServiceCommand extends BotCommand {
    public static SendMessage.SendMessageBuilder sendAnswer(Message message, String text) {
        return SendMessage.builder()
                .text(text)
                .parseMode(HTML)
                .chatId(message.getChatId().toString());
    }
    public static DeleteMessage.DeleteMessageBuilder deleteMessage(Message message) {
        return DeleteMessage.builder()
                .chatId(message.getChatId().toString())
                .messageId(message.getMessageId());
    }

    public static EditMessageReplyMarkup.EditMessageReplyMarkupBuilder updateButtons(Message message) {
        List<List<InlineKeyboardButton>> buttons = DeleteCategory.deleteCategory();
        return EditMessageReplyMarkup.builder()
                .chatId(message.getChatId().toString())
                .messageId(message.getMessageId())
                .replyMarkup(InlineKeyboardMarkup.builder().keyboard(buttons).build());
    }
}
