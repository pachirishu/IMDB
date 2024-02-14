import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.SortedSet;
import com.google.gson.*;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
public class Regular<T> extends User<T> implements RequestsManager{

    public Regular() {
        super();
    }
    public Regular(Information info, AccountType accountType, String username, int experience, List<String> notifications, SortedSet<T> favorites) {
        super(info, accountType, username, experience, notifications, favorites);
    }


    @Override
    void addFavorite(T favorite) {
        favorites.add(favorite);
    }

    @Override
    void removeFavorite(T favorite) {
        favorites.remove(favorite);
    }

    @Override
    void updateExperience() {
        experience ++;
    }


    @Override
    boolean logout() {
        System.out.println("Do you want to log out? (y/n)");
        Scanner scanner = new Scanner(System.in);
        String option = scanner.nextLine();
        if (option.equals("y")) {
            return true;
        }
        return false;
    }

    @Override
    public void createRequest(Request request) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("What kind of request do you want to make?");
        System.out.println("1. DELETE_ACCOUNT");
        System.out.println("2. ACTOR_ISSUE");
        System.out.println("3. MOVIE_ISSUE");
        System.out.println("4. OTHERS");
        System.out.print("Write your option: ");
        int option = scanner.nextInt();
        scanner.nextLine();
        switch (option) {
            case 1:
                // delete account
                System.out.println("Do you really want to delete your account? ),:");
                System.out.println("1. Yes");
                System.out.println("2. No");
                System.out.print("Write your option: ");
                int option1 = scanner.nextInt();
                switch (option1) {
                    case 1:
                        // delete account
                        System.out.println("Account to be deleted! Sorry to see you go!");
                        Scanner scanner1 = new Scanner(System.in);
                        System.out.print("Description: ");
                        String description = scanner1.nextLine();
                        request.setRequestType(RequestTypes.DELETE_ACCOUNT);
                        request.setRequestTime(LocalDateTime.now());
                        request.user = this.username;
                        request.userSolver = "ADMIN";
                        request.problem = description;

                        RequestsHolder.addARequest(request);
                        IMDB.getInstance().requests.add(request);
                        for (User<String> u : IMDB.getInstance().users) {
                            if (u instanceof Admin) {
                                System.out.println("Admin found!" + u.username);
                                request.registerObserver(u);
                            }
                        }
                        request.uploadRequest("A new request has been added to the system by " + this.username + "!");
                        request.registerObserver(this);
                        break;
                    case 2:
                        // do nothing
                        System.out.println("Account not deleted! Horray!");
                        break;
                    default:
                        System.out.println("Invalid option!");
                }
                break;
            case 2:
                // actor issue
                String actorName = null;
                int i = 0;
                while(i == 0) {
                    System.out.print("Write the name of the actor: ");
                    actorName = scanner.nextLine();
                    System.out.println(actorName);
                    for (Actor a : IMDB.getInstance().actors) {
                        System.out.println(a.name);
                        if (a.name.equals(actorName)) {
                            i = 1; System.out.println(a.name); }
                    }
                    if(i == 0)
                        System.out.println("Invalid actor name!");
                }
                System.out.print("Description: ");
                String description = scanner.nextLine();

                request.setRequestType(RequestTypes.ACTOR_ISSUE);
                request.setRequestTime(LocalDateTime.now());
                request.user = this.username;
                request.actorName = actorName;
                request.problem = description;
                for (User<String> u : IMDB.getInstance().users) {
                    if (u instanceof Admin) {
                        for (String actor : ((Staff<String>) u).addedProductions) {
                            if (actor.equals(actorName)) {
                                request.userSolver = u.username;
                                ((Admin<String>) u).requests.add(request);
                                request.registerObserver(u);
                                request.uploadRequest("A new request has been added to the system by " + this.username + "!");
                                request.registerObserver(this);
                                break;
                            }
                        }
                    }
                    if (u instanceof Contributor) {
                        for (String actor : ((Staff<String>) u).addedProductions) {
                            if (actor.equals(actorName)) {
                                request.userSolver = u.username;
                                ((Contributor<String>) u).requests.add(request);
                                request.registerObserver(u);
                                request.uploadRequest("A new request has been added to the system by " + this.username + "!");
                                request.registerObserver(this);
                                break;
                            }
                        }
                    }
                }
                IMDB.getInstance().requests.add(request);
                break;
            case 3:
                // movie issue
                String movieName = null;
                int j = 0;
                while(j == 0) {
                    System.out.print("Write the name of the movie/series: ");
                    movieName = scanner.nextLine();
                    System.out.println(movieName);
                    for (Production a : IMDB.getInstance().productions) {
                        System.out.println(a.name);
                        if (a.name.equals(movieName)) {
                            j = 1; System.out.println(a.name); }
                    }
                    if(j == 0)
                        System.out.println("Invalid production title!");
                }
                System.out.print("Description: ");
                description = scanner.nextLine();

                request.setRequestType(RequestTypes.MOVIE_ISSUE);
                request.setRequestTime(LocalDateTime.now());
                request.user = this.username;
                request.productionTitle = movieName;
                request.problem = description;
                for (User<String> u : IMDB.getInstance().users) {
                    if (u instanceof Admin) {
                        for (String movie : ((Staff<String>) u).addedProductions) {
                            if (movie.equals(movieName)) {
                                request.userSolver = u.username;
                                ((Admin<String>) u).requests.add(request);
                                request.registerObserver(u);
                                request.uploadRequest("A new request has been added to the system by " + this.username + "!");
                                request.registerObserver(this);
                                break;
                            }
                        }
                    }
                    if (u instanceof Contributor) {
                        for (String movie : ((Staff<String>) u).addedProductions) {
                            if (movie.equals(movieName)) {
                                request.userSolver = u.username;
                                ((Contributor<String>) u).requests.add(request);
                                request.registerObserver(u);
                                request.uploadRequest("A new request has been added to the system by " + this.username + "!");
                                request.registerObserver(this);
                                break;
                            }
                        }
                    }
                }
                IMDB.getInstance().requests.add(request);
                break;
            case 4:
                // others
                System.out.print("Description: ");
                description = scanner.nextLine();

                request.setRequestType(RequestTypes.OTHERS);
                request.setRequestTime(LocalDateTime.now());
                request.user = this.username;
                request.problem = description;
                request.userSolver = "ADMIN";
                for (User<String> u : IMDB.getInstance().users) {
                    if (u instanceof Admin) {
                        request.registerObserver(u);
                    }
                }
                request.uploadRequest("A new request has been added to the system by " + this.username + "!");
                request.registerObserver(this);
                IMDB.getInstance().requests.add(request);
                RequestsHolder.addARequest(request);
                break;
            default:
                System.out.println("Invalid option!");
        }
    }

    @Override
    public void removeRequest(Request request) {
        // check all the user requests
        for (Request r : IMDB.getInstance().requests) {
            if (r.user.equals(this.username)) {
                r.displayInfo();
            }
        }
        Scanner scanner = new Scanner(System.in);
        System.out.print("Write the description of the request: ");
        String requestDescription = scanner.nextLine();
        for (Request r : IMDB.getInstance().requests) {
            if (r.user.equals(this.username) && r.problem.equals(requestDescription)) {
                IMDB.getInstance().requests.remove(r);
                break;
            }
        }


    }

    public void addAReview(Production p){
        System.out.println("Give a note: ");
        Scanner scanner = new Scanner(System.in);
        int rating = scanner.nextInt();
        scanner.nextLine();
        System.out.println("Give a comment: ");
        String comment = scanner.nextLine();
        // check if there is already a rating from the user
        int i = 0;
        for (Rating rating1 : p.ratings) {
            if (rating1.username.equals(this.username)) {
                p.ratings.remove(rating1);
                i = 1;
                break;
            }
        }
        if (i == 0) {
            ExperienceChose experienceChose = new ExperienceChose();
            experienceChose.setExperienceStrategy(new RequestExperience(1));
            for (User<String> u : IMDB.getInstance().users) {
                if (u.username.equals(this.username)) {
                    u.experience += experienceChose.calculateExperience();
                    break;
                }
            }

        }
        Rating r = new Rating(this.username, rating, comment);
        p.ratings.add(r);
        p.notifyObservers("A new rating has been added to " + p.name + " by " + this.username + "!");
        p.registerObserver(this);

        // get all the ratings for the production and calculate the average
        double sum = 0;
        for (Rating rating1 : p.ratings) {
            sum += rating1.rating;
        }
        p.averageRating = sum / p.ratings.size();
        // update the production
        for (Production production : IMDB.getInstance().productions) {
            if (production.name.equals(p.name)) {
                production = p;
                break;
            }
        }
    }
    public void deleteAReview(Production p){
        for (Rating rating : p.ratings) {
            if (rating.username.equals(this.username)) {
                p.ratings.remove(rating);
                rating.username = this.username;
                rating.rating = 0;
                rating.comment = null;
                p.ratings.add(rating);
                break;
            }
        }
        // get all the ratings for the production and calculate the average
        double sum = 0;
        for (Rating rating1 : p.ratings) {
            sum += rating1.rating;
        }
        System.out.println(sum);
        System.out.println(p.ratings.size());
        int i = 0;
        for (Rating rating1 : p.ratings) {
            if(rating1.rating != 0)
                i++;
        }
        p.averageRating = sum / i;
        System.out.println(p.averageRating);
        // update the production
        for (Production production : IMDB.getInstance().productions) {
            if (production.name.equals(p.name)) {
                production = p;
                break;
            }
        }
        p.removeObserver(this);
    }
    public void update(String message) {
        if(notifications == null)
            notifications = new ArrayList<>();
        notifications.add(message);
    }
    public void displayInfo(){
        System.out.println("Username: " + this.username);
        System.out.println("Account type: " + this.accountType);
        System.out.println("Experience: " + this.experience);
        System.out.println("Notifications: " + this.notifications);
        System.out.println("Favorites: " + this.favorites);
        info.displayCredentials();
        info.displayInformation();
    }
}
