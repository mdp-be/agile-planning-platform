package de.bearingpoint.app.skilldatabase.servlets;

import com.atlassian.jira.config.ConstantsManager;
import com.atlassian.jira.issue.CustomFieldManager;
import com.atlassian.jira.issue.fields.config.manager.IssueTypeSchemeManager;
import com.atlassian.jira.issue.issuetype.IssueType;
import com.atlassian.jira.issue.status.Status;
import com.atlassian.jira.permission.ProjectPermissions;
import com.atlassian.jira.project.Project;
import com.atlassian.jira.security.PermissionManager;
import com.atlassian.jira.security.Permissions;
import com.atlassian.jira.user.ApplicationUser;
import com.atlassian.jira.user.UserProjectHistoryManager;
import com.atlassian.jira.util.velocity.VelocityRequestContextFactory;
import com.atlassian.plugin.spring.scanner.annotation.imports.ComponentImport;
import com.atlassian.templaterenderer.TemplateRenderer;
import de.bearingpoint.app.capacityplanner.servlets.settings.project.MyTeamServlet;
import de.bearingpoint.app.capacityplanner.teamfunction.TeamService;
import de.bearingpoint.app.skilldatabase.ao.SkillDBService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import static com.atlassian.jira.component.ComponentAccessor.getJiraAuthenticationContext;

public class SkillDBServlet extends HttpServlet {
    private static final Logger log = LoggerFactory.getLogger(MyTeamServlet.class);

    @Inject
    @ComponentImport
    private UserProjectHistoryManager userProjectHistoryManager;

    @Inject
    @ComponentImport
    private PermissionManager permissionManager;

    @Inject
    @ComponentImport
    private ConstantsManager constantsManager;

    @Inject
    @ComponentImport
    private IssueTypeSchemeManager issueTypeSchemeManager;

    @ComponentImport
    private final TemplateRenderer renderer;
    @Inject
    @ComponentImport
    private VelocityRequestContextFactory velocityRequestContextFactory;
    @Inject
    private SkillDBService skillDBService;

    public SkillDBServlet(TemplateRenderer renderer, SkillDBService skillDBService) {
        this.renderer = renderer;
        this.skillDBService = skillDBService;
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Map<String, Object> params = new HashMap<>();
        ApplicationUser user = getJiraAuthenticationContext().getLoggedInUser();
        //using a deprecated class because there is no such method for getCurrentProject with ProjectPermissionKey yet
        Project currentProject = userProjectHistoryManager.getCurrentProject(Permissions.BROWSE, user);
        Collection<Project> allProjects = permissionManager.getProjects(ProjectPermissions.BROWSE_PROJECTS, user);
        Collection<Status> statuses = constantsManager.getStatusObjects();
        Collection<IssueType> issueType = constantsManager.getAllIssueTypeObjects();
        Collection<IssueType> issueTypes = issueTypeSchemeManager.getIssueTypesForDefaultScheme();

        params.put("baseurl", velocityRequestContextFactory.getJiraVelocityRequestContext().getCanonicalBaseUrl());
        params.put("userkey", user.getKey());
        params.put("currentProject", currentProject);
        params.put("allProjects", allProjects);
        params.put("statuses", statuses);
        params.put("issueType", issueType);
        params.put("userName", user.getDisplayName());
        params.put("skillSets", skillDBService.getAllUserSkillSet());

        for (IssueType type : issueTypes) {
            if (type.getName().equals("Request")) {
                params.put("request_id", type);
            }
        }
        if (!params.containsKey("request_id")) {
            params.put("request_id", "00000");
        }

        resp.setContentType("text/html;charset=utf-8");
        renderer.render("pages/skilldatabase.vm", params, resp.getWriter());
    }

}
