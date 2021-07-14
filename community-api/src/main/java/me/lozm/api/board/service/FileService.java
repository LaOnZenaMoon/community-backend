package me.lozm.api.board.service;

import com.google.common.io.ByteStreams;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import java.io.*;
import java.net.URLEncoder;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

import static java.lang.String.format;

@Slf4j
@Service
@RequiredArgsConstructor
public class FileService {

    private final String REAL_PATH = "/Users/junlee/git/lozm/community-backend/upload";
    private final String DEFAULT_IMAGE_FILE = "default_picture.jpeg";


    public void uploadSingleFile(HttpServletRequest httpServletRequest) {
        final Part uploadPart = getUploadPart(httpServletRequest);
        final String filePath = getFullFilePath(uploadPart.getSubmittedFileName());
        createUploadFilePath();

        final Path normalize = normalizedFilePath(filePath);

        try {
            Files.copy(getInputStream(uploadPart), normalize, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            throw new RuntimeException(format("업로드 파일 복사 중 에러가 발생하였습니다. 에러 이유: %s", e.getMessage()));
        }

//        final FileOutputStream fileOutputStream = new FileOutputStream(filePath);
//        byte[] buffer = new byte[1024];
//        int size = 0;
//        while ((size = inputStream.read()) != -1) {
//            fileOutputStream.write(buffer, 0, size);
//            fileOutputStream.flush();
//            fileOutputStream.write(size);
//        }

//        inputStream.close();
//        fileOutputStream.close();
    }

    public void downloadSingleFile(String fileName, HttpServletResponse httpServletResponse) throws IOException {
        httpServletResponse.setContentType("application/octet-stream");
        httpServletResponse.setHeader("Content-Disposition", "attachment;filename=\"" + URLEncoder.encode(fileName, "UTF-8") + "\";");

        try (
                BufferedOutputStream bos = new BufferedOutputStream(httpServletResponse.getOutputStream(), 1024 * 64);
                FileInputStream fis = new FileInputStream(REAL_PATH + File.separator + fileName);
        ) {
            byte[] data = new byte[2048];
            int input = 0;

            while ((input = fis.read(data)) != -1) {
                bos.write(data, 0, input);
                bos.flush();
            }
        } catch (Exception e) {
            throw new RuntimeException(format("파일 다운로드 중 에러가 발생하였습니다. 에러 이유: %s", e.getMessage()));
        }

//        final String mimeType = httpServletRequest.getServletContext().getMimeType(REAL_PATH + File.separator + fileName);
//
//        Path filePath = Paths.get(REAL_PATH)
//                .toAbsolutePath()
//                .normalize()
//                .resolve(fileName)
//                .normalize();
//
//        Resource resource = new UrlResource(filePath.toUri());
//
//        return ResponseEntity.ok()
//                .contentType(mimeType == null ? MediaType.parseMediaType("application/octet-stream") : MediaType.parseMediaType(mimeType))
//                .header("Content-Disposition", "attachment;filename=\"" + fileName + "\";")
//                .body(resource);
    }

    public ResponseEntity<byte[]> getSingleImageFile(String fileName) {
        FileInputStream fileInputStream = null;

        try {
            fileInputStream = new FileInputStream(REAL_PATH + File.separator + fileName);
        } catch (FileNotFoundException e) {
            log.error(format("요청한 이미지가 존재하지 않습니다. 이미지명: %s, 에러 내용: %s", fileName, e.getMessage()));
        }

        try {
            fileInputStream = new FileInputStream(REAL_PATH + File.separator + DEFAULT_IMAGE_FILE);
        } catch (FileNotFoundException e) {
            log.error(format("기본 이미지가 존재하지 않습니다. 기본 이미지명: %s, 에러 내용: %s", DEFAULT_IMAGE_FILE, e.getMessage()));
        }

        try {
            return new ResponseEntity<>(ByteStreams.toByteArray(fileInputStream), HttpStatus.OK);
        } catch (IOException e) {
            log.error(format("이미지 파일 응답 전송에 실패하였습니다. 에러 내용: %s", e.getMessage()));
        }

        return null;
    }

    private Path normalizedFilePath(String filePath) {
        return Paths.get(filePath)
                .toAbsolutePath()
                .normalize();
    }

    private void createUploadFilePath() {
        try {
            Files.createDirectories(normalizedFilePath(REAL_PATH));
        } catch (IOException e) {
            throw new RuntimeException(format("파일 업로드 경로 생성 중 에러가 발생하였습니다. 에러 이유: %s", e.getMessage()));
        }
    }

    private InputStream getInputStream(Part uploadPart) {
        try {
            return uploadPart.getInputStream();
        } catch (IOException e) {
            throw new RuntimeException(format("파일 업로드 중 에러가 발생하였습니다. 에러 이유: %s", e.getMessage()));
        }
    }

    private String getFullFilePath(String fileName) {
        return REAL_PATH + File.separator + fileName;
    }

    private Part getUploadPart(HttpServletRequest httpServletRequest) {
        try {
            return httpServletRequest.getPart("upload-file");
        } catch (IOException e) {
            throw new IllegalArgumentException(format("요청하신 업로드 파일 정보가 잘못 되었습니다. 에러 이유: %s", e.getMessage()));
        } catch (ServletException e) {
            throw new IllegalArgumentException(format("요청하신 업로드 파일 정보가 잘못 되었습니다. 에러 이유: %s", e.getMessage()));
        }
    }

}
