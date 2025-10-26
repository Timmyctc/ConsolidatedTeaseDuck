package com.luv2code.springboot.consolidatedteaseduck.config;

import com.luv2code.springboot.consolidatedteaseduck.domain.MetricType;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class MetricTypeConverter implements Converter<String, MetricType> {
    @Override
    public MetricType convert(String source) {
        return MetricType.valueOf(source.toUpperCase());
    }
}
