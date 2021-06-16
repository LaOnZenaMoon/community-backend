package me.lozm.domain.board.service;

import lombok.RequiredArgsConstructor;
import me.lozm.domain.board.entity.Board;
import me.lozm.domain.board.repository.BoardRepository;
import me.lozm.global.code.UseYn;
import org.springframework.stereotype.Service;

import java.util.Optional;

import static java.lang.String.format;

@Service
@RequiredArgsConstructor
public class BoardHelperService {

    private final BoardRepository boardRepository;


    public Board getBoard(Long boardId) {
        final Board board = findBoard(boardId).orElseThrow(() -> new IllegalArgumentException(format("존재하지 않는 게시글입니다. 게시판 ID: [%d]", boardId)));
        if (board.getUse() == UseYn.NOT_USE) {
            throw new IllegalArgumentException(format("삭제된 게시글입니다. 게시판 ID: [%d]", boardId));
        }
        return board;
    }

    public Optional<Board> findBoard(Long boardId) {
        return boardRepository.findById(boardId);
    }

}
