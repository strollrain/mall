package com.mmall.service;

import org.springframework.web.multipart.MultipartFile;

/**
 * @author rain
 *
 */
public interface IFileService {
	String upload(MultipartFile file,String path);
}
