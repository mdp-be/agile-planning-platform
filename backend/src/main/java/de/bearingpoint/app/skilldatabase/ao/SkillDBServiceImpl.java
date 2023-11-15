package de.bearingpoint.app.skilldatabase.ao;

import com.atlassian.activeobjects.external.ActiveObjects;
import com.atlassian.plugin.spring.scanner.annotation.imports.ComponentImport;
import de.bearingpoint.app.capacityplanner.teamfunction.AssociatedTeamFieldManager;
import de.bearingpoint.app.capacityplanner.teamfunction.TeamServiceImpl;
import net.java.ao.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.inject.Named;
import java.util.ArrayList;
import java.util.Arrays;

import static com.google.gson.internal.$Gson$Preconditions.checkNotNull;

@Named
public class SkillDBServiceImpl implements SkillDBService {

    @ComponentImport
    private final ActiveObjects ao;
    private static final Logger log = LoggerFactory.getLogger(TeamServiceImpl.class);

    @Inject
    public SkillDBServiceImpl(ActiveObjects ao) {
        this.ao = checkNotNull(ao);
    }

    @Override
    public ArrayList<SkillDBUserEntry> getAllUserSkillSet() {
        return new ArrayList<>(Arrays.asList(ao.executeInTransaction(() -> ao.find(SkillDBUserEntry.class))));
    }

    @Override
    public void createUserSkillSet(String userKey, String topSkill1, String topSkill2, String topSkill3) {
        ao.executeInTransaction(()-> {
                   SkillDBUserEntry userToSkill = ao.create(SkillDBUserEntry.class);
                   userToSkill.setUserKey(userKey);
                   userToSkill.setTopSkill1(topSkill1);
                   userToSkill.setTopSkill2(topSkill2);
                   userToSkill.setTopSkill3(topSkill3);
                   userToSkill.save();
                   return null;
                }
                );
    }

    @Override
    public ArrayList<SkillDBUserEntry> getUserSkillSet(String userKey) {
        return new ArrayList<>(Arrays.asList(ao.executeInTransaction(() -> ao.find(SkillDBUserEntry.class, Query.select().where("USER_KEY = ?", userKey)))));
    }
}