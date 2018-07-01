function displayStatesFromGeoNames(stateName) {
	switch(stateName.toLowerCase()) {
    case "województwo dolnośląskie": return "dolnośląskie";
    case "województwo kujawsko-pomorskie": return "kujawsko-pomorskie";
    case "lubelskie": return "lubelskie";
    case "województwo lubuskie": return "lubuskie";
    case "lódzkie": return "łódzkie";
    case "województwo małopolskie": return "małopolskie";
    case "województwo mazowieckie": return "mazowieckie";
    case "województwo opolskie": return "opolskie";
    case "województwo podkarpackie": return "podkarpackie";
    case "województwo podlaskie": return "podlaskie";
    case "województwo pomorskie": return "pomorskie";
    case "województwo śląskie": return "śląskie";
    case "województwo świętokrzyskie": return "świętokrzyskie";
    case "woj. warmińsko-mazurskie": return "warmińsko-mazurskie";
    case "województwo wielkopolskie": return "wielkopolskie";
    case "województwo zachodniopomorskie": return "zachodniopomorskie";
    
    default: return "Unknown";
	} 
}