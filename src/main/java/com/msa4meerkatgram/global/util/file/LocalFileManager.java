package com.msa4meerkatgram.global.util.file;

import com.msa4meerkatgram.global.errors.custom.FileManagedException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class LocalFileManager {

    // 환경변수 설정 객체
    private final FileConfig fileConfig;


    /*
    * 파일 확장자 추출
    * @Param file 파일
    *
    * 확장자를 가져온다(소문자로)
    * */
    public String extractExtension(MultipartFile file){
        // 파일 존재 체크 (파일이 없을 때)
        if(file == null || file.isEmpty()) {
            throw new FileManagedException("파일 저장 실패: 파일 확장 획득 실패(파일없음)");
        }

        // 파일 확장자 검증 (파일 확장자가 없을 때)
        String fileName = file.getOriginalFilename(); // 파일명을 바로 가져올 수 있음
        if(fileName == null || !fileName.contains(".")){
            throw new FileManagedException("파일 저장 실패: 파일 확장자 획득 실패(파일명 이상)");
        }

        String extractExtension = fileName.substring(fileName.lastIndexOf(".") + 1).toLowerCase();


        // 허용하는 확장자 체크
        // image/에 우리가 추출한 확장자를 합쳐서 체크함
        if(!fileConfig.allowExtensionList().contains("image/" + extractExtension)) {
            throw new FileManagedException("파일 저장 실패: 허용하지 않는 파일 확장자");
        }

        return extractExtension;
    }

    /*
    * 랜덤 파일명 생성
    * @return 파일명 `yyyyMMdd_UUID`
    *
    * */

    public String generateFileName() {
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyyMMdd");
        LocalDate now = LocalDate.now();

        return now.format(dateFormatter) + "_" + UUID.randomUUID();
    }


    // 경로 생성하는 메소드 :  경로가 /files/profiles/~~.ext 라고 저장됨
    public String generateProfilePath(MultipartFile file) {
        return fileConfig.profilePath() + "/" + generateFileName() + "." + extractExtension(file);
    }

    public String generatePostPath(MultipartFile file) {
        return fileConfig.postPath() + "/" + generateFileName() + "." + extractExtension(file);
    }

    public boolean makeDir(Path targetPath) {
        try {
            if(!Files.exists(targetPath)) {
                Files.createDirectories(targetPath);
            }
            return true;
        } catch (IOException | IllegalStateException e) {
            return false;
        }
    }

    public void saveFile(MultipartFile file, String logicalPath) {
        try {
            // 실제 물리적인 절대 경로 합성 (OS 별 구분자 자동 보정)
            Path physicalPath = Paths.get(fileConfig.storagePath(), logicalPath).normalize();

            // 디렉토리 확인
            if(!this.makeDir(physicalPath.getParent())) {
                throw new FileManagedException(String.format("파일 저장 실패 : 디렉토리 생성 실패 (%s)", physicalPath.getParent()));
            }

            // 파일 저장
            file.transferTo(physicalPath.toFile());

        } catch (IOException | IllegalStateException e) {
            throw new FileManagedException(String.format("파일 저장 실패: 쓰기 작업 실패 (파일명 %s)", logicalPath));
        }
    }

}



// 프론트의 file 관련 처리 클래스.


// IllegalStateException 파일 생성하다 이상이 생기면

//physicalPath.getParent() 부모의 디렉터리를 자동으로 추출
// file.transferTo(physicalPath.toFile());