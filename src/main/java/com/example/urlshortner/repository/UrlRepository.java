package com.example.urlshortner.repository;

import com.example.urlshortner.model.UrlModel;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UrlRepository extends MongoRepository<UrlModel, String> {

    public Optional<UrlModel> findByShortId(String code);
}
