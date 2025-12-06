package com.samsung.storypilot.fileupload;

import java.util.List;

public interface FileUploadService {
    FileUploadDTO saveFile(FileUploadDTO dto);

    FileUploadDTO getFileById(Long id);

    List<FileUploadDTO> getAllFiles();

    FileUploadDTO updateFile(Long id, FileUploadDTO dto);

    void deleteFile(Long id);
}
