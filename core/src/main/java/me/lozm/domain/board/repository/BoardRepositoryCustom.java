package me.lozm.domain.board.repository;

import me.lozm.domain.board.entity.Board;
import me.lozm.domain.board.vo.BoardVo;
import me.lozm.global.code.BoardType;
import me.lozm.global.object.dto.SearchDto;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface BoardRepositoryCustom {

    List<BoardVo.BoardList> getBoardList(BoardType boardType, Pageable pageable, SearchDto searchDto);

    long getBoardTotalCount(BoardType boardType, SearchDto searchDto);

    List<Board> getBoardListByCommonParentId(Long commonParentId);

}
