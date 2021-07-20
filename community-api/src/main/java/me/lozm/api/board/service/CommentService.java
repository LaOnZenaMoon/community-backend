package me.lozm.api.board.service;

import lombok.RequiredArgsConstructor;
import me.lozm.domain.board.dto.CommentDto;
import me.lozm.domain.board.entity.Board;
import me.lozm.domain.board.entity.Comment;
import me.lozm.domain.board.repository.BoardRepository;
import me.lozm.domain.board.repository.BoardRepositoryImpl;
import me.lozm.domain.board.repository.CommentRepository;
import me.lozm.domain.board.service.BoardHelperService;
import me.lozm.domain.board.service.CommentHelperService;
import me.lozm.global.code.UseYn;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static java.lang.String.format;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class CommentService {

    private final BoardHelperService boardHelperService;
    private final CommentRepository commentRepository;
    private final CommentHelperService commentHelperService;


    public Page<Comment> getCommentList(Long boardId, Pageable pageable) {
        List<Comment> commentList = commentRepository.getCommentList(boardId, pageable);
        long totalCount = commentRepository.getCommentTotalCount(boardId);
        return new PageImpl<>(commentList, pageable, totalCount);
    }

    @Transactional
    public Comment addComment(CommentDto.AddRequest requestDto) {
        Board board = boardHelperService.getBoard(requestDto.getBoardId());
        Comment savedComment = commentRepository.save(CommentDto.AddRequest.createEntity(requestDto, board));
        savedComment.getHierarchicalBoard().setDefaultParentId(savedComment.getId());
        return savedComment;
    }

    @Transactional
    public Comment addRelyComment(CommentDto.AddReplyRequest requestDto) {
        Board board = boardHelperService.getBoard(requestDto.getBoardId());

        final Long commonParentId = requestDto.getCommonParentId();
        final Long parentId = requestDto.getParentId();

        List<Comment> commentList = commentRepository.getCommentListByCommonParentId(commonParentId);
        if (commentList.size() == 0) {
            throw new IllegalArgumentException(format("존재하지 않는 댓글입니다. 댓글 ID: [%d]", commonParentId));
        }

        final Comment savedComment = commentRepository.save(CommentDto.AddReplyRequest.createEntity(requestDto, board));

        // Case2: 원글에 대한 답글
        final Comment commonParentComment = commentList.get(0);
        if (commonParentComment.getId().equals(commonParentId) && commonParentComment.getId().equals(parentId)) {
            updateOrders(commentList, 0);
            savedComment.getHierarchicalBoard().setReplyInfo(
                    commonParentComment.getHierarchicalBoard().getGroupOrder(),
                    commonParentComment.getHierarchicalBoard().getGroupLayer()
            );
            return savedComment;
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
        savedComment.getHierarchicalBoard().setReplyInfo(
                commentList.get(repliedIndex).getHierarchicalBoard().getGroupOrder(),
                commentList.get(repliedIndex).getHierarchicalBoard().getGroupLayer()
        );

        return savedComment;
    }

    @Transactional
    public Comment editComment(CommentDto.EditRequest requestDto) {
        Comment comment = commentHelperService.getComment(requestDto.getId());
        comment.edit(requestDto.getCommentType(), requestDto.getContent(), requestDto.getModifiedBy(), UseYn.USE);
        return comment;
    }

    @Transactional
    public Comment removeComment(Long commentId) {
        Comment comment = commentHelperService.getComment(commentId);
        //TODO 삭제 요청자 세팅
        comment.remove(null, UseYn.NOT_USE);
        return comment;
    }


    private void updateOrders(List<Comment> commentList, int startIndex) {
        for (int i = startIndex + 1; i < commentList.size(); i++) {
            commentList.get(i).getHierarchicalBoard().setGroupOrder(commentList.get(i).getHierarchicalBoard().getGroupOrder() + 1);
        }
    }

}
