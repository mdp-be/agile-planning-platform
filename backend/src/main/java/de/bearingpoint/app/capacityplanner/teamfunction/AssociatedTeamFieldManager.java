package de.bearingpoint.app.capacityplanner.teamfunction;

import com.atlassian.jira.component.ComponentAccessor;
import com.atlassian.jira.issue.CustomFieldManager;
import com.atlassian.jira.issue.context.IssueContext;
import com.atlassian.jira.issue.context.IssueContextImpl;
import com.atlassian.jira.issue.customfields.manager.OptionsManager;
import com.atlassian.jira.issue.customfields.option.Option;
import com.atlassian.jira.issue.customfields.option.Options;
import com.atlassian.jira.issue.fields.CustomField;
import com.atlassian.jira.issue.fields.config.FieldConfig;
import de.bearingpoint.app.capacityplanner.init.IssueTypePluginInitializer;

import javax.inject.Named;
import java.util.ArrayList;
import java.util.List;

@Named
public class AssociatedTeamFieldManager {
    private CustomField cf = ComponentAccessor.getCustomFieldManager().getCustomFieldObjectsByName("Associated Team").iterator().next();
    private OptionsManager om = ComponentAccessor.getOptionsManager();
    private CustomFieldManager cfm = ComponentAccessor.getCustomFieldManager();
    private Options options = ComponentAccessor.getOptionsManager().getOptions(cf.getConfigurationSchemes().listIterator().next().getOneAndOnlyConfig());
    private IssueContext issueContext = new IssueContextImpl(null, IssueTypePluginInitializer.getRequestIssueTypeID());
    private FieldConfig associatedTeamFieldConfig = cf.getRelevantConfig(issueContext);

    public AssociatedTeamFieldManager() {

    }

    public void updateCustomField(ArrayList<APPTeam> teams) {
        for (APPTeam team : teams) {
            om.createOption(associatedTeamFieldConfig, null, 0L, team.getTeamName());
            options = ComponentAccessor.getOptionsManager().getOptions(cf.getConfigurationSchemes().listIterator().next().getOneAndOnlyConfig());
            om.updateOptions(options);
        }
    }

    public void removeTeamOption(String teamName) {
        List<Option> values = om.findByOptionValue(teamName);
        om.deleteOptionAndChildren(values.get(0));
        options = ComponentAccessor.getOptionsManager().getOptions(cf.getConfigurationSchemes().listIterator().next().getOneAndOnlyConfig());
        om.updateOptions(options);
    }

    public void addTeamOption(String teamName) {
        om.createOption(associatedTeamFieldConfig, null, 0L, teamName);
        options = ComponentAccessor.getOptionsManager().getOptions(cf.getConfigurationSchemes().listIterator().next().getOneAndOnlyConfig());
        om.updateOptions(options);
    }

}
