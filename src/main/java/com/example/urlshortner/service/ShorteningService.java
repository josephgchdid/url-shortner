package com.example.urlshortner.service;

import com.example.urlshortner.model.UrlModel;
import com.example.urlshortner.repository.UrlRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import java.net.URI;
import java.net.URL;
import java.security.SecureRandom;
import java.util.Optional;

@Service
public class ShorteningService {

    @Autowired
    UrlRepository repository;

    private final String ALPHA_NUMERIC = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";

    private final SecureRandom RAND = new SecureRandom();

    private final int STR_LEN = 10;

    public ResponseEntity<?> shortenUrl(String url){

        if(!validateURI(url)){
            return new ResponseEntity<>(String.format("%s is an invalid URL", url), HttpStatus.NOT_ACCEPTABLE);
        }

        return save(new UrlModel(generateShortId() , url));
    }

    public ResponseEntity<?> shortenUrl(String url, String customCode){

        int codeLen = customCode.length();

        //remove white space
        customCode = customCode.replaceAll("\\s+", "" );

        //avoid code that is a URL
        if(!validateURI(url)  ){
            return new ResponseEntity<>(String.format("%s is an invalid URL", url), HttpStatus.NOT_ACCEPTABLE);
        }
        else if(validateURI(customCode) ||  codeLen < 10 || codeLen >= 20)
            return new ResponseEntity<>(String.format("%s is an invalid Code", customCode), HttpStatus.NOT_ACCEPTABLE);

        return save(new UrlModel(customCode, url));

    }

    //helper function to reduce code
    private ResponseEntity<?> save(UrlModel urlModel){
        boolean urlIsSaved = saveUrl(urlModel);

        if(!urlIsSaved)
            return  new ResponseEntity<>("Url not saved", HttpStatus.BAD_REQUEST);

        return  new ResponseEntity<>(urlModel, HttpStatus.OK);
    }

    private boolean saveUrl(UrlModel urlModel){

        try{

            Optional<UrlModel> urlToLookFor =  repository.findByShortId(urlModel.getShortId());

            //code is already taken, this is used for when a custom code is passed.
            //and the slight chance two generated codes are identical
            if(urlToLookFor.isPresent())
                return false;

            repository.save(urlModel);
            return true;

        }catch (Exception e){
            System.out.println(e.getMessage());
            return false;
        }
    }

    public ResponseEntity<?> redirectToOriginalURL(String code){

        try {

            Optional<UrlModel> uriToLookFor =  repository.findByShortId(code);

            if(uriToLookFor.isEmpty())
                return  new ResponseEntity<>(String.format("%s is not a valid code", code), HttpStatus.NOT_FOUND);

            URI uriToRedirectTo = new URI( uriToLookFor.get().getDestination());

            HttpHeaders httpHeaders = new HttpHeaders();
            httpHeaders.setLocation(uriToRedirectTo);

            return new ResponseEntity<>(httpHeaders, HttpStatus.MOVED_PERMANENTLY);

        }catch (Exception e){
            System.out.println(e.getMessage());
            return  new ResponseEntity<>("error fetching code ", HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    private String generateShortId(){

        StringBuilder sb = new StringBuilder(STR_LEN);

        for(int i = 0; i < STR_LEN; i++){
            sb.append(ALPHA_NUMERIC.charAt(RAND.nextInt(ALPHA_NUMERIC.length())));
        }

        return sb.toString();
    }

    //small trick to validate URI
    private boolean validateURI(String uriToCheck) {
        try {
            new URL(uriToCheck).toURI();
            return true;
        }catch (Exception e){
            return false;
        }
    }
}
