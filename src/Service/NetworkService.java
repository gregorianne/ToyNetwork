package Service;

import Domain.Friendship;
import Domain.User;
import Repositories.FriendshipRepository;
import Repositories.UserRepository;

import java.util.ArrayList;
import java.util.List;

public class NetworkService {
    private final UserRepository userRepo;
    private final FriendshipRepository friendshipRepo;

    private List<User> users;       // poziția în listă = numărul nodului
    private boolean[][] adj;       // adj[i][j] = true daca i-j sunt prieteno
    private int[] comp;           // comp[i] = nr comunitate nodu i
    private int nrComp;

    public NetworkService(UserRepository userRepo, FriendshipRepository friendshipRepo) {
        this.userRepo = userRepo;
        this.friendshipRepo = friendshipRepo;
        build();
    }

    //completeaza matricea de adiacenta
    private void build() {
        users = new ArrayList<>(userRepo.getAll());
        int n = users.size();
        adj = new boolean[n][n];
        comp = new int[n];                 // 0 = nevizitat
        nrComp = 0;

        for (Friendship f : friendshipRepo.getAll()) {
            int a = position(f.getId_user1());
            int b = position(f.getId_user2());
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

    // poziția userului cu id-ul dat în listă (-1 dacă nu există)
    private int position(int id) {
        for (int i = 0; i < users.size(); i++) {
            if (users.get(i).getId() == id) return i;
        }
        return -1;
    }

    private void dfs(int node, int number) {
        comp[node] = number;
        for (int next = 0; next < users.size(); next++) {
            if (adj[node][next] && comp[next] == 0) {
                dfs(next, number);
            }
        }
    }

    public int numberOfCommunities() {
        build();
        return nrComp;
    }

    // cea mai mare comunitate + afisare membrii
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

    // DFS cu bactracking
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