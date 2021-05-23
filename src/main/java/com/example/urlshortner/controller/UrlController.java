package com.example.urlshortner.controller;

import com.example.urlshortner.service.ShorteningService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
public class UrlController {

    @Autowired
    ShorteningService shorteningService;

    @PostMapping()
    public ResponseEntity<?> shortenUrl(
            @RequestParam("url") String url,
            @Nullable @RequestParam("customCode")String customCode){

        if(customCode != null)
            return shorteningService.shortenUrl(url, customCode);

        return shorteningService.shortenUrl(url);
    }

    @GetMapping(path = "/{code}")
    public ResponseEntity<?> redirect(@NonNull @PathVariable String code){
        return shorteningService.redirectToOriginalURL(code);
    }
}
