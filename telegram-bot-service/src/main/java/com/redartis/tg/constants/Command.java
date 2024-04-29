package com.redartis.tg.constants;

import lombok.Getter;

@Getter
public enum Command {
    START("/start", """
            Цей бот створений для управління вашими фінансами.
            Перейти до веб-версії можна за наступним посиланням %s
            
            Як користувач ви можете надсилати транзакції у форматі "<назва> <сума>" \
            в будь-якому порядку, сума може бути як цілим так і дійсним числом.
            Також є можливість надсилати голосові транзакції в такому ж форматі.
            
            Для редагування транзакції просто зробіть її реплай і вкажіть нові дані.
            Для видалення - зробіть реплай і напишіть "delete" без кавичок.""");

    private final String alias;
    private final String description;

    Command(String alias, String description) {
        this.alias = alias;
        this.description = description;
    }
}
