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

public class FCFSMain {
    static Comparator<Process> compareArrival = Comparator.comparingInt(p -> p.arrivalTime);
    static Comparator<Process> compareID = Comparator.comparingInt(p -> p.pid);

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        int n;
        Process[] p = new Process[100];
        float avgTurnaroundTime;
        float avgWaitingTime;
        float avgResponseTime;
        float cpuUtilization;
        int totalTurnaroundTime = 0;
        int totalWaitingTime = 0;
        int totalResponseTime = 0;
        int totalIdleTime = 0;
        float throughput;

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
            System.out.println();
        }

        Arrays.sort(p, 0, n, compareArrival);

        for (int i = 0; i < n; i++) {
            p[i].startTime = (i == 0) ? p[i].arrivalTime : Math.max(p[i - 1].completionTime, p[i].arrivalTime);
            p[i].completionTime = p[i].startTime + p[i].burstTime;
            p[i].turnaroundTime = p[i].completionTime - p[i].arrivalTime;
            p[i].waitingTime = p[i].turnaroundTime - p[i].burstTime;
            p[i].responseTime = p[i].startTime - p[i].arrivalTime;

            totalTurnaroundTime += p[i].turnaroundTime;
            totalWaitingTime += p[i].waitingTime;
            totalResponseTime += p[i].responseTime;
            totalIdleTime += (i == 0) ? (p[i].arrivalTime) : (p[i].startTime - p[i - 1].completionTime);
        }

        avgTurnaroundTime = (float) totalTurnaroundTime / n;
        avgWaitingTime = (float) totalWaitingTime / n;
        avgResponseTime = (float) totalResponseTime / n;
        cpuUtilization = ((p[n - 1].completionTime - totalIdleTime) / (float) p[n - 1].completionTime) * 100;
        throughput = (float) n / (p[n - 1].completionTime - p[0].arrivalTime);

        Arrays.sort(p, 0, n, compareID);

        System.out.println();
        System.out.println("P\tAT\tBT\tCT\tTAT\tWT\t\n");

        for (int i = 0; i < n; i++) {
            System.out.println(p[i].pid + "\t" + p[i].arrivalTime + "\t" + p[i].burstTime + "\t" + p[i].completionTime + "\t" + p[i].turnaroundTime + "\t" + p[i].waitingTime + "\t" + "\n");
        }
        
        System.out.println("\nGrafik Waiting Time FCFS:");
        for (int i = 0; i < n; i++) {
            System.out.print("P" + p[i].pid + ": ");
            for (int j = 0; j < p[i].waitingTime; j++) {
                System.out.print("*");
            }
            System.out.println();
        }

        System.out.println("\nGrafik Turnaround Time FCFS:");
        for (int i = 0; i < n; i++) {
            System.out.print("P" + p[i].pid + ": ");
            for (int j = 0; j < p[i].turnaroundTime; j++) {
                System.out.print("*");
            }
            System.out.println();
        }
    }
}
