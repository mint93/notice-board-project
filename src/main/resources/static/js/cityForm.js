$(document).ready(function () {
    var autocompleteIsOpen = false;
    if($('#citySearch').val()==""){
    	$('#citySearch').val('Polska');
    }

    init();
    function init() {
        $('#subdropdown').children().hide();
    }

    $('#citySearch').click(function (event) {

        event.stopPropagation();
        $('#subdropdown').children().hide();

        if (!autocompleteIsOpen) {
        	$('#dropdown').width($(this).width()+20);
            $('#dropdown').toggle();
            $('#dropdown').children('a').each(function () {
                $(this).bind('mouseenter', hover);
                $(this).css({
                    "background": "#F8F8F8",
                    "color": "#333"
                });
            });
        }
        if ($(this).val() == 'Polska') {
            $(this).val('');
        }
    });

    $('#citySearch').keydown(function (e) {
        hideDropDowns();
    });
    $('#citySearch').keyup(function (e) {
        if ($(this).val() == '') {
            $('#dropdown').show();
        }
    });

    $('#dropdown').on('click', function (e) {
        $('#citySearch').val(e.target.textContent.replace(/\s/g,''));
    })

    $('#subdropdown').on('click', function (e) {
        $('#citySearch').val(e.target.textContent).replace(/\s/g,'');
    })

    $('#dropdown').children('a').each(function () {
        $(this).click(function (e) {
            $(this).unbind('mouseenter mouseleave');
            hideDropDowns();
            $(document).trigger('click');
        });
    });    	

    $('#dropdown').children('a').each(function () {
        $(this).hover(hover);
    });

    function hover(e) {
        var id = e.currentTarget.id;
        var left = $('#' + id).width() + 57;
        var top = $('#' + id).position().top + 46;
        
        $(this).parent().children().each(function() {
        	$(this).css({
                "background": "#F8F8F8",
                "color": "#333"
            });
        });
        
        $(this).css({
        	"background": "#333",
            "color": "#FFF"
        });
        
        
        $('#subdropdown-' + id).css("background", "#F8F8F8");
        $('#subdropdown-' + id).css("border", "1px solid #CCC");
        $('#subdropdown-' + id).css("box-shadow", "0 1px 3px rgba(0,0,0,0.15)");
        $('#subdropdown-' + id).css("border-radius", "3px");
        $('#subdropdown-' + id).css("position", "absolute");
        $('#subdropdown-' + id).css("left", left + "px");
        $('#subdropdown-' + id).css("top", top + "px");
        $('#subdropdown-' + id).css("width", "200px");
        $('#subdropdown-' + id).css("z-index", "10");
        $('#subdropdown-' + id).css("overflow", "auto");
        $('#subdropdown-' + id).css("max-height", window.innerHeight-top-70 + "px");
        $('#subdropdown').children().hide()
        $('#subdropdown-' + id).show();


        $('#subdropdown-' + id + ' a').css({
            "color": "#333",
            "display": "block",
            "padding": "5px 10px",
            "font-family": "Helvetica, Arial, sans-serif",
            "font-size": "12px",
            "text-decoration": "none"
        });
        $('#subdropdown-' + id + ' a').hover
            (function () {
                $(this).css({
                    "background": "#333",
                    "color": "#FFF"
                });
                $('#' + id).css({
                	"background": "#333",
                	"color": "#FFF"});
        },
            function () {
                $(this).css({
                    "background": "#F8F8F8",
                    "color": "#333"
                });
        });
    }

    $(document).click(function () {

        hideDropDowns();
        if ($('#citySearch').val() == '') {
            $('#citySearch').val('Polska');
        }

    });

    function hideDropDowns() {
        $('#dropdown').hide();
        $('#subdropdown').children().hide();
    }

    $("#citySearch").autocomplete({
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
            jQuery("#citySearch").val(selectedObj.value);
            return false;
        },

        open: function () {
            autocompleteIsOpen = true;
        },
        close: function () {
            autocompleteIsOpen = false;
        }
        
    });
});