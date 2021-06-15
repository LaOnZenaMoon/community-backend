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

import static java.lang.String.format;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class BoardService {

    private final BoardRepository boardRepository;
    private final BoardHelperService boardHelperService;
    private final BoardRepositorySupport boardRepositorySupport;


    public Page<Board> getBoardList(BoardType boardType, Pageable pageable) {
        List<Board> boardList = boardRepositorySupport.getBoardList(boardType, pageable);
        long totalCount = boardRepositorySupport.getBoardTotalCount(boardType);
        return new PageImpl<>(boardList, pageable, totalCount);
    }

    public Board getBoardDetail(Long boardId) {
        Board board = boardHelperService.getBoard(boardId);
        board.addViewCount();
        return board;
    }

    @Transactional
    public Board addBoard(BoardDto.AddRequest requestDto) {
        // Case1: 새로운 글 추가
        Board savedEntity = boardRepository.save(BoardDto.AddRequest.createEntity(requestDto));
        savedEntity.setDefaultParentId();
        return savedEntity;
    }

    @Transactional
    public Board addReplyBoard(BoardDto.AddReplyRequest requestDto) {
        final Long commonParentId = requestDto.getCommonParentId();
        final Long parentId = requestDto.getParentId();

        List<Board> boardList = boardRepositorySupport.getBoardListByCommonParentId(commonParentId);
        if (boardList.size() == 0) {
            throw new IllegalArgumentException(format("존재하지 않는 게시글입니다. 게시판 ID: [%d]", commonParentId));
        }

        final Board savedEntity = boardRepository.save(BoardDto.AddReplyRequest.createEntity(requestDto));

        // Case2: 원글에 대한 답글
        final Board commonParentBoard = boardList.get(0);
        if (commonParentBoard.getId().equals(commonParentId) && commonParentBoard.getId().equals(parentId)) {
            for (int i = 1; i < boardList.size(); i++) {
                boardList.get(i).setGroupOrder(boardList.get(i).getGroupOrder() + 1);
            }

            savedEntity.setReplyInfo(commonParentBoard.getGroupOrder(), commonParentBoard.getGroupLayer());
            return savedEntity;
        }

        // Case3: 답글에 대한 답글
        // 답글을 달 답글 찾기
        int index = -1;
        Board repliedBoard = null;
        for (int i = 0; i < boardList.size(); i++) {
            if (boardList.get(i).getId().equals(parentId)) {
                index = i;
                repliedBoard = boardList.get(i);
                break;
            }
        }

        if (repliedBoard == null) {
            throw new IllegalArgumentException(format("존재하지 않는 게시글입니다. 게시판 ID: [%d]", parentId));
        }

        for (int i = index + 1; i < boardList.size(); i++) {
            boardList.get(i).setGroupOrder(boardList.get(i).getGroupOrder() + 1);
        }

        savedEntity.setReplyInfo(repliedBoard.getGroupOrder(), repliedBoard.getGroupLayer());
        return savedEntity;
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
