package de.bearingpoint.app.capacityplanner.api;


import com.google.gson.Gson;
import de.bearingpoint.app.capacityplanner.teamfunction.APPTeam;
import de.bearingpoint.app.capacityplanner.teamfunction.APPUser;
import de.bearingpoint.app.capacityplanner.teamfunction.APPUserToTeam;
import de.bearingpoint.app.capacityplanner.teamfunction.TeamService;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.ArrayList;

/**
 * A resource of message.
 */
@Path("/team")
public class TeamServiceAPI {
    @Inject
    private TeamService teamService;
    private Gson gson = new Gson();


    @GET
    @Produces({MediaType.APPLICATION_JSON})
    public Response getTeams() {
        ArrayList<APPTeam> teams = teamService.getTeams();
        return Response.ok(teams).build();
    }

    @GET
    @Produces({MediaType.APPLICATION_JSON})
    @Path("/associated/{userkey}")
    public Response getAssociatedTeams(@PathParam("userkey") String userKey) {
        ArrayList<String> teamMembers = teamService.getAssociatedTeams(userKey);
        return Response.ok(teamMembers).build();
    }

    @GET
    @Produces({MediaType.APPLICATION_JSON})
    @Path("/{teamname}")
    public Response getTeamMembers(@PathParam("teamname") String teamName) {
        ArrayList<APPUser> teamMembers = teamService.getTeamMembers(teamName);
        return Response.ok(teamMembers).build();
    }

    @GET
    @Produces({MediaType.APPLICATION_JSON})
    @Path("/{teamname}/{userkey}")
    public Response userInTeam(@PathParam("teamname") String teamName, @PathParam("userkey") String userKey) {
        ArrayList<APPUser> teamMembers = teamService.getTeamMembers(teamName);
        return Response.ok(teamMembers).build();
    }

    @PUT
    @Consumes({MediaType.APPLICATION_JSON})
    public Response createTeam(String jsonBody) {
        try {
            Team appTeam = gson.fromJson(jsonBody, Team.class);
            if (teamService.teamAlreadyExists(appTeam.getTeamName().trim())) {
                return Response.status(409).build();
            }
            teamService.createTeam(appTeam.getTeamName().trim(), appTeam.getTeamLead(), appTeam.getAvatarURL(), appTeam.getUserName());
        } catch (Exception e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
            return Response.status(500).build();
        }
        return Response.status(201).build();
    }

    @PUT
    @Consumes({MediaType.APPLICATION_JSON})
    @Path("/{teamname}")
    public Response addTeamMembers(@PathParam("teamname") String teamName, String jsonBody) {
        try {
            RestUser[] restUsers = gson.fromJson(jsonBody, RestUser[].class);
            if (restUsers != null) {
                for (RestUser restUser : restUsers) {
                    teamService.addUserToTeam(restUser.getDisplayName(), restUser.getAvatarUrl(), restUser.getKey(), teamName);
                }
            }
        } catch (Exception e) {
            return Response.status(500).build();
        }
        return Response.status(201).build();
    }

    @DELETE
    @Consumes({MediaType.APPLICATION_JSON})
    @Path("/{teamname}/{userKey}")
    public Response deleteTeamMember(@PathParam("teamname") String teamName, @PathParam("userKey") String userKey) {
        teamName.replace("%20", " ");
        teamService.deleteUserFromTeam(userKey, teamName);
        return Response.status(201).build();
    }

    @DELETE
    @Consumes({MediaType.APPLICATION_JSON})
    @Path("/{teamname}")
    public Response deleteTeam(@PathParam("teamname") String teamName) {
        teamService.deleteTeam(teamName);
        return Response.status(201).build();
    }
}



