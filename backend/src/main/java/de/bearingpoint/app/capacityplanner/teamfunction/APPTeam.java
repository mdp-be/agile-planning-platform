package de.bearingpoint.app.capacityplanner.teamfunction;

import net.java.ao.Entity;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "APPTeam")
public interface APPTeam extends Entity {

    @XmlElement(name = "teamName")
    @XmlAttribute
    String getTeamName();
    void setTeamName(String teamName);

    @XmlElement(name = "teamLead")
    @XmlAttribute
    String getTeamLead();
    void setTeamLead(String teamLead);
}
