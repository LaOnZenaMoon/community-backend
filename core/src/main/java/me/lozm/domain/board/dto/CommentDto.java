package me.lozm.domain.board.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import me.lozm.domain.board.entity.Comment;
import me.lozm.domain.board.vo.CommentVo;
import me.lozm.global.code.CommentType;
import me.lozm.global.code.UseYn;
import me.lozm.global.object.dto.BaseUserDto;
import org.springframework.data.domain.Page;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

import static me.lozm.global.swagger.BoardCode.BOARD_ID_DESCRIPTION;
import static me.lozm.global.swagger.CommentCode.*;
import static me.lozm.global.swagger.CommonCode.*;

public class CommentDto {

    @Getter
    @Builder
    public static class CommentListInfo {
        @ApiModelProperty(value = COMMENT_ID_DESCRIPTION)
        private Long id;

        @ApiModelProperty(value = COMMENT_TYPE_DESCRIPTION)
        private CommentType commentType;

        @ApiModelProperty(value = CONTENT_DESCRIPTION)
        private String content;

        @ApiModelProperty(value = USE_YN_DESCRIPTION)
        private UseYn useYn;

        @ApiModelProperty(value = CREATED_DATETIME_DESCRIPTION)
        private LocalDateTime createdDateTime;

        @ApiModelProperty(value = CREATED_BY_DESCRIPTION)
        private Long createdUserId;

        @ApiModelProperty(value = CREATED_USER_LOGIN_ID_DESCRIPTION)
        private String createdUserIdentifier;

        @ApiModelProperty(value = MODIFIED_DATETIME_DESCRIPTION)
        private LocalDateTime modifiedDateTime;

        @ApiModelProperty(value = MODIFIED_BY_DESCRIPTION)
        private Long modifiedUserId;

        @ApiModelProperty(value = MODIFIED_USER_LOGIN_ID_DESCRIPTION)
        private String modifiedUserIdentifier;

        public static CommentListInfo from(CommentVo.CommentList comment) {
            return CommentListInfo.builder()
                    .id(comment.getCommentId())
                    .commentType(comment.getCommentType())
                    .content(comment.getContent())
                    .useYn(comment.getCommentUse())
                    .createdDateTime(comment.getCommentCreatedDateTime())
                    .createdUserId(comment.getCreatedUserId())
                    .createdUserIdentifier(comment.getCreatedUserIdentifier())
                    .modifiedDateTime(comment.getCommentModifiedDateTime())
                    .modifiedUserId(comment.getModifiedUserId())
                    .modifiedUserIdentifier(comment.getModifiedUserIdentifier())
                    .build();
        }
    }

    @Getter
    public static class CommentList {
        Page<CommentListInfo> commentList;

        public static CommentList createCommentList(Page<CommentVo.CommentList> boardList) {
            CommentList list = new CommentList();
            list.commentList = boardList.map(CommentListInfo::from);
            return list;
        }
    }

    @Getter
    @Builder
    public static class CommentInfo {
        @ApiModelProperty(value = COMMENT_ID_DESCRIPTION)
        private Long id;

        @ApiModelProperty(value = COMMENT_TYPE_DESCRIPTION)
        private CommentType commentType;

        @ApiModelProperty(value = CONTENT_DESCRIPTION)
        private String content;

        @ApiModelProperty(value = USE_YN_DESCRIPTION)
        private UseYn useYn;

        public static CommentInfo from(Comment comment) {
            return CommentInfo.builder()
                    .id(comment.getId())
                    .commentType(comment.getCommentType())
                    .content(comment.getContent())
                    .useYn(comment.getUse())
                    .build();
        }
    }

    @Getter
    @SuperBuilder
    @NoArgsConstructor
    public static class AddRequest extends BaseUserDto {
        @NotNull
        @ApiModelProperty(value = BOARD_ID_DESCRIPTION)
        private Long boardId;

        @NotNull
        @ApiModelProperty(value = COMMENT_TYPE_DESCRIPTION)
        private CommentType commentType;

        @NotEmpty
        @ApiModelProperty(value = CONTENT_DESCRIPTION)
        private String content;
    }

    @Getter
    @SuperBuilder
    @NoArgsConstructor
    public static class AddReplyRequest extends AddRequest {
        @NotNull
        @ApiModelProperty(value = COMMENT_ID_DESCRIPTION)
        private Long commonParentId;

        @NotNull
        @ApiModelProperty(value = COMMENT_ID_DESCRIPTION)
        private Long parentId;
    }

    @Getter
    @SuperBuilder
    @NoArgsConstructor
    public static class EditRequest extends BaseUserDto {
        @NotNull
        @ApiModelProperty(value = COMMENT_ID_DESCRIPTION)
        private Long id;

        @ApiModelProperty(value = COMMENT_TYPE_DESCRIPTION)
        private CommentType commentType;

        @ApiModelProperty(value = CONTENT_DESCRIPTION)
        private String content;

        @ApiModelProperty(value = USE_YN_DESCRIPTION)
        private UseYn useYn;
    }

    @Getter
    @SuperBuilder
    @NoArgsConstructor
    public static class RemoveRequest extends BaseUserDto {
        @NotNull
        @ApiModelProperty(value = COMMENT_ID_DESCRIPTION)
        private Long id;
    }

}
