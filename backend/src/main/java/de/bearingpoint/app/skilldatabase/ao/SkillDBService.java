package de.bearingpoint.app.skilldatabase.ao;

import java.util.ArrayList;

public interface SkillDBService {

    ArrayList<SkillDBUserEntry> getAllUserSkillSet();

    void createUserSkillSet(String userKey, String topSkill1, String topSkill2, String topSkill3);

    ArrayList<SkillDBUserEntry> getUserSkillSet(String userKey);

}
