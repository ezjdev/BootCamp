package com.colvir.bootcamp.homework4;

import com.colvir.bootcamp.homework4.service.ZipService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;

@Slf4j
@SpringBootApplication
public class AppRunner {

    public static final String ZIP_EXTENSION = ".zip";
    public static final String UNZIPPED_FILES_DIR = "unzipped";

    public static void main(String[] args) {
        try (ConfigurableApplicationContext context = SpringApplication.run(AppRunner.class, args)) {
            ZipService zipService = context.getBean(ZipService.class);
            if (args.length > 1) {
                Path destPath = Optional.ofNullable(args[0])
                        .map(Paths::get)
                        .map(path -> zipService.zip(path, Optional.ofNullable(args[1])
                                .map(file -> file + ZIP_EXTENSION)
                                .map(Paths::get)
                                .orElseThrow(() -> new RuntimeException("Destination file not specified"))))
                        .orElseThrow(() -> new RuntimeException("Source file not specified"));
                File dir = Paths.get(UNZIPPED_FILES_DIR).toFile();
                if (dir.exists() || dir.mkdir()) {
                    zipService.unZip(destPath, dir.toPath());
                }
            }
        }
    }
}
