package com.fdddf.emailfetcher;

import com.sun.mail.imap.IMAPMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.mail.FetchProfile;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.search.*;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

public class MessageIterator implements Iterator<Message> {

    private static final Logger log = LoggerFactory.getLogger(MessageIterator.class);

    private Folder folder;
    private Message[] messagesInCurBatch;
    private int current = 0;
    private int currentBatch = 0;
    private int lastIndex = 0;
    private int batchSize = 0;
    private int totalInFolder = 0;
    private boolean doBatching = true;

    private List<String> keywords;

    private static final FetchProfile fp = new FetchProfile();

    private Date fetchMailsSince;

    public MessageIterator(Folder folder, int batchSize) throws EmailFetchException {
        try {
            this.folder = folder;
            this.batchSize = batchSize;
            SearchTerm st = getSearchTerm();
            if (st != null) {
                doBatching = false;
                messagesInCurBatch = folder.search(st);
                totalInFolder = messagesInCurBatch.length;
                folder.fetch(messagesInCurBatch, fp);
                current = 0;
                log.info("Total messages: {}", totalInFolder);
                log.info("Search criteria applied. Batching disabled.");
            } else {
                totalInFolder = folder.getMessageCount();
                log.info("Total messages: {}", totalInFolder);
                getNextBatch(batchSize, folder);
            }
        } catch (MessagingException e) {
            throw new EmailFetchException("Message retreival failed", e);
        }
    }

    private void getNextBatch(int batchSize, Folder folder) throws MessagingException {
        // after each batch invalidate cache
        if (messagesInCurBatch != null) {
            for (Message m : messagesInCurBatch) {
                if (m instanceof IMAPMessage) {
                    ((IMAPMessage) m).invalidateHeaders();
                }
            }
        }
        int lastMsg = lastIndex + (currentBatch + 1) * batchSize;
        lastMsg = Math.min(lastMsg, totalInFolder);
        messagesInCurBatch = folder.getMessages(lastIndex + currentBatch * batchSize + 1, lastMsg);
        folder.fetch(messagesInCurBatch, fp);
        current = 0;
        currentBatch++;
        log.info("Current batch: {}", currentBatch);
        log.info("Messages in this batch: {}", messagesInCurBatch.length);
    }

    public boolean hasNext() {
        boolean hasMore = current < messagesInCurBatch.length;
        if (!hasMore && doBatching && lastIndex + currentBatch * batchSize < totalInFolder) {
            // try next batch
            try {
                getNextBatch(batchSize, folder);
                hasMore = current < messagesInCurBatch.length;
            } catch (MessagingException e) {
                log.error("Message retreival failed");
            }
        }
        return hasMore;
    }

    private SearchTerm getSearchTerm() {
        SearchTerm st = null;

        if (fetchMailsSince != null) {
            CustomFilter dateFilter = new MailsSinceLastCheckFilter(fetchMailsSince);
            st = dateFilter.getCustomSearch(folder);
        }

        if (keywords != null && !keywords.isEmpty()) {
            SearchTerm[] terms = new SearchTerm[keywords.size() * 2];
            int i = 0;
            for (String keyword : keywords) {
                terms[i++] = new SubjectTerm(keyword);
                terms[i++] = new BodyTerm(keyword);
            }

            OrTerm orTerm = new OrTerm(terms);

            if (st == null) {
                st = orTerm;
            } else {
                st = new AndTerm(st, orTerm);
            }
        }

        return st;
    }

    public String getFolder() {
        return this.folder.getFullName();
    }

    public Message next() {
        return hasNext() ? messagesInCurBatch[current++] : null;
    }

    public void remove() {
        throw new UnsupportedOperationException("Its read only mode...");
    }
}
