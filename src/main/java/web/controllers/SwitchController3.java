package web.controllers;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import web.daos.SwitchRepository;
import web.models.Switch;

import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@RestController
@RequestMapping("/api")
public class SwitchController3 {
	
	@Autowired
	SwitchRepository swRepo;
	
	@RequestMapping(value = {"switches"}, method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
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
//		String token = req.getHeader(jwtVariable.getHEADER());
//		System.out.println("Token: " + token);
//		if (token == null || !tokenHelper.validateToken(token)) {
//			System.out.println("Invalid token!");
//			return ResponseEntity.ok(HttpStatus.NOT_ACCEPTABLE);
//		}
		
		try {
			if (!body.IsNullOrEmpty()) {
				body.setId(null);
				String err = body.checkData();
				if (err.equals("")) {
					if (swRepo.insert(body) != null) {
						System.out.println("Insert success!");
						return ResponseEntity.ok(HttpStatus.CREATED);
					}
				} else {
					System.out.println(err);
					return ResponseEntity.ok(HttpStatus.NOT_ACCEPTABLE);
				}
			} else {
				return ResponseEntity.ok(HttpStatus.NO_CONTENT);
			}
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.ok(HttpStatus.BAD_REQUEST);
		}
		return ResponseEntity.ok(HttpStatus.NOT_FOUND);
	}
	
	@RequestMapping(value = "switch", method = RequestMethod.PUT)
	public ResponseEntity<HttpStatus> updateSwitch(@RequestBody Switch body) {
		String err = body.checkData();
		if (err.equals("")) {
			String mac = body.getMAC();
			if (!StringUtils.isEmpty(mac)) {
				mac = normalizeMAC(mac);
				Switch sw = swRepo.findByMAC(mac);
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
					if(swRepo.save(sw) != null) {
						System.out.println("new: " + sw.toString());
						return ResponseEntity.ok(HttpStatus.OK);
					}
				}
			}	
		} else {
			System.out.println(err);
		}
		return ResponseEntity.ok(HttpStatus.NOT_FOUND);
	}
	
	@RequestMapping(value = "switch", method = RequestMethod.DELETE)
	public ResponseEntity<HttpStatus> deleteSwitch(@RequestBody Switch body) {
		String mac = body.getMAC();
		if (!StringUtils.isEmpty(mac)) {
			mac = normalizeMAC(mac);
			Switch sw = swRepo.findByMAC(mac);
			try {
				if (sw != null) {
					System.out.println("Delete " + sw.toString());
					swRepo.delete(sw);
					return ResponseEntity.ok(HttpStatus.ACCEPTED);
				}
			}
			catch (Exception e) {
				e.printStackTrace();
				return ResponseEntity.ok(HttpStatus.BAD_REQUEST);
			}
		}
		
		return ResponseEntity.ok(HttpStatus.NOT_FOUND);
	}
	
	private String normalizeMAC(String mac) {
		return mac.replaceAll("\\-", ":");
	}
}
