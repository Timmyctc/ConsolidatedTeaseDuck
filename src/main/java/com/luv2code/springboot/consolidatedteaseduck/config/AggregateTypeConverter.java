package com.luv2code.springboot.consolidatedteaseduck.config;

import com.luv2code.springboot.consolidatedteaseduck.domain.AggregationType;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class AggregateTypeConverter implements Converter<String, AggregationType> {
    @Override
    public AggregationType convert(String source) {
        return AggregationType.valueOf(source.toUpperCase());
    }
}
