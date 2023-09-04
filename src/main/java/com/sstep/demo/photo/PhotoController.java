package com.sstep.demo.photo;

import com.sstep.demo.photo.dto.PhotoResponseDto;
import com.sstep.demo.photo.service.PhotoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RequiredArgsConstructor
@RestController
@RequestMapping("/photo")
public class PhotoController {
    private final PhotoService photoService;

    //사진 등록
    @PostMapping(value = "/add", produces = MediaType.IMAGE_JPEG_VALUE)
    public PhotoResponseDto savePhoto(@RequestParam("file") MultipartFile multipartFile) throws IOException {
        return photoService.savePhoto(multipartFile);
    }
}