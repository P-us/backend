package projectus.pus.utils;

import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.web.multipart.MultipartFile;
import projectus.pus.dto.PhotoDto;
import projectus.pus.entity.Photo;

import java.io.File;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Component
public class PhotoHandler {
    public List<Photo> parseFileInfo(List<MultipartFile> multipartFiles) throws Exception {
        List<Photo> fileList = new ArrayList<>();
        if(!CollectionUtils.isEmpty(multipartFiles)) {
            LocalDateTime now = LocalDateTime.now();
            DateTimeFormatter dateTimeFormatter =
                    DateTimeFormatter.ofPattern("yyyyMMdd");
            String current_date = now.format(dateTimeFormatter);
            String absolutePath = new File("").getAbsolutePath() + File.separator + File.separator;
            String path = "images" + File.separator + current_date;
            File file = new File(path);
            if(!file.exists()) {
                boolean wasSuccessful = file.mkdirs();
                if(!wasSuccessful)
                    System.out.println("file: was not successful");
            }
            for(MultipartFile multipartFile : multipartFiles) {
                String originalFileExtension;
                String contentType = multipartFile.getContentType();
                if(ObjectUtils.isEmpty(contentType)) {
                    break;
                }
                else {
                    if(contentType.contains("image/jpeg"))
                        originalFileExtension = ".jpg";
                    else if(contentType.contains("image/png"))
                        originalFileExtension = ".png";
                    else
                        break;
                }
                String new_file_name = System.nanoTime() + originalFileExtension;
                PhotoDto photoDto = PhotoDto.builder()
                        .origFileName(multipartFile.getOriginalFilename())
                        .filePath(path + File.separator + new_file_name)
                        .fileSize(multipartFile.getSize())
                        .build();

                Photo photo = new Photo(
                        photoDto.getOrigFileName(),
                        photoDto.getFilePath(),
                        photoDto.getFileSize()
                );

                fileList.add(photo);
                file = new File(absolutePath + path + File.separator + new_file_name);
                multipartFile.transferTo(file);
                file.setWritable(true);
                file.setReadable(true);
            }
        }
        return fileList;
    }
}
