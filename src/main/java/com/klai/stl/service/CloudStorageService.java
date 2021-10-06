package com.klai.stl.service;

import com.klai.stl.service.dto.requests.s3.UploadImageRequest;
import java.net.URL;

public interface CloudStorageService {
    /**
     * Uploads the image with the requested data
     * @param newImageRequest the image parameters neecessary to upload
     * @return the url of the cloud stored image
     */
    URL uploadImage(UploadImageRequest newImageRequest);

    /**
     * Removes the image from cloud
     * @param imageReference the reference of the image to delete
     * @param spaceReference the reference of the space where the image is stored
     */
    void removeImage(String spaceReference, String imageReference);

    /**
     * Removes the folder and all its contents
     * @param folderName the folder name to delete
     */
    void removeFolder(String folderName);
}
