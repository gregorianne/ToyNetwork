Concepte folosite - NiceBook

SOLID

1. Single Responsibility: o clasă = o responsabilitate.
User, Friendship: doar datele unui utilizator / ale unei prietenii.
UserRepository, FriendshipRepository: doar păstrează datele în memorie și le citesc/scriu în CSV, fără verificări, etc

2. Open/Closed = codul se extinde, nu se modifică.
O entitate nouă (ex. Message) înseamnă clase noi (Message, MessageRepository, MessageService, MessageValidator), fără să modificăm cele existente.

3. Liskov Substitution: orice copil poate fi folosit în locul părintelui.
User și Friendship extind Entity și respectă contractul ei (au id, getId/setId), deci pot fi folosite oriunde se așteaptă un Entity.
Orice implementare de Validator<User> poate înlocui UserValidator în UserService, iar service-ul funcționează la fel.

4. Interface Segregation: nu obligăm o clasă să implementeze ce nu folosește.
Fiecare mostenitor nu are functii pe care nu le foloseste.

5. Dependency Inversion: ne bazăm pe abstracții, nu pe clase concrete.
UserService și FriendshipService lucrează cu Validator<T> (interfața), nu cu UserValidator direct. Implementarea concretă e aleasă în Main.

ȘABLOANE

- Strategy Pattern: Validator<T> este strategia, UserValidator și FriendshipValidator sunt variantele.
Service-ul primește strategia prin constructor și apelează validator.validate(...), fără să știe cum se validează. Schimbarea regulilor înseamnă schimbarea unei singure linii în Main.

- Dependency Injection: service-urile nu își creează dependențele cu new, le primesc prin constructor. Există un singur UserRepository și un singur FriendshipRepository, create în Main și date tuturor service-urilor. Așa toate văd aceleași date (un user adăugat în UserService e vizibil imediat în FriendshipService).

- Repository: separă accesul la date (fișier CSV) de logica aplicației. Dacă se schimbă formatul de stocare, se modifică doar repository-ul.

- Arhitectură pe straturi: UI -> Service -> Repository -> Domain. Dependențele merg într-o singură direcție, nu există dependențe circulare. Repository-urile nu se cunosc între ele, iar coordonarea (ex. ștergerea unui user șterge și prieteniile lui) se face în service.

