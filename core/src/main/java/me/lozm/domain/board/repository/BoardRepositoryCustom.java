package me.lozm.domain.board.repository;

import me.lozm.domain.board.entity.Board;
import me.lozm.domain.board.vo.BoardVo;
import me.lozm.global.code.BoardType;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface BoardRepositoryCustom {

    List<BoardVo.ListInfo> getBoardList(BoardType boardType, Pageable pageable);

    long getBoardTotalCount(BoardType boardType);

    List<Board> getBoardListByCommonParentId(Long commonParentId);

}
