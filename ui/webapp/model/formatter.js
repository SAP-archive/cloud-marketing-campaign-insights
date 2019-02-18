sap.ui.define([
	"sap/ui/core/format/DateFormat"
], function(DateFormat) {
	"use strict";
	return {
		numberUnit: function(sValue) {
			if (!sValue) {
				return "";
			}
			return parseFloat(sValue).toFixed(2);
		},
		formatCampaignID: function(sId) {
			if (sId) {
				return this.getResourceBundle().getText("id") + ": " + sId;
			}
		},
		statusColor: function(sLifeCycleStatus) {
			switch (sLifeCycleStatus) {
				case this.getResourceBundle().getText("worklistStatusInPrepTitle"):
					return "Warning";
				case this.getResourceBundle().getText("worklistStatusReleasedTitle"):
					return "Success";
				default:
					return "None";
			}
		},
		formatKPIData: function(value) {
			if (value === null) {
				return 0;
			} else {
				return value;
			}
		},

		setKPIVisibility: function(lifeCycleStatusName) {
			if (lifeCycleStatusName === "Released") {
				return true;
			} else {
				return false;
			}
		},

		convertToDateTimeObject: function(sDate) {
			var sJsonDate = sDate;
			if (sJsonDate) {
				var sNumber = sJsonDate.replace(/[^0-9]+/g, '');
				var iNumber = sNumber * 1;
				var oDate = new Date(iNumber);
				var dateFormat = DateFormat.getDateInstance({
					style: 'medium'
				});
				var dateFormatted = dateFormat.format(oDate);
				return dateFormatted;
			}
		},

		getFullName: function(sFirstName, sLastName) {
			return sFirstName + " " + sLastName;
		},

		teamMemberName: function(sFullName, sTeamMemberName) {
			if (sFullName) {
				return sFullName;
			} else {
				return sTeamMemberName;
			}
		},

		getState: function(sValue) {
			if (sValue === "1") {
				return "Warning";
			} else if (sValue === "2") {
				return "Success";
			} else if (sValue === "3") {
				return "None";
			}
		}
	
	};

});