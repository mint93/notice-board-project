function displayDate(date) {
	var dateAndTime = date.split(" ");
	var yearMounthDay = dateAndTime[0].split("-");
	var timeAndMiliseconds = dateAndTime[1].split(".");
	var hourMinuteSecond = timeAndMiliseconds[0].split(":"); 
	var milisecond = timeAndMiliseconds[1]; 
	var newDate = new Date(yearMounthDay[0], yearMounthDay[1], yearMounthDay[2], hourMinuteSecond[0], hourMinuteSecond[1], hourMinuteSecond[2], milisecond);
	return newDate.toLocaleString();
}