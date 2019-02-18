sap.ui.define(["sap/ui/base/Object",
	"sap/ui/model/json/JSONModel",
	"sap/ui/core/Fragment",
	"mktinsights/MktInsightsWorklist/controllers/ErrorHandler",
	"mktinsights/manager/ServiceManager"
], function(Object, JSONModel, Fragment, ErrorHandler, ServiceManager) {

	return Object.extend("mktinsights.manager.ServiceManager", {

		constructor: function(oController) {
			this._delegate = oController;
		},

		read: function(sBindingPath, sEntitySet) {
			var sPath;
			if (sBindingPath !== null) {
				sPath = this._delegate.getModel().sServiceUrl + sBindingPath + "/" + sEntitySet;
			} else {
				sPath = this._delegate.getModel().sServiceUrl + sEntitySet;
			}
			return new Promise(function(resolve, reject) {
				jQuery.get(sPath, function(odata) {
					resolve(odata);
				}.bind(this)).fail(function(oError) {
					if (oError.status !== "404" || (oError.status === 404 && oError.statusText.indexOf("Cannot POST") === 0)) {
						new ErrorHandler(this).showServiceError();
					}
					reject();
				}.bind(this._delegate.getOwnerComponent()));
			}.bind(this));
		},

		submitData: function(sBindingPath, sEntitySet, oPayload) {
			var sPath;
			if (sBindingPath !== null) {
				sPath = this._delegate.getModel().sServiceUrl + sBindingPath + "/" + sEntitySet;
			} else {
				sPath = this._delegate.getModel().sServiceUrl + sEntitySet;
			}
			return new Promise(function(resolve, reject) {
				$.ajaxSetup({
					contentType: "application/json; charset=utf-8"
				});
				jQuery.post(sPath, oPayload,
					function(odata) {
						resolve(odata);
					}.bind(this)).fail(function(oError) {
					if (oError.status !== "404" || (oError.status === 404 && oError.statusText.indexOf("Cannot POST") === 0)) {
						new ErrorHandler(this).showServiceError();
					}
					reject();
				}.bind(this._delegate.getOwnerComponent()));
			}.bind(this));
		}
	});
});