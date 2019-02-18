sap.ui.define(["sap/ui/base/Object",
	"sap/ui/model/json/JSONModel",
	"sap/ui/core/Fragment",
	"mktinsights/MktInsightsWorklist/controllers/ErrorHandler",
	"mktinsights/manager/ServiceManager",
	"mktinsights/model/formatter"
], function(Object, JSONModel, Fragment, ErrorHandler, ServiceManager, formatter) {

	return Object.extend("mktinsights.manager.CampaignTeamMemberManager", {

		formatter: formatter,

		constructor: function(oController) {
			this._delegate = oController;
			this._oServiceManager = new ServiceManager(this._delegate);
		},

		loadCampaignMembersSection: function() {
			this.readCampaignTeamMemberCount();
			//Get Campaign Team Members section layout
			var oCampaignTeamMembersVerticalLayout = this._delegate.getView().byId("campaignTeamMembers_verticalLayout");

			// Initialise Campaign Team Members fragments
			this.oCampaignTeamMembersFragment = sap.ui.xmlfragment("campaignTeamMembers_fragment", "mktinsights.fragment.CampaignTeamMembers",
				this);

			// Add the fragments to Campaign Team Members Layout
			oCampaignTeamMembersVerticalLayout.addContent(this.oCampaignTeamMembersFragment);

			var sBindingPath = this._delegate.getView().getBindingContext().sPath.split('/')[1];

			var sBindingPathForTeamMembers = "/" + sBindingPath + "/CampaignAssignedTeamMembers";
			var self = this;
			var oTemplate = new sap.m.ColumnListItem({
				cells: [
					new sap.m.Text({
						text: "{FullName}"
					}),
					new sap.m.Link({
						text: "{EmailAddress}",
						press: function(oEvent) {
							self.onPressOfEmailAddress(oEvent);
						}
					}),
					new sap.m.Text({
						text: "{PhoneNumber}"
					})
				]
			});
			Fragment.byId("campaignTeamMembers_fragment", "tableCampaignTeamMembers").bindItems({
				path: sBindingPathForTeamMembers,
				template: oTemplate
			});
			Fragment.byId("campaignTeamMembers_fragment", "tableCampaignTeamMembers").setBusy(false);
		},

		readCampaignTeamMemberCount: function() {
			var sBindingPath = this._delegate.getView().getBindingContext().sPath.split('/')[1];
			this._oServiceManager.read(sBindingPath, "CampaignAssignedTeamMembers" + "?$count=true").then(function(odata) {
				var campaignTeamMembersCount = odata["@odata.count"];
				var campaignTeamMemberTableTitle = this._delegate.getResourceBundle().getText("campaign_team_members") + " (" +
					campaignTeamMembersCount + ")";
				this._delegate.getView().getModel("objectView").setProperty("/campaignTeamMemberTableTitle", campaignTeamMemberTableTitle);
			}.bind(this));
		},

		onPressOfEmailAddress: function(oEvent) {
			sap.m.URLHelper.triggerEmail(oEvent.getSource().getText());
		},

		getCampaignTeamMembersFragment: function() {
			return this.oCampaignTeamMembersFragment;
		}
	});
});