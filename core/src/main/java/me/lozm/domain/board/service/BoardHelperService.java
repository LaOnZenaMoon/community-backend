package me.lozm.domain.board.service;

import lombok.RequiredArgsConstructor;
import me.lozm.domain.board.entity.Board;
import me.lozm.domain.board.repository.BoardRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BoardHelperService {

    private final BoardRepository boardRepository;


    public Board getBoard(Long boardId) {
        return findBoard(boardId)
                .orElseThrow(() -> new IllegalArgumentException(String.format("존재하지 않는 게시글입니다. 게시판 ID: [%d]", boardId)));
    }

    public Optional<Board> findBoard(Long boardId) {
        return boardRepository.findById(boardId);
    }

}
