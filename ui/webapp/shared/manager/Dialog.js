sap.ui.define(["sap/ui/base/Object",
	"sap/ui/model/json/JSONModel",
	"sap/ui/core/Fragment",
	"mktinsights/MktInsightsWorklist/controllers/ErrorHandler",
	"mktinsights/manager/ServiceManager",
	"mktinsights/model/formatter"
], function(Object, JSONModel, Fragment, ErrorHandler, ServiceManager, formatter) {

	return Object.extend("mktinsights.shared.manager.Dialog", {

		formatter: formatter,

		constructor: function(oController, dialogObject) {
			this._delegate = oController;
			this._oServiceManager = new ServiceManager(this._delegate);
			this.dialogObject = dialogObject;
		},

		_setModel: function(dialogObject) {
			var dialogModel = new JSONModel({
				body: (dialogObject.body === undefined) ? "DELETE_BODY" : dialogObject.body
			});

			this.getView().setModel(dialogModel, "dialogModel");
		},

		getView: function() {
			return this._oDialog;
		},

		createDialog: function() {

			this._oDialog = sap.ui.xmlfragment("Dialog", "mktinsights.shared.fragments.Dialog",
				this);
			this._delegate.getView().addDependent(this._oDialog);
			this._setModel(this.dialogObject);
			this._oDialog.open();

		},

		handleCancelPress: function() {
			this._oDialog.destroy(true);

		},

		handleOKPress: function() {
			this._delegate.dialogOKPress();
		}

	});
});