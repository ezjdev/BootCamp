package com.colvir.bootcamp.homework4.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

@Slf4j
@Service
public class ZipService {

    public Path zip(Path source, Path destination) {
        try (ZipOutputStream zipOutputStream = new ZipOutputStream(new FileOutputStream(destination.toFile()));
                FileInputStream inputStream = new FileInputStream(source.toFile())) {
            ZipEntry zipEntry = new ZipEntry(source.toString());
            zipOutputStream.putNextEntry(zipEntry);
            byte[] bytes = new byte[1024];
            int length;
            while((length = inputStream.read(bytes)) >= 0) {
                zipOutputStream.write(bytes, 0, length);
            }
        } catch (IOException e) {
            log.info(e.getMessage());
        }
        return destination;
    }

    public void unZip(Path source, Path destination){
        byte[] buffer = new byte[1024];
        try (ZipInputStream zipInputStream = new ZipInputStream(new FileInputStream(source.toFile()))) {
            ZipEntry zipEntry = zipInputStream.getNextEntry();
            while (zipEntry != null) {
                String fileName = zipEntry.getName();
                try (FileOutputStream outputStream = new FileOutputStream(destination + File.separator + fileName)) {
                    int len;
                    while ((len = zipInputStream.read(buffer)) > 0) {
                        outputStream.write(buffer, 0, len);
                    }
                }
                zipEntry = zipInputStream.getNextEntry();
            }
        } catch (IOException e) {
            log.info(e.getMessage());
        }
    }

}
