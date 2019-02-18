sap.ui.define(["sap/ui/base/Object",
	"sap/ui/model/json/JSONModel",
	"sap/ui/core/Fragment",
	"mktinsights/MktInsightsWorklist/controllers/ErrorHandler",
	"mktinsights/manager/ServiceManager",
	"mktinsights/model/formatter"
], function(Object, JSONModel, Fragment, ErrorHandler, ServiceManager, formatter) {

	return Object.extend("mktinsights.manager.TargetGroupMemberManager", {

		formatter: formatter,

		constructor: function(oController) {
			this._delegate = oController;
			this._oServiceManager = new ServiceManager(this._delegate);
		},

		loadTargetGroupSection: function() {
			//Get Target Group section layout
			var oTargetGroupVerticalLayout = this._delegate.getView().byId("targetGroupMembers_verticalLayout");

			// Initialise TG fragments
			this.oTargetGroupFragment = sap.ui.xmlfragment("targetGroup_fragment", "mktinsights.fragment.TargetGroupMembers", this);
			oTargetGroupVerticalLayout.addContent(this.oTargetGroupFragment);
			var sBindingPath = this._delegate.getView().getBindingContext().sPath.split('/')[1];
			var sBindingPathForContacts = "/" + sBindingPath + "/Contacts";
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
					}),
					new sap.m.Text({
						text: "{SalesRepresentativeFullName}"
					})
				]
			});
			Fragment.byId("targetGroup_fragment", "tableTargetGroupMembers").bindItems({
				path: sBindingPathForContacts,
				template: oTemplate
			});
			Fragment.byId("targetGroup_fragment", "tableTargetGroupMembers").setBusy(false);
		},

		onPressOfEmailAddress: function(oEvent) {
			sap.m.URLHelper.triggerEmail(oEvent.getSource().getText());
		},

		getTargetGroupMembersFragment: function() {
			return this.oTargetGroupFragment;
		}
	});
});