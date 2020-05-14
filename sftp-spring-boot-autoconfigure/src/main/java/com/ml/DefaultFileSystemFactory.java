package com.ml;

import lombok.extern.slf4j.Slf4j;
import org.apache.sshd.common.file.FileSystemFactory;
import org.apache.sshd.common.file.root.RootedFileSystemProvider;
import org.apache.sshd.common.session.Session;

import java.io.IOException;
import java.nio.file.*;
import java.util.Collections;

/**
 * @author maling
 */
@Slf4j
public class DefaultFileSystemFactory implements FileSystemFactory {

    private final String homeRoot;

    DefaultFileSystemFactory(String homeRoot) {
        this.homeRoot = homeRoot;
    }

    @Override
    public FileSystem createFileSystem(Session session) throws IOException {
        String userName = session.getUsername();
        // create home if does not exist
        Path homeDir = Paths.get(homeRoot, userName).normalize().toAbsolutePath();
        if (Files.exists(homeDir)) {
            if (!Files.isDirectory(homeDir)) {
                throw new NotDirectoryException(homeDir.toString());
            }
        } else {
            Path p = Files.createDirectories(homeDir);
            log.info("createFileSystem({}) created {}", session, p);
        }
        return new RootedFileSystemProvider().newFileSystem(homeDir, Collections.emptyMap());
    }
}
