package me.lozm.domain.board.dto;

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

public class CommentDto {

    @Getter
    @Builder
    public static class CommentListInfo {
        private Long id;
        private CommentType commentType;
        private String content;
        private UseYn useYn;
        private LocalDateTime createdDateTime;
        private Long createdUserId;
        private String createdUserIdentifier;
        private LocalDateTime modifiedDateTime;
        private Long modifiedUserId;
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
        private Long id;
        private CommentType commentType;
        private String content;
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
        private Long boardId;

        @NotNull
        private CommentType commentType;

        @NotEmpty
        private String content;
    }

    @Getter
    @SuperBuilder
    @NoArgsConstructor
    public static class AddReplyRequest extends AddRequest {
        @NotNull
        private Long commonParentId;

        @NotNull
        private Long parentId;
    }

    @Getter
    @SuperBuilder
    @NoArgsConstructor
    public static class EditRequest extends BaseUserDto {
        @NotNull
        private Long id;

        private CommentType commentType;

        private String content;

        private UseYn useYn;
    }

    @Getter
    @SuperBuilder
    @NoArgsConstructor
    public static class RemoveRequest extends BaseUserDto {
        @NotNull
        private Long id;
    }

}
