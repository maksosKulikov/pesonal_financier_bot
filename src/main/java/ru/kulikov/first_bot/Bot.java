package ru.kulikov.first_bot;

import org.apache.logging.log4j.LogManager;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;
import ru.kulikov.first_bot.command_classes.*;
import ru.kulikov.first_bot.service_classes.*;

import org.apache.logging.log4j.Logger;


import java.sql.*;
import java.util.*;

public class Bot extends TelegramLongPollingBot {
    private static final Logger logger = LogManager.getLogger();

    private static final String URL = "jdbc:postgresql://localhost:5432/db_for_bot";
    private static final String USERNAME = "postgres";
    private static final String PASSWORD = "mk11072002";

    public static Connection connection;
    public static String[] lastDelete = new String[2];
    public static Message messageAboutDelete;
    public static Message messageForNewButtons;
    public static Long user_id;

    private final String[] not_command = new String[2];
    private int old_local_sum = 0;

    static {
        try {
            Class.forName("org.postgresql.Driver");
        }
        catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        try {
            connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public Bot () {

    }


    @Override
    public String getBotUsername() {
        return "personal_financier1_bot";
    }

    @Override
    public String getBotToken() {
        return "5421479853:AAEJp7zJy2r8yOhR95oz_XtNmDQMzi3k7jw";
    }

    @Override
    public void onUpdateReceived(Update update) {
        logger.info("New update!");
        if (update.hasCallbackQuery()) {
            logger.info("Delete a button: " + update.getCallbackQuery().getData());
            try {
                deleteMessageAboutDelete();
                int local_sum = CheckCategory.checkCategory(update.getCallbackQuery().getData());
                lastDelete[0] = update.getCallbackQuery().getData();
                lastDelete[1] = String.valueOf(local_sum);
                Message message = update.getCallbackQuery().getMessage();
                PressButton.pressButton(update);
                execute(ServiceCommand.updateButtons(message).build());
                execute(ServiceCommand.sendAnswer(message,
                        "Категория \"<b>" + update.getCallbackQuery().getData() +
                        "\"</b> с суммой \"<b>" + local_sum + "</b>\" удалена").build());
                messageForNewButtons = message;
                messageAboutDelete = execute(ServiceCommand.sendAnswer(message,
                        "Если хочешь отменить удаление, нажми /cancel_delete").build());
            } catch (SQLException | TelegramApiException e) {
                logger.error("Delete Button", e);
                throw new RuntimeException(e);
            }
        }
        else if (update.hasMessage()) {
            user_id = update.getMessage().getChatId();
            Message message = update.getMessage();
            if (message.hasText() && !message.hasEntities()) {
                logger.info("Processing message: " + update.getMessage().getText());
                try {
                    deleteMessageAboutDelete();
                    String[] textMessage = message.getText().split(" ");
                    not_command[0] = textMessage[0];
                    not_command[1] = textMessage[textMessage.length - 1];
                    try {
                        Integer.parseInt(not_command[1]);
                    }
                    catch (Exception e) {
                        execute(ServiceCommand.sendAnswer(message, "Неправильный формат").build());
                        return;
                    }
                    for (int i = 1; i < textMessage.length - 1; i++) {
                        not_command[0] += " ";
                        not_command[0] += textMessage[i];
                    }
                    old_local_sum = CheckCategory.checkCategory(not_command[0]);
                    if (old_local_sum == -1) {
                        logger.info("Create category : " + message.getText());
                        CreateCategory.createCategory(not_command);
                        execute(ServiceCommand.sendAnswer(message,
                                "Категория \"<b>" + not_command[0] + "\" </b>создана").build());
                        messageAboutDelete = execute(ServiceCommand.sendAnswer(message,
                                "Если хочешь отменить создание категории, нажми /cancel_delete").build());
                    }
                    else {
                        logger.info("Add to category - " + not_command[0]);
                        AddToCategory.addToCategory(old_local_sum, not_command);
                        execute(ServiceCommand.sendAnswer(message,
                                "Категория \"<b>" + not_command[0] + "</b>\" изменена").build());
                        messageAboutDelete = execute(ServiceCommand.sendAnswer(message,
                                "Если хочешь отменить изменение суммы, нажми /cancel_delete").build());
                    }
                }
                catch (TelegramApiException | SQLException e) {
                    logger.error("Create or Add", e);
                    throw new RuntimeException(e);
                }
            }
            else if (message.hasText() && message.hasEntities()) {
                logger.info("Execution command: " + update.getMessage().getText());
                String command = message.getText();
                switch (command) {
                    case "/delete_category":
                        try {
                            deleteMessageAboutDelete();
                            List<List<InlineKeyboardButton>> buttons = DeleteCategory.deleteCategory();
                            if (buttons.size() == 0) {
                                execute(ServiceCommand.sendAnswer(message, "<b>Список категорий пуст!</b>").build());
                            }
                            else {
                                execute(ServiceCommand.sendAnswer(message, "Выбери категорию:").
                                        replyMarkup(InlineKeyboardMarkup.builder().keyboard(buttons).build()).
                                        build());
                            }
                        } catch (TelegramApiException e) {
                            logger.error("Command /delete_category", e);
                            throw new RuntimeException(e);
                        }
                        break;
                    case "/list_expenses":
                        try {
                            deleteMessageAboutDelete();
                            String answer = ListExpenses.listExpenses();
                            if (answer.equals("\n<b>ВСЕГО - 0</b>")) {
                                execute(ServiceCommand.sendAnswer(message, "<b>Список категорий пуст!</b>").build());
                            }
                            else {
                                execute(ServiceCommand.sendAnswer(message, answer).build());
                            }
                        } catch (SQLException | TelegramApiException e) {
                            logger.error("Command /list_expenses", e);
                            throw new RuntimeException(e);
                        }
                        break;
                    case "/start":
                        try {
                            execute(ServiceCommand.sendAnswer(message, StartCommand.start()).build());
                        } catch (TelegramApiException e) {
                            logger.error("Command /start", e);
                            throw new RuntimeException(e);
                        }
                        break;
                    case "/help":
                        try {
                            execute(ServiceCommand.sendAnswer(message, HelpCommand.help()).build());
                        } catch (TelegramApiException e) {
                            logger.error("Command /help", e);
                            throw new RuntimeException(e);
                        }
                        break;
                    case "/cancel_delete":
                        try {
                            if (messageAboutDelete.getText().equals("Если хочешь отменить удаление, нажми /cancel_delete")) {
                                CancelDelete.deleteCategory();
                                execute(ServiceCommand.updateButtons(messageForNewButtons).build());
                            } else if (messageAboutDelete.getText().equals("Если хочешь отменить создание категории, нажми /cancel_delete")) {
                                CancelDelete.newCategory(not_command[0]);
                            } else if (messageAboutDelete.getText().equals("Если хочешь отменить изменение суммы, нажми /cancel_delete")) {
                                CancelDelete.newLocalSum(old_local_sum, not_command);
                            }
                            deleteMessageAboutDelete();
                            execute(ServiceCommand.deleteMessage(message).build());
                            execute(ServiceCommand.sendAnswer(message,
                                    "Прошлое измененние отменено").build());
                        } catch (TelegramApiException | SQLException e) {
                            logger.error("Command /cancel_delete", e);
                            throw new RuntimeException(e);
                        }
                        break;
                }
            }
        }
    }

    public void deleteMessageAboutDelete() throws TelegramApiException {
        if (messageAboutDelete != null) {
            execute(ServiceCommand.deleteMessage(messageAboutDelete).build());
            messageAboutDelete = null;
        }
    }

    public static void main(String[] args){
        logger.info("Bot started!");
        try {
            TelegramBotsApi botsApi = new TelegramBotsApi(DefaultBotSession.class);
            botsApi.registerBot(new Bot());
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }
}