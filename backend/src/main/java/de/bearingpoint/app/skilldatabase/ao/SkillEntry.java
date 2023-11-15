package de.bearingpoint.app.skilldatabase.ao;

import net.java.ao.Entity;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class SkillEntry {

    @XmlElement
    String userKey;
    @XmlElement
    String topSkill1;
    @XmlElement
    String topSkill2;
    @XmlElement
    String topSkill3;

    void setUserKey(String userKey) {
        this.userKey = userKey;
    }

    String getUserKey() {
        return this.userKey;
    }

    void setTopSkill1(String topSkill1) {
        this.topSkill1 = topSkill1;
    }

    String getTopSkill1() {
        return this.topSkill1;
    }

    void setTopSkill2(String topSkill2) {
        this.topSkill2 = topSkill2;
    }

    String getTopSkill2() {
        return this.topSkill2;
    }

    void setTopSkill3(String topSkill3) {
        this.topSkill3 = topSkill3;
    }

    String getTopSkill3() {
        return this.topSkill3;
    }
}
