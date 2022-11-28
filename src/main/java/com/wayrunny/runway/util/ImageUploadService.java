package com.wayrunny.runway.util;

import net.bytebuddy.utility.RandomString;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Calendar;

@Component
public class ImageUploadService {

    /*localhost에서 실행시키면 C:\\upload 에 저장된다.*/
    private static final String SAVE_PATH = "/upload";

    public String restore(MultipartFile multipartFile, String type) {
        String url = null;

        try {
            String originFilename = multipartFile.getOriginalFilename();
            String extName = originFilename.substring(originFilename.lastIndexOf("."), originFilename.length());    //확장자

            String saveFileName = genSaveFileName(extName, type);

            writeFile(multipartFile, saveFileName);
            url = SAVE_PATH + "/" + saveFileName;
        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }
        return url;
    }

    public Boolean deleteImage(String image_url){

        if(image_url != null) {
            File file = new File(image_url);

            if (file.exists()) {
                if (file.delete()) {
                    return true;
                } else {
                    return false;
                }
            }
        }

        return true;
    }

    private String genSaveFileName(String extName, String type) {
        String fileName = "";
        String dir = SAVE_PATH + "/";

        fileName += type;

        //파일 정리용 폴더 생성
        dir += fileName;
        File folder = new File(dir);
        if (!folder.exists()) {
            try {
                Files.createDirectory(folder.toPath());
            } catch (Exception e) {
                /*에러 수정*/
                throw new RuntimeException(e);
            }
        }
        fileName += "/";
        RandomString randomString = new RandomString(16);

        fileName += randomString.nextString();
        fileName += extName;

        return fileName;
    }


    // 파일을 실제로 write 하는 메서드
    private void writeFile(MultipartFile multipartFile, String saveFileName) throws IOException {
        byte[] data = multipartFile.getBytes();
        FileOutputStream fos = new FileOutputStream(SAVE_PATH + "/" + saveFileName);
        fos.write(data);
        fos.close();
    }
}