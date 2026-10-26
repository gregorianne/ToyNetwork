package Service;

import Domain.Friendship;
import Domain.Tuple;
import Domain.User;
import Repositories.InFile.FriendshipFileRepository;
import Repositories.InFile.UserFileRepository;
import Repositories.RepoInterface;

import java.util.ArrayList;
import java.util.List;

/**
 * Analizează rețeaua de socializare. Rețeaua este un graf neorientat:
 * nodurile sunt userii, iar muchiile sunt prieteniile.
 * O comunitate este o componentă conexă a acestui graf
 * Graful nu este salvat nicăieri: este reconstruit din
 * repository-uri din memorie, deci reflectă mereu starea curentă.
 */
public class NetworkService {
    /** Repository-ul userilor. */
    private final RepoInterface<Integer, User> userRepo;
    /** Repository-ul prieteniilor. */
    private final RepoInterface<Tuple<Integer, Integer>, Friendship> friendshipRepo;

    /** Userii rețelei; poziția în listă este numărul nodului în graf. */
    private List<User> users;
    /** Matricea de adiacență: adj[i][j] este true dacă userii de pe pozițiile i și j sunt prieteni. */
    private boolean[][] adj;
    /** comp[i] este numărul comunității (1, 2, ...) din care face parte nodul i; 0 înseamnă nevizitat. */
    private int[] comp;
    /** Numărul de comunități găsite. */
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
     * Reconstruiește graful din starea curentă a repository-urilor și numerotează comunitățile.
     * pune userii într-o listă (poziția devine numărul nodului);
     * pentru fiecare prietenie, marchează muchia în matricea de adiacență,
     * în ambele sensuri; prieteniile cu un user inexistent se ignoră;
     * pornește un DFS din fiecare nod nevizitat;
     * fiecare pornire înseamnă o comunitate nouă.
     */
    private void build() {
        users = new ArrayList<>();
        for (User u : userRepo.findAll()) {
            users.add(u);
        }
        int n = users.size();

        adj = new boolean[n][n]; //rescriem
        comp = new int[n];
        nrComp = 0;

        for (Friendship f : friendshipRepo.findAll()) {
            int a = position(f.getIdUser1());
            int b = position(f.getIdUser2());
            if (a != -1 && b != -1) {
                adj[a][b] = true;
                adj[b][a] = true;
            }
        }

        //componenta comnexa careia apartine -- ruleaza dfs din nodurile nevizitate inca
        for (int i = 0; i < n; i++) {
            if (comp[i] == 0) {
                nrComp++;
                dfs(i, nrComp);
            }
        }
    }

    /**
     * Găsește poziția unui user în lista de noduri.
     *
     * @param id id-ul userului căutat
     * @return poziția în listă sau -1 dacă userul nu există
     */
    private int position(int id) {
        for (int i = 0; i < users.size(); i++) {
            if (users.get(i).getId() == id) return i;
        }
        return -1;
    }

    /**
     * Parcurgere în adâncime (DFS) care marchează toți userii accesibili din {@code node} cu numărul comunității. Un nod este vizitat dacă {@code comp[nod] != 0}.
     *
     * @param node   nodul de pornire
     * @param number numărul comunității atribuit nodurilor parcurse
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
     * Calculează numărul de comunități din rețea (numărul de componente conexe).
     *
     * @return nrComp = numărul de comunități
     */
    public int numberOfCommunities() {
        build();
        return nrComp;
    }

    /**
     * Determină cea mai sociabilă comunitate, adică componenta conexă care conține cel mai lung drum.
     * Pentru fiecare comunitate se pornește căutarea celui mai lung drum din fiecare membru al ei și se reține maximul.
     * Comunitatea al cărei maxim este cel mai mare câștigă. La egalitate rămâne prima găsită.
     * @return membrii comunității câștigătoare; listă goală dacă nu există useri
     */
    public List<User> mostSociableCommunity() {
        build();
        int bestComp = 0;
        int bestLength = -1;

        for (int c = 1; c <= nrComp; c++) {
            int length = 0;
            for (int i = 0; i < users.size(); i++) {
                if (comp[i] == c) {
                    boolean[] visited = new boolean[users.size()];
                    length = Math.max(length, longestPath(i, visited));
                }
            }
            if (length > bestLength) {
                bestLength = length;
                bestComp = c;
            }
        }

        List<User> result = new ArrayList<>();
        for (int i = 0; i < users.size(); i++) {
            if (comp[i] == bestComp) result.add(users.get(i));
        }
        return result;
    }

    /**
     * Calculează lungimea celui mai lung drum elementar (fără noduri repetate)
     * care pornește din {@code node}. Lungimea se măsoară în număr de muchii.
     * Algoritm: DFS cu backtracking. Nodul curent se marchează vizitat,
     * se încearcă toți vecinii nevizitați (lungime = 1 + drumul din vecin),
     * apoi marcajul se anulează, pentru ca nodul să poată apărea
     * în alte drumuri.
     * @param node    nodul de pornire
     * @param visited nodurile aflate pe drumul curent
     * @return lungimea celui mai lung drum care pornește din {@code node}
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