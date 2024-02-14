import java.time.LocalDateTime;
import java.util.*;

public class Contributor<T>  extends Staff<T> implements RequestsManager{
    public Contributor(Information info, AccountType accountType, String username, int experience, List<String> notifications, SortedSet<T> favorites, List<Request> requests, SortedSet<T> addedProductions) {
        super(info, accountType, username, experience, notifications, favorites, requests, addedProductions);
    }
    void addFavorite(T favorite) {
        favorites.add(favorite);
    }
    void removeFavorite(T favorite) {
        favorites.remove(favorite);
    }
    void updateExperience(int exp) {
        experience += exp;
    }
    public void addProductionSystem(Production p){
        addedProductions.add((T) p.name);
        IMDB.getInstance().productions.add(p);
        ExperienceChose experienceChose = new ExperienceChose();
        experienceChose.setExperienceStrategy(new AddProductionExperience(10));
        for (User<String> u : IMDB.getInstance().users) {
            if (u.username.equals(this.username)) {
                u.experience += experienceChose.calculateExperience();
                break;
            }
        }

    }
    public void addActorSystem(Actor a){
        addedProductions.add((T) a.name);
        IMDB.getInstance().actors.add(a);
        ExperienceChose experienceChose = new ExperienceChose();
        experienceChose.setExperienceStrategy(new AddProductionExperience(10));
        for (User<String> u : IMDB.getInstance().users) {
            if (u.username.equals(this.username)) {
                u.experience += experienceChose.calculateExperience();
                break;
            }
        }
    }
    public void removeProductionSystem(String name) {
        for (T pr : addedProductions) {
            if (pr.equals(name)) {
                addedProductions.remove(pr);
                for (Production p : IMDB.getInstance().productions) {
                    if (p.name.equals(name)) {
                        IMDB.getInstance().productions.remove(p);
                        break;
                    }
                }
                break;
            }
        }
        for (Actor a : IMDB.getInstance().actors) {
            for (Map<String, String> map : a.performances) {
                if (map.get("title").equals(name)) {
                    a.performances.remove(map);
                    break;
                }
            }
        }
        for (User<String> u : IMDB.getInstance().users) {
            for (String fav : u.favorites) {
                if (fav.equals(name)) {
                    u.favorites.remove(fav);
                    break;
                }
            }
        }

    }
    public void removeActorSystem(String name) {
        for (T ac : addedProductions) {
            if (ac.equals(name)) {
                addedProductions.remove(ac);
                for (Actor a : IMDB.getInstance().actors) {
                    if (a.name.equals(name)) {
                        IMDB.getInstance().actors.remove(a);
                        break;
                    }
                }
                break;
            }
        }
        for (Production p : IMDB.getInstance().productions) {
            if (p instanceof Movie) {
                for (String actor : ((Movie) p).actors) {
                    if (actor.equals(name)) {
                        ((Movie) p).actors.remove(actor);
                        break;
                    }
                }
            }
            if (p instanceof Series) {
                for (String actor : ((Series) p).actors) {
                    if (actor.equals(name)) {
                        ((Series) p).actors.remove(actor);
                        break;
                    }
                }
            }
        }
        for (User<String> u : IMDB.getInstance().users) {
            for (String fav : u.favorites) {
                if (fav.equals(name)) {
                    u.favorites.remove(fav);
                    break;
                }
            }
        }

    }
    public void updateProduction(Production p) {
        System.out.println("What you wanna change: ");
        if (p instanceof Movie) {
            System.out.println("1. Duration");
            System.out.println("2. Release Year");
            System.out.println("3. Directors");
            System.out.println("4. Cast");
            System.out.println("5. Plot");
            System.out.print("Write your option: ");
            Scanner scanner = new Scanner(System.in);
            int option = scanner.nextInt();
            scanner.nextLine();
            switch (option) {
                case 1:
                    System.out.print("New duration: ");
                    String duration = scanner.nextLine();
                    ((Movie)p).duration = duration;
                    break;
                case 2:
                    System.out.print("New release year: ");
                    int releaseYear = scanner.nextInt();
                    scanner.nextLine();
                    ((Movie)p).releaseYear = releaseYear;
                    break;
                case 3:
                    System.out.print("New directors: ");
                    String directors = scanner.nextLine();
                    ((Movie)p).directors.add(directors);
                    break;
                case 4:
                    System.out.print("New cast: ");
                    String cast = scanner.nextLine();
                    ((Movie)p).actors.add(cast);
                    break;
                case 5:
                    System.out.print("New plot: ");
                    String plot = scanner.nextLine();
                    ((Movie)p).plot = plot;
                    break;
                default:
                    System.out.println("Invalid option!");
            }
        }
        if (p instanceof Series) {
            System.out.println("1. Release Year");
            System.out.println("2. Directors");
            System.out.println("3. Cast");
            System.out.println("4. Plot");
            System.out.println("5. Seasons");
            System.out.print("Write your option: ");
            Scanner scanner = new Scanner(System.in);
            int option = scanner.nextInt();
            scanner.nextLine();
            switch (option) {
                case 1:
                    System.out.print("New release year: ");
                    int releaseYear = scanner.nextInt();
                    scanner.nextLine();
                    ((Series)p).releaseYear = releaseYear;
                    break;
                case 2:
                    System.out.print("New directors: ");
                    String directors = scanner.nextLine();
                    ((Series)p).directors.add(directors);
                    break;
                case 3:
                    System.out.print("New cast: ");
                    String cast = scanner.nextLine();
                    ((Series)p).actors.add(cast);
                    break;
                case 4:
                    System.out.print("New plot: ");
                    String plot = scanner.nextLine();
                    ((Series)p).plot = plot;
                    break;
                case 5:
                    System.out.print("New seasons: ");
                    int seasons = scanner.nextInt();
                    scanner.nextLine();
                    ((Series)p).numSeasons = seasons;
                    break;
                default:
                    System.out.println("Invalid option!");
            }
        }
        for (Production prod : IMDB.getInstance().productions) {
            if (prod.name.equals(p.name)) {
                IMDB.getInstance().productions.remove(prod);
                IMDB.getInstance().productions.add(p);
                break;
            }
        }
    }
    public void updateActor(Actor a) {
        System.out.println("What you wanna change: ");
        System.out.println("1. Biography");
        System.out.println("2. Add a production");
        System.out.println("3. Remove a production");
        System.out.print("Write your option: ");
        Scanner scanner = new Scanner(System.in);
        int option = scanner.nextInt();
        scanner.nextLine();
        switch (option) {
            case 1:
                System.out.print("New biography: ");
                String biography = scanner.nextLine();
                a.biography = biography;
                break;
            case 2:
                System.out.print("New production: ");
                String production = scanner.nextLine();
                System.out.print("Type (Movie/Series): ");
                String type = scanner.nextLine();
                Map<String, String> map = new HashMap<>();
                map.put("title", production);
                map.put("type", type);
                a.performances.add(map);
                break;
            case 3:
                System.out.print("Production to be removed: ");
                String production1 = scanner.nextLine();
                for (Map<String, String> map1 : a.performances) {
                    if (map1.get("title").equals(production1)) {
                        a.performances.remove(map1);
                        break;
                    }
                }
                break;
            default:
                System.out.println("Invalid option!");
        }
        for (Actor actor : IMDB.getInstance().actors) {
            if (actor.name.equals(a.name)) {
                IMDB.getInstance().actors.remove(actor);
                IMDB.getInstance().actors.add(a);
                break;
            }
        }
    }
    public void solveRequest(Request request){
        System.out.println("What you wanna do: ");
        System.out.println("1. Solve the request");
        System.out.println("2. Deny the request");
        System.out.println("3. Nothing");
        System.out.print("Write your option: ");
        Scanner scanner = new Scanner(System.in);
        int option = scanner.nextInt();
        scanner.nextLine();
        switch (option) {
            case 1:
                // solve the request
                System.out.println("Request solved!");
                IMDB.getInstance().requests.remove(request);

                ExperienceChose experienceChose = new ExperienceChose();
                experienceChose.setExperienceStrategy(new RequestExperience(5));
                // get the user from the request
                for (User<String> u : IMDB.getInstance().users) {
                    if (u.username.equals(request.user)) {
                        u.experience += experienceChose.calculateExperience();
                        break;
                    }
                }

                request.uploadRequest("A request has been solved by " + this.username + "!");


                break;
            case 2:
                // deny the request
                System.out.println("Request denied!");
                IMDB.getInstance().requests.remove(request);
                request.uploadRequest("A request has been denied by " + this.username + "!");
                break;
            case 3:
                // do nothing
                System.out.println("Request not solved!");
                break;
            default:
                System.out.println("Invalid option!");
        }

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
                if(addedProductions.contains(actorName)) {
                    System.out.println("Actor added by you!");
                    return;
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
                if(addedProductions.contains(movieName)) {
                    System.out.println("Production added by you!");
                    return;
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
                IMDB.getInstance().requests.add(request);
                RequestsHolder.addARequest(request);
                for (User<String> u : IMDB.getInstance().users) {
                    if (u instanceof Admin) {
                        request.registerObserver(u);
                    }
                }
                request.uploadRequest("A new request has been added to the system by " + this.username + "!");
                request.registerObserver(this);
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

    @Override
    void updateExperience() {
        experience ++;
    }

    public void update(String message) {
        if (notifications == null)
            notifications = new ArrayList<>();
        notifications.add(message);
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
    public void displayInfo(){
        System.out.println("Username: " + this.username);
        System.out.println("Account type: " + this.accountType);
        System.out.println("Experience: " + this.experience);
        System.out.println("Notifications: " + this.notifications);
        System.out.println("Favorites: " + this.favorites);
        System.out.println("Contribution: " + this.addedProductions);
        // show the Information of the user
        System.out.println("Information: ");
        info.displayCredentials();
        info.displayInformation();
        System.out.println();
    }

}
