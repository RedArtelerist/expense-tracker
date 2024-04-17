package com.redartis.tg.util;

public interface TelegramBotAnswer {
    String ACCOUNT_NOT_FOUND_MESSAGE = """
            It looks like you don't have an account yet. \
            Send /start to create a personal account \
            or register account with web application: %s""";
    String TRANSACTION_MESSAGE_INVALID = """
            We were unable to recognize your message.
            Make sure the amount and item are correct and try again""";
    String MERGE_REQUEST_TEXT = """
            Hello, you added me to the group chat, \
            now I will track the transactions of all users in this chat.

            Do you want to transfer your financial data and share it?""";
    String MERGE_REQUEST_COMPLETED_DEFAULT_TEXT = "Happy sharing!";
    String MERGE_REQUEST_COMPLETED_TEXT = "Account data has been transferred";
    String REGISTRATION_INFO_TEXT = """
            To register your account correctly, \
            make sure that at the time of adding the bot to the chat, \
            you are the only one in the chat with the bot. \
            After transferring data, you can add other users""";
    String INVALID_TRANSACTION_TO_DELETE = "Invalid transaction for deletion";
    String SUCCESSFUL_DELETION_TRANSACTION =
            "This entry has been successfully deleted!";
    String SUCCESSFUL_UPDATE_TRANSACTION_TEXT =
            "Entry successfully modified.\n";
    String INVALID_UPDATE_TRANSACTION_TEXT = """
            Invalid transaction for change.
            You may have selected a transaction that has already been changed or a bot message""";

    String VOICE_MESSAGE_TOO_LONG = """
            Unfortunately, we can't recognize a voice message longer than %d seconds - \
            try breaking it down into smaller parts :)""";

}
