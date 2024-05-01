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

public class SRTFMain {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int n;
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
        int[] isCompleted = new int[100];
        Arrays.fill(isCompleted, 0);

        System.out.print("Masukan jumlah proses: ");
        n = scanner.nextInt();

        for (int i = 0; i < n; i++) {
            p[i] = new Process();
            System.out.println("Proses " + (i + 1));
            System.out.print("Arrival time: ");
            p[i].arrivalTime = scanner.nextInt();
            System.out.print("Burst time: ");
            p[i].burstTime = scanner.nextInt();
            p[i].pid = i + 1;
            burstRemaining[i] = p[i].burstTime;
            System.out.println();
        }

        int current_time = 0;
        int completed = 0;
        int prev = 0;

        while (completed != n) {
            int idx = -1;
            int mn = 10000000;
            for (int i = 0; i < n; i++) {
                if (p[i].arrivalTime <= current_time && isCompleted[i] == 0) {
                    if (burstRemaining[i] < mn) {
                        mn = burstRemaining[i];
                        idx = i;
                    }
                    if (burstRemaining[i] == mn) {
                        if (p[i].arrivalTime < p[idx].arrivalTime) {
                            mn = burstRemaining[i];
                            idx = i;
                        }
                    }
                }
            }

            if (idx != -1) {
                if (burstRemaining[idx] == p[idx].burstTime) {
                    p[idx].startTime = current_time;
                    totalIdleTime += p[idx].startTime - prev;
                }
                burstRemaining[idx] -= 1;
                current_time++;
                prev = current_time;

                if (burstRemaining[idx] == 0) {
                    p[idx].completionTime = current_time;
                    p[idx].turnaroundTime = p[idx].completionTime - p[idx].arrivalTime;
                    p[idx].waitingTime = p[idx].turnaroundTime - p[idx].burstTime;
                    p[idx].responseTime = p[idx].startTime - p[idx].arrivalTime;

                    totalTurnaroundTime += p[idx].turnaroundTime;
                    totalWaitingTime += p[idx].waitingTime;
                    totalResponseTime += p[idx].responseTime;

                    isCompleted[idx] = 1;
                    completed++;
                }
            } else {
                current_time++;
            }
        }

        int minArrivalTime = 10000000;
        int maxCompletionTime = -1;
        for (int i = 0; i < n; i++) {
            minArrivalTime = Math.min(minArrivalTime, p[i].arrivalTime);
            maxCompletionTime = Math.max(maxCompletionTime, p[i].completionTime);
        }

        avgTurnaroundTime = (float) totalTurnaroundTime / n;
        avgWaitingTime = (float) totalWaitingTime / n;
        avgResponseTime = (float) totalResponseTime / n;
        cpuUtilisation = ((maxCompletionTime - totalIdleTime) / (float) maxCompletionTime) * 100;
        throughput = (float) n / (maxCompletionTime - minArrivalTime);

        System.out.println("\nP\tAT\tBT\tCT\tTAT\tWT\t\n");

        for (int i = 0; i < n; i++) {
            System.out.println(p[i].pid + "\t" + p[i].arrivalTime + "\t" + p[i].burstTime + "\t" + p[i].completionTime + "\t" + p[i].turnaroundTime + "\t" + p[i].waitingTime + "\t\n");
        }

        System.out.println("\nGrafik Waiting Time SRTF:");
        for (int i = 0; i < n; i++) {
            System.out.print("P" + p[i].pid + ": ");
            for (int j = 0; j < p[i].waitingTime; j++) {
                System.out.print("*");
            }
            System.out.println();
        }

        System.out.println("\nGrafik Turnaround Time SRTF:");
        for (int i = 0; i < n; i++) {
            System.out.print("P" + p[i].pid + ": ");
            for (int j = 0; j < p[i].turnaroundTime; j++) {
                System.out.print("*");
            }
            System.out.println();
        }
    }
}
