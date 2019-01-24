package com.genauth.sys.config;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;

import util.APIConstants;

@Configuration
public class DateConvertConfig {
	
	@Bean
	public Converter<String, Date> addNewConvert() {
		return new Converter<String, Date>() {
			@Override
			public Date convert(String source) {
				Date date = null;
				for(String df : APIConstants.commonDateFormatters) {
					SimpleDateFormat sdf = new SimpleDateFormat(df);
					try {
						date = sdf.parse(source);
						break;
					}catch(ParseException e) {
						continue;
					}
				}
				return date;
			}
		};
	}
}
