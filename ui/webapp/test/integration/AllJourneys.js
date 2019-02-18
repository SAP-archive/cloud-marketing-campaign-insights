jQuery.sap.require("sap.ui.qunit.qunit-css");
jQuery.sap.require("sap.ui.thirdparty.qunit");
jQuery.sap.require("sap.ui.qunit.qunit-junit");
QUnit.config.autostart = false;

sap.ui.require([
		"sap/ui/test/Opa5",
		"mktinsights/test/integration/pages/Common",
		"sap/ui/test/opaQunit",
		"mktinsights/test/integration/pages/Worklist",
		"mktinsights/test/integration/pages/Object",
		"mktinsights/test/integration/pages/NotFound",
		"mktinsights/test/integration/pages/Browser",
		"mktinsights/test/integration/pages/App"
	], function (Opa5, Common) {
	"use strict";
	Opa5.extendConfig({
		arrangements: new Common(),
		viewNamespace: "mktinsights.view."
	});

	sap.ui.require([
		"mktinsights/test/integration/WorklistJourney",
		"mktinsights/test/integration/ObjectJourney",
		"mktinsights/test/integration/NavigationJourney",
		"mktinsights/test/integration/NotFoundJourney"
	], function () {
		QUnit.start();
	});
});