package me.lozm.repository.board;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import me.lozm.entity.board.Board;
import me.lozm.entity.board.Comment;
import me.lozm.object.code.BoardType;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;

import static me.lozm.entity.board.QBoard.board;
import static me.lozm.entity.board.QComment.comment;


@Repository
@RequiredArgsConstructor
public class BoardRepositorySupport {

    private final JPAQueryFactory jpaQueryFactory;


    public List<Board> getBoardListByBoardType(BoardType boardType, Pageable pageable) {
        return jpaQueryFactory
                .select(board)
                .from(board)
                        .join(board.comments, comment).fetchJoin()
                .where(
                        checkBoardType(boardType)
                )
                .orderBy(board.createdDt.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();
    }

    public long getBoardTotalCountByBoardType(BoardType boardType) {
        return jpaQueryFactory
                .select(board)
                .from(board)
                .where(
                        checkBoardType(boardType)
                )
                .fetchCount();
    }

    public List<Comment> getCommentListByBoardId(Long boardId, Pageable pageable) {
        return jpaQueryFactory
                .select(comment)
                .from(comment)
                .where(
                        comment.board.id.eq(boardId)
                )
                .orderBy(comment.createdDt.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();
    }

    public long getCommentTotalCountByBoardType(Long boardId) {
        return jpaQueryFactory
                .select(comment)
                .from(comment)
                .where(
                        comment.board.id.eq(boardId)
                )
                .fetchCount();
    }


    private BooleanExpression checkBoardType(BoardType boardType) {
        if (boardType.equals(BoardType.ALL)) {
            return null;
        }

        return board.boardType.eq(String.valueOf(boardType));
    }

}
