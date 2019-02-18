/*eslint-disable */
sap.ui.define([
	"sap/ui/core/UIComponent",
	"sap/ui/Device",
	"mktinsights/model/models",
	"mktinsights/MktInsightsWorklist/controllers/ErrorHandler"
], function(UIComponent, Device, models, ErrorHandler) {
	"use strict";

	return UIComponent.extend("mktinsights.Component", {

		metadata: {
			manifest: "json"
		},

		/**
		 * The component is initialized by UI5 automatically during the startup of the app and calls the init method once.
		 * In this function, the device models are set and the router is initialized.
		 * @public
		 * @override
		 */
		init: function() {

			// call the base component's init function
			UIComponent.prototype.init.apply(this, arguments);

			// initialize the error handler with the component
			this._oErrorHandler = new ErrorHandler(this);

			// set the device model
			this.setModel(models.createDeviceModel(), "device");

			// set the navigation model
			this.setModel(models.createNavigationModel(), "navigationModel");

			// Redirect navigation based on URL Paramaters
			this.setNavigationRedirect();

			this.batchResponse();

		},

		batchResponse: function() {
			var self = this;
			$.ajaxSetup({
				beforeSend: function() {},
				complete: function(data, responseText) {
					if (data.responseText.includes("500 Internal Server Error")) {
						new ErrorHandler(this).showServiceError();
					}
					var headers = {};
					var headerStr = data.getAllResponseHeaders();
					var val = data.getResponseHeader('com.sap.cloud.security.login')
					if (!headerStr) {
						return headers;
					} else if (val) {
						window.location.reload();
					}
				}.bind(this),
				error: function(oError) {
					var error = oError;
					if (oError.status !== "404" || (oError.status === 404 && oError.statusText.indexOf("Cannot POST") === 0)) {
						new ErrorHandler(this).showServiceError();
					}
				}
			});

		},

		/**
		 * The component is destroyed by UI5 automatically.
		 * In this method, the ErrorHandler is destroyed.
		 * @public
		 * @override
		 */
		destroy: function() {
			this._oErrorHandler.destroy();
			// call the base component's destroy function
			UIComponent.prototype.destroy.apply(this, arguments);
		},

		/**
		 * This method can be called to determine whether the sapUiSizeCompact or sapUiSizeCozy
		 * design mode class should be set, which influences the size appearance of some controls.
		 * @public
		 * @return {string} css class, either 'sapUiSizeCompact' or 'sapUiSizeCozy' - or an empty string if no css class should be set
		 */
		getContentDensityClass: function() {
			if (this._sContentDensityClass === undefined) {
				// check whether FLP has already set the content density class; do nothing in this case
				if (jQuery(document.body).hasClass("sapUiSizeCozy") || jQuery(document.body).hasClass("sapUiSizeCompact")) {
					this._sContentDensityClass = "";
				} else if (!Device.support.touch) { // apply "compact" mode if touch is not supported
					this._sContentDensityClass = "sapUiSizeCompact";
				} else {
					// "cozy" in case of touch support; default for most sap.m controls, but needed for desktop-first controls like sap.ui.table.Table
					this._sContentDensityClass = "sapUiSizeCozy";
				}
			}
			return this._sContentDensityClass;
		},

		// Redirect navigation based on URL Paramaters
		setNavigationRedirect: function() {
			var oStartupParameters = jQuery.sap.getUriParameters().mParams;

			if (oStartupParameters.CampaignID) {
				sap.ui.core.BusyIndicator.show();
				var campaignId = oStartupParameters.CampaignID[0];
				if (campaignId) {
					var sPath = this.getModel().sServiceUrl + "Campaigns" + "?$filter=CampaignID eq '" + campaignId + "'";
					jQuery.get(sPath, function(odata) {
						var oHashChanger = new sap.ui.core.routing.HashChanger.getInstance();
						oHashChanger.replaceHash("Campaigns/" + odata.value[0].CampaignUUID);
						// create the views based on the url/hash
						this.getRouter().initialize();
						sap.ui.core.BusyIndicator.hide();
					}.bind(this)).fail(function(oError) {
						if (oError.status !== "404" || (oError.status === 404 && oError.statusText.indexOf("Cannot POST") === 0)) {
							new ErrorHandler(this).showServiceError();
						}
						sap.ui.core.BusyIndicator.hide();
					}.bind(this));
				}
			} else {
				this.getRouter().initialize();
				this.getModel("navigationModel").setProperty("/isNavFromWorklist", true);
				sap.ui.core.BusyIndicator.hide();
			}
		}

	});

});