package me.lozm.domain.board.repository;

import me.lozm.domain.board.entity.Board;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface BoardRepositoryPaging {

    Page<Board> selectBoardList(Pageable pageable);

}
