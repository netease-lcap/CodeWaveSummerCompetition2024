package com.fdddf.emailfetcher;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.mail.Folder;
import javax.mail.MessagingException;
import javax.mail.Store;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class FolderIterator implements Iterator<Folder> {
    private static final Logger logger = LoggerFactory.getLogger(FolderIterator.class);

    private final Store mailbox;
    private final String[] excludes;
    private final String[] includes;

    private List<Folder> folders = null;
    private Folder lastFolder = null;

    public FolderIterator(Store mailBox, String[] excludes, String[] includes) throws EmailFetchException {
        if (excludes == null) {
            excludes = new String[0];
        }
        if (includes == null) {
            includes = new String[0];
        }
        this.mailbox = mailBox;
        this.excludes = excludes;
        this.includes = includes;
        this.folders = new ArrayList<>();
        getTopLevelFolders(mailBox);
    }

    public boolean hasNext() {
        return !folders.isEmpty();
    }

    public Folder next() {
        try {
            boolean hasMessages = false;
            Folder next;
            do {
                if (lastFolder != null) {
                    lastFolder.close(false);
                    lastFolder = null;
                }
                if (folders.isEmpty()) {
                    mailbox.close();
                    return null;
                }
                next = folders.remove(0);
                if (next != null) {
                    String fullName = next.getFullName();
                    if (!excludeFolder(fullName)) {
                        hasMessages = (next.getType() & Folder.HOLDS_MESSAGES) != 0;
                        next.open(Folder.READ_ONLY);
                        lastFolder = next;
                        logger.info("Opened folder: {}", fullName);
                    }
                    if (((next.getType() & Folder.HOLDS_FOLDERS) != 0)) {
                        Folder[] children = next.list();
                        logger.debug("Adding its children to list");
                        for (int i = children.length - 1; i >= 0; i--) {
                            folders.add(0, children[i]);
                            logger.debug("Child name: {}", children[i].getFullName());
                        }
                        if (children.length == 0) {
                            logger.debug("No children");
                        }
                    }
                }
            } while (!hasMessages);
            return next;
        } catch (MessagingException e) {
            return null;
        }
    }

    public void remove() {
        throw new UnsupportedOperationException("Its read only mode.");
    }

    private void getTopLevelFolders(Store mailBox) throws EmailFetchException {
        try {
            folders.add(mailBox.getDefaultFolder());
        } catch (MessagingException e) {
            throw new EmailFetchException("Folder retrieve failed", e);
        }
    }

    private boolean excludeFolder(String name) {
        for (String s : excludes) {
            if (name.matches(s))
                return true;
        }
        for (String s : includes) {
            if (name.matches(s))
                return false;
        }
        return includes.length > 0;
    }
}
