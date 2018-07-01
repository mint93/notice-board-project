$(document).ready(function () {
	
	for (var element of document.getElementsByClassName("creation-date")) {
		var oldDate = element.innerHTML;
	    element.innerHTML = displayDate(oldDate);
	}

});