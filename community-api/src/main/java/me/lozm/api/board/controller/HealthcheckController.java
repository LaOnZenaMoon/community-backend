package me.lozm.api.board.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;

@RestController
public class HealthcheckController {


    class ShortResponseDto {
        String status;

        public ShortResponseDto(String status) {
            this.status = status;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }
    }

    class FullResponseDto extends ShortResponseDto {
        String currentTime;

        public FullResponseDto(String status) {
            super(status);
        }

        public FullResponseDto(String status, LocalDateTime currentTime) {
            super(status);
            this.currentTime = currentTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss'Z'"));
        }

        public String getCurrentTime() {
            return currentTime;
        }

        public void setCurrentTime(String currentTime) {
            this.currentTime = currentTime;
        }
    }

    @GetMapping(value = "/healthcheck", produces = "application/json")
    public ResponseEntity<ShortResponseDto> healthcheck(@RequestParam(value = "format", required = false) String format) {
        if ("short".equals(format)) {
            return new ResponseEntity<>(new ShortResponseDto(HttpStatus.OK.getReasonPhrase()), HttpStatus.OK);
        } else if ("full".equals(format)) {
            return new ResponseEntity<>(new FullResponseDto(HttpStatus.OK.getReasonPhrase(), LocalDateTime.now(ZoneOffset.UTC)), HttpStatus.OK);
        }

        return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
    }

    @PutMapping(value = "/healthcheck", produces = "application/json")
    public ResponseEntity healthcheckPut() {
        return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
    }

    @PostMapping(value = "/healthcheck", produces = "application/json")
    public ResponseEntity healthcheckPost() {
        return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
    }


    @DeleteMapping(value = "/healthcheck", produces = "application/json")
    public ResponseEntity healthcheckDelete() {
        return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
    }

}
