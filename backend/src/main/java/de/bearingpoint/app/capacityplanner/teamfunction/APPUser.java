package de.bearingpoint.app.capacityplanner.teamfunction;

import net.java.ao.Entity;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "APPUser")
public interface APPUser extends Entity {

    @XmlElement(name = "username")
    @XmlAttribute
    String getUserName();
    void setUserName(String userName);

    @XmlElement(name = "avatarURL")
    @XmlAttribute
    String getAvatarURL();
    void setAvatarURL(String avatarURL);

    @XmlElement(name = "userKey")
    @XmlAttribute
    String getUserKey();
    void setUserKey(String userKey);
}
