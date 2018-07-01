$(function() {
	init();
});

function init() {
	var imageThumbnail = $('.preview-thumbnail').children();
	for (i = 0; i < imageThumbnail.length; i++) {
		imageThumbnail[i].addEventListener('click', imageSelectHandler, false);
	}
	if(imageThumbnail.find('img').attr('src')=='/img/no-image.png') {
		imageThumbnail.find('img').addClass('hidden');
	}
	
	var oldDate = document.getElementById("creation-date").innerHTML;
	document.getElementById("creation-date").innerHTML = displayDate(oldDate);
}

function imageSelectHandler(e) {
	var previewPic = $('.preview-pic').children().each(function() {
		$(this).removeClass('active');
	});
	
	$($(this).find("a[data-toggle='tab']").attr('data-target')).addClass('active');
}