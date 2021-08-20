package me.lozm.domain.board.repository;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import me.lozm.domain.board.entity.Comment;
import me.lozm.domain.board.vo.CommentVo;
import me.lozm.domain.user.entity.QUser;
import me.lozm.global.code.UseYn;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;

import static me.lozm.domain.board.entity.QComment.comment;


@Repository
@RequiredArgsConstructor
public class CommentRepositoryImpl implements CommentRepositoryCustom {

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public List<CommentVo.CommentList> getCommentList(Long boardId, Pageable pageable) {
        final QUser createdUser = new QUser("createdUser");
        final QUser modifiedUser = new QUser("modifiedUser");

        return jpaQueryFactory
                .select(Projections.fields(
                        CommentVo.CommentList.class,
                        comment.id.as("commentId"),
                        comment.hierarchicalComment.as("hierarchicalComment"),
                        comment.commentType.as("commentType"),
                        comment.content.as("content"),
                        comment.use.as("commentUse"),
                        comment.createdDateTime.as("commentCreatedDateTime"),
                        createdUser.id.as("createdUserId"),
                        createdUser.identifier.as("createdUserIdentifier"),
                        comment.modifiedDateTime.as("commentModifiedDateTime"),
                        modifiedUser.id.as("modifiedUserId"),
                        modifiedUser.identifier.as("modifiedUserIdentifier")
                ))
                .from(comment)
                .leftJoin(comment.createdUser, createdUser)
                .leftJoin(comment.modifiedUser, modifiedUser)
                .where(
                        comment.board.id.eq(boardId),
                        comment.use.eq(UseYn.USE)
                )
                .orderBy(comment.hierarchicalComment.commonParentId.desc(), comment.hierarchicalComment.groupOrder.asc())
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
                        comment.hierarchicalComment.commonParentId.eq(commonParentId),
                        comment.use.eq(UseYn.USE)
                )
                .orderBy(comment.hierarchicalComment.groupOrder.asc())
                .fetch();
    }

}
