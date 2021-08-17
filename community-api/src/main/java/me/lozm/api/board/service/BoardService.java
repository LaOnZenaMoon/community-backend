package me.lozm.api.board.service;

import lombok.RequiredArgsConstructor;
import me.lozm.domain.board.dto.BoardDto;
import me.lozm.domain.board.entity.Board;
import me.lozm.domain.board.repository.BoardRepository;
import me.lozm.domain.board.service.BoardHelperService;
import me.lozm.domain.board.vo.BoardVo;
import me.lozm.global.code.BoardType;
import me.lozm.global.code.UseYn;
import me.lozm.global.object.dto.SearchDto;
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


    public Page<BoardVo.ListInfo> getBoardList(BoardType boardType, Pageable pageable, SearchDto searchDto) {
        final List<BoardVo.ListInfo> boardList = boardRepository.getBoardList(boardType, pageable, searchDto);
        long totalCount = boardRepository.getBoardTotalCount(boardType, searchDto);
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
        Board savedBoard = boardRepository.save(BoardDto.AddRequest.createEntity(requestDto));
        savedBoard.getHierarchicalBoard().setDefaultParentId(savedBoard.getId());
        return savedBoard;
    }

    @Transactional
    public Board addReplyBoard(BoardDto.AddReplyRequest requestDto) {
        final Long commonParentId = requestDto.getCommonParentId();
        final Long parentId = requestDto.getParentId();

        List<Board> boardList = boardRepository.getBoardListByCommonParentId(commonParentId);
        if (boardList.size() == 0) {
            throw new IllegalArgumentException(format("존재하지 않는 게시글입니다. 게시판 ID: [%d]", commonParentId));
        }

        final Board savedBoard = boardRepository.save(BoardDto.AddReplyRequest.createEntity(requestDto));

        // Case2: 원글에 대한 답글
        final Board commonParentBoard = boardList.get(0);
        if (commonParentBoard.getId().equals(commonParentId) && commonParentBoard.getId().equals(parentId)) {
            updateOrders(boardList, 0);
            savedBoard.getHierarchicalBoard().setReplyInfo(
                    commonParentBoard.getHierarchicalBoard().getGroupOrder(),
                    commonParentBoard.getHierarchicalBoard().getGroupLayer()
            );
            return savedBoard;
        }

        // Case3: 답글에 대한 답글
        int repliedIndex = -1;
        for (int i = 0; i < boardList.size(); i++) {
            if (boardList.get(i).getId().equals(parentId)) {
                repliedIndex = i;
                break;
            }
        }

        if (repliedIndex == -1) {
            throw new IllegalArgumentException(format("존재하지 않는 게시글입니다. 게시판 ID: [%d]", parentId));
        }

        updateOrders(boardList, repliedIndex);
        savedBoard.getHierarchicalBoard().setReplyInfo(
                boardList.get(repliedIndex).getHierarchicalBoard().getGroupOrder(),
                boardList.get(repliedIndex).getHierarchicalBoard().getGroupLayer()
        );

        return savedBoard;
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
        //TODO 삭제 요청자 세팅
        board.remove(null, UseYn.NOT_USE);
        return board;
    }


    private void updateOrders(List<Board> boardList, int startIndex) {
        for (int i = startIndex + 1; i < boardList.size(); i++) {
            boardList.get(i).getHierarchicalBoard().setGroupOrder(boardList.get(i).getHierarchicalBoard().getGroupOrder() + 1);
        }
    }

}
