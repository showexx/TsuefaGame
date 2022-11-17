package io.proj3ct.TsuefaGame.service;

import com.vdurmont.emoji.EmojiParser;
import io.proj3ct.TsuefaGame.config.BotConfig;
import io.proj3ct.TsuefaGame.model.User;
import io.proj3ct.TsuefaGame.model.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@Component
public class TelegramBot extends TelegramLongPollingBot {

    //Ножницы = 0
    //Камень = 1
    //Бумага = 2

    @Autowired
    private UserRepository userRepository;

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
            long chatId = update.getMessage().getChatId();

            switch (messageText) {
                case "/start":
                    regiserUser(update.getMessage());
                    sendMessage(chatId, sendHelloMessage());
                    menuSelectObject(chatId);
                    break;
                case "Ножницы✂":
                    sendMessage(chatId,"noj");
                    break;
                default:

            }
        }
    }

    private void regiserUser(Message message) {
        if (userRepository.findById(message.getChatId()).isEmpty()) {
            var chatId = message.getChatId();
            var chat = message.getChat();

            User user = new User();
            user.setChatId(chatId);
            user.setUserName(chat.getUserName());

            userRepository.save(user);
        }
    }


    private String sendHelloMessage() {
        String helloMessage = EmojiParser.parseToUnicode("Привет!\uD83D\uDC4B" + "\n"
                + "ЦУ-Е-ФА - это самая популярная и самая простая игра!\uD83C\uDFB2" + "\n"
                + "Для игры отправьте ник друга(зарегистрированного в боте).");
        return helloMessage;
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
