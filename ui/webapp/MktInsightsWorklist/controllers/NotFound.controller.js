sap.ui.define([
		"mktinsights/base/controllers/BaseController"
	], function (BaseController) {
		"use strict";

		return BaseController.extend("mktinsights.MktInsightsWorklist.controllers.NotFound", {

			/**
			 * Navigates to the worklist when the link is pressed
			 * @public
			 */
			onLinkPressed : function () {
				this.getRouter().navTo("worklist");
			}

		});

	}
);