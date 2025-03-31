package com.servidores.projeto.commons;

import java.io.InputStream;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import io.minio.MinioClient;
import io.minio.PutObjectArgs;

@Service
public class MinioService {

    private final MinioClient minioClient;
    public static final String BUCKET_NAME = "fotos-pessoas";

    public MinioService(MinioClient minioClient) {
        this.minioClient = minioClient;
    }

    public String uploadFile(MultipartFile file) {
        try {
            String fileName = UUID.randomUUID().toString() + "-" + file.getOriginalFilename();

            InputStream inputStream = file.getInputStream();

            minioClient.putObject(
                    PutObjectArgs.builder()
                            .bucket(BUCKET_NAME)
                            .object(fileName)
                            .stream(inputStream, file.getSize(), -1)
                            .contentType(file.getContentType())
                            .build());

            return fileName;
        } catch (Exception e) {
            throw new RuntimeException("Erro ao fazer upload do arquivo para o MinIO", e);
        }
    }
}