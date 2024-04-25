package com.fdddf.shorturl.controller;

import com.fdddf.shorturl.api.ShortUrlApi;
import com.fdddf.shorturl.LinkService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class GoController {

    private static final Logger log = LoggerFactory.getLogger(GoController.class);

    @Autowired
    LinkService linkService;
    @RequestMapping(value="/go/{hashid}", method=RequestMethod.GET)
    public String go(@PathVariable("hashid") String hashid) {

        String longUrl = linkService.getLongUrl(hashid);
        if (longUrl == null) {
            log.warn("hashid not found: " + hashid);
            return "redirect:/";
        }
        linkService.updateAccessCount(hashid);

        return "redirect:" + longUrl;
    }
}
