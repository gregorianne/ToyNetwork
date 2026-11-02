Concepte folosite - NiceBook

==================== LAB1 & LAB2

SOLID

1. Single Responsibility: o clasă = o responsabilitate.
User, Friendship: doar datele unui utilizator / ale unei prietenii.
UserRepository, FriendshipRepository (orice persistenta ar fi): se ocupa doar de felul specific de citire/scriere al datelor in memorie/fisier/db

2. Open/Closed = codul se extinde, nu se modifică.
O entitate nouă (ex. Message) înseamnă clase noi (Message, MessageRepository, MessageService, MessageValidator), fără să modificăm cele existente.

3. Liskov Substitution: orice copil poate fi folosit în locul părintelui.
User și Friendship extind Entity<ID> și respectă contractul ei (au id, getId/setId), deci pot fi folosite oriunde se utilizeaza un Entity.
Orice implementare de Validator<User> poate înlocui UserValidator în UserService, iar service-ul funcționează la fel.

4. Interface Segregation: nu obligăm o clasă să implementeze ce nu folosește.
Fiecare mostenitor nu are functii pe care nu le foloseste.

5. Dependency Inversion: ne bazăm pe abstracții, nu pe clase concrete.
UserService și FriendshipService lucrează cu Validator<T> (interfața), nu cu UserValidator direct. Implementarea concretă e aleasă în Main.
Service foloseste obiecte generice cu interfata RepoInterface, nu repo-uri specifice (UserFileRepository, etc)

ȘABLOANE

- Strategy Pattern: Validator<T> este strategia, UserValidator și FriendshipValidator sunt variantele.
Service-ul primește strategia prin constructor și apelează validator.validate(...), fără să știe cum se validează. Schimbarea regulilor înseamnă schimbarea unei singure linii în Main.

- Dependency Injection: service-urile nu își creează dependențele cu new, le primesc prin constructor. Există un singur (de ex) UserRepository și un singur FriendshipRepository, create în Main și date tuturor service-urilor. Așa toate văd aceleași date (un user adăugat în UserService e vizibil imediat în FriendshipService).

- Repository: separă accesul la date de logica aplicației. Dacă se schimbă formatul de stocare, se modifică doar repository-ul.

- Arhitectură pe straturi: UI -> Service -> Repository -> Domain. Dependențele merg într-o singură direcție, nu există dependențe circulare. Repository-urile nu se cunosc între ele, iar coordonarea (ex. ștergerea unui user șterge și prieteniile lui) se face în service.

==================== LAB3

- Factory: Fabrica de Repo este singura clasa care stie in ce fel se face persistenta datelor si decide bazat pe datele din config.properties. 
           Putem adauga un numar nelimitat de tipuri in enumeratia noastra, fara sa modificam nimic in utilizarea lor in Service/main
- am mutat validarea in repo:
        decizie: forma unei entitati se valideaza in repository, in InMemoryRepository, la save si la update. Validatorul este primit prin constructor si pasat de RepositoryFactory.
        (regulile care depind de mai multe repo-uri raman in service -  de ex sa nu fie o prietenie noua intre acelasi utilizator si el insusi)
        justificare: enuntul cere "datele incarcate din fisier trec prin validator", iar incarcarea se face in AbstractFileRepository, inainte sa existe vreun service. Daca am valida si in service, am valida de doua ori aceleasi date.
- cerinta spune ca UI prinde erorile si le afiseaza, dar a trebuit sa aruncam unele exceptii incepand cu repo
        justificare: Incarcarea (loadData) ruleaza in constructorul repository-ului, adica inainte sa existe interactiune cu UI-ul.
                     Pentru exceptia care arata linia corupta pe care o ignoram la citirea din fisier, a trebuit sa putem continua rularea cu celelalte linii corecte.
                     Repository-ul este singurul loc care stie numarul liniei in momentul citirii, deci este singurul in masura sa dea un mesaj util.
                     Enuntul accepta explicit aceasta varianta ("linia este raportata (numarul liniei si motivul) si ignorata").
        sugestie: scrierea acestora intr-un fisier LOG care nu se afiseaza si in consola

==================== LAB4:
=========== sapt 6:

- nu mai sunt for-loops decat la manevrare efectiva de indici folositi in algoritmi elementari pe grafuri / bactracking si recursie in Netwrok Service
- Am adaugat UserCriteria (functii pt predicatele lui findAll din repo) si am testat scurt in main (inclusiv concatenare cu and)
- Validator este acum FunctionalInterface, am putut comasa mai multe validatorare cu andThen (default din Validator)
- Am pastrat AbstractFileRepository, cu UserFile/FriendshipFileRepository care implementeaza doar conversia linie <-> entitate
    justificare: cod scurt si usor de citit, cu nume si documentatie proprie, tratarea erorilor / exception chaining este mai clar
                 codul fiecarei subclase este unic (SRP)
                 codul pentru Function e demonstrat in RepositoryFactory
- chiar daca se specifica adunarea rezultatelor cu collector, am urmat recomandarea din IDE de a transforma direct in ".toList" simplu unde era posibil.
- Am inlocuit BufferedWriter cu Path
- Sortările folosesc Comparator.comparing am implementat in user service