package me.lozm.domain.board.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import me.lozm.domain.board.entity.Comment;
import me.lozm.global.code.UseYn;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;

import static me.lozm.domain.board.entity.QBoard.board;
import static me.lozm.domain.board.entity.QComment.comment;


@Repository
@RequiredArgsConstructor
public class CommentRepositoryImpl implements CommentRepositoryCustom {

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public List<Comment> getCommentList(Long boardId, Pageable pageable) {
        return jpaQueryFactory
                .select(comment)
                .from(comment)
                .where(comment.board.id.eq(boardId))
                .orderBy(board.hierarchicalBoard.commonParentId.desc(), board.hierarchicalBoard.groupOrder.asc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();
    }

    @Override
    public long getCommentTotalCount(Long boardId) {
        return jpaQueryFactory
                .select(comment)
                .from(comment)
                .where(
                        comment.board.id.eq(boardId),
                        comment.use.eq(UseYn.USE)
                )
                .fetchCount();
    }

    @Override
    public List<Comment> getCommentListByCommonParentId(Long commonParentId) {
        return jpaQueryFactory
                .select(comment)
                .from(comment)
                .where(
                        comment.hierarchicalBoard.commonParentId.eq(commonParentId),
                        comment.use.eq(UseYn.USE)
                )
                .orderBy(comment.hierarchicalBoard.groupOrder.asc())
                .fetch();
    }

}
