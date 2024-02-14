import com.google.gson.annotations.SerializedName;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.SortedSet;
import java.util.*;
public abstract class User<T> implements Observer {
    public class Information {
        public static class Credentials {
            @SerializedName("email")
            private String email;
            @SerializedName("password")
            private String password;
            public Credentials(String email, String password) {
                this.email = email;
                this.password = password;
            }
            public String getEmail() {
                return email;
            }
            public String getPassword() {
                return password;
            }
        }
        @SerializedName("credentials")
        private final Credentials credentials;
        @SerializedName("name")
        private final String name;
        @SerializedName("age")
        private final String age;
        @SerializedName("gender")
        private final String gen;
        @SerializedName("birthDate")
        private final LocalDateTime birthday;

        public String getEmail() {
            return credentials.getEmail();
        }
        public String getPassword() {
            return credentials.getPassword();
        }
        public String getName() {
            return name;
        }
        public String getAge() {
            return age;
        }
        public String getGender() {
            return gen;
        }
        public LocalDateTime getBirthday() {
            return birthday;
        }

        private Information(InformationBuilder builder) {
            this.credentials = builder.credentials;
            this.name = builder.name;
            this.age = builder.age;
            this.birthday = builder.birthday;
            this.gen = builder.gender;
        }
        public class InformationBuilder {
            private final Credentials credentials;
            private final String name;
            private String age;
            private String gender;
            private LocalDateTime birthday;

            public InformationBuilder (Credentials credentials, String name) {
                this.credentials = credentials;
                this.name = name;
            }

            public InformationBuilder setAge(String age) {
                this.age = age;
                return this;
            }

            public InformationBuilder setGender(String gender) {
                this.gender = gender;
                return this;
            }

            public InformationBuilder setBirthday(LocalDateTime birthday) {
                this.birthday = birthday;
                return this;
            }

            public User.Information build() {
                return new Information(this);
            }
        }


        public void displayCredentials() {
            System.out.println("Email: " + credentials.getEmail());
            System.out.println("Password: " + credentials.getPassword());
        }
        public void displayInformation() {
            System.out.println("Name: " + name);
            System.out.println("Age: " + age);
            System.out.println("Gender" + gen);
            System.out.println("Birthday: " + birthday.format(DateTimeFormatter.ofPattern("dd-MM-yyyy")));
        }
        public void addUser() {
            System.out.print("Email: ");
            Scanner scanner = new Scanner(System.in);
            String email = scanner.nextLine();
            System.out.print("Password: ");
            String password = scanner.nextLine();
            System.out.print("Name: ");
            String name = scanner.nextLine();
            System.out.print("Age: ");
            String age = scanner.nextLine();
            System.out.print("Gender:");
            String gender = scanner.nextLine();
            User.Information information = new User.Information.InformationBuilder(new User.Information.Credentials(email, password), name)
                    .setAge(age)
                    .setGender(gender)
                    .setBirthday(null)
                    .build();
        }
    }
    @SerializedName("information")
    Information info;
    @SerializedName("userType")
    AccountType accountType;
    @SerializedName("username")
    String username;
    @SerializedName("experience")
    int experience;
    @SerializedName("notifications")
    List<String> notifications;
    @SerializedName("favoriteActors")
    SortedSet<T> favorites;
    abstract void addFavorite(T favorite);
    abstract void removeFavorite(T favorite);
    abstract void updateExperience();
    abstract boolean logout();

    public User() {
        this.info = null;
        this.accountType = null;
        this.username = null;
        this.experience = 0;
        this.notifications = null;
        this.favorites = null;
    }
    public User(Information info, AccountType accountType, String username, int experience, List<String> notifications, SortedSet<T> favorites) {
        this.info = info;
        this.accountType = accountType;
        this.username = username;
        this.experience = experience;
        this.notifications = notifications;
        this.favorites = favorites;
    }

}
