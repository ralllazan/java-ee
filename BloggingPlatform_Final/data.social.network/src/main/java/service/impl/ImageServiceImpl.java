package service.impl;

import dto.UserDTO;
import service.ImageService;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static constants.Constants.PROFILE_IMAGES;

@Service
public class ImageServiceImpl implements ImageService {

    private static String getNewFileName(UserDTO user, MultipartFile multipartFile) {
        String fileName = multipartFile.getOriginalFilename();
        String extension = getFileExtension(fileName);
        return user.getId() + "." + extension;
    }

    private static String getFileExtension(String fileName) {
        int index = fileName.lastIndexOf('.');
        return index == -1 ? fileName : fileName.substring(index + 1);
    }

    @Override
    public Path getProfileImagesPath() {
        return Paths.get(".").resolve(PROFILE_IMAGES);
    }

    @Override
    public String updateProfileImage(UserDTO user, MultipartFile multipartFile) throws IOException {
        byte[] bytes = multipartFile.getBytes();
        Path pathImages = getProfileImagesPath();
        Files.delete(pathImages.resolve(user.getImage()));
        String newFileName = getNewFileName(user, multipartFile);
        Files.write(pathImages.resolve(newFileName), bytes);
        return newFileName;
    }
}
