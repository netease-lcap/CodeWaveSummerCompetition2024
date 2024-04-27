package com.fdddf.shorturl.controller;

import com.fdddf.shorturl.LinkService;
import com.fdddf.shorturl.model.Link;
import com.fdddf.shorturl.model.ShortUrlRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
public class GoController {

    private static final Logger log = LoggerFactory.getLogger(GoController.class);

    @Autowired
    private LinkService linkService;

    @RequestMapping(value = "/go/{hashid}", method = RequestMethod.GET)
    public String go(@PathVariable("hashid") String hashid) {

        String longUrl = linkService.getLongUrl(hashid);
        if (longUrl == null) {
            log.warn("hashid not found: " + hashid);
            return "redirect:/";
        }
        linkService.updateAccessCount(hashid);

        return "redirect:" + longUrl;
    }

    @ResponseBody
    @RequestMapping(value = "/shorten", method = RequestMethod.POST)
    public Link shortenUrl(@RequestBody ShortUrlRequest request) {
        return linkService.saveUrlMap(request);
    }
}
