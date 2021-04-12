package me.lozm.api.board.service;

import lombok.RequiredArgsConstructor;
import me.lozm.domain.board.dto.BoardDto;
import me.lozm.domain.board.entity.Board;
import me.lozm.domain.board.repository.BoardRepository;
import me.lozm.domain.board.repository.BoardRepositorySupport;
import me.lozm.domain.board.service.BoardHelperService;
import me.lozm.global.code.BoardType;
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
public class BoardService {

    private final BoardRepository boardRepository;
    private final BoardHelperService boardHelperService;
    private final BoardRepositorySupport boardRepositorySupport;


    public Page<Board> getBoardList(BoardType boardType, Pageable pageable) {

        List<Board> boardList = boardRepositorySupport.getBoardListByBoardType(boardType, pageable);
        long totalCount = boardRepositorySupport.getBoardTotalCountByBoardType(boardType);
        return new PageImpl<>(boardList, pageable, totalCount);
    }

    public Board getBoardDetail(Long boardId) {
        return boardHelperService.getBoard(boardId);
    }

    @Transactional
    public Board addBoard(BoardDto.AddRequest requestDto) {

        return boardRepository.save(Board.builder()
                .boardType(requestDto.getBoardType())
                .contentType(requestDto.getContentType())
                .title(requestDto.getTitle())
                .content(requestDto.getContent())
                .createdBy(requestDto.getCreatedBy())
                .use(UseYn.USE)
                .build());
    }

    @Transactional
    public Board editBoard(BoardDto.EditRequest requestDto) {

        Board board = boardHelperService.getBoard(requestDto.getId());
        board.edit(requestDto.getBoardType(), requestDto.getContentType(), requestDto.getTitle(), requestDto.getContent(), requestDto.getModifiedBy(), UseYn.USE);
        return board;
    }

    @Transactional
    public Board removeBoard(Long boardId) {

        Board board = boardHelperService.getBoard(boardId);
        board.edit(null, null, null, null, null, UseYn.NOT_USE);
        return board;
    }

}
