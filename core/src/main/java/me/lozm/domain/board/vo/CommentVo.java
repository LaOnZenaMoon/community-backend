package me.lozm.domain.board.vo;

import lombok.Getter;
import me.lozm.global.code.BoardType;
import me.lozm.global.code.CommentType;
import me.lozm.global.code.ContentType;
import me.lozm.global.code.UseYn;
import me.lozm.global.object.entity.HierarchicalEntity;

import java.time.LocalDateTime;

public class CommentVo {

    @Getter
    public static class CommentList {
        private Long commentId;
        private HierarchicalEntity hierarchicalComment;
        private CommentType commentType;
        private String content;
        private UseYn commentUse;
        private LocalDateTime commentCreatedDateTime;
        private Long createdUserId;
        private String createdUserIdentifier;
        private LocalDateTime commentModifiedDateTime;
        private Long modifiedUserId;
        private String modifiedUserIdentifier;
    }

}
