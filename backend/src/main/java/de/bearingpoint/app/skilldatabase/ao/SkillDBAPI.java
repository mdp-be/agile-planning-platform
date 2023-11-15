package de.bearingpoint.app.skilldatabase.ao;

import com.google.gson.Gson;
import de.bearingpoint.app.capacityplanner.api.Team;
import de.bearingpoint.app.capacityplanner.teamfunction.APPTeam;
import de.bearingpoint.app.capacityplanner.teamfunction.TeamService;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.ArrayList;

@Path("/skilldb")
public class SkillDBAPI {

    @Inject
    private SkillDBService skillDBService;
    private Gson gson = new Gson();

    @GET
    @Produces({MediaType.APPLICATION_JSON})
    public Response getSkillSets() {
        ArrayList<SkillDBUserEntry> skillSets = skillDBService.getAllUserSkillSet();
        return Response.ok(skillSets).build();
    }

    @PUT
    @Consumes({MediaType.APPLICATION_JSON})
    public Response createSkillEntry(String jsonBody) {
        try {
            SkillEntry skillEntry = gson.fromJson(jsonBody, SkillEntry.class);
//            if (teamService.teamAlreadyExists(appTeam.getTeamName().trim())) {
//                return Response.status(409).build();
//            }
//            teamService.createTeam(appTeam.getTeamName().trim(), appTeam.getTeamLead(), appTeam.getAvatarURL(), appTeam.getUserName());
            skillDBService.createUserSkillSet(skillEntry.getUserKey(), skillEntry.topSkill1, skillEntry.topSkill2, skillEntry.topSkill3);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
            return Response.status(500).build();
        }
        return Response.status(201).build();
    }

}
