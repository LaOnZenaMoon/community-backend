package me.lozm.api.board;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import me.lozm.domain.board.dto.BoardDto;
import me.lozm.global.code.BoardType;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Api(tags = {"게시판"})
@RequestMapping("board")
@RestController
@RequiredArgsConstructor
public class BoardController {

    private final BoardService boardService;


    @ApiOperation(value = "게시판 목록 조회")
    @GetMapping("boardType/{boardType}")
    public BoardDto.ResponseList getBoardList(@PathVariable(value = "boardType") BoardType boardType,
                                              Pageable pageable) {

        return BoardDto.ResponseList.createBoardList(boardService.getBoardList(boardType, pageable));
    }

    @ApiOperation(value = "게시판 상세 조회")
    @GetMapping("{boardId}")
    public BoardDto.ResponseOne getBoardDetail(@PathVariable(value = "boardId") Long boardId) {
        return BoardDto.ResponseOne.from(boardService.getBoardDetail(boardId));
    }

    @ApiOperation(value = "게시판 추가")
    @PostMapping
    public void addBoard(@RequestBody @Valid BoardDto.AddRequest requestDto) {
        boardService.addBoard(requestDto);
    }

    @ApiOperation(value = "게시판 수정")
    @PutMapping
    public void editBoard(@RequestBody @Valid BoardDto.EditRequest requestDto) {
        boardService.editBoard(requestDto);
    }

    @ApiOperation(value = "게시판 삭제")
    @DeleteMapping
    public void removeBoard(@RequestBody @Valid BoardDto.RemoveRequest requestDto) {
        boardService.removeBoard(requestDto);
    }

}
