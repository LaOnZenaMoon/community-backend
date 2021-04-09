package me.lozm.repository.board;

import me.lozm.entity.board.Board;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface BoardRepositoryPaging {

    Page<Board> selectBoardList(Pageable pageable);

}
