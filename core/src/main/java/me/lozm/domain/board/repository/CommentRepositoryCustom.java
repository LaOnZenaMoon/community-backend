package me.lozm.domain.board.repository;

import me.lozm.domain.board.entity.Comment;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface CommentRepositoryCustom {

    List<Comment> getCommentList(Long boardId, Pageable pageable);

    long getCommentTotalCount(Long boardId);

    List<Comment> getCommentListByCommonParentId(Long commonParentId);

}
