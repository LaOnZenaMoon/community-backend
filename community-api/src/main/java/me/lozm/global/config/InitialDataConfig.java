package me.lozm.global.config;

import com.github.javafaker.Faker;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.lozm.api.board.service.BoardService;
import me.lozm.api.board.service.CommentService;
import me.lozm.domain.board.dto.BoardDto;
import me.lozm.domain.board.dto.CommentDto;
import me.lozm.domain.board.entity.Board;
import me.lozm.domain.board.repository.BoardRepository;
import me.lozm.domain.user.entity.User;
import me.lozm.domain.user.repository.UserRepository;
import me.lozm.global.code.*;
import me.lozm.global.object.entity.HierarchicalEntity;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingClass;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

@Slf4j
@Component
@RequiredArgsConstructor
@ConfigurationProperties(prefix = "lozm.data")
@ConditionalOnMissingClass
public class InitialDataConfig {

    private final BoardService boardService;
    private final CommentService commentService;
    private final BoardRepository boardRepository;
    private final UserRepository userRepository;


    private final int DATA_SIZE_LIMIT = 2000;


    private boolean enabled = false;
    private int size = 100;


    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public void setSize(int size) {
        if (size > DATA_SIZE_LIMIT) {
            log.warn(String.format("Data cannot be initialized because the size is too large. The size limit is %d", DATA_SIZE_LIMIT));
        }

        this.size = size;
    }

    @PostConstruct
    public void init() {
        if (!isEnabled()) {
            return;
        }

        final Faker faker = new Faker();
        final Random random = new Random();

        final BoardType[] boardTypes = BoardType.values();
        final ContentType[] contentTypes = ContentType.values();

        final List<User> userList = userRepository.findAll();

        // board
        for (int i = 0; i < size; i++) {
            boardService.addBoard(BoardDto.AddRequest.builder()
                    .boardType(boardTypes[random.nextInt(boardTypes.length)])
                    .contentType(contentTypes[random.nextInt(contentTypes.length)])
                    .title(faker.book().title())
                    .content(getRandomContent(faker, random))
                    .createdBy(userList.get(random.nextInt(userList.size())).getId())
                    .build());
        }

        // comment
        final List<Board> boardList = boardRepository.findAll();

        final CommentType[] commentTypes = CommentType.values();

        for (int i = 0; i < size; i++) {
            commentService.addComment(CommentDto.AddRequest.builder()
                    .boardId(boardList.get(random.nextInt(boardList.size())).getId())
                    .commentType(commentTypes[random.nextInt(commentTypes.length)])
                    .content(getRandomContent(faker, random))
                    .createdBy(userList.get(random.nextInt(userList.size())).getId())
                    .build());
        }
    }


    private String getRandomContent(Faker faker, Random random) {
        return faker.lorem().sentences(random.nextInt(20) + 1).toString();
    }

}
