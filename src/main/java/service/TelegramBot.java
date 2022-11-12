package service;

import config.BotConfig;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

public class TelegramBot extends TelegramLongPollingBot {
    BotConfig config;

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
                    sendMessage(chatId, "Hi");
                    break;
                default:

                    sendMessage(chatId, "Sorry, command was not recognized");
            }
        }
    }

    private void sendMessage(long chatId, String textToSend) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setText(textToSend);
        sendMessage.setChatId(String.valueOf(chatId));
        try {
            execute(sendMessage);
        } catch (TelegramApiException e) {

        }
    }
}
