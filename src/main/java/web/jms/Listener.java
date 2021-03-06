package web.jms;

import java.util.HashMap;
import java.util.Map;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.ObjectMessage;
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
	Producer producer;

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
					producer.sendSwitchMessage("create_switch", tmp, "createc");
				} else if (type.equals("update")) {
					producer.sendSwitchMessage("update_switch", tmp, "updatec");
				} else if (type.equals("delete")) {
					producer.sendSwitchMessage("delete_switch", tmp, "deletec");
				}
			} catch (Exception e) {
				System.out.println(e.getMessage());
			}
		}
	}

	@JmsListener(destination = "create_switch", selector = "JMSCorrelationID = 'createc'")
	private void createSwitch(Message objMessage) {
		if (objMessage instanceof ObjectMessage) {
			try {
				ObjectMessage objectMessage = (ObjectMessage) objMessage;
				Switch sw = (Switch) objectMessage.getObject();
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
			} catch (Exception e) {
				System.out.println(e.getMessage());
			}
		}
	}

	@JmsListener(destination = "update_switch", selector = "JMSCorrelationID = 'updatec'")
	private void updateSwitch(Message objMessage) {
		if (objMessage instanceof ObjectMessage) {
			try {
				ObjectMessage objectMessage = (ObjectMessage) objMessage;
				Switch body = (Switch) objectMessage.getObject();
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
			} catch (Exception e) {
				System.out.println(e.getMessage());
			}
		}
	}

	@JmsListener(destination = "delete_switch", selector = "JMSCorrelationID = 'deletec'")
	private void deleteSwitch(Message objMessage) {
		if (objMessage instanceof ObjectMessage) {
			try {
				ObjectMessage objectMessage = (ObjectMessage) objMessage;
				Switch body = (Switch) objectMessage.getObject();
				String mac = body.getMAC();
				if (!StringUtils.isEmpty(mac)) {
					mac = normalizeMAC(mac);
					Switch sw = swrepo.findByMAC(mac);
					try {
						if (sw != null) {
							System.out.println("Delete " + sw.toString());
							swrepo.delete(sw);
						} else {
							System.out.println("This switch doesn't exist!");
						}
					} catch (Exception e) {
						System.out.println(e.getMessage());
					}
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
