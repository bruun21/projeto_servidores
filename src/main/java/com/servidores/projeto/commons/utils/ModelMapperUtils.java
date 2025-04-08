package com.servidores.projeto.commons.utils;

import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.stereotype.Component;

@Component
public class ModelMapperUtils {

    public void mapNonNullFields(Object source, Object destination) {
        ModelMapper localMapper = new ModelMapper();
        localMapper.getConfiguration()
                .setSkipNullEnabled(true)
                .setMatchingStrategy(MatchingStrategies.STRICT);

        localMapper.map(source, destination);
    }
}