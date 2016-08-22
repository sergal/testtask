package ru.sergal.testtask;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import ru.sergal.testtask.localization.LocalizationConfig;
import ru.sergal.testtask.localization.MessageLocalization;

@Configuration
@Import(LocalizationConfig.class)
@ComponentScan(basePackages = "ru.sergal.testtask")
public class AppConfig {

    @Autowired
    private ReloadableResourceBundleMessageSource messageSource;

    @Bean
    public MessageLocalization loc() {
        return new MessageLocalization(messageSource);
    }
}
