package de.bearingpoint.app.capacityplanner.teamfunction;

import com.atlassian.activeobjects.external.ActiveObjects;
import com.atlassian.plugin.spring.scanner.annotation.imports.ComponentImport;
import net.java.ao.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.inject.Named;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

import static com.google.gson.internal.$Gson$Preconditions.checkNotNull;

@Named
public class TeamServiceImpl implements TeamService {

    @ComponentImport
    private final ActiveObjects ao;
    private static final Logger log = LoggerFactory.getLogger(TeamServiceImpl.class);
    private final AssociatedTeamFieldManager atfm = new AssociatedTeamFieldManager();

    @Inject
    public TeamServiceImpl(ActiveObjects ao) {
        this.ao = checkNotNull(ao);
    }

    @Override
    public ArrayList<APPTeam> getTeams() {
        return new ArrayList<>(Arrays.asList(ao.executeInTransaction(() -> ao.find(APPTeam.class))));
    }

    @Override
    public ArrayList<String> getAssociatedTeams(String userKey) {
        ArrayList<String> associatedTeams = new ArrayList<>();
        APPUserToTeam[] userToTeams = ao.executeInTransaction(() -> ao.find(APPUserToTeam.class, Query.select().where("USER_KEY = ?", userKey)));
        for (APPUserToTeam team : userToTeams) {
            associatedTeams.add(team.getTeamName());
        }
        return associatedTeams;
    }

    @Override
    public APPUser getUserByUserKey(String userkey) {
        APPUser[] user = ao.executeInTransaction(() -> ao.find(APPUser.class, Query.select().where("USER_KEY = ?", userkey)));
        if (user.length == 1) {
            return user[0];
        }
        return null;
    }

    @Override
    public APPTeam getCurrentTeam(String user, String currentProject) {
        return null;
    }

    @Override
    public APPUser getTeamLead(String teamName) {
        return ao.executeInTransaction(() -> {
            APPTeam[] resp = ao.find(APPTeam.class, Query.select().where("TEAM_NAME = ?", teamName));
            if (resp.length == 1) {
                APPUser[] teamlead = ao.executeInTransaction(() -> ao.find(APPUser.class, Query.select().where("USER_KEY = ?", resp[0].getTeamLead())));
                if (teamlead.length == 1)
                    return teamlead[0];
            }
            return null;
        });
    }

    @Override
    @XmlElementWrapper(name = "APPUser")
    @XmlElement(name = "user")
    public ArrayList<APPUser> getTeamMembers(String teamName) {
        ArrayList<APPUser> teamMembers = new ArrayList<>();
        ao.executeInTransaction(() -> {
            APPUserToTeam[] team = ao.find(APPUserToTeam.class, Query.select().where("TEAM_NAME = ?", teamName));
            for (APPUserToTeam member : team) {
                Collections.addAll(teamMembers, ao.find(APPUser.class, Query.select().where("USER_KEY = ?", member.getUserKey())));
            }
            return null;
        });
        return teamMembers;
    }

    @Override
    public void createTeam(String teamName, String teamLead, String avatarUrl, String userName) {
        ao.executeInTransaction(() -> {
            if (ao.find(APPUser.class, Query.select().where("USER_KEY = ?", teamLead)).length == 0) {
                APPUser user = ao.create(APPUser.class);
                user.setUserName(userName);
                user.setAvatarURL(avatarUrl);
                user.setUserKey(teamLead);
                user.save();
            }
            APPTeam team = ao.create(APPTeam.class);
            team.setTeamName(teamName);
            team.setTeamLead(teamLead);
            team.save();
            APPUserToTeam userToTeam = ao.create(APPUserToTeam.class);
            userToTeam.setUserKey(teamLead);
            userToTeam.setTeamName(teamName);
            userToTeam.save();
            return null;
        });
        atfm.addTeamOption(teamName);
    }

    @Override
    public void addUserToTeam(String userName, String avatarUrl, String userKey, String teamName) {
        ao.executeInTransaction(() -> {
            if (ao.find(APPUser.class, Query.select().where("USER_KEY = ?", userKey)).length == 0) {
                APPUser user = ao.create(APPUser.class);
                user.setUserName(userName);
                user.setAvatarURL(avatarUrl);
                user.setUserKey(userKey);
                user.save();
            }
            APPUserToTeam userToTeam = ao.create(APPUserToTeam.class);
            userToTeam.setUserKey(userKey);
            userToTeam.setTeamName(teamName);
            userToTeam.save();
            return null;
        });
    }

    @Override
    public void deleteUserFromTeam(String userKey, String teamName) {
        ao.executeInTransaction(() -> {
            if (ao.find(APPTeam.class, Query.select().where("TEAM_NAME = ? AND TEAM_LEAD = ?", teamName, userKey)).length != 1) {
                ao.delete(ao.find(APPUserToTeam.class, Query.select().where("TEAM_NAME = ? AND USER_KEY = ?", teamName, userKey)));
                if (ao.find(APPUserToTeam.class, Query.select().where("USER_KEY = ?", userKey)).length < 1) {
                    ao.delete(ao.find(APPUser.class, Query.select().where("USER_KEY = ?", userKey)));
                }
            }
            return null;
        });
    }

    @Override
    public void deleteTeam(String teamName) {
        ao.executeInTransaction(() -> {
            ao.delete(ao.find(APPTeam.class, Query.select().where("TEAM_NAME = ?", teamName)));
            APPUserToTeam[] members = ao.find(APPUserToTeam.class, Query.select().where("TEAM_NAME = ?", teamName));
            for (APPUserToTeam member : members) {
                ao.delete(ao.find(APPUserToTeam.class, Query.select().where("TEAM_NAME = ? AND USER_KEY = ?", teamName, member.getUserKey())));
                APPUserToTeam[] size = ao.find(APPUserToTeam.class, Query.select().where("USER_KEY = ?", member.getUserKey()));
                if (!(size.length >= 1)) {
                    ao.delete(ao.find(APPUser.class, Query.select().where("USER_KEY = ?", member.getUserKey())));
                }
            }
            return null;
        });
        atfm.removeTeamOption(teamName);
    }

    @Override
    public boolean teamAlreadyExists(String teamName) {
        APPTeam[] teams = ao.executeInTransaction(() -> ao.find(APPTeam.class, Query.select().where("TEAM_NAME = ?", teamName)));
        return teams.length != 0;
    }

    @Override
    public boolean userAlreadyInTeam(String userKey, String teamName) {
        APPUserToTeam[] result = ao.executeInTransaction(() -> ao.find(APPUserToTeam.class, Query.select().where("TEAM_NAME = ? AND USER_KEY = ?", teamName, userKey)));
        return result.length > 0;
    }

    @Override
    public boolean isAPPUser(String userKey) {
        return false;
    }

}