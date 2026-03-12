import java.io.*;
import java.util.*;

/* ==============================
   USER CLASS
============================== */
class User {
    String id;
    String name;
    int goalSteps;

    User(String id, String name, int goalSteps) {
        this.id = id;
        this.name = name;
        this.goalSteps = goalSteps;
    }

    public String toString() {
        return id + " - " + name + " | Goal Steps: " + goalSteps;
    }
}

/* ==============================
   ACTIVITY CLASS
============================== */
class Activity {
    String userId;
    String type;
    int steps;
    int calories;
    int duration;

    Activity(String userId, String type, int steps, int calories, int duration) {
        this.userId = userId;
        this.type = type;
        this.steps = steps;
        this.calories = calories;
        this.duration = duration;
    }

    public String toString() {
        return userId + " | " + type + " | Steps: " + steps +
               " | Calories: " + calories +
               " | Duration: " + duration + " mins";
    }
}

/* ==============================
   MAIN FITNESS SYSTEM
============================== */
public class FitnessTrackingSystem {

    static Scanner sc = new Scanner(System.in);

    /* ----- DSA Structures ----- */

    static ArrayList<User> users = new ArrayList<>();
    static ArrayList<Activity> activities = new ArrayList<>();

    static LinkedList<Activity> timeline = new LinkedList<>();

    static Stack<Activity> undoStack = new Stack<>();

    static Queue<Activity> activityQueue = new LinkedList<>();

    static Deque<Integer> sensorBuffer = new ArrayDeque<>();

    static HashMap<String, List<Activity>> userActivities = new HashMap<>();

    static PriorityQueue<Activity> leaderboard =
            new PriorityQueue<>((a, b) -> b.calories - a.calories);

    /* ==============================
       USER REGISTRATION
    ============================== */

    static void registerUser() {

        System.out.print("Enter User ID: ");
        String id = sc.next();

        System.out.print("Enter Name: ");
        String name = sc.next();

        System.out.print("Enter Daily Step Goal: ");
        int goal = sc.nextInt();

        users.add(new User(id, name, goal));

        System.out.println("User Registered Successfully.");
    }

    /* ==============================
       DISPLAY USERS
    ============================== */

    static void showUsers() {
        for (User u : users)
            System.out.println(u);
    }

    /* ==============================
       ADD ACTIVITY
    ============================== */

    static void addActivity() {

        System.out.print("User ID: ");
        String id = sc.next();

        System.out.print("Activity Type: ");
        String type = sc.next();

        System.out.print("Steps: ");
        int steps = sc.nextInt();

        System.out.print("Calories: ");
        int cal = sc.nextInt();

        System.out.print("Duration(min): ");
        int dur = sc.nextInt();

        Activity a = new Activity(id, type, steps, cal, dur);

        activities.add(a);
        timeline.add(a);
        undoStack.push(a);
        activityQueue.add(a);
        leaderboard.add(a);

        userActivities.computeIfAbsent(id, k -> new ArrayList<>()).add(a);

        System.out.println("Activity Recorded.");
    }

    /* ==============================
       DISPLAY ACTIVITIES
    ============================== */

    static void showActivities() {

        if (activities.isEmpty()) {
            System.out.println("No activities found.");
            return;
        }

        for (Activity a : activities)
            System.out.println(a);
    }

    /* ==============================
       UNDO LAST ACTIVITY (STACK)
    ============================== */

    static void undoActivity() {

        if (undoStack.isEmpty()) {
            System.out.println("Nothing to undo.");
            return;
        }

        Activity a = undoStack.pop();

        activities.remove(a);
        timeline.remove(a);

        System.out.println("Last activity removed.");
    }

    /* ==============================
       SENSOR BUFFER (DEQUE)
    ============================== */

    static void addSensorData() {

        System.out.print("Enter step sensor value: ");
        int value = sc.nextInt();

        sensorBuffer.addLast(value);

        if (sensorBuffer.size() > 5)
            sensorBuffer.removeFirst();

        System.out.println("Sensor data stored.");
    }

    static void showSensorBuffer() {
        System.out.println("Recent Sensor Data: " + sensorBuffer);
    }

    /* ==============================
       SEARCH USER ACTIVITIES
    ============================== */

    static void searchUser() {

        System.out.print("Enter User ID: ");
        String id = sc.next();

        List<Activity> list = userActivities.get(id);

        if (list == null) {
            System.out.println("No activities for this user.");
            return;
        }

        for (Activity a : list)
            System.out.println(a);
    }

    /* ==============================
       BINARY SEARCH (STEPS)
    ============================== */

    static void binarySearchSteps() {

        if (activities.isEmpty()) {
            System.out.println("No data available.");
            return;
        }

        activities.sort(Comparator.comparingInt(a -> a.steps));

        System.out.print("Enter step count to search: ");
        int key = sc.nextInt();

        int low = 0;
        int high = activities.size() - 1;

        while (low <= high) {

            int mid = (low + high) / 2;

            if (activities.get(mid).steps == key) {
                System.out.println("Found: " + activities.get(mid));
                return;
            }

            if (activities.get(mid).steps < key)
                low = mid + 1;
            else
                high = mid - 1;
        }

        System.out.println("Activity not found.");
    }

    /* ==============================
       LEADERBOARD (MAX HEAP)
    ============================== */

    static void showLeaderboard() {

        PriorityQueue<Activity> temp =
                new PriorityQueue<>(leaderboard);

        int rank = 1;

        System.out.println("Top Performers:");

        while (!temp.isEmpty() && rank <= 5) {

            System.out.println("Rank " + rank + " : " + temp.poll());
            rank++;
        }
    }

    /* ==============================
       GOAL CHECK
    ============================== */

    static void checkGoals() {

        for (User u : users) {

            int totalSteps = 0;

            List<Activity> list = userActivities.get(u.id);

            if (list != null)
                for (Activity a : list)
                    totalSteps += a.steps;

            if (totalSteps >= u.goalSteps)
                System.out.println(u.name + " achieved the goal!");
            else
                System.out.println(u.name + " progress: " + totalSteps);
        }
    }

    /* ==============================
       SAVE DATA (FILE)
    ============================== */

    static void saveData() {

        try {

            PrintWriter pw =
                    new PrintWriter(new FileWriter("fitness.txt"));

            for (Activity a : activities)
                pw.println(a.userId + "," + a.type + "," + a.steps +
                        "," + a.calories + "," + a.duration);

            pw.close();

            System.out.println("Data saved successfully.");

        } catch (Exception e) {

            System.out.println("Error saving file.");
        }
    }

    /* ==============================
       LOAD DATA
    ============================== */

    static void loadData() {

        try {

            BufferedReader br =
                    new BufferedReader(new FileReader("fitness.txt"));

            String line;

            while ((line = br.readLine()) != null) {

                String[] p = line.split(",");

                Activity a = new Activity(
                        p[0], p[1],
                        Integer.parseInt(p[2]),
                        Integer.parseInt(p[3]),
                        Integer.parseInt(p[4])
                );

                activities.add(a);
            }

            br.close();

        } catch (Exception e) {

            System.out.println("No previous data found.");
        }
    }

    /* ==============================
       MENU
    ============================== */

    static void menu() {

        while (true) {

            System.out.println("\n--- FITNESS TRACKING SYSTEM ---");
            System.out.println("1 Register User");
            System.out.println("2 Show Users");
            System.out.println("3 Add Activity");
            System.out.println("4 Show Activities");
            System.out.println("5 Search User Activities");
            System.out.println("6 Binary Search Steps");
            System.out.println("7 Leaderboard");
            System.out.println("8 Check Goals");
            System.out.println("9 Undo Activity");
            System.out.println("10 Add Sensor Data");
            System.out.println("11 Show Sensor Buffer");
            System.out.println("12 Save Data");
            System.out.println("13 Exit");

            int choice = sc.nextInt();

            switch (choice) {

                case 1: registerUser(); break;
                case 2: showUsers(); break;
                case 3: addActivity(); break;
                case 4: showActivities(); break;
                case 5: searchUser(); break;
                case 6: binarySearchSteps(); break;
                case 7: showLeaderboard(); break;
                case 8: checkGoals(); break;
                case 9: undoActivity(); break;
                case 10: addSensorData(); break;
                case 11: showSensorBuffer(); break;
                case 12: saveData(); break;
                case 13: System.exit(0);
            }
        }
    }

    /* ==============================
       MAIN METHOD
    ============================== */

    public static void main(String[] args) {

        loadData();

        menu();
    }
}