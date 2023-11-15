package de.bearingpoint.app.skilldatabase.ao;

import net.java.ao.Entity;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "SkillDBUserEntry")
public interface SkillDBUserEntry extends Entity {

    @XmlElement(name = "userKey")
    @XmlAttribute
    String getUserKey();
    void setUserKey(String userKey);

    @XmlElement(name = "topSkill1")
    @XmlAttribute
    String getTopSkill1();
    void setTopSkill1(String topSkill);

    @XmlElement(name = "topSkill2")
    @XmlAttribute
    String getTopSkill2();
    void setTopSkill2(String topSkill2);

    @XmlElement(name = "topSkill3")
    @XmlAttribute
    String getTopSkill3();
    void setTopSkill3(String topSkill3);



}
