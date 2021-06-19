package me.lozm.domain.board.repository;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import me.lozm.domain.board.entity.Board;
import me.lozm.domain.board.entity.Comment;
import me.lozm.global.code.BoardType;
import me.lozm.global.code.UseYn;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;

import static me.lozm.domain.board.entity.QBoard.board;
import static me.lozm.domain.board.entity.QComment.comment;


@Repository
@RequiredArgsConstructor
public class BoardRepositorySupport {

    private final JPAQueryFactory jpaQueryFactory;


    public List<Board> getBoardList(BoardType boardType, Pageable pageable) {
        return jpaQueryFactory
                .select(board)
                .from(board)
                .where(
                        checkBoardType(boardType),
                        board.use.eq(UseYn.USE)
                )
                .orderBy(board.hierarchicalBoard.commonParentId.desc(), board.hierarchicalBoard.groupOrder.asc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();
    }

    public long getBoardTotalCount(BoardType boardType) {
        return jpaQueryFactory
                .select(board)
                .from(board)
                .where(
                        checkBoardType(boardType),
                        board.use.eq(UseYn.USE)
                )
                .fetchCount();
    }

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

    public List<Board> getBoardListByCommonParentId(Long commonParentId) {
        return jpaQueryFactory
                .select(board)
                .from(board)
                .where(
                        board.hierarchicalBoard.commonParentId.eq(commonParentId),
                        board.use.eq(UseYn.USE)
                )
                .orderBy(board.hierarchicalBoard.groupOrder.asc())
                .fetch();
    }

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


    private BooleanExpression checkBoardType(BoardType boardType) {
        if (boardType.equals(BoardType.ALL)) {
            return null;
        }

        return board.boardType.eq(boardType);
    }

}
