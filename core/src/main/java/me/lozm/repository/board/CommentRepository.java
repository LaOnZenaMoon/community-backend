package me.lozm.repository.board;

import me.lozm.entity.board.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    @Query("SELECT C FROM Comment C WHERE C.board.id = :boardId ORDER BY C.createdDt DESC")
    List<Comment> selectCommentListByBoardId(Long boardId);

}
