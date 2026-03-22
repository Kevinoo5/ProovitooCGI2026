## CGI suvepraktikale kandideerimise ülesanne - Kevin Noormets
### Nutikas Restorani Reserveerimissüsteem 

### Käivitamine

Rakendus on konteineriseeritud, mis tähendab, et selle käivitamiseks ei ole vaja kohalikku Java ega Node.js installatsiooni. Piisab Dockerist.

Ava terminal projekti juurkaustas (seal, kus asub docker-compose.yml) ja sisesta:
```
docker compose up --build
```

#### Rakenduse aadressid
**Front-end:** [http://localhost](http://localhost) (Port 80)

**Back-end:** [http://localhost:8080](http://localhost:8080)

#### Peatamine
Konteinerite sulgemiseks vajuta terminalis Ctrl + C või kasuta käsku:
```
docker compose down
```


### Front-endi arendamine
Front-end on loodud React raamistikuga ning peamiselt loodud kasutades AI abi, kuna ma ei olnud pikalt Reacti projekti kirjutanud
ja ei olnud kindel oma oskustes luua küllalt kiiresti selline disain, mis vastaks minu ootustele.

- Algselt lõin väga pooliku testlehe, aga siis hakkasin sellele lisama enda ideid.
- Läksin liiale oma paari ideega ning lõin süsteemi, kus said külastajad kalendrivaatega broneerida, aga see ei olnud otseselt ülesande kirjelduses, mispärast ma tegin kergema disaini.

---

### Back-endi arendamine
Back-end kasutab Spring Booti ning on jagatud erinevatesse kihtidesse (Service, Controller, Model, Repository jne.).
- Ma ei olnud kunagi varem Spring Boot raamistikuga kokku puutunud, mis tegi selle õppimise kõige raskemaks osaks back-endi kirjutamise koha pealt.

- Aga olin varem kokku puutunud kihilise disainiga, mis aitas mul välja mõelda kõike, mis mul võib vaja minna selle projekti puhul.

- Back-endi poole pealt kasutasin AI abi projekti seadistamisel, kihtide välja mõtlemisel ja võimalusel koodi parendamisel või suuremate koodijuppide kiirelt kirjutamisel.
Konfiguratsioonifailid on näiteks mõlemad tehtud täielikult AI abiga.

- Back-endi ei soovinud ma väga palju AI abiga kirjutada kuna tahtsin õppida Spring Booti raamistiku kohta. 

---

### Kokkuvõte
Projektile kulus umbes 30-40 tundi, kuna pidin õppima uut raamistikku ning ka enda vigade pärast, kus hakkasin liiga palju looma enda loomingut selle asemel, et järgida ülesande püstitust.
    
See oli mulle väga huvitav projekt ja olen päris õnnelik sellega, mis sai loodud.
Ma õppisin selle projekti käigus väga palju uusi asju ning ei saa kurta selle üle, mis valmis sai tehtud.
