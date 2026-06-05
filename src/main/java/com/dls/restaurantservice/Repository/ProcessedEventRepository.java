package com.dls.restaurantservice.Repository;

import com.dls.restaurantservice.Document.ProcessedEvent;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProcessedEventRepository extends MongoRepository<ProcessedEvent, String> {
}
