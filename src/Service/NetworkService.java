package Service;

import Domain.Friendship;
import Domain.Tuple;
import Domain.User;
import Repositories.InFile.FriendshipFileRepository;
import Repositories.InFile.UserFileRepository;
import Repositories.RepoInterface;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * Analizeaza reteaua de socializare. Reteaua este un graf neorientat:
 * nodurile sunt userii, iar muchiile sunt prieteniile.
 * O comunitate este o componenta conexa a acestui graf
 * Graful nu este salvat nicaieri: este reconstruit din
 * repository-uri din memorie, deci reflecta mereu starea curenta.
 */
public class NetworkService {
    /** Repository-ul userilor. */
    private final RepoInterface<Integer, User> userRepo;
    /** Repository-ul prieteniilor. */
    private final RepoInterface<Tuple<Integer, Integer>, Friendship> friendshipRepo;

    /** Userii retelei; pozitia în lista este numarul nodului în graf. */
    private List<User> users;
    /** Matricea de adiacenta: adj[i][j] este true daca userii de pe pozitiile i si j sunt prieteni. */
    private boolean[][] adj;
    /** comp[i] este numarul comunitatii (1, 2, ...) din care face parte nodul i; 0 înseamna nevizitat. */
    private int[] comp;
    /** Numarul de comunitati gasite. */
    private int nrComp;

    /**
     * @param userRepo       repository-ul userilor
     * @param friendshipRepo repository-ul prieteniilor
     */
    public NetworkService(RepoInterface<Integer, User> userRepo, RepoInterface<Tuple<Integer, Integer>, Friendship> friendshipRepo) {
        this.userRepo = userRepo;
        this.friendshipRepo = friendshipRepo;
    }

    //completeaza matricea de adiacenta
    /**
     * Reconstruieste graful din starea curenta a repository-urilor si numeroteaza comunitatile.
     * pune userii într-o lista (pozitia devine numarul nodului);
     * pentru fiecare prietenie, marcheaza muchia în matricea de adiacenta,
     * în ambele sensuri; prieteniile cu un user inexistent se ignora;
     * porneste un DFS din fiecare nod nevizitat;
     * fiecare pornire înseamna o comunitate noua.
     */
    private void build() {
        users = new ArrayList<>();
        users.addAll(userRepo.findAll());

        int n = users.size();

        adj = new boolean[n][n]; //rescriem
        comp = new int[n];
        nrComp = 0;

        friendshipRepo.findAll().forEach(f -> {
            int a = position(f.getIdUser1());
            int b = position(f.getIdUser2());
            if (a != -1 && b != -1) {
                adj[a][b] = true;
                adj[b][a] = true;
            }
        });

        //componenta comnexa careia apartine -- ruleaza dfs din nodurile nevizitate inca
        //dupa stream, un pic necitibil dar nu ceva de speriat
        IntStream.range(0, n)
                .filter(i -> comp[i] == 0)
                .forEach(i -> {
                    nrComp++;
                    dfs(i, nrComp);
        });
    }

    /**
     * Gaseste pozitia unui user în lista de noduri.
     *
     * @param id id-ul userului cautat
     * @return pozitia în lista sau -1 daca userul nu exista
     */
    private int position(int id) {
        return IntStream.range(0, users.size())
                .filter(i -> users.get(i).getId() == id)
                .findFirst()
                .orElse(-1);
    }

    /**
     * Parcurgere în adancime (DFS) care marcheaza toti userii accesibili din {@code node} cu numarul comunitatii. Un nod este vizitat daca {@code comp[nod] != 0}.
     * Pastrarea lui for pastreaza citirea rapida a logicii
     * @param node   nodul de pornire
     * @param number numarul comunitatii atribuit nodurilor parcurse
     */
    private void dfs(int node, int number) {
        comp[node] = number;
        for (int next = 0; next < users.size(); next++) {
            if (adj[node][next] && comp[next] == 0) {
                dfs(next, number);
            }
        }
    }

    /**
     * Calculeaza numarul de comunitati din retea (numarul de componente conexe).
     *
     * @return nrComp = numarul de comunitati
     */
    public int numberOfCommunities() {
        build();
        return nrComp;
    }

    /**
     * Pentru membrii comunitatii c, calculeaza lungimea maxima a legaturilor (lantul de lungime maxima din componenta conexa)
     * @param c componenta specifica
     * @return lungimea maxima a lantului gasit
     */
    private int lenght_community(int c){
        int length = 0;
        for (int i = 0; i < users.size(); i++) {
            if (comp[i] == c) {
                boolean[] visited = new boolean[users.size()];
                length = Math.max(length, longestPath(i, visited));
            }
        }
        return length;
    }

    /**
     * Returneaza o lista cu utilizatorii comunitaii c
     * @param c codul comunitatii a caror useri vrem sa aflam
     * @return o lista cu utilizatorii gasiti
     */
    private List<User> useriiUneiComunitati(int c){
        return IntStream.range(0, users.size())
                .filter(i->comp[i]==c)
                .mapToObj(users::get)
                .toList();
    }

    /**
     * Determina cea mai sociabila comunitate, adica componenta conexa care contine cel mai lung drum.
     * Pentru fiecare comunitate se porneste cautarea celui mai lung drum din fiecare membru al ei si se retine maximul.
     * Comunitatea al carei maxim este cel mai mare castiga. La egalitate ramane prima gasita.
     * @return membrii comunitatii castigatoare; lista goala daca nu exista useri
     */
    public List<User> mostSociableCommunity() {
        build();

        return IntStream.rangeClosed(1,nrComp)
                .boxed()
                .max(Comparator.comparingInt(this::lenght_community)) //cel mai mare dupa lenght_community, cumva order by
                .map(this::useriiUneiComunitati) //toti din comunitatea respectiva cu max, nu flatmap?
                .orElse(List.of());
    }

    /**
     * Calculeaza lungimea celui mai lung drum elementar (fara noduri repetate)
     * care porneste din {@code node}. Lungimea se masoara în numar de muchii.
     * Algoritm: DFS cu backtracking. Nodul curent se marcheaza vizitat,
     * se încearca toti vecinii nevizitati (lungime = 1 + drumul din vecin),
     * apoi marcajul se anuleaza, pentru ca nodul sa poata aparea
     * în alte drumuri.
     *
     * am pastrat for-loop pentru ca facem backtracking / recursivitate si e mai usor de urmarit.
     *
     * @param node    nodul de pornire
     * @param visited nodurile aflate pe drumul curent
     * @return lungimea celui mai lung drum care porneste din {@code node}
     */
    private int longestPath(int node, boolean[] visited) {
        visited[node] = true;
        int best = 0;
        for (int next = 0; next < users.size(); next++) {
            if (adj[node][next] && !visited[next]) {
                best = Math.max(best, 1 + longestPath(next, visited));
            }
        }
        visited[node] = false;     // îl eliberezi pentru alte drumuri
        return best;
    }
}