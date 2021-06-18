package me.lozm.api.board.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import me.lozm.api.board.service.CommentService;
import me.lozm.domain.board.dto.CommentDto;
import me.lozm.object.dto.PageDto;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Api(tags = {"게시판 댓글"})
@CrossOrigin
@RequestMapping("comment")
@RestController
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;


    @ApiOperation("게시판 댓글 목록 조회")
    @GetMapping("board/{boardId}")
    public CommentDto.ResponseList getCommentList(@PathVariable("boardId") Long boardId, PageDto pageDto) {
        return CommentDto.ResponseList.createCommentList(commentService.getCommentList(boardId, pageDto.getPageRequest()));
    }

    @ApiOperation("신규 게시판 댓글 추가")
    @PostMapping
    public CommentDto.ResponseOne addComment(@RequestBody @Valid CommentDto.AddRequest requestDto) {
        return CommentDto.ResponseOne.from(commentService.addComment(requestDto));
    }

    @ApiOperation("신규 게시판 댓글 추가")
    @PostMapping("reply")
    public CommentDto.ResponseOne addReplyComment(@RequestBody @Valid CommentDto.AddReplyRequest requestDto) {
        return CommentDto.ResponseOne.from(commentService.addRelyComment(requestDto));
    }

    @ApiOperation("게시판 댓글 수정")
    @PutMapping
    public CommentDto.ResponseOne editComment(@RequestBody @Valid CommentDto.EditRequest requestDto) {
        return CommentDto.ResponseOne.from(commentService.editComment(requestDto));
    }

    @ApiOperation("게시판 댓글 삭제")
    @DeleteMapping("{commentId}")
    public CommentDto.ResponseOne removeComment(@PathVariable("commentId") Long commentId) {
        return CommentDto.ResponseOne.from(commentService.removeComment(commentId));
    }

}
