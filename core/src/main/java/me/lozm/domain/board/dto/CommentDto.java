package me.lozm.domain.board.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import me.lozm.domain.board.entity.Comment;
import me.lozm.global.code.CommentType;
import me.lozm.global.code.UseYn;
import me.lozm.global.common.BaseUserDto;
import org.springframework.data.domain.Page;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

public class CommentDto {

    @Getter
    @Builder
    public static class ResponseListInfo {
        private Long id;
        private CommentType commentType;
        private String content;
        private UseYn use;

        public static CommentDto.ResponseListInfo from(Comment comment) {
            return CommentDto.ResponseListInfo.builder()
                    .id(comment.getId())
                    .commentType(comment.getCommentType())
                    .content(comment.getContent())
                    .use(comment.getUse())
                    .build();
        }
    }

    @Getter
    public static class ResponseList {
        Page<CommentDto.ResponseListInfo> commentList;

        public static CommentDto.ResponseList createCommentList(Page<Comment> boardList) {
            CommentDto.ResponseList list = new CommentDto.ResponseList();
            list.commentList = boardList.map(CommentDto.ResponseListInfo::from);
            return list;
        }
    }

    @Getter
    @Builder
    public static class ResponseOne {
        private Long id;
        private CommentType commentType;
        private String content;
        private UseYn use;

        public static CommentDto.ResponseOne from(Comment comment) {
            return ResponseOne.builder()
                    .id(comment.getId())
                    .commentType(comment.getCommentType())
                    .content(comment.getContent())
                    .use(comment.getUse())
                    .build();
        }
    }

    @Getter
    @Builder
    public static class AddRequest extends BaseUserDto {
        @NotNull
        private Long boardId;

        @NotNull
        private CommentType commentType;

        @NotEmpty
        private String content;
    }

    @Getter
    @Builder
    public static class EditRequest extends BaseUserDto {
        @NotNull
        private Long id;

        private CommentType commentType;

        private String content;
    }

    @Getter
    @NoArgsConstructor
    public static class RemoveRequest extends BaseUserDto {
        @NotNull
        private Long id;
    }

}
