package me.lozm.domain.board.vo;

import lombok.Getter;
import me.lozm.global.code.BoardType;
import me.lozm.global.code.ContentType;
import me.lozm.global.code.UseYn;
import me.lozm.global.object.entity.HierarchicalEntity;

import java.time.LocalDateTime;

public class BoardVo {

    @Getter
    public static class ListInfo {
        private Long boardId;
        private HierarchicalEntity hierarchicalBoard;
        private BoardType boardType;
        private ContentType boardContentType;
        private Long boardViewCount;
        private String boardTitle;
        private String boardContent;
        private UseYn boardUse;
        private LocalDateTime boardCreatedDateTime;
        private Long createdUserId;
        private String createdUserIdentifier;
        private LocalDateTime boardModifiedDateTime;
        private Long modifiedUserId;
        private String modifiedUserIdentifier;
    }

}
