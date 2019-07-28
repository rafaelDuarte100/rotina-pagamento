package br.com.pagamento.messages;

import java.util.Locale;

import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class SourceMessage {

    private final MessageSource messageSource;

    public String getMessage(String key, Object... params) {
		Locale locale = LocaleContextHolder.getLocale();
		return messageSource.getMessage(key, params, locale);
	}
}