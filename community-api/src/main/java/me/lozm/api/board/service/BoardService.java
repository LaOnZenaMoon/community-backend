package me.lozm.api.board.service;

import lombok.RequiredArgsConstructor;
import me.lozm.domain.board.dto.BoardDto;
import me.lozm.domain.board.entity.Board;
import me.lozm.domain.board.repository.BoardRepository;
import me.lozm.domain.board.service.BoardHelperService;
import me.lozm.domain.board.vo.BoardVo;
import me.lozm.domain.user.entity.User;
import me.lozm.domain.user.service.UserHelperService;
import me.lozm.global.code.BoardType;
import me.lozm.global.code.UseYn;
import me.lozm.global.object.dto.SearchDto;
import me.lozm.global.object.entity.HierarchicalEntity;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

import static java.lang.String.format;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class BoardService {

    private final BoardRepository boardRepository;
    private final BoardHelperService boardHelperService;
    private final UserHelperService userHelperService;


    public BoardDto.BoardList getBoardList(BoardType boardType, Pageable pageable, SearchDto searchDto) {
        final List<BoardVo.ListInfo> boardList = boardRepository.getBoardList(boardType, pageable, searchDto);
        long totalCount = boardRepository.getBoardTotalCount(boardType, searchDto);
        return BoardDto.BoardList.createBoardList(new PageImpl<>(boardList, pageable, totalCount));
    }

    @Transactional
    public BoardDto.BoardInfo getBoardDetail(Long boardId) {
        Board board = boardHelperService.getBoard(boardId);
        board.addViewCount();
        return BoardDto.BoardInfo.from(board);
    }

    @Transactional
    public BoardDto.BoardInfo addBoard(BoardDto.AddRequest requestDto) {
        // Case1: 새로운 글 추가
        final User createdUser = userHelperService.getUser(requestDto.getCreatedBy(), UseYn.USE);
        Board savedBoard = boardRepository.save(Board.builder()
                .hierarchicalBoard(HierarchicalEntity.createEntity())
                .boardType(requestDto.getBoardType())
                .contentType(requestDto.getContentType())
                .viewCount(0L)
                .title(requestDto.getTitle())
                .content(requestDto.getContent())
                .createdBy(createdUser.getId())
                .createdUser(createdUser)
                .createdDateTime(LocalDateTime.now())
                .use(UseYn.USE)
                .build());
        savedBoard.getHierarchicalBoard().setDefaultParentId(savedBoard.getId());
        return BoardDto.BoardInfo.from(savedBoard);
    }

    @Transactional
    public BoardDto.BoardInfo addReplyBoard(BoardDto.AddReplyRequest requestDto) {
        final User createdUser = userHelperService.getUser(requestDto.getCreatedBy(), UseYn.USE);

        final Long commonParentId = requestDto.getCommonParentId();
        final Long parentId = requestDto.getParentId();

        List<Board> boardList = boardRepository.getBoardListByCommonParentId(commonParentId);
        if (boardList.size() == 0) {
            throw new IllegalArgumentException(format("존재하지 않는 게시글입니다. 게시판 ID: [%d]", commonParentId));
        }

        final Board savedBoard = boardRepository.save(Board.builder()
                .hierarchicalBoard(HierarchicalEntity.createEntity(requestDto.getCommonParentId(), requestDto.getParentId()))
                .boardType(requestDto.getBoardType())
                .contentType(requestDto.getContentType())
                .viewCount(0L)
                .title(requestDto.getTitle())
                .content(requestDto.getContent())
                .createdBy(createdUser.getId())
                .createdUser(createdUser)
                .createdDateTime(LocalDateTime.now())
                .use(UseYn.USE)
                .build());

        // Case2: 원글에 대한 답글
        final Board commonParentBoard = boardList.get(0);
        if (commonParentBoard.getId().equals(commonParentId) && commonParentBoard.getId().equals(parentId)) {
            updateOrders(boardList, 0);
            savedBoard.getHierarchicalBoard().setReplyInfo(
                    commonParentBoard.getHierarchicalBoard().getGroupOrder(),
                    commonParentBoard.getHierarchicalBoard().getGroupLayer()
            );

            return BoardDto.BoardInfo.from(savedBoard);
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

        return BoardDto.BoardInfo.from(savedBoard);
    }

    @Transactional
    public BoardDto.BoardInfo editBoard(BoardDto.EditRequest requestDto) {
        Board board = boardHelperService.getBoard(requestDto.getId());
        final User modifiedUser = userHelperService.getUser(requestDto.getModifiedBy(), UseYn.USE);
        board.edit(
                modifiedUser,
                requestDto.getUseYn(),
                requestDto.getBoardType(),
                requestDto.getContentType(),
                requestDto.getTitle(),
                requestDto.getContent()
        );
        return BoardDto.BoardInfo.from(board);
    }

    @Transactional
    public BoardDto.BoardInfo removeBoard(Long boardId, Long userId) {
        Board board = boardHelperService.getBoard(boardId);
        final User modifiedUser = userHelperService.getUser(userId, UseYn.USE);
        board.remove(modifiedUser);
        return BoardDto.BoardInfo.from(board);
    }


    private void updateOrders(List<Board> boardList, int startIndex) {
        for (int i = startIndex + 1; i < boardList.size(); i++) {
            boardList.get(i).getHierarchicalBoard().setGroupOrder(boardList.get(i).getHierarchicalBoard().getGroupOrder() + 1);
        }
    }

}
