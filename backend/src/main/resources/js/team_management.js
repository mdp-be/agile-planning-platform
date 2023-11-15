// All JS that is used for the team management

// AUI Select2
AJS.$("#user-picker-teamlead").auiSelect2({
    hasAvatar: true, // auiSelect2 speciffic option, adds styling needed to properly display avatars
    multiple: false, // make the control a multi-select
    ajax: {
        url: base_url + "/rest/api/2/user/picker", // JIRA URL to the REST end-point
        type: "GET",
        dataType: 'json',
        cache: true,
        // query parameters for the remote ajax call
        data: function data(term) {
            return {
                query: term,
                maxResults: 1000,
                showAvatar: true
            };
        },
        // parse data from the server into form select2 expects
        results: function results(data) {
            let res =
                console.log(data)
            return {
                results: data.users
            };
        },
    },
    // specify id parameter of each user entity
    id: function id(user) {
        return user.key;
    },
    // define how selected element should look like
    formatSelection: function formatSelection(user) {
        var avatarHtml = aui.avatar.avatar({
            size: 'small',
            avatarImageUrl: user.avatarUrl
        });
        return avatarHtml + Select2.util.escapeMarkup(user.displayName);
    },
    // define how single option should look like
    formatResult: function formatResult(user, container, query, escapeMarkup) {
        // format result string
        var resultText = user.displayName + " - (" + user.name + ")";
        var avatarHtml = aui.avatar.avatar({
            size: 'small',
            avatarImageUrl: user.avatarUrl
        });
        var higlightedMatch = [];
        // we need this to disable html escaping by select2 as we are doing it on our own
        var noopEscapeMarkup = function noopEscapeMarkup(s) {
            return s;
        }
        // highlight matches of the query term using matcher provided by the select2 library
        Select2.util.markMatch(escapeMarkup(resultText), escapeMarkup(query.term), higlightedMatch, noopEscapeMarkup);
        // convert array to string
        higlightedMatch = higlightedMatch.join("");
        return avatarHtml + higlightedMatch;
    },
    // define message showed when there are no matches
    formatNoMatches: function formatNoMatches(query) {
        return "No users found who matched your input";
    }
});

// Functions for the Dialogs
function opendialog(dialog) {
    AJS.dialog2("#" + dialog).show();
}

// Shows the create team dialog
AJS.$("#create-team").click(function (e) {
    e.preventDefault();
    AJS.dialog2("#create-team-dialog").show();
});

// Hides the create team dialog
AJS.$("#dialog-submit-button").click(function (e) {
    e.preventDefault();
    AJS.dialog2("#create-team-dialog").hide();
});

AJS.$("#searchbutton").click(function (e) {
    JIRA.Forms.createEditIssueForm(
        {issueId: '10010'}).asDialog({
        id: "my-custom-edit-dialog",
    }).show();
})