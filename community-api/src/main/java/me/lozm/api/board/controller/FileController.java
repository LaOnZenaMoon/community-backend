package me.lozm.api.board.controller;

import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.lozm.api.board.service.FileService;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
@Api(tags = {"파일"})
@RequestMapping("file")
@RestController
@RequiredArgsConstructor
public class FileController {

    private final FileService fileService;


    @PostMapping("single-upload")
    public void uploadSingleFile(HttpServletRequest httpServletRequest) throws ServletException, IOException {
        fileService.uploadSingleFile(httpServletRequest);
    }

    @GetMapping("{fileName}/single-download")
    public void downloadSingleFile(@PathVariable String fileName, HttpServletResponse httpServletResponse) throws IOException {
        fileService.downloadSingleFile(fileName, httpServletResponse);
    }

    @Cacheable(cacheNames = "getDefaultCache")
    @GetMapping("{fileName}/single-image")
    public ResponseEntity<byte[]> getSingleImageFile(@PathVariable String fileName) {
        return fileService.getSingleImageFile(fileName);
    }

}
