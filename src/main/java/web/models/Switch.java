package web.models;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.util.StringUtils;

@Document(collection = "Devices")
public class Switch {
	@Id
	private String id;
	@Field("Name")
	private String name;
	@Field("MAC")
	@Indexed(unique = true)
	private String MAC;
	@Field("Address")
	private String address;
	@Field("Status")
	private String status; 
	@Field("Type")
	private String type;
	@Field("Version")
	private String version;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getMAC() {
		return MAC;
	}
	public void setMAC(String mAC) {
		this.MAC = mAC;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getVersion() {
		return version;
	}
	public void setVersion(String version) {
		this.version = version;
	}
	
	public Switch () {}
	
	public Switch(String name, String mAC, String address, String status, String type, String version) {
		this.name = name;
		this.MAC = mAC;
		this.address = address;
		this.status = status;
		this.type = type;
		this.version = version;
	}
	
	public Switch(String id, String name, String mAC, String address,
			String status, String type, String version) {
		this.id = id;
		this.name = name;
		this.MAC = mAC;
		this.address = address;
		this.status = status;
		this.type = type;
		this.version = version;
	}
	
	@Override
	public String toString() {
		return "Switch [id=" + id + ", name=" + name + ", MAC=" + MAC + ", address=" + address + ", status=" + status
				+ ", type=" + type + ", version=" + version + "]";
	}
		
	public boolean IsNullOrEmpty() {
		if (StringUtils.isEmpty(name) || StringUtils.isEmpty(MAC) || StringUtils.isEmpty(address)
				|| StringUtils.isEmpty(status) || StringUtils.isEmpty(type) || StringUtils.isEmpty(version)) {
			return true;
		}		
		return false;
	}
	
	public String checkData() {
		String rs = "";
		if (!StringUtils.isEmpty(name)) {
			rs += checkName();
		}
		if (!StringUtils.isEmpty(address)) {
			rs += checkAddrress();
		}
		if (!StringUtils.isEmpty(MAC)) {
			rs += checkMAC();
		}
		if (!StringUtils.isEmpty(status)) {
			rs += checkStatus();
		}
		if (!StringUtils.isEmpty(version)) {
			rs += checkVersion();
		}
		if (!StringUtils.isEmpty(type)) {
			rs += checkType();
		}		
		return rs;
	} 
	
	public String checkName() {
		final int NAME_MAX_LENGTH = 128;
		if (name.length() > NAME_MAX_LENGTH) {
			return "Name's max length is " + NAME_MAX_LENGTH + ".\n"; 
		}
		return "";
	}
	
	public String checkAddrress() {
		if (!checkIPv4Format(address)) {
			return "Address isn't an IPv4 format.\n";
		}
		return "";
	}
	
	public boolean checkIPv4Format(String ip) {
		try {
			String[] parts = ip.split("\\.");
	        if ( parts.length != 4 ) {
	            return false;
	        }
	        for (String s : parts) {
	            int i = Integer.parseInt(s);
	            if ((i < 0) || (i > 255)) {
	                return false;
	            }
	        }
	        if (ip.endsWith(".")) {
	            return false;
	        }
	        return true;
			
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
	
	public String checkMAC() {
		String mac = getMAC(MAC);
		if (mac == null) {
			return "MAC isn't an MAC format.\n";
		}
		MAC = mac.replaceAll("\\-", ":");
		return "";
	}
	
	public String getMAC(String mac) {
		if (mac.length() <= 17) {
			Pattern p = Pattern.compile("^((([0-9A-Fa-f]{2}[:-]){5}))[0-9A-Fa-f]{2}$");
			Matcher m = p.matcher(mac);
			if (m.find()) {
			   return m.group();
			}
		}
		return null;
	}
	
	public String checkStatus() {
		String tmp = status.toLowerCase();
		if (tmp.equals("up")) {
			status = "Up";
		} else if (tmp.equals("down")) {
			status = "Down";
		} else if (tmp.equals("warning")) {
			status = "Warning";
		} else {
			return "Status must be one of these: Up, Down or Warning.\n";
		}
		return "";
	}
	
	public String checkVersion() {
		final int VERSION_MAX_LENGTH = 64;
		if (version.length() > VERSION_MAX_LENGTH) {
			return "Version's max length is " + VERSION_MAX_LENGTH + ".\n"; 
		}
		return "";
	}
	
	public String checkType() {
		final int TYPE_MAX_LENGTH = 64;
		if (type.length() > TYPE_MAX_LENGTH) {
			return "Type's max length is " + TYPE_MAX_LENGTH + "."; 
		}
		return "";
	}
}
