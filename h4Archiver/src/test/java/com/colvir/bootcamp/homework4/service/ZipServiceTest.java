package com.colvir.bootcamp.homework4.service;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Comparator;
import java.util.function.Predicate;
import java.util.stream.Stream;

@Slf4j
class ZipServiceTest {

    public static final ZipService underTest = new ZipService();
    private static final Path tempDir;
    public static final String SOURCE_FILE_NAME = "source";
    private static final String TARGET_FILE_NAME = "target";
    private static final String UNZIPPED_FILES_DIR_NAME = "unzipped";
    public static final String ARCHIVED_STRING = "ASD123!@#";

    static {
        try {
            tempDir = Files.createTempDirectory("archiverTest");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @AfterEach
    void tearDown() throws IOException {
        try (Stream<Path> entries = Files.walk(tempDir)) {
            entries.sorted(Comparator.reverseOrder())
                    .map(Path::toFile)
                    .filter(Predicate.not(File::delete))
                    .forEach(file -> log.error("Can't delete file: {}", file.getAbsolutePath()));
        }

    }

    @Test
    void testArchiveToZip() throws IOException {
        Path fileToArchive = Files.writeString(tempDir.resolve(SOURCE_FILE_NAME), ARCHIVED_STRING);
        underTest.zip(fileToArchive, tempDir.resolve(TARGET_FILE_NAME));
        Path unzippedFilesDir = tempDir.resolve(UNZIPPED_FILES_DIR_NAME);
        Files.createDirectory(unzippedFilesDir);
        underTest.unZip(tempDir.resolve(TARGET_FILE_NAME), unzippedFilesDir);
        String result = Files.readString(unzippedFilesDir.resolve(SOURCE_FILE_NAME));
        Assertions.assertEquals(ARCHIVED_STRING, result);
    }
}