/*eslint-disable */
sap.ui.define([
	"mktinsights/base/controllers/BaseController",
	"sap/ui/model/json/JSONModel",
	"sap/ui/core/routing/History",
	"mktinsights/model/formatter",
	"sap/ui/model/Filter",
	"sap/ui/model/FilterOperator",
	"mktinsights/MktInsightsWorklist/controllers/ErrorHandler",
	"mktinsights/manager/ServiceManager",
	"mktinsights/util/Const",
	"sap/ui/export/Spreadsheet",
	"mktinsights/shared/manager/Dialog",
], function(BaseController, JSONModel, History, formatter, Filter, FilterOperator, ErrorHandler, ServiceManager, Const, Spreadsheet,
	Dialog) {
	"use strict";
	return BaseController.extend("mktinsights.MktInsightsWorklist.controllers.Worklist", {
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
			var oViewModel;

			var iOriginalBusyDelay,

				// Model used to manipulate control states
				oViewModel = new JSONModel({
					worklistTitle: this.getResourceBundle().getText("worklistTitle"),
					worklistStatusInPrepTitle: this.getResourceBundle().getText("worklistStatusInPrepTitle"),
					worklistStatusCompletedTitle: this.getResourceBundle().getText("worklistStatusCompletedTitle"),
					worklistStatusReleasedTitle: this.getResourceBundle().getText("worklistStatusReleasedTitle"),
					worklistStatusAllTitle: this.getResourceBundle().getText("worklistStatusAllTitle"),
					tableBusyDelay: 0,
					displayBlockView: true,
					displayGroupItemView: false,
					selectedCampaignTypeKey: "0"

				});

			this.setModel(oViewModel, "worklistView");

			this.getRouter().getRoute("worklist").attachPatternMatched(this._onObjectMatched, this);

		},

		_onObjectMatched: function() {
			//	set app view busy to false
			this.getOwnerComponent().getRootControl().getContent()[0].setBusy(false);
			sap.ui.core.BusyIndicator.hide();
		},

		onAfterRendering: function() {
			var selectedCampaignTypeKey = this.getView().getModel("worklistView").getProperty("/selectedCampaignTypeKey");
			this.setLoginUser();
			this.loadAppropriateData(selectedCampaignTypeKey);

		},

		// Event called when binding data is requested
		onBindingDataRequested: function() {
			this.getView().setBusy(true);
		},

		onBindingDataReceived: function(oEvent) {
			if (!this._oServiceManager) {
				this._oServiceManager = new ServiceManager(this);
			}

			var getInprepCountPromise = new Promise(function(resolve) {
				this._oServiceManager.read(null, "CampaignsByTeamMember/$count?$filter=CampaignLifecycleStatus eq '" + Const.CampaignLifecycleStatusCode
					.InPreparation + "' and CampaignCategory eq '" + Const.CampaignCategory.AutomatedCampaign + "'").then(
					function(count) {
						resolve(count);
					}.bind(this)
				);
			}.bind(this));

			var getReleasedCountPromise = new Promise(function(resolve) {
				this._oServiceManager.read(null, "CampaignsByTeamMember/$count?$filter=CampaignLifecycleStatus eq '" + Const.CampaignLifecycleStatusCode
					.Released + "' and CampaignCategory eq '" + Const.CampaignCategory.AutomatedCampaign + "'").then(
					function(count) {
						resolve(count);
					}.bind(this)
				);
			}.bind(this));

			var getCompletedCountPromise = new Promise(function(resolve) {
				this._oServiceManager.read(null, "CampaignsByTeamMember/$count?$filter=CampaignLifecycleStatus eq '" + Const.CampaignLifecycleStatusCode
					.Completed + "' and CampaignCategory eq '" + Const.CampaignCategory.AutomatedCampaign + "'").then(
					function(count) {
						resolve(count);
					}.bind(this)
				);
			}.bind(this));

			Promise.all([getInprepCountPromise, getReleasedCountPromise, getCompletedCountPromise]).then(function(
				results) {
				var allCampaignCount = Number(results[0]) + Number(results[1]) + Number(results[2]);
				var inPrepCount = results[0];
				var releasedCount = results[1];
				var completedCount = results[2];

				this.getModel("worklistView").setProperty("/AllCampaignsCount", allCampaignCount);
				this.getModel("worklistView").setProperty("/InPreparationCampaignsCount", inPrepCount);
				this.getModel("worklistView").setProperty("/ReleasedCampaignsCount", releasedCount);
				this.getModel("worklistView").setProperty("/CompletedCampaignsCount", completedCount);

				this.updateTitle(allCampaignCount, null);
				this.updateTitle(inPrepCount, Const.CampaignLifecycleStatusCode.InPreparation);
				this.updateTitle(releasedCount, Const.CampaignLifecycleStatusCode.Released);
				this.updateTitle(completedCount, Const.CampaignLifecycleStatusCode.Completed);

				this.getView().setBusy(false);

			}.bind(this), function(err) {
				//	this.handleErrorInBindingData(err);
				this.getView().setBusy(false);
			}.bind(this));

		},

		setLoginUser: function() {
			this.getView().setBusy(true);
			var oLoginUserModel = new sap.ui.model.json.JSONModel();

			if (!this._oServiceManager) {
				this._oServiceManager = new ServiceManager(this);
			}
			this._oServiceManager.read(null, "LoggedInUserDetails").then(
				function(oData) {
					var data;

					if (typeof oData === "string") {
						data = JSON.parse(oData);
					} else {
						data = oData;
					}
					oLoginUserModel.setData(data.value);
					this.getView().byId("headerBar").setModel(oLoginUserModel, "loginUserModel");
					this.getView().setBusy(false);
				}.bind(this),
				function() {
					this.getView().setBusy(false);
				}.bind(this)
			);
		},

		updateTitle: function(count, sCampaignLifecycleStatusCode) {
			var sTitleRow;
			if (sCampaignLifecycleStatusCode === Const.CampaignLifecycleStatusCode.InPreparation) {
				sTitleRow = this.getResourceBundle().getText("worklistStatusInPrepTitleCount", [count]);
				this.getModel("worklistView").setProperty("/worklistStatusInPrepTitle", sTitleRow);
			}
			if (sCampaignLifecycleStatusCode === Const.CampaignLifecycleStatusCode.Released) {
				sTitleRow = this.getResourceBundle().getText("worklistStatusReleasedTitleCount", [count]);
				this.getModel("worklistView").setProperty("/worklistStatusReleasedTitle", sTitleRow);
			}
			if (sCampaignLifecycleStatusCode === Const.CampaignLifecycleStatusCode.Completed) {
				sTitleRow = this.getResourceBundle().getText("worklistStatusCompletedTitleCount", [count]);
				this.getModel("worklistView").setProperty("/worklistStatusCompletedTitle", sTitleRow);
			}
			if (sCampaignLifecycleStatusCode === null) {
				sTitleRow = this.getResourceBundle().getText("worklistStatusAllTitleCount", [count]);
				this.getModel("worklistView").setProperty("/worklistStatusAllTitle", sTitleRow);
			}

		},

		handleErrorInBindingData: function(oParams) {
			// Error handling
			if (oParams.error) {
				if (oParams.error.status !== "404" || (oParams.error.status === 404 && oParams.error.statusText.indexOf("Cannot POST") === 0)) {
					new ErrorHandler(this.getOwnerComponent()).showServiceError();
				}
			}
		},

		/* =========================================================== */
		/* event handlers                                              */
		/* =========================================================== */

		/**
		 * Event handler when a block item title/table item is clicked
		 * @param {sap.ui.base.Event}
		 * @public
		 */
		onTitleClicked: function(oEvent) {
			// The source is the item that got pressed. 
			this._showObject(oEvent.getSource());

		},

		// Search event on the Page
		onSearch: function(oEvent) {
			//	this.getView().byId("segBtn").setSelectedKey("groupItemView");
			if (oEvent.getParameters().refreshButtonPressed) {
				// Search field's 'refresh' button has been pressed.
				// This is visible if you select any master list item.
				// In this case no new search is triggered, we only
				// refresh the list binding.
				this.onRefresh();
			} else {
				var sQuery = oEvent.getParameter("query");
				var selectedKey;
				if (this.selectedCampaignTypeKey !== undefined) {
					this.getModel("worklistView").setProperty("/selectedCampaignTypeKey", this.selectedCampaignTypeKey);
					selectedKey = this.getModel("worklistView").getProperty("/selectedCampaignTypeKey");
				} else {
					selectedKey = this.getModel("worklistView").getProperty("/selectedCampaignTypeKey");
				}
				this.getCampaignFilters(selectedKey, sQuery);
			}
		},

		getCampaignFilters: function(selectedKey, sQuery) {
			var aTableSearchState = [];
			if (selectedKey === Const.CampaignLifecycleStatusCode.All) {
				if (sQuery && sQuery.length > 0) {
					aTableSearchState = [new Filter("CampaignID", FilterOperator.EQ, sQuery), new Filter("CampaignCategory", FilterOperator.EQ, Const
						.CampaignCategory.AutomatedCampaign)];
					this._applySearch(aTableSearchState);
				} else {
					aTableSearchState = [new Filter("CampaignCategory", FilterOperator.EQ, Const
						.CampaignCategory.AutomatedCampaign)];
					this._applySearch(aTableSearchState);
				}
			} else if (selectedKey === Const.CampaignLifecycleStatusCode.InPreparation) {
				if (sQuery && sQuery.length > 0) {
					aTableSearchState = [new Filter("CampaignID", FilterOperator.EQ, sQuery), new Filter("CampaignCategory", FilterOperator.EQ, Const
						.CampaignCategory.AutomatedCampaign), new Filter("CampaignLifecycleStatus", FilterOperator.EQ, Const.CampaignLifecycleStatusCode
						.InPreparation)];
					this._applySearch(aTableSearchState);
				} else {
					aTableSearchState = [new Filter("CampaignCategory", FilterOperator.EQ, Const
						.CampaignCategory.AutomatedCampaign), new Filter("CampaignLifecycleStatus", FilterOperator.EQ, Const.CampaignLifecycleStatusCode
						.InPreparation)];
					this._applySearch(aTableSearchState);
				}
			} else if (selectedKey === Const.CampaignLifecycleStatusCode.Released) {
				if (sQuery && sQuery.length > 0) {
					aTableSearchState = [new Filter("CampaignID", FilterOperator.EQ, sQuery), new Filter("CampaignCategory", FilterOperator.EQ, Const
						.CampaignCategory.AutomatedCampaign), new Filter("CampaignLifecycleStatus", FilterOperator.EQ, Const.CampaignLifecycleStatusCode
						.Released)];
					this._applySearch(aTableSearchState);
				} else {
					aTableSearchState = [new Filter("CampaignCategory", FilterOperator.EQ, Const
						.CampaignCategory.AutomatedCampaign), new Filter("CampaignLifecycleStatus", FilterOperator.EQ, Const.CampaignLifecycleStatusCode
						.Released)];
					this._applySearch(aTableSearchState);
				}
			} else if (selectedKey === Const.CampaignLifecycleStatusCode.Completed) {
				if (sQuery && sQuery.length > 0) {
					aTableSearchState = [new Filter("CampaignID", FilterOperator.EQ, sQuery), new Filter("CampaignCategory", FilterOperator.EQ, Const
						.CampaignCategory.AutomatedCampaign), new Filter("CampaignLifecycleStatus", FilterOperator.EQ, Const.CampaignLifecycleStatusCode
						.Completed)];
					this._applySearch(aTableSearchState);
				} else {

					aTableSearchState = [new Filter("CampaignCategory", FilterOperator.EQ, Const
						.CampaignCategory.AutomatedCampaign), new Filter("CampaignLifecycleStatus", FilterOperator.EQ, Const.CampaignLifecycleStatusCode
						.Completed)];
					this._applySearch(aTableSearchState);
				}
			}

		},

		/**
		 * Event handler for refresh event. Keeps filter, sort
		 * and group settings and refreshes the list binding grouped by status/date
		 * @public
		 */
		onRefresh: function() {
			// update grid binding
			var grid = sap.ui.core.Fragment.byId("gridFragment", "campaignGrids");
			var binding = grid.getBinding("content");
			binding.filter(new Filter("CampaignCategory", FilterOperator.EQ, Const.CampaignCategory.AutomatedCampaign));

			// update table binding
			var oTable = sap.ui.core.Fragment.byId("tableFragment", "table_campaigns");
			binding = oTable.getBinding("items");
			binding.filter(new Filter("CampaignCategory", FilterOperator.EQ, Const.CampaignCategory.AutomatedCampaign));
		},

		/* =========================================================== */
		/* internal methods                                            */
		/* =========================================================== */

		/**
		 * Shows the selected item on the object page
		 * On phones a additional history entry is created
		 * @param {sap.m.ObjectListItem} oItem selected Item
		 * @private
		 */
		_showObject: function(oItem) {
			//set app view busy to true
			this.getOwnerComponent().getRootControl().getContent()[0].setBusy(true);
			sap.ui.core.BusyIndicator.show(1);

			this.getRouter().navTo("object", {
				objectId: oItem.getBindingContext().getProperty("CampaignUUID")

			});
		},

		/**
		 * Internal helper method to apply both filter and search state together on the list binding
		 * @param {sap.ui.model.Filter[]} aTableSearchState An array of filters for the search
		 * @private
		 */
		_applySearch: function(aTableSearchState) {
			// update grid binding
			var grid = sap.ui.core.Fragment.byId("gridFragment", "campaignGrids");
			var binding = grid.getBinding("content");
			binding.filter(aTableSearchState, "Application");

			// update table binding
			var oTable = sap.ui.core.Fragment.byId("tableFragment", "table_campaigns");
			binding = oTable.getBinding("items");
			binding.filter(aTableSearchState, "Application");
		},

		// Event on change of worklist view - Block View/ Table View
		onViewChange: function(oEvent) {
			if (oEvent.getSource().getKey() === "listView") {
				oEvent.getSource().setKey("listView");
				this.getModel("worklistView").setProperty("/displayGroupItemView", true);
				this.getModel("worklistView").setProperty("/displayBlockView", false);
			} else if (oEvent.getSource().getKey() === "gridView") {
				oEvent.getSource().setKey("gridView");
				this.getModel("worklistView").setProperty("/displayGroupItemView", false);
				this.getModel("worklistView").setProperty("/displayBlockView", true);
			}
		},

		destroyFragments: function() {
			if (this.oCampaignGridFragment) {
				this.oCampaignGridFragment.destroy();
			}

			if (this.oCampaignTableFragment) {
				this.oCampaignTableFragment.destroy();
			}

			if (this.oCampaignToolBarFragment) {
				this.oCampaignToolBarFragment.destroy();
			}
		},

		loadAppropriateData: function(sSelectedSegmentKey) {

			this.destroyFragments();

			this.oCampaignGridFragment = sap.ui.xmlfragment("gridFragment", "mktinsights.fragment.CampaignsGridView", this);
			this.oCampaignTableFragment = sap.ui.xmlfragment("tableFragment", "mktinsights.fragment.CampaignsTableView", this);
			this.oCampaignToolBarFragment = sap.ui.xmlfragment("toolBarFragment", "mktinsights.fragment.CampaignToolBar", this);

			this.getView().byId("campaignTypeIconBar").insertContent(this.oCampaignGridFragment);
			this.getView().byId("campaignTypeIconBar").insertContent(this.oCampaignTableFragment);
			this.getView().byId("campaignTypeIconBar").insertContent(this.oCampaignToolBarFragment);

			var oSegBtn = sap.ui.core.Fragment.byId("toolBarFragment", "segBtn");
			if (this.getModel("worklistView").getProperty("/displayGroupItemView") === true) {
				oSegBtn.setSelectedKey("listView");
			} else {
				oSegBtn.setSelectedKey("gridView");
			}

			this.getCampaignFilters(sSelectedSegmentKey);

		},

		handleSegmentSelectionPress: function(oEvent) {
			this.selectedCampaignTypeKey = oEvent.getSource().getSelectedKey();
			this.getModel("worklistView").setProperty("/selectedCampaignTypeKey", this.selectedCampaignTypeKey);
			this.loadAppropriateData(this.selectedCampaignTypeKey);

			/*	var c= this.getModel("worklistView").getProperty("/completedCount");
				
				if(this.selectedCampaignTypeKey === 3 && c === 0){
					this.getView().byId("nodata").setVisible(true);
				}*/

		},

		onExport: function() {
			var dialogObject = {
				body: this.getResourceBundle().getText("Export_To_Excel_Msg")
			};
			this.dialogInstance = new Dialog(this, dialogObject);
			this.dialogInstance.createDialog();
		},

		createColumnConfig: function() {
			var columns = [{
				label: this.getResourceBundle().getText("name"),
				property: 'CampaignName'
			}, {
				label: this.getResourceBundle().getText("id"),
				property: 'CampaignID'
			}, {
				label: this.getResourceBundle().getText("category"),
				property: 'CampaignCategoryName'
			}, {
				label: this.getResourceBundle().getText("marketingArea"),
				property: 'MarketingAreaName'
			}, {
				label: this.getResourceBundle().getText("owner"),
				property: 'CampaignOwnerName'

			}, {
				label: this.getResourceBundle().getText("startDate"),
				property: 'CampaignStartDate'
			}, {
				label: this.getResourceBundle().getText("endDate"),
				property: 'CampaignEndDate'
			}, {
				label: this.getResourceBundle().getText("status"),
				property: 'CampaignLifecycleStatusName'
			}];

			return columns;
		},

		exportAllCampaigns: function() {
			var aCols = this.createColumnConfig();
			var totalCampaignCount = this.getView().getModel("worklistView").getProperty("/AllCampaignsCount");
			if (!this._oServiceManager) {
				this._oServiceManager = new ServiceManager(this);
			}

			this._oServiceManager.read(null, "CampaignsByTeamMember?$top=" + totalCampaignCount + "&$filter=CampaignCategory eq '" + Const.CampaignCategory
				.AutomatedCampaign +
				"'").then(
				function(response) {
					var aRowsToExport = response.value;
					var oSettings = {
						workbook: {
							columns: aCols
						},
						dataSource: aRowsToExport
					};

					jQuery.sap.require("sap.ui.export.Spreadsheet");
					new sap.ui.export.Spreadsheet(oSettings)
						.build()
						.then(function() {
							sap.m.MessageToast.show(this.getResourceBundle().getText("Export_Success_Msg"));
						}.bind(this));
					this.getView().setBusy(false);
				}.bind(this),
				function() {
					sap.m.MessageToast.show(this.getResourceBundle().getText("Export_Fail_Msg"));
					this.getView().setBusy(false);
				}.bind(this)
			);

		},

		exportInPreparationCampaigns: function() {
			var aCols = this.createColumnConfig();
			var inPreparationCampaignCount = this.getView().getModel("worklistView").getProperty("/InPreparationCampaignsCount");
			if (!this._oServiceManager) {
				this._oServiceManager = new ServiceManager(this);
			}

			this._oServiceManager.read(null, "CampaignsByTeamMember?$top=" + inPreparationCampaignCount + "&" +
				"$filter=CampaignLifecycleStatus eq '" + Const.CampaignLifecycleStatusCode
				.InPreparation + "' and CampaignCategory eq '" + Const.CampaignCategory.AutomatedCampaign + "'").then(
				function(response) {
					var aRowsToExport = response.value;
					var oSettings = {
						workbook: {
							columns: aCols
						},
						dataSource: aRowsToExport
					};

					jQuery.sap.require("sap.ui.export.Spreadsheet");
					new sap.ui.export.Spreadsheet(oSettings)
						.build()
						.then(function() {
							sap.m.MessageToast.show(this.getResourceBundle().getText("Export_Success_Msg"));
						}.bind(this));
					this.getView().setBusy(false);
				}.bind(this),
				function() {
					sap.m.MessageToast.show(this.getResourceBundle().getText("Export_Fail_Msg"));
					this.getView().setBusy(false);
				}.bind(this)
			);

		},

		exportReleasedCampaigns: function() {
			var aCols = this.createColumnConfig();
			var releasedCampaignCount = this.getView().getModel("worklistView").getProperty("/ReleasedCampaignsCount");
			if (!this._oServiceManager) {
				this._oServiceManager = new ServiceManager(this);
			}

			this._oServiceManager.read(null, "CampaignsByTeamMember?$top=" + releasedCampaignCount + "&" +
				"$filter=CampaignLifecycleStatus eq '" + Const.CampaignLifecycleStatusCode
				.Released + "' and CampaignCategory eq '" + Const.CampaignCategory.AutomatedCampaign + "'").then(
				function(response) {
					var aRowsToExport = response.value;
					var oSettings = {
						workbook: {
							columns: aCols
						},
						dataSource: aRowsToExport
					};

					jQuery.sap.require("sap.ui.export.Spreadsheet");
					new sap.ui.export.Spreadsheet(oSettings)
						.build()
						.then(function() {
							sap.m.MessageToast.show(this.getResourceBundle().getText("Export_Success_Msg"));
						}.bind(this));
					this.getView().setBusy(false);
				}.bind(this),
				function() {
					sap.m.MessageToast.show(this.getResourceBundle().getText("Export_Fail_Msg"));
					this.getView().setBusy(false);
				}.bind(this)
			);
		},

		exportCompletedCampaigns: function() {
			var aCols = this.createColumnConfig();
			var completedCampaignCount = this.getView().getModel("worklistView").getProperty("/CompletedCampaignsCount");
			if (!this._oServiceManager) {
				this._oServiceManager = new ServiceManager(this);
			}

			this._oServiceManager.read(null, "CampaignsByTeamMember?$top=" + completedCampaignCount + "&" +
				"$filter=CampaignLifecycleStatus eq '" + Const.CampaignLifecycleStatusCode
				.Completed + "' and CampaignCategory eq '" + Const.CampaignCategory.AutomatedCampaign + "'").then(
				function(response) {
					var aRowsToExport = response.value;
					var oSettings = {
						workbook: {
							columns: aCols
						},
						dataSource: aRowsToExport
					};

					jQuery.sap.require("sap.ui.export.Spreadsheet");
					new sap.ui.export.Spreadsheet(oSettings)
						.build()
						.then(function() {
							sap.m.MessageToast.show(this.getResourceBundle().getText("Export_Success_Msg"));
						}.bind(this));
					this.getView().setBusy(false);
				}.bind(this),
				function() {
					sap.m.MessageToast.show(this.getResourceBundle().getText("Export_Fail_Msg"));
					this.getView().setBusy(false);
				}.bind(this)
			);
		},

		dialogOKPress: function() {
			var sSelectedKey = this.getModel("worklistView").getProperty("/selectedCampaignTypeKey");

			if (sSelectedKey === Const.CampaignLifecycleStatusCode.All) {
				this.exportAllCampaigns();
			} else if (sSelectedKey === Const.CampaignLifecycleStatusCode.InPreparation) {
				this.exportInPreparationCampaigns();
			} else if (sSelectedKey === Const.CampaignLifecycleStatusCode.Released) {
				this.exportReleasedCampaigns();
			} else if (sSelectedKey === Const.CampaignLifecycleStatusCode.Completed) {
				this.exportCompletedCampaigns();
			}

			this.dialogInstance.handleCancelPress();
		}

	});

});