package me.lozm.api.board.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.lozm.domain.board.dto.CommentDto;
import me.lozm.domain.board.entity.Board;
import me.lozm.domain.board.entity.Comment;
import me.lozm.domain.board.repository.CommentRepository;
import me.lozm.domain.board.service.BoardHelperService;
import me.lozm.domain.board.service.CommentHelperService;
import me.lozm.domain.board.vo.CommentVo;
import me.lozm.domain.user.entity.User;
import me.lozm.domain.user.service.UserHelperService;
import me.lozm.global.code.UseYn;
import me.lozm.global.code.UsersType;
import me.lozm.global.object.entity.HierarchicalEntity;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

import static java.lang.String.format;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class CommentService {

    private final BoardHelperService boardHelperService;
    private final CommentRepository commentRepository;
    private final CommentHelperService commentHelperService;
    private final UserHelperService userHelperService;


    public CommentDto.CommentList getCommentList(Long boardId, Pageable pageable) {
        List<CommentVo.CommentList> commentList = commentRepository.getCommentList(boardId, pageable);
        long totalCount = commentRepository.getCommentTotalCount(boardId);
        return CommentDto.CommentList.createCommentList(new PageImpl<>(commentList, pageable, totalCount));
    }

    public CommentDto.CommentInfo getCommentDetail(Long commentId) {
        return CommentDto.CommentInfo.from(commentHelperService.getComment(commentId));
    }

    @Transactional
    public CommentDto.CommentInfo addComment(CommentDto.AddRequest requestDto) {
        Board board = boardHelperService.getBoard(requestDto.getBoardId());
        final User createdUser = userHelperService.getUser(requestDto.getCreatedBy(), UseYn.USE);
        Comment savedComment = commentRepository.save(Comment.builder()
                .hierarchicalComment(HierarchicalEntity.createEntity())
                .commentType(requestDto.getCommentType())
                .content(requestDto.getContent())
                .board(board)
                .createdUser(createdUser)
                .createdDateTime(LocalDateTime.now())
                .use(UseYn.USE)
                .build());
        savedComment.getHierarchicalComment().setDefaultParentId(savedComment.getId());

        return CommentDto.CommentInfo.from(savedComment);
    }

    @Transactional
    public CommentDto.CommentInfo addRelyComment(CommentDto.AddReplyRequest requestDto) {
        Board board = boardHelperService.getBoard(requestDto.getBoardId());

        User createdUser = userHelperService.getUser(requestDto.getCreatedBy(), UseYn.USE);

        final Long commonParentId = requestDto.getCommonParentId();
        final Long parentId = requestDto.getParentId();

        List<Comment> commentList = commentRepository.getCommentListByCommonParentId(commonParentId);
        if (commentList.size() == 0) {
            throw new IllegalArgumentException(format("존재하지 않는 댓글입니다. 댓글 ID: [%d]", commonParentId));
        }

        final Comment savedComment = commentRepository.save(Comment.builder()
                .hierarchicalComment(HierarchicalEntity.createEntity(requestDto.getCommonParentId(), requestDto.getParentId()))
                .commentType(requestDto.getCommentType())
                .content(requestDto.getContent())
                .board(board)
                .createdUser(createdUser)
                .createdDateTime(LocalDateTime.now())
                .use(UseYn.USE)
                .build());

        // Case2: 원글에 대한 답글
        final Comment commonParentComment = commentList.get(0);
        if (commonParentComment.getId().equals(commonParentId) && commonParentComment.getId().equals(parentId)) {
            updateOrders(commentList, 0);
            savedComment.getHierarchicalComment().setReplyInfo(
                    commonParentComment.getHierarchicalComment().getGroupOrder(),
                    commonParentComment.getHierarchicalComment().getGroupLayer()
            );
            return CommentDto.CommentInfo.from(savedComment);
        }

        // Case3: 답글에 대한 답글
        int repliedIndex = -1;
        for (int i = 0; i < commentList.size(); i++) {
            if (commentList.get(i).getId().equals(parentId)) {
                repliedIndex = i;
                break;
            }
        }

        if (repliedIndex == -1) {
            throw new IllegalArgumentException(format("존재하지 않는 댓글입니다. 댓글 ID: [%d]", parentId));
        }

        updateOrders(commentList, repliedIndex);
        savedComment.getHierarchicalComment().setReplyInfo(
                commentList.get(repliedIndex).getHierarchicalComment().getGroupOrder(),
                commentList.get(repliedIndex).getHierarchicalComment().getGroupLayer()
        );

        return CommentDto.CommentInfo.from(savedComment);
    }

    @Transactional
    public CommentDto.CommentInfo editComment(CommentDto.EditRequest requestDto) {
        Comment comment = commentHelperService.getComment(requestDto.getId());
        if (comment.getCreatedUser() == null) {
            throw new IllegalStateException("댓글의 최초 작성자 정보가 없어서, 수정할 수 없습니다.");
        }

        final User modifiedUser = userHelperService.getUser(requestDto.getModifiedBy(), UseYn.USE);
        if (!comment.getCreatedUser().getId().equals(modifiedUser.getId())) {
            throw new IllegalArgumentException(String.format("댓글의 최초 작성자만 수정할 수 있습니다. 수정 요청자 ID: [%s], Identifier: [%s]",
                    modifiedUser.getId(), modifiedUser.getIdentifier()));
        }

        comment.edit(
                modifiedUser,
                requestDto.getUseYn(),
                requestDto.getCommentType(),
                requestDto.getContent());
        return CommentDto.CommentInfo.from(comment);
    }

    @Transactional
    public CommentDto.CommentInfo removeComment(Long commentId, Long userId) {
        Comment comment = commentHelperService.getComment(commentId);
        if (comment.getCreatedUser() == null) {
            throw new IllegalStateException("댓글의 최초 작성자 정보가 없어서, 삭제할 수 없습니다.");
        }

        final User modifiedUser = userHelperService.getUser(userId, UseYn.USE);
        if (modifiedUser.getType() == UsersType.ADMIN || modifiedUser.getType() == UsersType.MANAGER) {
            log.debug(String.format("관리자 권한으로 댓글을 삭제합니다. 댓글 ID: [%d]", comment.getId()));
        } else if (!comment.getCreatedUser().getId().equals(modifiedUser.getId())) {
            throw new IllegalArgumentException(String.format("댓글의 최초 작성자만 삭제할 수 있습니다. 삭제 요청자 ID: [%s], Identifier: [%s]",
                    modifiedUser.getId(), modifiedUser.getIdentifier()));
        }

        comment.remove(modifiedUser);
        return CommentDto.CommentInfo.from(comment);
    }


    private void updateOrders(List<Comment> commentList, int startIndex) {
        for (int i = startIndex + 1; i < commentList.size(); i++) {
            commentList.get(i).getHierarchicalComment().setGroupOrder(commentList.get(i).getHierarchicalComment().getGroupOrder() + 1);
        }
    }

}
