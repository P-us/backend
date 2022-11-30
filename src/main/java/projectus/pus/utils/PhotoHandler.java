package projectus.pus.utils;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.*;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;
import projectus.pus.dto.PhotoDto;
import projectus.pus.entity.Photo;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class PhotoHandler {
    private final AmazonS3 amazonS3;
    @Value("${cloud.aws.s3.bucket}")
    private String bucket;
    public List<Photo> parseFileInfo(List<MultipartFile> multipartFiles) throws Exception {
        List<Photo> fileList = new ArrayList<>();
        if(!CollectionUtils.isEmpty(multipartFiles)) {
            LocalDateTime now = LocalDateTime.now();
            DateTimeFormatter dateTimeFormatter =
                    DateTimeFormatter.ofPattern("yyyyMMdd");
            String current_date = now.format(dateTimeFormatter);
            String absolutePath = new File("").getAbsolutePath() + File.separator + File.separator;
            String path = "images"+File.separator + current_date;
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
                        .filePath(path +File.separator+ new_file_name)
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
    public List<Photo> parseFileInfoS3(List<MultipartFile> multipartFiles) throws Exception {
        List<Photo> fileList = new ArrayList<>();
        if(!CollectionUtils.isEmpty(multipartFiles)) {
            LocalDateTime now = LocalDateTime.now();
            DateTimeFormatter dateTimeFormatter =
                    DateTimeFormatter.ofPattern("yyyyMMdd");
            String current_date = now.format(dateTimeFormatter);
            String absolutePath = new File("").getAbsolutePath() + File.separator + File.separator;
            String path = current_date;
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
                        .filePath(path + new_file_name)
                        .fileSize(multipartFile.getSize())
                        .build();

                Photo photo = new Photo(
                        photoDto.getOrigFileName(),
                        photoDto.getFilePath(),
                        photoDto.getFileSize()
                );
                ObjectMetadata objectMetadata = new ObjectMetadata();
                objectMetadata.setContentLength(photo.getFileSize());
                objectMetadata.setContentType(multipartFile.getContentType());

                try(InputStream inputStream = multipartFile.getInputStream()) {
                    amazonS3.putObject(new PutObjectRequest(bucket, path+new_file_name, inputStream, objectMetadata)
                            .withCannedAcl(CannedAccessControlList.PublicRead));
                } catch(IOException e) {
                    throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "파일 업로드에 실패했습니다.");
                }
                fileList.add(photo);
            }
        }
        return fileList;
    }

    public void deleteFile(String fileName){
        amazonS3.deleteObject(new DeleteObjectRequest(bucket, fileName));
    }
    public S3ObjectInputStream getS3InputStream(String fileName){
        S3Object o = amazonS3.getObject(new GetObjectRequest(bucket, fileName));
        return ((S3Object) o).getObjectContent();
    }
}
