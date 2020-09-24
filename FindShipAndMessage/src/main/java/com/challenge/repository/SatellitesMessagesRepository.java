package com.challenge.repository;

import com.challenge.model.SatelliteMessages;
import org.springframework.data.repository.CrudRepository;

public interface SatellitesMessagesRepository extends CrudRepository<SatelliteMessages, String> {
}
