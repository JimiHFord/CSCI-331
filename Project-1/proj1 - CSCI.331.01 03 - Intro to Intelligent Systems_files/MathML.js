/**
* @namespace
*/
D2LMathML = {

	IsLatex: false,
	/**
	* @private
	*/
	m_message: '',

	/**
	* @private
	*/
	MobileInit: function (message) {
		D2LMathML.m_message = message;
	},

	DesktopInit: function (mathMLUrl, latexUrl) {

		(function () {

			var head = document.getElementsByTagName("head")[0];
			var configScript = 'MathJax.Hub.Config({ delayStartupUntil: "onload",showProcessingMessages: false, messageStyle: "none", showMathMenu: true, showMathMenuMSIE: true, menuSettings: { context: "MathJax", zoom: "None" },NativeMML: { showMathMenuMSIE: false, scale: 140 }, "HTML-CSS": { linebreaks: { automatic: true, width: "container"}, imageFont: null, scale: 130 } });';
			var script = document.createElement('script');
			script.type = 'text/x-mathjax-config';
			script.textContent = configScript;			
			head.appendChild(script);

			var mathJaxScript = document.createElement('script');
			mathJaxScript.type = 'text/javascript';
			mathJaxScript.src = D2LMathML.IsLatex ? latexUrl : mathMLUrl;
			head.appendChild(mathJaxScript);

		})();

	}
};