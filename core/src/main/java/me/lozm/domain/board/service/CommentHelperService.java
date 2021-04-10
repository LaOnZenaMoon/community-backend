package me.lozm.domain.board.service;

import lombok.RequiredArgsConstructor;
import me.lozm.domain.board.entity.Comment;
import me.lozm.domain.board.repository.CommentRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CommentHelperService {

    private final CommentRepository commentRepository;


    public Comment getComment(Long commentId) {
        return findComment(commentId)
                .orElseThrow(() -> new IllegalArgumentException(String.format("존재하지 않는 댓글입니다. 댓글 ID: [%d]", commentId)));
    }

    public Optional<Comment> findComment(Long commentId) {
        return commentRepository.findById(commentId);
    }

}
