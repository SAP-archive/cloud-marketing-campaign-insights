/*eslint-disable*/
sap.ui.define(["sap/ui/base/Object",
	"sap/ui/model/json/JSONModel",
	"sap/ui/core/Fragment",
	"mktinsights/MktInsightsWorklist/controllers/ErrorHandler",
	"mktinsights/manager/ServiceManager",
	"mktinsights/model/formatter"

], function(Object, JSONModel, Fragment, ErrorHandler, ServiceManager, formatter) {

	return Object.extend("mktinsights.manager.OverviewSectionManager", {

		formatter: formatter,

		constructor: function(oController) {
			this._delegate = oController;
			this._oServiceManager = new ServiceManager(this._delegate);

		},

		loadOverviewSection: function() {
			//Add Overview fragment to Overview subsection
			var oOverviewSection = this._delegate.getView().byId("overview_panel");

			this.oOverviewFragment = sap.ui.xmlfragment("overview_fragment", "mktinsights.fragment.Overview", this);
			oOverviewSection.addContent(this.oOverviewFragment);

			//Add Campaign Content BlockCell
			this.oContentDashboardFragment = sap.ui.xmlfragment("contentDashboard_fragment", "mktinsights.fragment.ContentDashboard", this);
			Fragment.byId("overview_fragment", "emailContentDashboard").addContent(this.oContentDashboardFragment);

			//Set chart view as selected key
			Fragment.byId("overview_fragment", "segBtn").setSelectedKey("chartView");

			//Read Campaign Content
			this.readCampaignContent();

			//Read Campaign Metrics
			this.readCampaignMetrics();

		},

		loadKPIFragments: function(oSuccessMetrics) {

			this.oKPIModel = new sap.ui.model.json.JSONModel();
			this.oKPIModel.setData(oSuccessMetrics);

			//Initialise chart view kpi fragment
			this.oKPIChartViewFragment = sap.ui.xmlfragment("kpiChartView_fragment", "mktinsights.fragment.KPIChartView", this);
			//Initialise Tile view kpi fragment
			this.oKPITileViewFragment = sap.ui.xmlfragment("kpiTileView_fragment", "mktinsights.fragment.KPITileView", this);

			//set KPIModel on chart view fragment
			this.oKPIChartViewFragment.setModel(this.oKPIModel, "KPIModel");
			//set KPIModel on tile view  fragment
			this.oKPITileViewFragment.setModel(this.oKPIModel, "KPIModel");

			//add chart view to scroll container
			Fragment.byId("overview_fragment", "scrollCont_KPIDashboard").addContent(this.oKPIChartViewFragment);
		},

		readCampaignContent: function() {
			Fragment.byId("contentDashboard_fragment", "emailCarousel").setBusy(true);
			var sBindingPath = this._delegate.getView().getBindingContext().sPath.split('/')[1];
			return new Promise(function(resolve, reject) {
				this._oServiceManager.read(sBindingPath, "CampaignContents?$filter=LanguageCode eq '" + sap.ui.getCore().getConfiguration().getSAPLogonLanguage() +
					"'").then(function(odata) {
					resolve(odata);
					if (!jQuery.isEmptyObject(odata.value)) {
						this.oContentDashboardFragment.setVisible(true);
						this.oContentDashboardFragment.setModel(new sap.ui.model.json.JSONModel(odata), "contentModel");
					} else {
						this.loadNoDataFragment();
						Fragment.byId("contentDashboard_fragment", "emailCarousel").addPage(this.getNoDataFragment().addStyleClass(
							"sapUiSmallMargin"));
					}
					if (Fragment.byId("contentDashboard_fragment", "emailCarousel")) {
						Fragment.byId("contentDashboard_fragment", "emailCarousel").setBusy(false);
					}

				}.bind(this));
			}.bind(this));
		},

		onPreviewLoaderReady: function(oEvent) {
			var mParameters = oEvent.getParameters();
			var oPreviewLoader = mParameters.oPreviewLoader;
			var oModel = oPreviewLoader.getModel("contentModel");
			var oContext = oPreviewLoader.getBindingContext("contentModel");
			var fnCallback = mParameters.fnCallback;
			this.setDataInPreview(oModel.getProperty("MessageContentHTMLString", oContext), fnCallback);
		},

		setDataInPreview: function(sEmailBody, fnCallback) {
			var iSubjectInsertingPosition = sEmailBody.search(/<body[^>]*>/gi);
			// get whole body tag  (can be of variable length (for example styleclasses))
			var aCompleteBodyTag = sEmailBody.match(/<body[^>]*>/gi);
			// only insert if body tag found
			if (aCompleteBodyTag && aCompleteBodyTag.length > 0) {
				var sCompleteBodyTag = aCompleteBodyTag[0];
				// insert AFTER whole body tag
				iSubjectInsertingPosition += sCompleteBodyTag.length;
				sEmailBody = sEmailBody.slice(0, iSubjectInsertingPosition) + sEmailBody.slice(
					iSubjectInsertingPosition);
			}
			fnCallback(sEmailBody);
		},

		handleEmailContentPress: function(oEvent) {
			if (!this.oPreviewDialog) {
				this.oPreviewDialog = sap.ui.xmlfragment("content_emailTilePreview", "mktinsights.fragment.EmailContentPreview", this);
				this._delegate.getView().addDependent(this.oPreviewDialog);
			}
			var sPath = oEvent.getSource().getBindingContext("contentModel").sPath;
			var sPreviewData = oEvent.getSource().getModel("contentModel").getProperty(sPath);

			this.oPreviewDialog.setModel(new JSONModel(sPreviewData), "emaildetailsModel");
			this.oPreviewDialog.open();

		},

		onBigPreviewReady: function(oEvent) {
			var mParameters = oEvent.getParameters();
			var oPreviewLoader = mParameters.oPreviewLoader;
			var fnCallback = mParameters.fnCallback;
			var oModel = oPreviewLoader.getModel("emaildetailsModel");
			this.setDataInPreview(oModel.getData().MessageContentHTMLString, fnCallback);
		},

		onPreviewClose: function(oEvent) {
			this.oPreviewDialog.close();
		},

		attachOverviewSectionEvents: function() {
			this._delegate.getView().byId("chartView_button").attachPress(this.onPressOfChartView, this);
			this._delegate.getView().byId("tileView_button").attachPress(this.onPressOfTileView, this);
		},

		readCampaignMetrics: function() {
			Fragment.byId("overview_fragment", "scrollCont_KPIDashboard").setBusy(true);
			var sBindingPath = this._delegate.getView().getBindingContext().sPath.split('/')[1];
			this._oServiceManager.read(sBindingPath, "Metrics").then(function(odata) {
				if (!jQuery.isEmptyObject(odata.value[0])) { //success metrics present
					this.loadKPIFragments(odata.value[0]);
				} else { //show message strip when no success metrics present 
					Fragment.byId("overview_fragment", "segBtn").setVisible(false);
					this.loadNoDataFragment();
					Fragment.byId("overview_fragment", "scrollCont_KPIDashboard").addContent(this.getNoDataFragment().addStyleClass(
						"sapUiLargeMargin"));
				}
				Fragment.byId("overview_fragment", "scrollCont_KPIDashboard").setBusy(false);
			}.bind(this), function() { // when call fails
				Fragment.byId("overview_fragment", "scrollCont_KPIDashboard").setBusy(true);
			}.bind(this));
		},

		loadNoDataFragment: function() {
			this.oNoDataFragment = sap.ui.xmlfragment("noData_fragment", "mktinsights.fragment.NoData", this);
		},

		onPressOfChartView: function() {
			Fragment.byId("overview_fragment", "scrollCont_KPIDashboard").removeAllContent();
			Fragment.byId("overview_fragment", "scrollCont_KPIDashboard").addContent(this.oKPIChartViewFragment);
		},

		onPressOfTileView: function() {
			/*var oContentDashboardBlock = Fragment.byId("overview_fragment", "emailContentDashboard");
			$(oContentDashboardBlock.$().children()[0]).css("height", "33rem");*/
			Fragment.byId("overview_fragment", "scrollCont_KPIDashboard").removeAllContent();
			Fragment.byId("overview_fragment", "scrollCont_KPIDashboard").addContent(this.oKPITileViewFragment);
		},

		destroyOverviewSectionFragments: function() {
			if (this.getKpiTileFragment()) {
				this.getKpiTileFragment().destroy(true);
			}
			if (this.getKpiChartFragment()) {
				this.getKpiChartFragment().destroy(true);
			}
			if (this.getNoDataFragment()) {
				this.getNoDataFragment().destroy(true);
			}
			if (this.getContentDashboardFragment()) {
				this.getContentDashboardFragment().destroy(true);
			}

		},

		getKpiTileFragment: function() {
			return this.oKPITileViewFragment;
		},

		getKpiChartFragment: function() {
			return this.oKPIChartViewFragment;
		},

		getNoDataFragment: function() {
			return this.oNoDataFragment;
		},

		getContentDashboardFragment: function() {
			return this.oContentDashboardFragment;
		},

		getOverviewSection: function() {
			return this.oOverviewFragment;
		},
		
		getPreviewDialog: function(){
			return this.oPreviewDialog;
		},

		checkOverviewSectionFragmentsExist: function() {
			if (this.getKpiTileFragment() || this.getKpiChartFragment() || this.getNoDataFragment() || this.getContentDashboardFragment() || this.getPreviewDialog()) {
				return true;
			} else {
				return false;
			}
		},

		onExit: function() {
			if (this.oOverviewFragment) {
				this.oOverviewFragment.destroy(true);
			}

			if (this.oContentDashboardFragment) {
				this.oContentDashboardFragment.destroy(true);
			}

			if (this.oKPIChartViewFragment) {
				this.oKPIChartViewFragment.destroy(true);
			}
			if (this.oKPITileViewFragment) {
				this.oKPITileViewFragment.destroy(true);
			}
			if (this.oNoDataFragment) {
				this.oNoDataFragment.destroy(true);
			}
			if(this.oPreviewDialog){
				this.oPreviewDialog.destroy(true);
			}

		}

	});
});