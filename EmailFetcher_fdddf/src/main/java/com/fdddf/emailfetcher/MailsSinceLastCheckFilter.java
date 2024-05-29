package com.fdddf.emailfetcher;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.search.ComparisonTerm;
import javax.mail.search.DateTerm;
import javax.mail.search.SearchTerm;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MailsSinceLastCheckFilter implements CustomFilter {

    private static final Logger logger = LoggerFactory.getLogger(MailsSinceLastCheckFilter.class);

    private Date since;

    public MailsSinceLastCheckFilter(Date date) {
        since = date;
    }

    @SuppressWarnings("serial")
    public SearchTerm getCustomSearch(final Folder folder) {
        final SimpleDateFormat parser = new SimpleDateFormat("yyyy-MM-dd");
        final String sinceText = new SimpleDateFormat("yyyy-MM-dd").format(since);

        logger.info("Building mail filter for messages in {} that occur from {}", folder.getName(), sinceText);
        return new DateTerm(ComparisonTerm.GE, since) {
            private int matched = 0;
            private int seen = 0;

            @Override
            public boolean match(Message msg) {
                boolean isMatch = false;
                ++seen;
                try {
                    Date msgDate = msg.getReceivedDate();
                    if (msgDate == null) msgDate = msg.getSentDate();

                    if (msgDate != null && msgDate.getTime() >= since.getTime()) {
                        ++matched;
                        isMatch = true;
                    } else {
                        if (logger.isDebugEnabled()) {
                            String msgDateStr = (msgDate != null) ? parser.format(msgDate) : "null";
                            String sinceDateStr = (since != null) ? sinceText : "null";
                            logger.debug("Message {} was received at [{}], since filter is [{}]", msg.getSubject(), msgDateStr, sinceDateStr);
                        }
                    }
                } catch (MessagingException e) {
                    logger.warn("Failed to process message", e);
                }

                if (seen % 100 == 0) {
                    logger.info("Matched {} of {} messages since {}", matched, seen, sinceText);
                }

                return isMatch;
            }
        };
    }
}
