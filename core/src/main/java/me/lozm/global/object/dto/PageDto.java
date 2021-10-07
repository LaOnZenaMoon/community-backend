package me.lozm.global.object.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Setter;
import org.springframework.data.domain.PageRequest;

import static java.lang.String.format;
import static me.lozm.global.config.CommonConfig.MAX_PAGE_SIZE;
import static me.lozm.global.swagger.CommonCode.*;

@Setter
public class PageDto {

    @ApiModelProperty(value = PAGE_NUMBER_DESCRIPTION, example = PAGE_NUMBER_EXAMPLE)
    private int pageNumber;

    @ApiModelProperty(value = PAGE_SIZE_DESCRIPTION, example = PAGE_SIZE_EXAMPLE)
    private int pageSize;


    public int getPageNumber() {
        return pageNumber;
    }

    public int getPageSize() {
        return pageSize == 0 ? 10 : pageSize;
    }

    public void checkPageSize() {
        if (getPageSize() > MAX_PAGE_SIZE) {
            throw new IllegalArgumentException(format("페이지 크기는 %d 을 초과할 수 없습니다.", MAX_PAGE_SIZE));
        }
    }

    public PageRequest getPageRequest() {
        checkPageSize();
        return PageRequest.of(getPageNumber(), getPageSize());
    }

}
