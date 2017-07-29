package vip.service;

import org.springframework.web.multipart.MultipartFile;

import utils.ResultData;

/**
 * Created by sunshine on 6/2/16.
 */
public interface UploadService {
    ResultData upload(MultipartFile file, String base);
}
