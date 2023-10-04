package com.hanteo.board;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.List;

@JsonInclude(JsonInclude.Include.NON_EMPTY)
public record CategoryDto(Long id,
                          CategoryType type,
                          String title,
                          List<CategoryDto> subCategory
                          ) {
}
