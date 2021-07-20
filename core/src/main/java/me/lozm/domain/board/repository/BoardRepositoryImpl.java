package me.lozm.domain.board.repository;

import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import me.lozm.domain.board.entity.Board;
import me.lozm.domain.board.entity.Comment;
import me.lozm.domain.board.vo.BoardVo;
import me.lozm.domain.user.entity.QUser;
import me.lozm.global.code.BoardType;
import me.lozm.global.code.UseYn;
import me.lozm.global.code.UsersType;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;

import static me.lozm.domain.board.entity.QBoard.board;
import static me.lozm.domain.board.entity.QComment.comment;
import static me.lozm.domain.user.entity.QUser.user;


@Repository
@RequiredArgsConstructor
public class BoardRepositoryImpl implements BoardRepositoryCustom {

    private final JPAQueryFactory jpaQueryFactory;


    @Override
    public List<BoardVo.ListInfo> getBoardList(BoardType boardType, Pageable pageable) {
        return jpaQueryFactory
                .select(Projections.fields(
                        BoardVo.ListInfo.class,
                        board.id.as("boardId"),
                        board.hierarchicalBoard.as("hierarchicalBoard"),
                        board.boardType.as("boardType"),
                        board.contentType.as("boardContentType"),
                        board.viewCount.as("boardViewCount"),
                        board.title.as("boardTitle"),
                        board.content.as("boardContent"),
                        board.createdDateTime.as("boardCreatedDateTime"),
                        user.id.coalesce(UsersType.API_SYSTEM.getCode()).as("userId"),
                        user.identifier.coalesce(UsersType.API_SYSTEM.getDescription()).as("userIdentifier")
                ))
                .from(board)
                .leftJoin(user).on(user.id.eq(board.createdBy))
                .where(
                        checkBoardType(boardType),
                        board.use.eq(UseYn.USE)
                )
                .orderBy(board.hierarchicalBoard.commonParentId.desc(), board.hierarchicalBoard.groupOrder.asc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();
    }

    @Override
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

    @Override
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


    private BooleanExpression checkBoardType(BoardType boardType) {
        if (boardType.equals(BoardType.ALL)) {
            return null;
        }

        return board.boardType.eq(boardType);
    }

}
