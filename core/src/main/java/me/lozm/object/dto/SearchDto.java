package me.lozm.object.dto;

import lombok.Getter;
import lombok.Setter;
import me.lozm.global.code.SearchType;

@Getter
@Setter
public class SearchDto {

    private SearchType searchType;
    private String searchContent;

}
