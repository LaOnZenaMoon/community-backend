package me.lozm.api.board.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import me.lozm.global.code.*;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Api(tags = {"코드"})
@RestController
@RequestMapping("code")
@RequiredArgsConstructor
public class CodeController {

    @Cacheable(cacheNames = "getDefaultCache", keyGenerator = "customKeyGenerator")
    @ApiOperation(value = "게시판 유형 코드 조회")
    @GetMapping("board-type")
    public List<String> getBoardType() {
        return Stream.of(BoardType.values())
                .map(Enum::name)
                .collect(Collectors.toList());
    }

    @Cacheable(cacheNames = "getDefaultCache", keyGenerator = "customKeyGenerator")
    @ApiOperation(value = "게시판 내용 유형 코드 조회")
    @GetMapping("content-type")
    public List<String> getContentType() {
        return Stream.of(ContentType.values())
                .map(Enum::name)
                .collect(Collectors.toList());
    }

    @Cacheable(cacheNames = "getDefaultCache", keyGenerator = "customKeyGenerator")
    @ApiOperation(value = "게시판 댓글 유형 코드 조회")
    @GetMapping("comment-type")
    public List<String> getCommentType() {
        return Stream.of(CommentType.values())
                .map(Enum::name)
                .collect(Collectors.toList());
    }

    @Cacheable(cacheNames = "getDefaultCache", keyGenerator = "customKeyGenerator")
    @ApiOperation(value = "사용자 유형 코드 조회")
    @GetMapping("users-type")
    public List<String> getUsersType() {
        return Stream.of(UsersType.values())
                .map(Enum::name)
                .collect(Collectors.toList());
    }

    @Cacheable(cacheNames = "getDefaultCache", keyGenerator = "customKeyGenerator")
    @ApiOperation(value = "사용 여부 유형 코드 조회")
    @GetMapping("use-yn-type")
    public List<String> getUseYnType() {
        return Stream.of(UseYn.values())
                .map(Enum::name)
                .collect(Collectors.toList());
    }

}
