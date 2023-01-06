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
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

@Component
public class TelegramBot extends TelegramLongPollingBot {

    //Ножницы = 0
    //Камень = 1
    //Бумага = 2

    final String HELLO_MESSAGE = EmojiParser.parseToUnicode("Привет!\uD83D\uDC4B" + "\n" + "ЦУ-Е-ФА - это самая популярная и самая простая игра!\uD83C\uDFB2" + "\n" + "Для игры отправьте ник друга(зарегистрированного в боте).");

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
                case "Да!":
                    menuSelectObject(chatId);
                    break;
                case "Нет!":
                    break;
                case "Готов✅":
                    ready(userName, chatId);
                    break;
                default:
                    checkOpponent(chatId, messageText, userName);
            }
        }
    }

    private void ready(String userName, long chatId) {
        FileCheck fileCheck = new FileCheck();
        String array[] = new String[4];
        try {
            if(fileCheck.readThirdLine(userName, array).equals("null")){
                sendMessage(chatId, "Сначала выберите оружие");
            }else {

            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void checkOpponent(long chatId, String opponentName, String userName) {
        Path opponent = Paths.get("C:\\var\\" + opponentName + ".txt");
        Path user = Paths.get("C:\\var\\" + userName + ".txt");

        String array[] = new String[4];

        if (opponentName.equals("null")) {
            sendMessage(chatId, "Укажите имя пользователя в настройках");
        } else {
            if (opponentName.equals(userName)) {
                sendMessage(chatId, "Неверное имя пользователя!");
            } else {
                if (Files.exists(opponent)) {
                    writeOpponent(opponent, userName);
                    writeOpponent(user, opponentName);
                    goPlay(opponentName, userName, array, chatId);
                } else {
                    sendMessage(chatId, "Неверная команда!");
                }
            }
        }
    }

    private void writeOpponent(Path opponent, String userName) {
        ArrayList<String> list = new ArrayList<>();
        try (Scanner scan = new Scanner(new File(String.valueOf(opponent)))) {
            while (scan.hasNextLine()) {
                list.add(scan.nextLine());
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        String[] array = list.toArray(new String[4]);
        array[3] = "opponent: " + userName;

        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(String.valueOf(opponent)));
            writer.write(array[0] + "\n" + array[1] + "\n" + array[2] + "\n" + array[3]);
            writer.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        System.out.println(Arrays.toString(array));
    }

    private void goPlay(String opponentName, String userName, String[] array, long chatId) {
        FileCheck fileCheck = new FileCheck();
        try {
            long idOpponent = Long.parseLong(fileCheck.readFirstLine(opponentName, array));
            sendMessage(idOpponent, userName + " предлагает вам сыграть, согласны?");
            menuSelectYesOrNot(idOpponent);
            menuSelectObject(chatId);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private ReplyKeyboardMarkup menuSelectYesOrNot(long chatId) {
        SendMessage message = new SendMessage();
        KeyboardYesOrNot keyboardYesOrNot = new KeyboardYesOrNot();

        message.setChatId(String.valueOf(chatId));
        message.setText(EmojiParser.parseToUnicode("Выбирай!"));
        message.setReplyMarkup(keyboardYesOrNot.getCreateKeyboard());
        executeMessage(message);

        return keyboardYesOrNot.getCreateKeyboard();
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
        registerUser(stringChatId, userName, subject, path);
    }

    private void registerUser(String stringChatId, String userName, String subject, String path) {
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(path));
            writer.write("chatId: " + stringChatId + "\n" + "userName: " + userName + "\n" + "subject: " + subject + "\n" + "opponent: " + null);
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
