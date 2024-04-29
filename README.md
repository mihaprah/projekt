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

### Zasnova podatkovne strukture
<p align="center">
  <img width="902" alt="SCM Data structure" src="https://github.com/mihaprah/projekt/assets/116807398/11934d1c-2c87-4a70-a180-aa62b83f3224">
</p>

### Wireframe aplikacije (prototip izgleda)
S pomočjo orodja [Figma](https://www.figma.com), ki omogoča enostavno in hitro izdelavo vizualnih prototipov, smo izdelali prototip, kako bi naj aplikacija izgledala. Prototip nam je nato služil, kot navodilo za izgradnjo spletne aplikacije v nadaljevanju projekta.

## Navodila za namestitev
