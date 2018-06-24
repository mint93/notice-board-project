	var maxImages = 3;
	var imagesNames = [];
	
$(document).ready(function () {
	init();
});

function init() {
	if (window.File && window.FileList && window.FileReader) {
		var fileSelect = document.getElementsByClassName("file-upload");
		for (i = 0; i < fileSelect.length; i++) {
			  fileSelect[i].addEventListener('change', fileSelectHandler, false);
		}
		
		var closeFile = document.getElementsByClassName("close");
		for (i = 0; i < closeFile.length; i++) {
			closeFile[i].addEventListener('click', closeFileHandler, true);
		}
	
		} else {
			var fileUploadArea = document.getElementsByClassName("file-upload-area");
			for (i = 0; i < fileUploadArea.length; i++) {
				fileUploadArea[i].value.style.display = 'none';
		}
	}
}
function preventDefaultTriggerHandler(e) {
	e.preventDefault();
}

function closeFileHandler(e){
	uploader = $(e.currentTarget).parent();
	deleteFile(uploader.find('.file-image').attr("src"));
	uploader.find('.file-image')[0].classList.add("hidden");
	uploader.find('.notimage')[0].classList.add("hidden");
	uploader.find('.start')[0].classList.remove("hidden");
	uploader.find('.close')[0].classList.add("hidden");
	uploader.find('.response')[0].classList.add("hidden");
	uploader.find('.file-image').attr({src:"#"});
	uploader.one('click', preventDefaultTriggerHandler);
	uploader.parent().find('.image-name').attr("value", "");
}

function deleteFile(srcImg) {
	$.get(srcImg + "/delete");
}

function fileSelectHandler(e) {
	 var files = e.target.files || e.dataTransfer.files;
	 parentUploader = $(e.currentTarget).parent();
	 var imgTag = parentUploader.find('.file-image');
	 if(imgTag.attr("src")!="#") {
		 var imageId = imgTag.attr('id');
		 var imageNumber = imageId.substring(imageId.length-1, imageId.length);
		 var imageName = imagesNames[imageNumber-1];
		 if(imageName!="") {
			 imagesNames[imageNumber-1] = "";
			 deleteFile(imgTag.attr("src"));
		 }
	 }
	 for (var i = 0, f; f = files[i]; i++) {
	   parseFile(f, parentUploader);
	   uploadFile(f, parentUploader);
	 }
}

function output(msg, target) {
	target.innerHTML = msg;
}

function parseFile(file, uploader) {
	 output(
	   '<strong>' + encodeURI(file.name) + '</strong>', uploader.find('.messages')[0]
	 );
	 //uploader.find('.image-name').attr("value", file.name);
	 
	 var imageName = file.name;

	 var isGood = (/\.(?=gif|jpg|png|jpeg)/gi).test(imageName);
	 if (isGood) {
		 uploader.find('.start')[0].classList.add("hidden");
		 uploader.find('.close')[0].classList.remove("hidden");
		 uploader.find('.response')[0].classList.remove("hidden");
		 uploader.find('.notimage')[0].classList.add("hidden");
		 uploader.find('.file-image')[0].classList.remove("hidden");
	 }
	 else {
		 uploader.find('.file-image')[0].classList.add("hidden");
		 uploader.find('.notimage')[0].classList.remove("hidden");
		 uploader.find('.start')[0].classList.remove("hidden");
		 uploader.find('.close')[0].classList.add("hidden");
		 uploader.find('response')[0].classList.add("hidden");
	 }
}

function uploadFile(file, uploader) {

	   fileSizeLimit = 1; // In MB
	   // Check if file is less than x MB
	   if (file.size <= fileSizeLimit * 1024 * 1024) {
		   fire_ajax_submit(file, uploader);
	   } else {
	     output('Please upload a smaller file (< ' + fileSizeLimit + ' MB).', uploader.find('.messages')[0]);
	     uploader.find('.file-image')[0].classList.add("hidden");
		 uploader.find('.start')[0].classList.remove("hidden");
		 uploader.find('.close')[0].classList.add("hidden");
		 uploader.find('response')[0].classList.add("hidden");
	   }
}

function preventDuplicateNames(availableNames, maxNumberOfNames, name){
	for(var i=0; i<maxNumberOfNames; i++) {
		if(availableNames[i]==name) {
			name = "0" + name;
			name = preventDuplicateNames(availableNames, maxNumberOfNames, name);
		}
	}
	return name
}

function fire_ajax_submit(file, uploader) {
	var imageName = file.name;
	var imageId = uploader.attr('id');
	var imageNumber = imageId.substring(imageId.length-1, imageId.length);

	newImageName = preventDuplicateNames(imagesNames, maxImages, imageName);
	
	uploader.find('.image-name').attr("value", newImageName);
	
	imagesNames[imageNumber-1]=newImageName;
	const newFileName = new File([file], newImageName, {type: file.type});
	var data = new FormData();
	data.append('file', newFileName);

	$.ajax({
		type: "POST",
		enctype: 'multipart/form-data',
		url: "/uploadFile",
		data: data,
		processData: false, //prevent jQuery from automatically transforming the data into a query string
		contentType: false,
		cache: false,
		timeout: 600000,
		success: function (data) {

			if(data.match("^Successfully uploaded - ")) {
				var imageName = data.substring(24, data.length);
				uploader.find('.file-image').attr({src:"../files/" + imageName});
			}
		}
	});

}

