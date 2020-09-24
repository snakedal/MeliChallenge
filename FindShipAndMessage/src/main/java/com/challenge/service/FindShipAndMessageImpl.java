package com.challenge.service;

import com.challenge.util.FindShipAndMessageUtil;
import com.challenge.dto.ResponsePositionAndMessage;
import com.challenge.model.SatelliteMessages;
import com.challenge.repository.SatellitesMessagesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.LinkedHashMap;

@Service
public class FindShipAndMessageImpl implements FindShipAndMessage {

	@Autowired
	public SatellitesMessagesRepository satellitesMessagesRepository;

	public FindShipAndMessageImpl() {}

	public FindShipAndMessageImpl(SatellitesMessagesRepository satellitesMessagesRepository) {
		this.satellitesMessagesRepository = satellitesMessagesRepository;
	}

	@Override
	public ResponsePositionAndMessage topSecret(ArrayList<LinkedHashMap> satellites) {
		return FindShipAndMessageUtil.getResponseTopSecret(satellites);
	}

	@Override
	public void postTopSecretSplit(SatelliteMessages satelliteMessage) throws Exception {
		if(FindShipAndMessageUtil.validateSatelliteName(satelliteMessage.getName())){
			satellitesMessagesRepository.save(satelliteMessage);
		}
	}

	@Override
	public ResponsePositionAndMessage getTopSecretSplit(SatelliteMessages satelliteMessageInput) throws Exception {

		ArrayList<SatelliteMessages> satelliteMessages = (ArrayList<SatelliteMessages>) satellitesMessagesRepository.findAll();

		boolean isNewSatellite = satelliteMessages.stream().noneMatch(satelliteName -> satelliteName.getName().equalsIgnoreCase(satelliteMessageInput.getName()));
		if(isNewSatellite && FindShipAndMessageUtil.validateSatelliteName(satelliteMessageInput.getName())){
			satelliteMessages.add(satelliteMessageInput);
		}

		return FindShipAndMessageUtil.getResponseTopSecretSplit(satelliteMessages);
	}


}
