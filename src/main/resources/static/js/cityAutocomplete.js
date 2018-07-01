$(document).ready(function () {
	
	$("#city").keydown(function(){
		$("#state").val("");
	});

    $("#city").autocomplete({
        source: function (request, response) {
            $.ajax({
                url: "http://api.geonames.org/searchJSON?username=mint93",
                dataType: "jsonp",
                data: {
                    featureClass: "P",
                    style: "full",
                    country: "PL",
                    lang: "pl",
                    maxRows: 12,
                    name_startsWith: request.term
                },
                success: function (data) {
                    response($.map(data.geonames, function (item) {
                        return {
                            label: item.name + (item.adminName1 ? ", " + displayStatesFromGeoNames(item.adminName1) : ""),
                            value: item.name
                        }
                    }));
                }
            });
        },
        minLength: 2,
        select: function (event, ui) {
            var selectedObj = ui.item;
            jQuery("#city").val(selectedObj.value);
            var idx = selectedObj.label.indexOf(", ");
            jQuery("#state").val(selectedObj.label.substr(idx+2));
            return false;
        }
    });
});