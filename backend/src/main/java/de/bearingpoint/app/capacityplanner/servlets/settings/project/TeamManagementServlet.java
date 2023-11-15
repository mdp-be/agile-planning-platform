package de.bearingpoint.app.capacityplanner.servlets.settings.project;

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
import java.util.*;

import static com.google.common.base.Preconditions.checkNotNull;


/**
 * Servlet for the team management page
 */
public class TeamManagementServlet extends HttpServlet {
    private static final Logger log = LoggerFactory.getLogger(TeamManagementServlet.class);
    @ComponentImport
    private final TemplateRenderer renderer;
    @Inject
    @ComponentImport
    private VelocityRequestContextFactory velocityRequestContextFactory;
    @Inject
    private TeamService teamService;

    public TeamManagementServlet(TemplateRenderer renderer, TeamService teamService) {
        this.renderer = renderer;
        this.teamService = checkNotNull(teamService);
    }

    /**
     * Loads velocity template for the project settings page
     *
     * @param req
     * @param res
     * @throws ServletException
     * @throws IOException
     */
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        Map<String, Object> params = new HashMap<>();
        log.debug("TeamManagementServlet loaded");

        params.put("teamservice",teamService);
        params.put("baseurl", velocityRequestContextFactory.getJiraVelocityRequestContext().getCanonicalBaseUrl());

        res.setContentType("text/html;charset=utf-8");
        renderer.render("pages/team_management.vm", params, res.getWriter());
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {

    }
}
