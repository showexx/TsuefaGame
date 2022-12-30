package io.proj3ct.TsuefaGame.service;

import com.vdurmont.emoji.EmojiParser;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

import java.util.ArrayList;
import java.util.List;

public class KeyboardYesOrNot {
    private ReplyKeyboardMarkup createKeyboard() {
        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();

        List<KeyboardRow> keyboardRows = new ArrayList<>();
        KeyboardRow row = new KeyboardRow();

        settingsKeyboard(replyKeyboardMarkup);

        addButtons(replyKeyboardMarkup, keyboardRows, row);

        return replyKeyboardMarkup;
    }

    private void addButtons(ReplyKeyboardMarkup replyKeyboardMarkup, List keyboardRows, KeyboardRow row) {
        row.add(putYes());

        keyboardRows.add(row);
        row = new KeyboardRow();

        row.add(putNo());

        keyboardRows.add(row);
        replyKeyboardMarkup.setKeyboard(keyboardRows);
    }

    private void settingsKeyboard(ReplyKeyboardMarkup keyboardMarkup) {
        keyboardMarkup.setSelective(true);
        keyboardMarkup.setResizeKeyboard(true);
        keyboardMarkup.setOneTimeKeyboard(false);
    }

    private KeyboardButton putYes() {
        KeyboardButton stone = new KeyboardButton();
        stone.setText(EmojiParser.parseToUnicode("Да!"));
        return stone;
    }

    private KeyboardButton putNo() {
        KeyboardButton scissors = new KeyboardButton();
        scissors.setText(EmojiParser.parseToUnicode("Нет!"));
        return scissors;
    }

    public ReplyKeyboardMarkup getCreateKeyboard() {
        return createKeyboard();
    }
}

