package NestedLoops.No;

import java.util.*;

public class OnlineExaminationSystem {

    static class User {
        private String username;
        private String password;
        private String profile;

        public User(String username, String password, String profile) {
            this.username = username;
            this.password = password;
            this.profile = profile;
        }

        public String getUsername() {
            return username;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }

        public String getProfile() {
            return profile;
        }

        public void setProfile(String profile) {
            this.profile = profile;
        }
    }

    static class Question {
        private String questionText;
        private String[] options;
        private int correctAnswer;

        public Question(String questionText, String[] options, int correctAnswer) {
            this.questionText = questionText;
            this.options = options;
            this.correctAnswer = correctAnswer;
        }

        public String getQuestionText() {
            return questionText;
        }

        public String[] getOptions() {
            return options;
        }

        public int getCorrectAnswer() {
            return correctAnswer;
        }
    }

    static class ExamSystem {
        private final Map<String, User> users = new HashMap<>();
        private final List<Question> questions = new ArrayList<>();
        private User currentUser;
        private final Scanner scanner = new Scanner(System.in);

        public ExamSystem() {
            users.put("user1", new User("user1", "password123", "Student Profile"));
            setupQuestions();
        }

        public void start() {
            System.out.println("Welcome to the Online Examination System!");
            while (true) {
                System.out.println("\n1. Login");
                System.out.println("2. Exit");
                System.out.print("Enter your choice: ");
                int choice = getIntInput();

                if (choice == 1) {
                    if (login()) {
                        userDashboard();
                    }
                } else if (choice == 2) {
                    System.out.println("Thank you for using the Online Examination System. Goodbye!");
                    break;
                } else {
                    System.out.println("Invalid choice. Try again.");
                }
            }
        }

        private boolean login() {
            System.out.print("Enter username: ");
            String username = scanner.nextLine();
            System.out.print("Enter password: ");
            String password = scanner.nextLine();

            if (users.containsKey(username) && users.get(username).getPassword().equals(password)) {
                currentUser = users.get(username);
                System.out.println("Login successful! Welcome, " + currentUser.getUsername());
                return true;
            } else {
                System.out.println("Invalid username or password.");
                return false;
            }
        }

        private void userDashboard() {
            while (true) {
                System.out.println("\nUser Dashboard:");
                System.out.println("1. Update Profile");
                System.out.println("2. Change Password");
                System.out.println("3. Start Exam");
                System.out.println("4. Logout");
                System.out.print("Enter your choice: ");
                int choice = getIntInput();

                switch (choice) {
                    case 1:
                        updateProfile();
                        break;
                    case 2:
                        changePassword();
                        break;
                    case 3:
                        startExam();
                        break;
                    case 4:
                        System.out.println("Logged out successfully.");
                        currentUser = null;
                        return;
                    default:
                        System.out.println("Invalid choice. Try again.");
                }
            }
        }

        private void updateProfile() {
            System.out.println("Current Profile: " + currentUser.getProfile());
            System.out.print("Enter new profile details: ");
            String newProfile = scanner.nextLine();
            currentUser.setProfile(newProfile);
            System.out.println("Profile updated successfully.");
        }

        private void changePassword() {
            System.out.print("Enter current password: ");
            String currentPassword = scanner.nextLine();

            if (currentPassword.equals(currentUser.getPassword())) {
                System.out.print("Enter new password: ");
                String newPassword = scanner.nextLine();
                currentUser.setPassword(newPassword);
                System.out.println("Password changed successfully.");
            } else {
                System.out.println("Incorrect current password.");
            }
        }

        private void startExam() {
            System.out.println("The exam has started! You have 1 minute to complete.");
            int timer = 60;
            int score = 0;
            TimerThread timerThread = new TimerThread(timer);
            Thread thread = new Thread(timerThread);
            thread.start();

            for (Question question : questions) {
                if (timerThread.isTimeUp()) {
                    break;
                }
                System.out.println("\n" + question.getQuestionText());
                String[] options = question.getOptions();
                for (int i = 0; i < options.length; i++) {
                    System.out.println((i + 1) + ". " + options[i]);
                }
                System.out.print("Enter your answer (1-4): ");
                int answer = getIntInput();

                if (answer == question.getCorrectAnswer()) {
                    score++;
                }
            }

            thread.interrupt();
            System.out.println("\nExam Over!");
            System.out.println("Your Score: " + score + "/" + questions.size());
        }

        private void setupQuestions() {
            questions.add(new Question("What is the capital of France?", new String[]{"Berlin", "Madrid", "Paris", "Rome"}, 3));
            questions.add(new Question("Which is the largest planet in our solar system?", new String[]{"Earth", "Jupiter", "Mars", "Venus"}, 2));
            questions.add(new Question("What is the square root of 64?", new String[]{"6", "8", "10", "12"}, 2));
            questions.add(new Question("Who wrote 'To Kill a Mockingbird'?", new String[]{"Harper Lee", "Mark Twain", "J.K. Rowling", "Jane Austen"}, 1));
        }

        private int getIntInput() {
            while (true) {
                try {
                    int input = scanner.nextInt();
                    scanner.nextLine(); // Consume newline
                    return input;
                } catch (InputMismatchException e) {
                    System.out.print("Invalid input. Please enter a valid number: ");
                    scanner.nextLine();
                }
            }
        }
    }

    static class TimerThread implements Runnable {
        private int timeLeft;
        private boolean timeUp = false;

        public TimerThread(int timeLeft) {
            this.timeLeft = timeLeft;
        }

        public boolean isTimeUp() {
            return timeUp;
        }

        @Override
        public void run() {
            while (timeLeft > 0 && !timeUp) {
                try {
                    Thread.sleep(1000);
                    timeLeft--;
                    System.out.println("Time left: " + timeLeft + " seconds");
                } catch (InterruptedException e) {
                    break;
                }
            }
            timeUp = true;
            System.out.println("Time is up!");
        }
    }

    public static void main(String[] args) {
        ExamSystem examSystem = new ExamSystem();
        examSystem.start();
    }
}
