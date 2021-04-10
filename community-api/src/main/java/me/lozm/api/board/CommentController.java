package me.lozm.api.board;

import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import me.lozm.domain.board.dto.CommentDto;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Api(tags = {"게시판 댓글"})
@RequestMapping("comment")
@RestController
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;


    @GetMapping("board/{boardId}")
    public CommentDto.ResponseList getCommentList(@PathVariable(value = "boardId") Long boardId, Pageable pageable) {

        return CommentDto.ResponseList.createCommentList(commentService.getCommentList(boardId, pageable));
    }

    @PostMapping
    public CommentDto.ResponseOne addComment(@RequestBody @Valid CommentDto.AddRequest requestDto) {

        return CommentDto.ResponseOne.from(commentService.addComment(requestDto));
    }

    @PutMapping
    public CommentDto.ResponseOne editComment(@RequestBody @Valid CommentDto.EditRequest requestDto) {

        return CommentDto.ResponseOne.from(commentService.editComment(requestDto));
    }

    @DeleteMapping
    public CommentDto.ResponseOne removeComment(@RequestBody @Valid CommentDto.RemoveRequest requestDto) {

        return CommentDto.ResponseOne.from(commentService.removeComment(requestDto));
    }

}
