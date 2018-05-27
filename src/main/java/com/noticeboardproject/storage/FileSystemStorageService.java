package com.noticeboardproject.storage;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.nio.file.DirectoryNotEmptyException;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.HashMap;
import java.util.Map;

import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

@Service
public class FileSystemStorageService implements StorageService {

    private Map<String, Map<String, Path>> userImages = new HashMap<>();

    @Override
	public void store(MultipartFile file, String username) {
		String filename = StringUtils.cleanPath(file.getOriginalFilename());
		if (isFileNameDuplicated(filename, username)) {
			delete(filename, username);
		}

		try {
			if (file.isEmpty()) {
				throw new StorageException("Failed to store empty file " + filename);
			}
			if (filename.contains("..")) {
				// security check
				throw new StorageException(
						"Cannot store file with relative path outside current directory " + filename);
			}
			try (InputStream inputStream = file.getInputStream()) {

				File folder = Files.createTempDirectory(username).toFile();
				File result = new File(folder, filename);
				Map<String, Path> tempMap = userImages.get(username);
				if (tempMap == null) {
					tempMap = new HashMap<>();
				}
				tempMap.put(filename, result.toPath());
				userImages.put(username, tempMap);
				Files.copy(inputStream, result.toPath(), StandardCopyOption.REPLACE_EXISTING);
			}
		} catch (IOException e) {
			throw new StorageException("Failed to store file " + filename, e);
		}
	}

    @Override
    public Path load(String filename, String username) {
    	return userImages.get(username).get(filename);
    }

    @Override
	public Resource loadAsResource(String filename, String username) {
		try {
			Path file = load(filename, username);
			Resource resource = new UrlResource(file.toUri());
			if (resource.exists() || resource.isReadable()) {
				return resource;
			} else {
				throw new StorageFileNotFoundException("Could not read file: " + filename);
			}
		} catch (MalformedURLException e) {
			throw new StorageFileNotFoundException("Could not read file: " + filename, e);
		}
	}
    
    @Override
	public void delete(String filename, String userName) {
		Path deleteFilePath = userImages.get(userName).get(filename);
		int index = deleteFilePath.toString().lastIndexOf('\\');
		String deleteFolderPath = deleteFilePath.toString().substring(0, index);
		File directoryToDelete = new File(deleteFolderPath);
		try {
			Files.delete(deleteFilePath);
			directoryToDelete.delete();
		} catch (NoSuchFileException e) {
			throw new StorageException("No such file or directory", e);
		} catch (DirectoryNotEmptyException e) {
			throw new StorageException("Directory is not empty", e);
		} catch (IOException e) {
			throw new StorageException("Failed to delete file " + filename, e);
		}
		userImages.get(userName).remove(filename);
	}

    @Override
    public void deleteAllFilesForUser(String userName) {
    	Map<String, Path> fileNamePath = userImages.get(userName);
    	if (!CollectionUtils.isEmpty(fileNamePath)) {
	    	int numberOfImages = fileNamePath.keySet().size();
	    	Object[] fileNames = fileNamePath.keySet().toArray();
	    	for (int i = 0; i < numberOfImages; i++) {
	    		delete(fileNames[i].toString(), userName);
			}
    	}
    }

	@Override
	public Byte[] loadAsByteArray(String fileName, String userName) {
		if (fileName != "") {
			File file = new File(load(fileName, userName).toString());
			byte[] bytesArray = new byte[0];
			bytesArray = readBytesFromFile(file);
			Byte[] imageBytesArray = new Byte[bytesArray.length];

			for (int i = 0; i < bytesArray.length; i++) {
				imageBytesArray[i] = new Byte(bytesArray[i]);
			}
			return imageBytesArray;
		} else {
			return null;
		}
	}
	
	private static byte[] readBytesFromFile(File file) {
		FileInputStream fileInputStream = null;
		byte[] bytesArray = null;

		try {
			bytesArray = new byte[(int) file.length()];
			fileInputStream = new FileInputStream(file);
			fileInputStream.read(bytesArray);
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (fileInputStream != null) {
				try {
					fileInputStream.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return bytesArray;
	}
	
	private boolean isFileNameDuplicated(String fileName, String username) {

		if (!CollectionUtils.isEmpty(userImages.get(username))) {

			for (String savedFileName : userImages.get(username).keySet()) {
				if (savedFileName.equals(fileName)) {
					return true;
				}
			}
		}
		return false;
	}
}