package de.bearingpoint.app.capacityplanner.teamfunction;

import net.java.ao.Entity;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "APPUserToTeam")
public interface APPUserToTeam extends Entity {

    @XmlElement(name = "userkey")
    @XmlAttribute
    String getUserKey();
    void setUserKey(String userKey);

    @XmlElement(name = "teamname")
    @XmlAttribute
    String getTeamName();
    void setTeamName(String teamName);
}
