package io.proj3ct.TsuefaGame.service;

import com.vdurmont.emoji.EmojiParser;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

import java.util.ArrayList;
import java.util.List;

public class KeyboardMenu {
    private ReplyKeyboardMarkup createKeyboard(){
        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();

        List<KeyboardRow> keyboardRows = new ArrayList<>();
        KeyboardRow row = new KeyboardRow();

        settingsKeyboard(replyKeyboardMarkup);

        addButtons(replyKeyboardMarkup, keyboardRows, row);

        return replyKeyboardMarkup;
    }

    private void addButtons(ReplyKeyboardMarkup replyKeyboardMarkup, List keyboardRows, KeyboardRow row) {
        row.add(putStone());

        keyboardRows.add(row);
        row = new KeyboardRow();

        row.add(putPaper());

        keyboardRows.add(row);
        row = new KeyboardRow();

        row.add(putScissors());

        keyboardRows.add(row);
        replyKeyboardMarkup.setKeyboard(keyboardRows);
    }

    private void settingsKeyboard(ReplyKeyboardMarkup keyboardMarkup) {
        keyboardMarkup.setSelective(true);
        keyboardMarkup.setResizeKeyboard(true);
        keyboardMarkup.setOneTimeKeyboard(false);
    }

    private KeyboardButton putStone() {
        KeyboardButton stone = new KeyboardButton();
        stone.setText(EmojiParser.parseToUnicode("Камень\uD83E\uDEA8"));
        return stone;
    }

    private KeyboardButton putScissors() {
        KeyboardButton scissors = new KeyboardButton();
        scissors.setText(EmojiParser.parseToUnicode("Ножницы✂"));
        return scissors;
    }

    private KeyboardButton putPaper() {
        KeyboardButton paper = new KeyboardButton();
        paper.setText(EmojiParser.parseToUnicode("Бумага\uD83D\uDCC3"));
        return paper;
    }

    public ReplyKeyboardMarkup getCreateKeyboard(){
        return createKeyboard();
    }
}
