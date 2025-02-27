package com.bumsoap.store.dto;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ObjMapper {
    private ModelMapper mapper = new ModelMapper();

    public <S, D> D mapToDto(S object, Class<D> dtoClass) {
        return mapper.map(object, dtoClass);
    }
}
