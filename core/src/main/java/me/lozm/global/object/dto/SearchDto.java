package me.lozm.global.object.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import me.lozm.global.code.SearchType;

import static me.lozm.global.swagger.CommonCode.*;

@Getter
@Setter
public class SearchDto {

    @ApiModelProperty(value = SEARCH_TYPE_DESCRIPTION, example = SEARCH_TYPE_DESCRIPTION)
    private SearchType searchType;

    @ApiModelProperty(value = SEARCH_CONTENT_DESCRIPTION, example = SEARCH_CONTENT_EXAMPLE)
    private String searchContent;

}
