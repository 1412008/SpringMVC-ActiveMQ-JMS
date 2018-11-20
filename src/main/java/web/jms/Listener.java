package web.jms;

import java.util.HashMap;
import java.util.Map;

import javax.jms.Message;
import javax.jms.TextMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import web.daos.SwitchRepository;
import web.models.Switch;

@Component
public class Listener {

	@Autowired
	SwitchRepository swrepo;

	@JmsListener(destination = "switch_manager")
	public void switchManager(Message jsonMessage) {
		if (jsonMessage instanceof TextMessage) {
			TextMessage textMessage = (TextMessage) jsonMessage;
			try {
				ObjectMapper mapper = new ObjectMapper();
				final TypeReference<HashMap<String, Object>> TYPE_REF = new TypeReference<HashMap<String, Object>>() {
				};
				Map<String, Object> map = mapper.readValue(textMessage.getText(), TYPE_REF);
				String type = (String) map.get("Type");
				Object datajson = map.get("Data");
				Switch tmp = mapper.convertValue(datajson, Switch.class);
				if (type.equals("create")) {
					createSwitch(tmp);
				} else if (type.equals("update")) {
					updateSwitch(tmp);
				} else if (type.equals("delete")) {
					deleteSwitch(tmp);
				}
			} catch (Exception e) {
				System.out.println(e.getMessage());
			}
		}
	}

	private void createSwitch(Switch sw) {
		if (!sw.IsNullOrEmpty()) {
			System.out.println(sw);
			sw.setId(null);
			String err = sw.checkData();
			if (err.equals("")) {
				System.out.println("ok");
				if (swrepo.insert(sw) != null) {
					System.out.println("Insert success!");
				}
			} else {
				System.out.println(err);
			}
		}
	}

	private void updateSwitch(Switch body) {
		String err = body.checkData();
		if (err.equals("")) {
			String mac = body.getMAC();
			if (!StringUtils.isEmpty(mac)) {
				mac = normalizeMAC(mac);
				Switch sw = swrepo.findByMAC(mac);
				if (sw != null) {
					System.out.println("Before update: " + sw.toString());
					if (!StringUtils.isEmpty(body.getName())) {
						sw.setName(body.getName());
					}
					if (!StringUtils.isEmpty(body.getType())) {
						sw.setType(body.getType());
					}
					if (!StringUtils.isEmpty(body.getAddress())) {
						sw.setAddress(body.getAddress());
					}
					if (!StringUtils.isEmpty(body.getVersion())) {
						sw.setVersion(body.getVersion());
					}
					try {
						if (swrepo.save(sw) != null) {
							System.out.println("New: " + sw.toString());
						} else {
							System.out.println("Update failed.");
						}
					} catch (Exception e) {
						System.out.println(e.getMessage());
					}
				} else {
					System.out.println("This switch doesn't exist!");
				}
			}
		} else {
			System.out.println(err);
		}
	}

	private void deleteSwitch(Switch body) {
		String mac = body.getMAC();
		if (!StringUtils.isEmpty(mac)) {
			mac = normalizeMAC(mac);
			Switch sw = swrepo.findByMAC(mac);
			try {
				if (sw != null) {
					System.out.println("Delete " + sw.toString());
					swrepo.delete(sw);
				}
			} catch (Exception e) {
				System.out.println(e.getMessage());
			}
		}
	}

	private String normalizeMAC(String mac) {
		return mac.replaceAll("\\-", ":");
	}
}
