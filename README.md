# Simple Contact Manager - MMA

<p align="center">
  <img alt="SCM Logo" width="400" src="https://github.com/mihaprah/projekt/assets/116807398/1d171956-57b7-49b9-93d9-3525148c70d7" >
</p>

SCM oziroma Simple Contact Manager, je spletna aplikacija namenjena upravljanju s kontakti. Njegova glavna prednost pred ostalimi CRM (Customer Relations Manager) programi, je njegova preposta uporaba, saj je namenjen izključno delu s kontakti.
Ta datoteka, bo služila kot predstavitev projekta, navodila za namestitev aplikacije in dokumentacija za aplikacijo SCM.
### Člani ekipe MMA
- [Miha Prah](https://github.com/mihaprah)
- [Matija Krsnik](https://github.com/Matija334)
- [Anže Golob](https://github.com/anzo26)

## Kazalo vsebine
1. [Predstavitev projekt](#1-predstavitev-projekta)
    - [Podroben opis projekta](#podroben-opis-projekta)
2. [Dokumentacija](#2-dokumentacija)
    - [Funkcionalnosti](#funkcionalnosti)
    - [Tehnološki nabor](#tehnološki-nabor)
    - [Organizacija in način dela](#organizacija-in-način-dela)
    - [Zasnova podatkovne strukture](#zasnova-podatkovne-strukture)
    - [Wireframe aplikacije (prototip izgleda)](#wireframe-aplikacije-prototip-izgleda)
    - [Testiranje](#testiranje)
    - [Optimizacija kode](#optimizacija-kode)
3. [Navodila za namestitev](#3-navodila-za-namestitev)
    - [Testno lokalno okolje](#testno-lokalnmo-okolje)
    - [Uporaba apliakcije](#uporaba-aplikacije)
    - [Uporabniški priročnik](#uporabniški-priročnik)

## 1. Predstavitev projekta
### Podroben opis projekta
Načrtovati, nadgraditi, implementirati in namestiti bo potrebno sistem, ki bo uporabnikom omogočal vodenje kontaktov na nekem projektu ali v nekem podjetju. Skupino kontaktov lahko uporabniki kreirajo sami, kontakti naj bodo vidni le znotraj te skupine; poskrbeti bo torej treba za izolacijo podatkov med različnimi uporabniki in njihovimi projekti.
Podatki o uporabnikih naj bodo do določene mere predefinirani, omogočeno pa naj bo tudi poljubno definiranje kontaktov v smislu kluč-vrednost. Pričakuje se označevanje kontaktov s poljubnimi značkami, ki bodo služile kot kategorije kontaktov. Voditi bo potrebno tudi vse spremembe kontaktov (revizijska sled) in jih tudi primerno verzionirati. Pričakuje se tudi določene napredne funkcionalnosti, kot npr. iskanje podvojenih kontaktov, njihovo združevanje ipd.
Dostop do zalednega sistema naj bo mogoč preko ustrezno zavarovanega REST vmesnika.
Uporabniški vmesnik naj nudi prijazen pregled, iskanje, filtriranje ter izvoz označenih kontaktov v
format MS Excel. Konkurenčna prednost izdelka prek konkurenčnimi CRM sistemi bo torej ravno
enostavnost uporabe.

## 2. Dokumentacija
V dokumentaciji projekta je predstavljen celoten proces dela od začetka do konca. Prav tako so predstavljena vsa orodja in programska oprema, ki je bila tekom razvoja aplikacije uporabljena.

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
Za shranjevanje podatkov smo uporabili MongoDB, kjer smo uporabljali različne kolekcije. Struktura temelji na skupini kontaktov, ki smo jo imenovali "Tenant". Ko uporabnik ustvari novo skupino, se usvarijo 3 kolekcije:
- Tenant main -> kolekcija, ki beleži vse aktivne kontakte znotraj ene skupine
- Tenant deleted -> kolekcija, ki beleži vse izbrisane kontakte znotraj ene skupine
- Tenant activity -> kolekcija, ki beleži vse aktivnosti, ki jih izvede uporabnik znotraj eneke skupine, kot so recimo ustvarjanje, posodabljanje ali brisanje kontaktov. Ta kolekcija nam omogoča usvarjanje revizijske sledi.

Posamezen kontakt ima kot privzeto nastavljena atributa ime in priimek, ostale atribute lahko poljubno dodaja uporabnik, kot pare ključ in vredost.

Obstaja tudi glavna kolekcija "Tenants", kjer so zabeleženi dodatni podatki o vsaki kolekciji, kot so njeno celo ime, opis, barva in pa ime uporabnika, ki jo je usvaril.

V bazi imamo tudi kolekcijo "Users", kjer je zabeležen osnovni podatek o uporabniku, ki je prijavljen v sistem in lahko ustvarja svoje kolekcije.

Kolekcija "Predefined searches", je namenjena shranjevanju iskalnih podatkov posameznih uporabnikov, če se ti uporabljajo recimo zelo pogosto.

Spodaj je skica, ki prikazuje strukturo naše podatkovne baze.
<p align="center">
  <img width="700" alt="data-strucute" src="https://github.com/mihaprah/projekt/assets/116807398/32533fd8-028f-4af9-98d5-d07653454180">
</p>

### Wireframe aplikacije (prototip izgleda)
S pomočjo orodja [Figma](https://www.figma.com), ki omogoča enostavno in hitro izdelavo vizualnih prototipov, smo izdelali prototip, kako bi naj aplikacija izgledala. Prototip nam je nato služil, kot navodilo za izgradnjo spletne aplikacije v nadaljevanju projekta.

<p align="center">
  <img alt="dashboard" width="800" src="https://github.com/mihaprah/projekt/assets/116807398/52fce584-1c14-4b32-81b3-1ceb4bac6364">
  <br/>
  Nadzorna plošča, kjer so vidni vsi "Tenanti", ki jih je uporabnik naredil.
</p>
<p align="center">
  <img alt="tenant-list-view" width="800" src="https://github.com/mihaprah/projekt/assets/116807398/b49dc710-3633-4574-a081-7016e831e872">
  <br/>
  <img alt="tenant-grid-view" width="800" src="https://github.com/mihaprah/projekt/assets/116807398/5fb5b106-8748-420f-929e-d29d10fe90d0">
  <br/>
  Pregled vseh kontaktov za enega "Tenanta". Pogled je nastavljen na <b>seznam</b> ali na <b>mrežo</b>.
</p>
<p align="center">
  <img alt="kontakt" width="800" src="https://github.com/mihaprah/projekt/assets/116807398/08aecbb0-0e32-4643-8590-816009e9fa72">
  <br/>
  Pregled posameznega kontakta.
</p>
<p align="center">
  <img alt="add-tenant" width="800" src="https://github.com/mihaprah/projekt/assets/116807398/5cbe449f-70b3-4968-aff2-fd731f7d9dc6">
  <br/>
  <img alt="add-tenant" width="800" src="https://github.com/mihaprah/projekt/assets/116807398/5ed4fdde-bf47-4ee3-adc7-0da143588f87">
  <br/>
  Dodajanje novega <b>"Tenanta"</b> in novega <b>kontakta</b> znotraj posameznega "Tenanta".
</p>
<p align="center">
  <img alt="kontakt" width="800" src="https://github.com/mihaprah/projekt/assets/116807398/695547c5-0494-4e8e-a45e-ef9e48f3c201">
  <br/>
  Pregled že v naprej shranjenih nastavitev za iskanje specifičnih kontaktov znotraj določenega "Tenanta".
</p>

### Testiranje
Za testiranje smo poskrbeli s sprotnim pisanjem unit testov. Teste smo napisali za sledeče entitete:
- Events
- PredefinedSearches
- Tenants
- Contacts

Unit teste smo pisali tudi za vse Service razrede:
- EventsServices
- PredefinedSearchesServices
- TenantsServices
- ContactServices

To nam je omogočalo, da smo z uporabo **GitHub Actions** naredili Workflow, ki nam je pognal vse teste, ki smo jih imeli v projektu z vsakim commitom na repozitorij. S tem smo lahko videli ali je nova koda, ki smo jo naložili na repozitorij pokvarila, katerega izmed testov in tako na napako tudi ustrezno reagirali s popravkom nove kode.

Ob pisanju REST vmesnika (Controller razredov) smo tudi testirali vse **API končne točke**, da smo preverili ali še vedno delujejo pravilno. Torej ali sprejmejo in vrnejo podatke, kot jih morajo in hkrati kako reagirajo ob prejemu napačnih podatkov. Za to smo si pomagali z orodjem [Swagger](https://swagger.io), ki nam je omogočalo pregled vseh API točk in podatkov s katerimi te delujejo. Swagger smo vključili v projekt, ta je dostopen na tem *[linku](https://projekt-test-environment.up.railway.app/swagger-ui/index.html)*.
<p align="center">
  <img alt="swagger-api" width="800" src="https://github.com/mihaprah/projekt/assets/116807398/a1983b6e-c604-4745-95c8-826ea1b2605f">
  <br/>
  Pregled APIja s orodjem Swagger
</p>

Orodje nam je omogočalo tudi pregled vseh shem, ki se uporabljajo v API klicih.

<p align="center">
  <img alt="swagger-sheme" width="800" src="https://github.com/mihaprah/projekt/assets/116807398/ae72425e-5976-4813-aaac-d4de057d352a">
  <br/>
  Pregled shem, ki jih uporablja API s orodjem Swagger
</p>

### Optimizacija kode
Za optimizacijo kode in pregled kode, smo uporabili orodje [SonarCloud](), ki nam je omogočalo, da smo v **GitHub Actions** Workflow vključili neprestan pregled kode. Repotizorij smo povezali s SonrCloudom in tako dobili dostop do nadzorne plošče projekta. Na njej smo lahko gledali kakša je koda glede na:
- Vzdrževanost
- Zanesljivost
- Varnost

Če smo opazili velika odstopanje, ali pa je padel **Quality Gate**, smo kodo nemudoma popravili po pravilih in s predlogi orodja SonarCloud. To nam je omogočalo, da se problemi s kodo niso sropnjevali, saj smo jih rešili pravočasno.
<p align="center">
  <img alt="sonarcloud-dashborad" width="800" src="https://github.com/mihaprah/projekt/assets/116807398/d98155bc-dbc0-4a8a-942b-a2df574b499a">
  <br/>
  Pregled nadzorne plošče v orodju SonarCloud
</p>

## 3. Navodila za namestitev 

### Testno lokalno okolje
Za namistitev aplikacije lokalno na vašem računalniku, smo naredili testno verzijo, ki je dostopna na [tej povezavi](https://github.com/Matija334/projekt_local/tree/main).

#### Koraki za zagon

###### 1. Kloniranje repozitorija

Najprej klonirajte repozitorij na vašo lokalno napravo.

###### 2. Pridobitev firebase serviceAccountKey.json datoteke za backend

Za konfiguracijo Firebase je potrebno pridobiti **serviceAccountKey.json** 

1. Pojdite na [Firebase Console](https://firebase.google.com).
2. Izberite svoj projekt.
3. V stranski vrstici kliknite na Project Settings (Nastavitve projekta).
4. Izberite zavihek Service accounts (Storitveni računi).
5. Izberite Java in kliknite na gumb Generate new private key (Ustvari nov zasebni ključ). To bo preneslo datoteko **serviceAccountKey.json** na vaš računalnik.

Ustvarjeno datoteko kopirajte v **projekt_local/backend/scm/src/main/resources**

###### 3. Konfiguracija docker compose

V datoteki **docker-compose.yml**, ki se nahaja v projekt_local/ uredite naslednjo vrstico
```bash
SPRING_DATA_MONGODB_URI=mongodb://mongodb:27017******Your database name goes here****** 
```
tukaj navedite poljubno ime za podatkovno bazo

###### 4. Zagon docker compose

Postavite se v mapo **projekt_local**, kjer se nahaja datoteka **docker-compose.yml** in izvedite ukaz 
```bash
docker-compose up --build
```

###### 5. Pridobitev firebase SDK za frontend

1. Pojdite na [Firebase Console](https://firebase.google.com).
2. Izberite svoj projekt.
3. V stranski vrstici kliknite na Project Settings (Nastavitve projekta).
4. Izberite zavihek General.

Generirane vrednosti prekopirajte v **projekt_local/frontend/scm/.env**

###### 6. Zagon frontenda

Postavite se v mapo projekt_local/frontend/scm in izvedite naslednje ukaze:
```bash
npm install
npm run build
npm run start
```

### Uporaba apliakcije
Aplikacije je dostopna na tej [povezavi](https://scm-frontend-seven.vercel.app/login)
