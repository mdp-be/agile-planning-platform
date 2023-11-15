package de.bearingpoint.app.capacityplanner.teamfunction;

import java.util.ArrayList;

public interface TeamService {

    ArrayList<APPTeam> getTeams();

    ArrayList<String> getAssociatedTeams(String user);

    APPUser getUserByUserKey (String userkey);

    APPTeam getCurrentTeam(String user, String currentProject);

    APPUser getTeamLead(String teamName);

    ArrayList<APPUser> getTeamMembers(String teamName);

    void createTeam(String teamName, String teamLead, String avatarUrl, String userName);

    void addUserToTeam(String userName, String avatarUrl, String userKey, String teamName);

    void deleteUserFromTeam(String userName, String teamName);

    void deleteTeam(String teamName);

    boolean teamAlreadyExists (String teamName);

    boolean userAlreadyInTeam (String userKey, String teamName);

    boolean isAPPUser (String userKey);

}
