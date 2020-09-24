package com.challenge;

import com.challenge.dto.ResponsePositionAndMessage;
import com.challenge.model.SatelliteMessages;
import com.challenge.util.AvailableSatellites;
import com.challenge.repository.SatellitesMessagesRepository;
import com.challenge.service.FindShipAndMessageImpl;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;

import static org.mockito.Mockito.when;


@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
public class FindShipAndMessageImplTest {

	private String nameKey = "name";
	private String distanceKey = "distance";
	private String messageKey = "message";

	ArrayList<LinkedHashMap> satellites = new ArrayList<>();
	ArrayList<SatelliteMessages> satelliteMessages = new ArrayList<>();

	@Autowired
	FindShipAndMessageImpl findShipAndMessageimpl;

	@Before
	public void setUp() {

		String[] messageKenobi = {"este", "", "", "mensaje", ""};
		ArrayList arrayKenobi = new ArrayList<String>();
		Collections.addAll(arrayKenobi, messageKenobi);

		String[] messageSkywalker = {"", "es", "", "", "secreto"};
		ArrayList arraySkywalker = new ArrayList<String>();
		Collections.addAll(arraySkywalker, messageSkywalker);

		String[] messageSato = {"este", "", "un", "", ""};
		ArrayList arraySato = new ArrayList<String>();
		Collections.addAll(arraySato, messageSato);

	    LinkedHashMap satelliteSato = new LinkedHashMap();
		LinkedHashMap satelliteSkywalker = new LinkedHashMap();
		LinkedHashMap satelliteKenobi = new LinkedHashMap();

		satelliteSato.put(nameKey, AvailableSatellites.KENOBI.name);
		satelliteSato.put(distanceKey, 400d);
		satelliteSato.put(messageKey,  arrayKenobi);

		satellites.add(satelliteSato);

		satelliteSkywalker.put(nameKey, AvailableSatellites.SKYWALKER.name);
		satelliteSkywalker.put(distanceKey, 400.5d);
		satelliteSkywalker.put(messageKey,  arraySkywalker);

		satellites.add(satelliteSkywalker);

		satelliteKenobi.put(nameKey, AvailableSatellites.SATO.name);
		satelliteKenobi.put(distanceKey, 899.4d);
		satelliteKenobi.put(messageKey,  arraySato);

		satellites.add(satelliteKenobi);

		satelliteMessages.add(new SatelliteMessages(AvailableSatellites.KENOBI.name, 400f, messageKenobi));
		satelliteMessages.add(new SatelliteMessages(AvailableSatellites.SKYWALKER.name, 400.5f, messageSkywalker));
		satelliteMessages.add(new SatelliteMessages(AvailableSatellites.SATO.name, 899.4f, messageSato));

	}

	@Test
	public void testTopSecret() {

		ResponsePositionAndMessage result = findShipAndMessageimpl.topSecret(satellites);
		Assert.assertNotNull(result.getMessage());
		Assert.assertNotNull(result.getPosition());
	}
	
	@Test
	public void testGetTopSecretSplit() throws Exception {
		SatellitesMessagesRepository satellitesMessagesRepository = Mockito.mock(SatellitesMessagesRepository.class);

		FindShipAndMessageImpl findTheCelebServiceImpl = new FindShipAndMessageImpl(satellitesMessagesRepository);
		when(satellitesMessagesRepository.findAll()).thenReturn(satelliteMessages);
		SatelliteMessages satelliteMessages = new SatelliteMessages();
		satelliteMessages.setName("kenobi");
		ResponsePositionAndMessage result = findTheCelebServiceImpl.getTopSecretSplit(satelliteMessages);
		Assert.assertNotNull(result.getMessage());
		Assert.assertNotNull(result.getPosition());
	}
	

}
