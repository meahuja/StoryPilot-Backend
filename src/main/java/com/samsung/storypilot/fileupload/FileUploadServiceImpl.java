package com.samsung.storypilot.fileupload;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class FileUploadServiceImpl implements FileUploadService {

    @Autowired
    FileUploadRepository repository;

    private FileUploadDTO mapToDTO(FileUploadEntity entity) {
        FileUploadDTO dto = new FileUploadDTO();
        dto.setId(entity.getId());
        dto.setFileName(entity.getFileName());
        dto.setFileSize(entity.getFileSize());
        dto.setUploadedBy(entity.getUploadedBy());
        dto.setUploadedDate(entity.getUploadedDate());
        return dto;
    }

    private FileUploadEntity mapToEntity(FileUploadDTO dto) {
        FileUploadEntity entity = new FileUploadEntity();
        entity.setId(dto.getId());
        entity.setFileName(dto.getFileName());
        entity.setFileSize(dto.getFileSize());
        entity.setUploadedBy(dto.getUploadedBy());
        entity.setUploadedDate(dto.getUploadedDate());
        return entity;
    }

    @Override
    public FileUploadDTO saveFile(FileUploadDTO dto) {
        FileUploadEntity entity = mapToEntity(dto);
        return mapToDTO(repository.save(entity));
    }

    @Override
    public FileUploadDTO getFileById(Long id) {
        Optional<FileUploadEntity> entity = repository.findById(id);
        return entity.map(this::mapToDTO).orElse(null);
    }

    @Override
    public List<FileUploadDTO> getAllFiles() {
        return repository.findAll().stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public FileUploadDTO updateFile(Long id, FileUploadDTO dto) {
        return repository.findById(id)
                .map(existing -> {
                    existing.setFileName(dto.getFileName());
                    existing.setFileSize(dto.getFileSize());
                    existing.setUploadedBy(dto.getUploadedBy());
                    existing.setUploadedDate(dto.getUploadedDate());
                    return mapToDTO(repository.save(existing));
                })
                .orElse(null);
    }

    @Override
    public void deleteFile(Long id) {
        repository.deleteById(id);
    }
}

