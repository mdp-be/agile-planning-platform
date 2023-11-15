package de.bearingpoint.app.capacityplanner.servlets.settings.project;

import com.atlassian.jira.user.ApplicationUser;
import com.atlassian.jira.util.velocity.VelocityRequestContextFactory;
import com.atlassian.plugin.spring.scanner.annotation.imports.ComponentImport;
import com.atlassian.templaterenderer.TemplateRenderer;
import de.bearingpoint.app.capacityplanner.teamfunction.TeamService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static com.atlassian.jira.component.ComponentAccessor.getJiraAuthenticationContext;

public class MyTeamServlet extends HttpServlet {
    private static final Logger log = LoggerFactory.getLogger(MyTeamServlet.class);
    @ComponentImport
    private final TemplateRenderer renderer;
    @Inject
    @ComponentImport
    private VelocityRequestContextFactory velocityRequestContextFactory;
    @Inject
    private TeamService teamService;

    public MyTeamServlet(TemplateRenderer renderer, TeamService teamService) {
        this.renderer = renderer;
        this.teamService = teamService;
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Map<String, Object> params = new HashMap<>();
        ApplicationUser user = getJiraAuthenticationContext().getLoggedInUser();
        String userKey = user.getKey();

        params.put("userKey", userKey);
        params.put("userName", user.getDisplayName());
        params.put("baseurl", velocityRequestContextFactory.getJiraVelocityRequestContext().getCanonicalBaseUrl());
        params.put("loggedInUser", getJiraAuthenticationContext().getLoggedInUser().getUsername());
        params.put("associatedTeams", teamService.getAssociatedTeams(userKey));
        params.put("teamService", teamService);

        resp.setContentType("text/html;charset=utf-8");
        renderer.render("pages/my_team.vm", params, resp.getWriter());
    }
}
