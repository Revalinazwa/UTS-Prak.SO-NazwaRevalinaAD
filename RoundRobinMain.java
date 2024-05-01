import java.util.*;

class Process {
    int pid;
    int arrivalTime;
    int burstTime;
    int startTime;
    int completionTime;
    int turnaroundTime;
    int waitingTime;
    int responseTime;
}

public class RoundRobinMain {
    static Comparator<Process> compare1 = Comparator.comparingInt(p -> p.arrivalTime);
    static Comparator<Process> compare2 = Comparator.comparingInt(p -> p.pid);

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        int n;
        int tq;
        Process[] p = new Process[100];
        float avgTurnaroundTime;
        float avgWaitingTime;
        float avgResponseTime;
        float cpuUtilisation;
        int totalTurnaroundTime = 0;
        int totalWaitingTime = 0;
        int totalResponseTime = 0;
        int totalIdleTime = 0;
        float throughput;
        int[] burstRemaining = new int[100];
        int idx;

        System.out.print("Masukan jumlah proses: ");
        n = scanner.nextInt();
        System.out.print("Masukan quantum waktu: ");
        tq = scanner.nextInt();

        for (int i = 0; i < n; i++) {
            p[i] = new Process();
            System.out.println("Proses " + (i + 1));
            System.out.print("Arrival time: ");
            p[i].arrivalTime = scanner.nextInt();
            System.out.print("Burst time: ");
            p[i].burstTime = scanner.nextInt();
            burstRemaining[i] = p[i].burstTime;
            p[i].pid = i + 1;
            System.out.println();
        }

        Arrays.sort(p, 0, n, compare1);

        Queue<Integer> q = new LinkedList<>();
        int currentTime = 0;
        q.add(0);
        int completed = 0;
        int[] mark = new int[100];
        Arrays.fill(mark, 0);
        mark[0] = 1;

        while (completed != n) {
            idx = q.remove();

            if (burstRemaining[idx] == p[idx].burstTime) {
                p[idx].startTime = Math.max(currentTime, p[idx].arrivalTime);
                totalIdleTime += p[idx].startTime - currentTime;
                currentTime = p[idx].startTime;
            }

            if (burstRemaining[idx] - tq > 0) {
                burstRemaining[idx] -= tq;
                currentTime += tq;
            } else {
                currentTime += burstRemaining[idx];
                burstRemaining[idx] = 0;
                completed++;

                p[idx].completionTime = currentTime;
                p[idx].turnaroundTime = p[idx].completionTime - p[idx].arrivalTime;
                p[idx].waitingTime = p[idx].turnaroundTime - p[idx].burstTime;
                p[idx].responseTime = p[idx].startTime - p[idx].arrivalTime;

                totalTurnaroundTime += p[idx].turnaroundTime;
                totalWaitingTime += p[idx].waitingTime;
                totalResponseTime += p[idx].responseTime;
            }

            for (int i = 1; i < n; i++) {
                if (burstRemaining[i] > 0 && p[i].arrivalTime <= currentTime && mark[i] == 0) {
                    q.add(i);
                    mark[i] = 1;
                }
            }
            if (burstRemaining[idx] > 0) {
                q.add(idx);
            }

            if (q.isEmpty()) {
                for (int i = 1; i < n; i++) {
                    if (burstRemaining[i] > 0) {
                        q.add(i);
                        mark[i] = 1;
                        break;
                    }
                }
            }
        }

        avgTurnaroundTime = (float) totalTurnaroundTime / n;
        avgWaitingTime = (float) totalWaitingTime / n;
        avgResponseTime = (float) totalResponseTime / n;
        cpuUtilisation = ((p[n - 1].completionTime - totalIdleTime) / (float) p[n - 1].completionTime) * 100;
        throughput = (float) n / (p[n - 1].completionTime - p[0].arrivalTime);

        Arrays.sort(p, 0, n, compare2);

        System.out.println();
        System.out.println("P\tAT\tBT\tCT\tTAT\tWT\t\n");

        for (int i = 0; i < n; i++) {
            System.out.println(p[i].pid + "\t" + p[i].arrivalTime + "\t" + p[i].burstTime + "\t" + p[i].completionTime + "\t" + p[i].turnaroundTime + "\t" + p[i].waitingTime + "\t" + "\t" + "\n");
        }

        System.out.println("\nGrafik Waiting Time Round Robin:");
        for (int i = 0; i < n; i++) {
            System.out.print("P" + p[i].pid + ": ");
            for (int j = 0; j < p[i].waitingTime; j++) {
                System.out.print("*");
            }
            System.out.println();
        }

        System.out.println("\nGrafik Turnaround Time Round Robin:");
        for (int i = 0; i < n; i++) {
            System.out.print("P" + p[i].pid + ": ");
            for (int j = 0; j < p[i].turnaroundTime; j++) {
                System.out.print("*");
            }
            System.out.println();
        }
    }
}
