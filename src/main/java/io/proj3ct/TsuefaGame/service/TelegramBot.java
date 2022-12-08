package io.proj3ct.TsuefaGame.service;

import com.vdurmont.emoji.EmojiParser;
import io.proj3ct.TsuefaGame.config.BotConfig;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Component
public class TelegramBot extends TelegramLongPollingBot {

    //Ножницы = 0
    //Камень = 1
    //Бумага = 2

    final String HELLO_MESSAGE = EmojiParser.parseToUnicode("Привет!\uD83D\uDC4B" + "\n"
            + "ЦУ-Е-ФА - это самая популярная и самая простая игра!\uD83C\uDFB2" + "\n"
            + "Для игры отправьте ник друга(зарегистрированного в боте).");

    final BotConfig config;

    public TelegramBot(BotConfig config) {
        this.config = config;
    }

    @Override
    public String getBotUsername() {
        return config.getBotName();
    }

    @Override
    public String getBotToken() {
        return config.getBotToken();
    }

    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            String messageText = update.getMessage().getText();
            String userName = update.getMessage().getChat().getUserName();

            long chatId = update.getMessage().getChatId();

            String path = "C:\\var\\" + userName + ".txt";
            String stringChatId = Long.toString(chatId);

            switch (messageText) {
                case "/start":
                    sendMessage(chatId, HELLO_MESSAGE);
                    registerUser(stringChatId, userName, null, path);
                    break;
                case "Ножницы✂":
                    sendMessage(chatId, "Вы выбрали ножницы!");
                    saveSubject(stringChatId, userName, "0", path);
                    break;
                case "Камень\uD83E\uDEA8":
                    sendMessage(chatId, "Вы выбрали камень!");
                    saveSubject(stringChatId, userName, "1", path);
                    break;
                case "Бумага\uD83D\uDCC3":
                    sendMessage(chatId, "Вы выбрали бумагу!");
                    saveSubject(stringChatId, userName, "2", path);
                    break;
                default:
                    String opponentName = update.getMessage().getText();
                    checkOpponent(chatId,opponentName,userName);
            }
        }
    }

    private void checkOpponent(long chatId, String opponentName, String userName){
        Path opponent = Paths.get("C:\\var\\" + opponentName + ".txt");
        if (Files.exists(opponent)) {

        } else {
            sendMessage(chatId, "Неверная команда!");
        }
    }

    private ReplyKeyboardMarkup menuSelectObject(long chatId) {
        SendMessage message = new SendMessage();
        KeyboardMenu keyboardMenu = new KeyboardMenu();

        message.setChatId(String.valueOf(chatId));
        message.setText(EmojiParser.parseToUnicode("Выбери свое оружие\uD83D\uDD2B"));
        message.setReplyMarkup(keyboardMenu.getCreateKeyboard());
        executeMessage(message);

        return keyboardMenu.getCreateKeyboard();
    }

    private void saveSubject(String stringChatId, String userName, String subject, String path) {
        registerFile(stringChatId, userName, subject, path);
    }

    private void readFile(String userName, String[] array) {
        try {
            File file = new File("C:\\var\\" + userName + ".txt");
            FileReader fr = new FileReader(file);
            BufferedReader reader = new BufferedReader(fr);
            String line = reader.readLine();
            while (line != null) {
                for (int i = 0; i < array.length; i++) {
                    array[i] = line;
                    line = reader.readLine();
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void registerUser(String stringChatId, String userName, String subject, String path) {
        registerFile(stringChatId, userName, subject, path);
    }

    private void registerFile(String stringChatId, String userName, String subject, String path) {
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(path));
            writer.write("chatId: " + stringChatId + "\n" + "userName: " + userName + "\n" + "subject: " + subject);
            writer.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void sendMessage(long chatId, String textToSend) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(String.valueOf(chatId));
        sendMessage.setText(textToSend);
        executeMessage(sendMessage);
    }

    private void executeMessage(SendMessage sendMessage) {
        try {
            execute(sendMessage);
        } catch (TelegramApiException e) {

        }
    }
}
