package me.lozm.api.board.service;

import lombok.RequiredArgsConstructor;
import me.lozm.domain.board.dto.CommentDto;
import me.lozm.domain.board.entity.Board;
import me.lozm.domain.board.entity.Comment;
import me.lozm.domain.board.repository.BoardRepositorySupport;
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

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class CommentService {

    private final BoardHelperService boardHelperService;
    private final BoardRepositorySupport boardRepositorySupport;
    private final CommentRepository commentRepository;
    private final CommentHelperService commentHelperService;


    public Page<Comment> getCommentList(Long boardId, Pageable pageable) {

        List<Comment> commentList = boardRepositorySupport.getCommentListByBoardId(boardId, pageable);
        long totalCount = boardRepositorySupport.getCommentTotalCountByBoardType(boardId);
        return new PageImpl<>(commentList, pageable, totalCount);
    }

    @Transactional
    public Comment addComment(CommentDto.AddRequest requestDto) {

        Board board = boardHelperService.getBoard(requestDto.getBoardId());

        return commentRepository.save(Comment.builder()
                .commentType(requestDto.getCommentType())
                .content(requestDto.getContent())
                .board(board)
                .createdBy(requestDto.getCreatedBy())
                .use(UseYn.USE)
                .build());
    }

    @Transactional
    public Comment editComment(CommentDto.EditRequest requestDto) {

        Comment comment = commentHelperService.getComment(requestDto.getId());
        comment.edit(requestDto.getCommentType(), requestDto.getContent(), requestDto.getModifiedBy(), UseYn.USE);
        return comment;
    }

    @Transactional
    public Comment removeComment(CommentDto.RemoveRequest requestDto) {

        Comment comment = commentHelperService.getComment(requestDto.getId());
        comment.edit(null, null, requestDto.getModifiedBy(), UseYn.NOT_USE);
        return comment;
    }
}
