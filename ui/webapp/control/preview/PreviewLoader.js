sap.ui.define([
	"sap/ui/core/Control",
	"sap/ui/core/IconPool"
], function(Control, Button, IconPool) {
	"use strict";

	return Control.extend("mktinsights.control.preview.PreviewLoader", {

		metadata: {
			properties: {
				content: {
					type: "string"
				},

				renderWidth: {
					type: "int",
					defaultValue: "602"
				},

				renderHeight: {
					type: "int",
					defaultValue: "650"
				},

				controlWidth: {
					type: "int"
				},

				controlHeight: {
					type: "int"
				},

				guid: {
					type: "string"
				},

				contentLoadingText: {
					type: "string",
					defaultValue: "Loading Content..."
				},

				contentNotFoundText: {
					type: "string",
					defaultValue: "No Content found"
				}
			},

			events: {
				previewLoaderReady: {}
			}
		},

		renderer: function(oRenderManager, oPreviewLoader) {
			// get desired (in View defined) dimensions
			var iControlWidth = oPreviewLoader.getControlWidth();
			var iControlHeight = oPreviewLoader.getControlHeight();

			// calculate zoomfactor based on internal iframe render dimensions
			// i.e. how much has the iframe content be shrinked to fit in desired dimensions
			var fZoomfactor = iControlWidth / oPreviewLoader.getRenderWidth();

			// calculate iFrame render height respecting the desired aspect ratio
			oPreviewLoader.setRenderHeight(Math.floor(iControlHeight * (1.0 / fZoomfactor)));

			oPreviewLoader.setGuid(oPreviewLoader.calculateGuid());

			// build iframe with calculated zoom factor
			var sIframeDefinition = "<iFrame scrolling=no style='border: none;";
			sIframeDefinition += "-webkit-transform: scale(" + fZoomfactor + ");";
			sIframeDefinition += "-ms-transform: scale(" + fZoomfactor + ");";
			sIframeDefinition += "-ms-transform-origin: 0 0;";
			sIframeDefinition += "-webkit-transform-origin: 0 0;'";
			sIframeDefinition += "id=\"" + oPreviewLoader.getGuid() + "\">;";
			var oNewIframe = jQuery(sIframeDefinition)[0];

			// render iframe content with internal iframe render dimensions
			oNewIframe.width = oPreviewLoader.getRenderWidth() + "px";
			oNewIframe.height = oPreviewLoader.getRenderHeight() + "px";

			oNewIframe.src = "about:blank";

			var mParameters = {};
			mParameters.oPreviewLoader = oPreviewLoader;
			mParameters.fnCallback = function(sHtml) {
				var sContentNotFoundMessage = oPreviewLoader.getContentNotFoundText();
				var sNotFoundText = "<!DOCTYPE html><html><head><meta charset=\"UTF-8\">";
				sNotFoundText +=
					"<style type=\"text/css\">.header {font-family: Arial, Helvetica, sans-serif;font-size: 34px;margin-top:60%;margin-left:30%;}</style>";
				sNotFoundText += "</head><body><p class=\"header\">";
				sNotFoundText += sContentNotFoundMessage;
				sNotFoundText += "</p></body></html>";

				var sContent = sHtml;
				sContent = sContent.replace("@media", "_@media");

				// no content from backend -> display default placeholder text
				if (sContent === undefined || sContent === "") {
					sContent = sNotFoundText;
				}

				// content from backend -> enable scrolling on matching scrollbox
				else {
					var sTargetId = "#" + oPreviewLoader.getGuid();
					jQuery(sTargetId).mouseover(oPreviewLoader.scrollBox);
				}

				/* eslint-disable sap-no-inner-html-write,sap-no-inner-html-access */
				if (oNewIframe.contentWindow && oNewIframe.contentWindow.document) {
					oNewIframe.contentWindow.document.body.innerHTML = sContent;
				}
				/* eslint-enable sap-no-inner-html-write,sap-no-inner-html-access */
			};

			oNewIframe.onload = function() {
				var sLoadingContentMessage = oPreviewLoader.getContentLoadingText();
				var sLoadingText = "<!DOCTYPE html><html><head><meta charset=\"UTF-8\">";
				sLoadingText +=
					"<style type=\"text/css\">.header {font-family: Arial, Helvetica, sans-serif;font-size: 34px;margin-top:60%;margin-left:30%;}</style>";
				sLoadingText += "</head><body><p class=\"header\">";
				sLoadingText += sLoadingContentMessage;
				sLoadingText += "</p></body></html>";

				/* eslint-disable sap-no-inner-html-write,sap-no-inner-html-access */
				oNewIframe.contentWindow.document.body.innerHTML = sLoadingText;
				/* eslint-enable sap-no-inner-html-write,sap-no-inner-html-access */
				oPreviewLoader.fireEvent("previewLoaderReady", mParameters);
			};

			oRenderManager.write("<div");
			oRenderManager.writeControlData(oPreviewLoader);

			// build overlay div serving as click preventer and scrollbox
			var sOverlayDiv = "><div class='sapCntPgPreviewLoader' style='z-index: 999; position: absolute;";
			sOverlayDiv += "width: " + oPreviewLoader.getControlWidth() + "px;";
			// force div height to be the same as shrinked iframe (because ratio of desired dimensions doesn't have
			// to be the same as ratio of render dimensions
			sOverlayDiv += "height: " + oPreviewLoader.getRenderHeight() * fZoomfactor + "px;";
			sOverlayDiv += "opacity: 0;'";
			sOverlayDiv += "id=\"" + oPreviewLoader.getGuid() + "\"></div></div>;";
			oRenderManager.write(sOverlayDiv);

			oPreviewLoader.onAfterRendering = function() {
				/* eslint-disable sap-no-dom-insertion */
				jQuery("#" + this.getId()).append(oNewIframe);
				/* eslint-enable sap-no-dom-insertion */
			};
		},

		scrollBox: function(element) {
			var oCurrentScrollbox = element.currentTarget;

			oCurrentScrollbox.addEventListener("mousemove", function(e) {
				var oCurrentIFrameBody = $(this.nextElementSibling.contentWindow.document.body);
				var fHeightOfIFrame = oCurrentIFrameBody.height();
				var fHeightOfScrollbox = $(this).height();
				var fClickedYCoordinateInsideScrollbox = e.offsetY;

				var fPercent = fClickedYCoordinateInsideScrollbox / fHeightOfScrollbox;

				var fScrollPositionInsideIFrame = fHeightOfIFrame * fPercent;

				// scrolled down close to end -> scroll to end
				if (fHeightOfIFrame - fScrollPositionInsideIFrame < (0.1 * fHeightOfIFrame)) {
					oCurrentIFrameBody.scrollTop(fHeightOfIFrame);
				} else {
					//scroll precisely
					oCurrentIFrameBody.scrollTop(fScrollPositionInsideIFrame);
				}
			});
		},

		calculateGuid: function() {
			function s4() {
				return Math.floor((1 + Math.random()) * 0x10000).toString(16).substring(1);
			}
			return s4() + s4() + "-" + s4() + "-" + s4() + "-" + s4() + "-" + s4() + s4() + s4();
		}
	});
}, true);