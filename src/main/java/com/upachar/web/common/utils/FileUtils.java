package com.upachar.web.common.utils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.apache.commons.io.FilenameUtils;
import org.springframework.web.multipart.MultipartFile;

import lombok.extern.slf4j.Slf4j;
import net.bytebuddy.utility.RandomString;

@Slf4j
public class FileUtils {

	public static String uploadImage(MultipartFile image, String filePath) throws IOException {
		String fileName = RandomString.make(15) + "." + FilenameUtils.getExtension(image.getOriginalFilename());
		Path imagePath = Paths.get(filePath + fileName);
		try {
			// Convert MultipartFile to File
			image.transferTo(imagePath);
		} catch (IOException e) {
			log.info("Exception occured while converting multipart file to file. Exception: {}", e.getMessage());
			throw e;
		}

		return fileName;
	}
	
	public static boolean deleteImage(String imageName,  String filePath) throws IOException {
		Path fileToDeletePath = Paths.get(filePath + imageName);
		try {
			Files.delete(fileToDeletePath);
		} catch (NoSuchFileException e) {
			log.info("Cannot find file to delete. Exception: {}", e);
			throw e;
		} catch (IOException e) {
			log.info("Exception occured while deleting drug image. Exception: {}", e);
			throw e;
		}
		
		return true;
	}
}
