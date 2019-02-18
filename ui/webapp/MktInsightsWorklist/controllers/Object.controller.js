/*global location*/
sap.ui.define([
	"mktinsights/base/controllers/BaseController",
	"sap/ui/model/json/JSONModel",
	"sap/ui/core/routing/History",
	"mktinsights/model/formatter",
	"mktinsights/MktInsightsWorklist/controllers/ErrorHandler",
	"sap/ui/core/Fragment",
	"mktinsights/manager/OverviewSectionManager",
	"mktinsights/manager/TargetGroupMemberManager",
	"mktinsights/manager/CampaignTeamMemberManager",
	"mktinsights/manager/ServiceManager"

], function(
	BaseController,
	JSONModel,
	History,
	formatter,
	ErrorHandler,
	Fragment,
	OverviewSectionManager,
	TargetGroupMemberManager,
	CampaignTeamMemberManager,
	ServiceManager

) {
	"use strict";

	return BaseController.extend("mktinsights.MktInsightsWorklist.controllers.Object", {

		formatter: formatter,

		/* =========================================================== */
		/* lifecycle methods                                           */
		/* =========================================================== */

		/**
		 * Called when the worklist controller is instantiated.
		 * @public
		 */
		onInit: function() {
			// set app view busy to false
			this.getOwnerComponent().getRootControl().getContent()[0].setBusy(false);
			// Model used to manipulate control states. The chosen values make sure,
			// detail page is busy indication immediately so there is no break in
			// between the busy indication for loading the view's meta data
			var iOriginalBusyDelay,
				oViewModel = new JSONModel({
					busy: true,
					delay: 0,
					campaignTeamMemberTableTitle: this.getResourceBundle().getText("campaign_team_members"),
					targetGroupMemberTableTitle: this.getResourceBundle().getText("target_group_members")

				});

			this.getRouter().getRoute("object").attachPatternMatched(this._onObjectMatched, this);
			this.getRouter().getRoute("object").attachBeforeMatched(this._onBeforeMatched, this);

			// Store original busy indicator delay, so it can be restored later on
			iOriginalBusyDelay = this.getView().getBusyIndicatorDelay();
			this.setModel(oViewModel, "objectView");
			// Restore original busy indicator delay for the object view
			oViewModel.setProperty("/delay", iOriginalBusyDelay);
		},

		/* =========================================================== */
		/* internal methods                                            */
		/* =========================================================== */

		/**
		 * Binds the view to the object path.
		 * @function
		 * @param {sap.ui.base.Event} oEvent pattern match event in route 'object'
		 * @private
		 */
		_onObjectMatched: function(oEvent) {
			//set app view busy to false
			this.getOwnerComponent().getRootControl().getContent()[0].setBusy(false);
			sap.ui.core.BusyIndicator.hide();

			var sObjectId = oEvent.getParameter("arguments").objectId;
			//Bind view based on this path
			this.sObjectId = sObjectId;
			var sObjectPath = "Campaigns('" + sObjectId + "')";
			this._bindView("/" + sObjectPath);
		},

		_onBeforeMatched: function() {
			this.destroyFragments();
		},

		/**
		 * Binds the view to the object path.
		 * @function
		 * @param {string} sObjectPath path to the object to be bound
		 * @private
		 */
		_bindView: function(sObjectPath) {
			var oViewModel = this.getModel("objectView");
			this.getView().bindElement({
				path: sObjectPath,
				events: {
					change: this._onBindingChange.bind(this),
					dataRequested: function(x) {
						oViewModel.setProperty("/busy", true);
					},
					dataReceived: function(oEvent) {
						this.campaignObject = this.getView().getBindingContext().getObject();
						var targetGroupCount = parseInt(this.campaignObject.AssignedTargetGroupMemberCount);
						var targetGroupMemberTableTitle = this.getResourceBundle().getText("target_group_members") + " (" + targetGroupCount + ")";
						this.getView().getModel("objectView").setProperty("/targetGroupMemberTableTitle", targetGroupMemberTableTitle);

						// Error Handling
						var oParams = oEvent.getParameters();
						if (oParams.error) {
							if (oParams.error.status !== "404" || (oParams.error.status === 404 && oParams.error.statusText.indexOf("Cannot POST") === 0)) {
								new ErrorHandler(this.getOwnerComponent()).showServiceError();
							}
						}
						oViewModel.setProperty("/busy", false);
					}.bind(this)
				}
			});
		},

		_onBindingChange: function() {
			var oView = this.getView(),
				oViewModel = this.getModel("objectView"),
				oElementBinding = oView.getElementBinding();

			// No data for the binding
			if (!oElementBinding.getBoundContext()) {
				this.getRouter().getTargets().display("objectNotFound");
				return;
			}
			var sSelectedSection = this.getView().byId("ObjectPageLayout").getSelectedSection();

			this.oOverviewSectionManager = new OverviewSectionManager(this);
			this.oCampaignTeamMemberManager = new CampaignTeamMemberManager(this);
			this.oTargetGroupMemberManager = new TargetGroupMemberManager(this);

			if (sSelectedSection.indexOf("overview_section") !== -1) {
				this.oOverviewSectionManager.loadOverviewSection();
			} else if (sSelectedSection.indexOf("campaignTeamMembers_section") !== -1) {
				this.oCampaignTeamMemberManager.loadCampaignMembersSection();
			} else if (sSelectedSection.indexOf("targetGroupMembers_section") !== -1) {
				this.oTargetGroupMemberManager.loadTargetGroupSection();
			}

			oViewModel.setProperty("/busy", false);

		},

		onExit: function() {
			this.destroyFragments();
		},

		/* =========================================================== */
		/* event handlers                                              */
		/* =========================================================== */

		// Navigation from the page
		onNavBack: function() {

			var oHistory = History.getInstance();
			var sPreviousHash = oHistory.getPreviousHash();

			if (sPreviousHash !== undefined) {
				history.go(-1);
			} else {
				// Otherwise we go backwards with a forward history
				var bReplace = true;
				this.getRouter().navTo("worklist", {}, bReplace);
			}

			var objectPageLayout = this.getView().byId("ObjectPageLayout");
			var objectLayoutSections = objectPageLayout.getSections();
			objectPageLayout.setSelectedSection(objectLayoutSections[0]);

			this.destroyFragments();

		},

		onSectionNavigate: function(oEvent) {
			this.destroyFragments();
			var oSelectedSection = oEvent.getParameter("section");
			if (oSelectedSection.getId().indexOf("overview_section") !== -1) {
				this.oOverviewSectionManager.loadOverviewSection();
			} else if (oSelectedSection.getId().indexOf("campaignTeamMembers_section") !== -1) {
				this.oCampaignTeamMemberManager.loadCampaignMembersSection();
			} else if (oSelectedSection.getId().indexOf("targetGroupMembers_section") !== -1) {
				this.oTargetGroupMemberManager.loadTargetGroupSection();
			}
		},

		destroyFragments: function() {
			if (this.oOverviewSectionManager.getOverviewSection() || this.oOverviewSectionManager.checkOverviewSectionFragmentsExist()) {
				if (this.oOverviewSectionManager.getOverviewSection()) {
					this.oOverviewSectionManager.getOverviewSection().destroy(true);
				}
				if (this.oOverviewSectionManager.checkOverviewSectionFragmentsExist()) {
					this.oOverviewSectionManager.destroyOverviewSectionFragments(true);
				}
			}
			if (this.oCampaignTeamMemberManager.getCampaignTeamMembersFragment()) {
				this.oCampaignTeamMemberManager.getCampaignTeamMembersFragment().destroy(true);
			}
			if (this.oTargetGroupMemberManager.getTargetGroupMembersFragment()) {
				this.oTargetGroupMemberManager.getTargetGroupMembersFragment().destroy(true);
			}
			if (this.oOverviewSectionManager.getPreviewDialog()) {
				this.oOverviewSectionManager.getPreviewDialog().destroy(true);
			}
		}

	});

});