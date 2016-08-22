package ru.sergal.testtask.localization;

import org.springframework.context.support.ReloadableResourceBundleMessageSource;

import java.util.Locale;

public class MessageLocalization {

    private ReloadableResourceBundleMessageSource messageSource;

    public MessageLocalization(ReloadableResourceBundleMessageSource messageSource) {
        this.messageSource = messageSource;
    }

    public String getMessage(String message){
        return messageSource.getMessage(message, null, Locale.getDefault());
    }

    public String getMessage(String message, Object... params) {
        return messageSource.getMessage(message, params, Locale.getDefault());
    }

}
