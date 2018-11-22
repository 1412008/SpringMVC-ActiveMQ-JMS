package web.controllers;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.ObjectMapper;

import web.daos.SwitchRepository;
import web.jms.Producer;
import web.models.Switch;

import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@RestController
@RequestMapping("/api")
public class SwitchController3 {

	@Autowired
	SwitchRepository swRepo;

	@Autowired
	private Producer producer;

	private final String DestinationName = "switch_manager";

	@RequestMapping(value = { "switches" }, method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody List<Switch> getAll() {
		List<Switch> switches = swRepo.findAll();
		return switches;
	}

	@RequestMapping(value = "switch", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Switch getOne(HttpServletRequest req) {
		String mac = req.getParameter("MAC");
		if (!StringUtils.isEmpty(mac)) {
			mac = normalizeMAC(mac);
			Switch sw = swRepo.findByMAC(mac);
			return sw;
		}
		return null;
	}

	@RequestMapping(value = "switch", method = RequestMethod.POST)
	public ResponseEntity<HttpStatus> createSwitch(@RequestBody Switch body, HttpServletRequest req) {
		System.out.println("Creating switch...");
		//SendHelper("create", body);
		producer.sendSwitchMessage("create_switch", body, "createc");
		return ResponseEntity.ok(HttpStatus.OK);
	}

	@RequestMapping(value = "switch", method = RequestMethod.PUT)
	public ResponseEntity<HttpStatus> updateSwitch(@RequestBody Switch body) {
		System.out.println("Updating switch...");
		//SendHelper("update", body);
		producer.sendSwitchMessage("update_switch", body, "updatec");
		return ResponseEntity.ok(HttpStatus.OK);
	}

	@RequestMapping(value = "switch", method = RequestMethod.DELETE)
	public ResponseEntity<HttpStatus> deleteSwitch(@RequestBody Switch body) {
		System.out.println("Deleting switch...");
		//SendHelper("delete", body);
		producer.sendSwitchMessage("delete_switch", body, "deletec");
		return ResponseEntity.ok(HttpStatus.OK);
	}

	@SuppressWarnings("unused")
	private void SendHelper(String type, Switch body) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("Type", type);
		try {
			ObjectMapper mapper = new ObjectMapper();
			map.put("Data", body);
			producer.sendMessage(DestinationName, mapper.writerWithDefaultPrettyPrinter().writeValueAsString(map));
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}

	private String normalizeMAC(String mac) {
		return mac.replaceAll("\\-", ":");
	}
}
