package me.lozm.api.board.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import me.lozm.api.board.service.CommentService;
import me.lozm.domain.board.dto.CommentDto;
import me.lozm.global.object.dto.PageDto;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Api(tags = {"게시판 댓글"})
@RequestMapping("comment")
@RestController
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;


    @ApiOperation("게시판 댓글 목록 조회")
    @GetMapping("board/{boardId}")
    public CommentDto.CommentList getCommentList(@PathVariable("boardId") Long boardId, PageDto pageDto) {
        return commentService.getCommentList(boardId, pageDto.getPageRequest());
    }

    @ApiOperation("게시판 댓글 상세 조회")
    @GetMapping("{commentId}")
    public CommentDto.CommentInfo getBoardDetail(@PathVariable("commentId") Long commentId) {
        return commentService.getCommentDetail(commentId);
    }

    @ApiOperation("신규 게시판 댓글 추가")
    @PostMapping
    public CommentDto.CommentInfo addComment(@RequestBody @Valid CommentDto.AddRequest requestDto) {
        return commentService.addComment(requestDto);
    }

    @ApiOperation("신규 게시판 댓글 추가")
    @PostMapping("reply")
    public CommentDto.CommentInfo addReplyComment(@RequestBody @Valid CommentDto.AddReplyRequest requestDto) {
        return commentService.addRelyComment(requestDto);
    }

    @ApiOperation("게시판 댓글 수정")
    @PutMapping("{commentId}")
    public CommentDto.CommentInfo editComment(@PathVariable("commentId") Long commentId, @RequestBody @Valid CommentDto.EditRequest requestDto) {
        return commentService.editComment(requestDto);
    }

    @ApiOperation("게시판 댓글 삭제")
    @DeleteMapping("{commentId}/user/{userId}")
    public CommentDto.CommentInfo removeComment(@PathVariable("commentId") Long commentId, @PathVariable("userId") Long userId) {
        return commentService.removeComment(commentId, userId);
    }

}
