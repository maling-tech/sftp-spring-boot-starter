package com.ml;

import lombok.extern.slf4j.Slf4j;
import org.apache.sshd.common.util.GenericUtils;
import org.apache.sshd.server.session.ServerSession;
import org.apache.sshd.server.subsystem.sftp.AbstractSftpEventListenerAdapter;
import org.apache.sshd.server.subsystem.sftp.DirectoryHandle;
import org.apache.sshd.server.subsystem.sftp.FileHandle;
import org.apache.sshd.server.subsystem.sftp.Handle;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.CopyOption;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collection;
import java.util.Map;

/**
 * @author maling
 */
@Slf4j
@Component
public class PersistLogSftpEventListener extends AbstractSftpEventListenerAdapter {

    public PersistLogSftpEventListener() {
        super();
    }

    @Override
    public void initialized(ServerSession session, int version) {
        if (log.isTraceEnabled()) {
            log.trace("initialized(" + session + ") version: " + version);
        }
    }

    @Override
    public void destroying(ServerSession session) {
        if (log.isTraceEnabled()) {
            log.trace("destroying(" + session + ")");
        }
    }

    @Override
    public void opening(ServerSession session, String remoteHandle, Handle localHandle) {
        if (log.isTraceEnabled()) {
            Path path = localHandle.getFile();
            log.trace("opening(" + session + ")[" + remoteHandle + "] " + (Files.isDirectory(path) ? "directory" : "file") + " " + path);
        }
    }

    @Override
    public void open(ServerSession session, String remoteHandle, Handle localHandle) {
        if (log.isTraceEnabled()) {
            Path path = localHandle.getFile();
            log.trace("open(" + session + ")[" + remoteHandle + "] " + (Files.isDirectory(path) ? "directory" : "file") + " " + path);
        }
    }

    @Override
    public void read(ServerSession session, String remoteHandle, DirectoryHandle localHandle, Map<String, Path> entries) {
        int numEntries = GenericUtils.size(entries);
        if (log.isDebugEnabled()) {
            log.debug("read(" + session + ")[" + localHandle.getFile() + "] " + numEntries + " entries");
        }

        if ((numEntries > 0) && log.isTraceEnabled()) {
            entries.forEach((key, value) ->
                    log.trace("read(" + session + ")[" + localHandle.getFile() + "] " + key + " - " + value));
        }
    }

    @Override
    public void reading(ServerSession session, String remoteHandle, FileHandle localHandle,
                        long offset, byte[] data, int dataOffset, int dataLen) {
        if (log.isTraceEnabled()) {
            log.trace("reading(" + session + ")[" + localHandle.getFile() + "] offset=" + offset + ", requested=" + dataLen);
        }
    }

    @Override
    @SuppressWarnings("checkstyle:ParameterNumber")
    public void read(ServerSession session, String remoteHandle, FileHandle localHandle,
                     long offset, byte[] data, int dataOffset, int dataLen, int readLen, Throwable thrown) {
        if (log.isTraceEnabled()) {
            log.trace("read(" + session + ")[" + localHandle.getFile() + "] offset=" + offset
                    + ", requested=" + dataLen + ", read=" + readLen
                    + ((thrown == null) ? "" : (": " + thrown.getClass().getSimpleName() + ": " + thrown.getMessage())));
        }
    }

    @Override
    public void writing(ServerSession session, String remoteHandle, FileHandle localHandle,
                        long offset, byte[] data, int dataOffset, int dataLen) {
        if (log.isTraceEnabled()) {
            log.trace("write(" + session + ")[" + localHandle.getFile() + "] offset=" + offset + ", requested=" + dataLen);
        }
    }

    @Override
    public void written(ServerSession session, String remoteHandle, FileHandle localHandle,
                        long offset, byte[] data, int dataOffset, int dataLen, Throwable thrown) {
        if (log.isTraceEnabled()) {
            log.trace("written(" + session + ")[" + localHandle.getFile() + "] offset=" + offset + ", requested=" + dataLen
                    + ((thrown == null) ? "" : (": " + thrown.getClass().getSimpleName() + ": " + thrown.getMessage())));
        }

        log.info("written({})[{}] offset={}", session, localHandle.getFile(), offset);
    }

    @Override
    public void blocking(ServerSession session, String remoteHandle, FileHandle localHandle, long offset, long length, int mask) {
        if (log.isTraceEnabled()) {
            log.trace("blocking(" + session + ")[" + localHandle.getFile() + "]"
                    + " offset=" + offset + ", length=" + length + ", mask=0x" + Integer.toHexString(mask));
        }
    }

    @Override
    public void blocked(
            ServerSession session, String remoteHandle, FileHandle localHandle, long offset, long length, int mask, Throwable thrown) {
        if (log.isTraceEnabled()) {
            log.trace("blocked(" + session + ")[" + localHandle.getFile() + "]"
                    + " offset=" + offset + ", length=" + length + ", mask=0x" + Integer.toHexString(mask)
                    + ((thrown == null) ? "" : (": " + thrown.getClass().getSimpleName() + ": " + thrown.getMessage())));
        }
    }

    @Override
    public void unblocking(ServerSession session, String remoteHandle, FileHandle localHandle, long offset, long length) {
        if (log.isTraceEnabled()) {
            log.trace("unblocking(" + session + ")[" + localHandle.getFile() + "] offset=" + offset + ", length=" + length);
        }
    }

    @Override
    public void unblocked(
            ServerSession session, String remoteHandle, FileHandle localHandle, long offset, long length, Throwable thrown) {
        if (log.isTraceEnabled()) {
            log.trace("unblocked(" + session + ")[" + localHandle.getFile() + "]"
                    + " offset=" + offset + ", length=" + length
                    + ((thrown == null) ? "" : (": " + thrown.getClass().getSimpleName() + ": " + thrown.getMessage())));
        }
    }

    @Override
    public void closing(ServerSession session, String remoteHandle, Handle localHandle) {
        if (log.isTraceEnabled()) {
            Path path = localHandle.getFile();
            log.trace("close(" + session + ")[" + remoteHandle + "] " + (Files.isDirectory(path) ? "directory" : "file") + " " + path);
        }
    }

    @Override
    public void creating(ServerSession session, Path path, Map<String, ?> attrs) {
        if (log.isTraceEnabled()) {
            log.trace("creating(" + session + ") " + (Files.isDirectory(path) ? "directory" : "file") + " " + path);
        }
    }

    @Override
    public void created(ServerSession session, Path path, Map<String, ?> attrs, Throwable thrown) {
        if (log.isTraceEnabled()) {
            log.trace("created(" + session + ") " + (Files.isDirectory(path) ? "directory" : "file") + " " + path
                    + ((thrown == null) ? "" : (": " + thrown.getClass().getSimpleName() + ": " + thrown.getMessage())));
        }
    }

    @Override
    public void moving(ServerSession session, Path srcPath, Path dstPath, Collection<CopyOption> opts) {
        if (log.isTraceEnabled()) {
            log.trace("moving(" + session + ")[" + opts + "]" + srcPath + " => " + dstPath);
        }
    }

    @Override
    public void moved(ServerSession session, Path srcPath, Path dstPath, Collection<CopyOption> opts, Throwable thrown) {
        if (log.isTraceEnabled()) {
            log.trace("moved(" + session + ")[" + opts + "]" + srcPath + " => " + dstPath
                    + ((thrown == null) ? "" : (": " + thrown.getClass().getSimpleName() + ": " + thrown.getMessage())));
        }
    }

    @Override
    public void removing(ServerSession session, Path path, boolean isDirectory) {
        if (log.isTraceEnabled()) {
            log.trace("removing(" + session + ")[dir=" + isDirectory + "] " + path);
        }
    }

    @Override
    public void removed(ServerSession session, Path path, boolean isDirectory, Throwable thrown) {
        if (log.isTraceEnabled()) {
            log.trace("removed(" + session + ")[dir=" + isDirectory + "] " + path
                    + ((thrown == null) ? "" : (": " + thrown.getClass().getSimpleName() + ": " + thrown.getMessage())));
        }
    }

    @Override
    public void linking(ServerSession session, Path source, Path target, boolean symLink) {
        if (log.isTraceEnabled()) {
            log.trace("linking(" + session + ")[" + symLink + "]" + source + " => " + target);
        }
    }

    @Override
    public void linked(ServerSession session, Path source, Path target, boolean symLink, Throwable thrown) {
        if (log.isTraceEnabled()) {
            log.trace("linked(" + session + ")[" + symLink + "]" + source + " => " + target
                    + ((thrown == null) ? "" : (": " + thrown.getClass().getSimpleName() + ": " + thrown.getMessage())));
        }
    }

    @Override
    public void modifyingAttributes(ServerSession session, Path path, Map<String, ?> attrs) {
        if (log.isTraceEnabled()) {
            log.trace("modifyingAttributes(" + session + ") " + path + ": " + attrs);
        }
    }

    @Override
    public void modifiedAttributes(ServerSession session, Path path, Map<String, ?> attrs, Throwable thrown) {
        if (log.isTraceEnabled()) {
            log.trace("modifiedAttributes(" + session + ") " + path
                    + ((thrown == null) ? "" : (": " + thrown.getClass().getSimpleName() + ": " + thrown.getMessage())));
        }
    }
}
