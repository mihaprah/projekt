# Simple Contact Manager - MMA

<p align="center">
  <img alt="SCM Logo" src="https://github.com/mihaprah/projekt/assets/116807398/1d171956-57b7-49b9-93d9-3525148c70d7" >
</p>

SCM oziroma Simple Contact Manager, je spletna aplikacija namenjena upravljanju s kontakti. Njegova glavna prednost pred ostalimi CRM (Customer Relations Manager) programi, je njegova preposta uporaba, saj je namenjen izključno delu s kontakti.
Ta datoteka, bo služila kot predstavitev projekta, navodila za namestitev aplikacije in dokumentacija za aplikacijo SCM.
### Člani ekipe MMA
- [Miha Prah](https://github.com/mihaprah)
- [Matija Krsnik](https://github.com/Matija334)
- [Anže Golob](https://github.com/anzo26)

## Dokumentacija
V dokumentaciji projekta je predstavljen celoten proces dela od začetka do konca. Prav tako so predstavljena vsa orodja in programska oprema, ki je bila tekom razvoja aplikacije uporabljena.

### Podroben opis projekta
Načrtovati, nadgraditi, implementirati in namestiti bo potrebno sistem, ki bo uporabnikom omogočal vodenje kontaktov na nekem projektu ali v nekem podjetju. Skupino kontaktov lahko uporabniki kreirajo sami, kontakti naj bodo vidni le znotraj te skupine; poskrbeti bo torej treba za izolacijo podatkov med različnimi uporabniki in njihovimi projekti.
Podatki o uporabnikih naj bodo do določene mere predefinirani, omogočeno pa naj bo tudi poljubno definiranje kontaktov v smislu kluč-vrednost. Pričakuje se označevanje kontaktov s poljubnimi značkami, ki bodo služile kot kategorije kontaktov. Voditi bo potrebno tudi vse spremembe kontaktov (revizijska sled) in jih tudi primerno verzionirati. Pričakuje se tudi določene napredne funkcionalnosti, kot npr. iskanje podvojenih kontaktov, njihovo združevanje ipd.
Dostop do zalednega sistema naj bo mogoč preko ustrezno zavarovanega REST vmesnika.
Uporabniški vmesnik naj nudi prijazen pregled, iskanje, filtriranje ter izvoz označenih kontaktov v
format MS Excel. Konkurenčna prednost izdelka prek konkurenčnimi CRM sistemi bo torej ravno
enostavnost uporabe.

### Funkcionalnosti
- Ustvarjanje skupin kontaktov
- Filtriranje in iskanje po kontaktih
- Dodajanje poljubnih značk kontaktom
- Ustvarjanje lastnih atributov za posamezen kontakt
- Združevanje podvojenih kontaktov
- Revizijska sled kontaktov
- Izvod kontaktov v format MS Excel

### Tehnološki nabor
#### Backend (zaledje)
Zaledje aplikacije smo izdelali v programskem jeziku [Java](https://www.java.com/en/). Uporabili smo ogrodje [SpringBoot](https://spring.io/projects/spring-boot), ki nam je pomagalo izgraditi REST vmesnik in komunicirati s podatkovno bazo. Za podatkovno bazo smo izbrali NoSQL dokumentno bazo [MongoDB](https://www.mongodb.com).

#### Frontend (pročelje)
Spletno aplikacijo smo idelali s pomočjo programske knjižnice [React](https://react.dev) v programskem jeziku [Typescript](https://www.typescriptlang.org).

### Organizacija in način dela
#### Komunikacija
Vsa komunikacija znotraj projekta je bila speljana preko [Discord](https://discord.com) strežnika, razen komuniciranje s profersorjem, ki je bilo preko maila. Naredili smo ločene kanale za posamezne dele projekta. Tako smo uspeli ločit komunikacijo, da smo lahko reševali probleme ločeno in se zadeve niso izgubile oziroma se niso problemi spregledali. Prav tako smo preko Discord strežnika izvedli vse skupinske sestanke in vso delo na daljavo, kjer smo si pomagali s funkcijo deljenja zaslona.
<p align="center">
  <img width="309" alt="discord-photo" src="https://github.com/mihaprah/projekt/assets/116807398/48f1979e-aaf7-4581-89ef-e99392d073d9">
</p>

#### Vodenje in deljenje dela
Za vodenja dela na projektu smo izbrali principe metode [SCRUM](https://www.scrum.org/resources/what-scrum-module), ki temelji na delu v sprintih. Vsak sprint je trajal 2 tedna in v njem smo opravili delo, ki smo si ga zastavili za ta sprint. Celoten projekt je bil sestavljen iz 5ih sprintov, pri čemer se je prvi začel 01.04.2024 in zadji 27.05.2024. Projekt je tako trajal **od 01.04.2024 do 03.06.2024**. Pred začetkom novega sprinta, smo ustvarili nova opravila, tako imenovane taske (ang. Task), ki jih moramo v naslednjem sprintu narediti. Za beleženje dela in pisanje novih opravil smo uporabili program [Notion](https://www.notion.so/Opis-8b1b88ecec974c03add9e5615c7a1f16), ki nam je omogočal pregled dela in uporabo scrum tabel za lepšo preglednost. Vsak task je imel naslednje atribute:

<p align="center">
  <img width="430" alt="task-example" src="https://github.com/mihaprah/projekt/assets/116807398/d2f0b728-0fd1-432e-a094-ef09c9df7327">
</p>
Task je nato šel v skupino vseh taskov imenovano "Backlog", kjer so bili vidno vsi taski na projektu in pa število ur dela, ki jih potrebujemo za končanje vseh taskov. Tako smo lahko vodili napredek na projektu in videli ali smo s delom na tekočem ali v zaostanku.

<p align="center">
  <img width="1272" alt="tasks-backlog" src="https://github.com/mihaprah/projekt/assets/116807398/08ec3001-ed98-42be-a5b0-2c65a20e12d0">
</p>

Za delo je vsak član skupine uporabljal svoj "Sprint board", kjer so bili zapisani vsi taski za vsakega posameznika, ki jih mora opraviti v trenutnem sprintu. Če katerega od taskov, ni uspel narediti, se je ta premaknil v naslednji sprint.

<p align="center">
  <img width="858" alt="sprint-board" src="https://github.com/mihaprah/projekt/assets/116807398/c4815865-cd86-444e-a97d-bd2c5d540920">
</p>

### Zasnova podatkovne strukture
<p align="center">
  <img width="902" alt="SCM Data structure" src="https://github.com/mihaprah/projekt/assets/116807398/11934d1c-2c87-4a70-a180-aa62b83f3224">
</p>

### Wireframe aplikacije (prototip izgleda)
S pomočjo orodja [Figma](https://www.figma.com), ki omogoča enostavno in hitro izdelavo vizualnih prototipov, smo izdelali prototip, kako bi naj aplikacija izgledala. Prototip nam je nato služil, kot navodilo za izgradnjo spletne aplikacije v nadaljevanju projekta.

## Navodila za namestitev
