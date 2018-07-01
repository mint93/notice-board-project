package com.noticeboardproject.domain;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class State {
	private String state;
	private List<String> cities;

	private State() {};
	
	private State(String state, List<String> cities) {
		this.state = state;
		this.cities = new ArrayList<>(cities);
	}
	
	public static List<State> generateStates() {
		List<State> states = new ArrayList<>();
		State dolnoSlaskie = new State("dolnośląskie", Arrays.asList("Bielawa", "Bogatynia", "Boguszów-Gorce", "Bolesławiec", "Dzierżoniów", "Głogów", "Jawor", "Jelcz-Laskowice", "Jelenia Góra", "Kamienna Góra", "Kłodzko", "Legnica", "Lubań", "Lubin", "Nowa Ruda", "Oława", "Oleśnica", "Polkowice", "Strzegom", "Świdnica", "Świebodzice", "Wałbrzych", "Wrocław", "Ząbkowice Śląskie", "Zgorzelec", "Złotoryja"));
		State kujawskoPomorskie = new State("kujawsko-pomorskie", Arrays.asList("Brodnica", "Bydgoszcz", "Chełmno", "Grudziądz", "Inowrocław", "Nakło nad Notecią", "Rypin", "Solec Kujawski", "Świecie", "Toruń", "Włocławek"));
		State lubelskie = new State("lubelskie", Arrays.asList("Biała Podlaska", "Biłgoraj", "Chełm", "Dęblin", "Hrubieszów", "Kraśnik", "Krasnystaw", "Łęczna", "Lubartów", "Lublin", "Łuków", "Międzyrzec Podlaski", "Puławy", "Radzyń Podlaski", "Świdnik", "Tomaszów Lubelski", "Zamość"));
		State lubuskie = new State("lubuskie", Arrays.asList("Gorzów Wielkopolski", "Gubin", "Kostrzyn nad Odrą", "Międzyrzecz", "Nowa Sól", "Słubice", "Sulechów", "Świebodzin", "Żagań", "Żary", "Zielona Góra"));
		State lodzkie = new State("łódzkie", Arrays.asList("Aleksandrów Łódzki", "Bełchatów", "Głowno", "Konstantynów Łódzki", "Kutno", "Łask", "Łęczyca", "Łódź", "Łowicz", "Opoczno", "Ozorków", "Pabianice", "Piotrków Trybunalski", "Radomsko", "Rawa Mazowiecka", "Sieradz", "Skierniewice", "Tomaszów Mazowiecki", "Wieluń", "Zgierz"));
		State malopolskie = new State("małopolskie", Arrays.asList("Andrychów", "Bochnia", "Brzesko", "Chrzanów", "Gorlice", "Kęty", "Kraków", "Libiąż", "Limanowa", "Myślenice", "Nowy Sącz", "Nowy Targ", "Olkusz", "Oświęcim", "Skawina", "Tarnów", "Trzebinia", "Wadowice", "Wieliczka", "Zakopane"));
		State mazowieckie = new State("mazowieckie", Arrays.asList("Ciechanów", "Garwolin", "Gostynin", "Grodzisk Mazowiecki", "Grójec", "Józefów", "Kobyłka", "Konstancin-Jeziorna", "Kozienice", "Legionowo", "Łomianki", "Marki", "Milanówek", "Mińsk Mazowiecki", "Mława", "Nowy Dwór Mazowiecki", "Ostrołęka", "Ostrów Mazowiecka", "Otwock", "Piaseczno", "Piastów", "Pionki", "Płock", "Płońsk", "Pruszków", "Przasnysz", "Pułtusk", "Radom", "Siedlce", "Sierpc", "Sochaczew", "Sokołów Podlaski", "Sulejówek", "Warszawa", "Wołomin", "Wyszków", "Ząbki", "Zielonka", "Żyrardów"));
		State opolskie = new State("opolskie", Arrays.asList("Brzeg", "Kędzierzyn-Koźle", "Kluczbork", "Krapkowice", "Namysłów", "Nysa", "Opole", "Prudnik", "Strzelce Opolskie"));
		State podkarpackie = new State("podkarpackie", Arrays.asList("Dębica", "Jarosław", "Jasło", "Krosno", "Łańcut", "Mielec", "Nisko", "Przemyśl", "Przeworsk", "Ropczyce", "Rzeszów", "Sanok", "Stalowa Wola", "Tarnobrzeg"));
		State podlaskie = new State("podlaskie", Arrays.asList("Augustów", "Białystok", "Bielsk Podlaski", "Grajewo", "Hajnówka", "Łapy", "Łomża", "Siemiatycze", "Sokółka", "Suwałki", "Zambrów"));
		State pomorskie = new State("pomorskie", Arrays.asList("Bytów", "Chojnice", "Gdańsk", "Gdynia", "Kartuzy", "Kościerzyna", "Kwidzyn", "Lębork", "Malbork", "Pruszcz Gdański", "Reda", "Rumia", "Słupsk", "Sopot", "Starogard Gdański", "Tczew", "Ustka", "Wejherowo"));
		State slaskie = new State("śląskie", Arrays.asList("Będzin", "Bielsko-Biała", "Bieruń", "Bytom", "Chorzów", "Cieszyn", "Czechowice-Dziedzice", "Czeladź", "Czerwionka-Leszczyny", "Częstochowa", "Dąbrowa Górnicza", "Gliwice", "Jastrzębie-Zdrój", "Jaworzno", "Katowice", "Knurów", "Łaziska Górne", "Lędziny", "Lubliniec", "Mikołów", "Mysłowice", "Myszków", "Orzesze", "Piekary Śląskie", "Pszczyna", "Pyskowice", "Racibórz", "Radlin", "Radzionków", "Ruda Śląska", "Rybnik", "Rydułtowy", "Siemianowice Śląskie", "Sosnowiec", "Świętochłowice", "Tarnowskie Góry", "Tychy", "Ustroń", "Wodzisław Śląski", "Zabrze", "Zawiercie", "Żory", "Żywiec"));
		State swietokrzyskie = new State("świętokrzyskie", Arrays.asList("Busko-Zdrój", "Jędrzejów", "Kielce", "Końskie", "Ostrowiec Świętokrzyski", "Sandomierz", "Skarżysko-Kamienna", "Starachowice", "Staszów"));
		State warminskoMazurskie = new State("warmińsko-mazurskie", Arrays.asList("Bartoszyce", "Braniewo", "Działdowo", "Elbląg", "Ełk", "Giżycko", "Iława", "Kętrzyn", "Lidzbark Warmiński", "Mrągowo", "Olecko", "Olsztyn", "Ostróda", "Pisz", "Szczytno"));
		State wielkopolskie = new State("wielkopolskie", Arrays.asList("Chodzież", "Gniezno", "Gostyń", "Jarocin", "Kalisz", "Koło", "Konin", "Kościan", "Krotoszyn", "Leszno", "Luboń", "Nowy Tomyśl", "Oborniki", "Ostrów Wielkopolski", "Piła", "Pleszew", "Poznań", "Rawicz", "Śrem", "Środa Wielkopolska", "Swarzędz", "Szamotuły", "Trzcianka", "Turek", "Wągrowiec", "Września", "Złotów"));
		State zachodniopomorskie = new State("zachodniopomorskie", Arrays.asList("Białogard", "Choszczno", "Białogard", "Gryfice", "Gryfino", "Kamień Pomorski", "Kołobrzeg", "Koszalin", "Nowogard", "Police", "Stargard", "Świdwin", "Świnoujście", "Szczecin", "Szczecinek", "Wałcz"));
		
		states.addAll(Arrays.asList(dolnoSlaskie, kujawskoPomorskie, lubelskie, lubuskie, lodzkie, malopolskie, mazowieckie, opolskie, podkarpackie, podlaskie, pomorskie, slaskie, swietokrzyskie, warminskoMazurskie, wielkopolskie, zachodniopomorskie));
		return states;
	}
}