import java.util.*;
public class Admin<T> extends Staff<T>{
    public Admin(Information info, AccountType accountType, String username, int experience, List<String> notifications, SortedSet<T> favorites, List<Request> requests, SortedSet<T> addedProductions) {
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

    }
    public void addActorSystem(Actor a){
        addedProductions.add((T) a.name);
        IMDB.getInstance().actors.add(a);
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
        // remove the production from the actors
        for (Actor a : IMDB.getInstance().actors) {
            for (Map<String, String> map : a.performances) {
                if (map.get("title").equals(name)) {
                    a.performances.remove(map);
                    break;
                }
            }
        }
        // remove the production from the users
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
        // remove the actor from the productions
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
        // remove the actor from the users
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
                System.out.println("Request solved!");
                IMDB.getInstance().requests.remove(request);
                ExperienceChose experienceChose = new ExperienceChose();
                experienceChose.setExperienceStrategy(new RequestExperience(5));
                for (User<String> u : IMDB.getInstance().users) {
                    if (u.username.equals(request.user)) {
                        u.experience += experienceChose.calculateExperience();
                        break;
                    }
                }
                for (User<String> u : IMDB.getInstance().users) {
                    if (u.username.equals(request.user)) {
                        u.notifications.add("Your request has been solved by" + this.username + "!");
                        break;
                    }
                }
                break;
            case 2:
                System.out.println("Request denied!");
                IMDB.getInstance().requests.remove(request);
                for (User<String> u : IMDB.getInstance().users) {
                    if (u.username.equals(request.user)) {
                        u.notifications.add("Your request has been denied by" + this.username + "!");
                        break;
                    }
                }
                break;
            case 3:
                System.out.println("Request not solved!");
                break;
            default:
                System.out.println("Invalid option!");
        }
    }
    public void addUser(String username){
        User.Information information = null;
        information.addUser();
        User<String> user = new Regular<String>(information, AccountType.Regular, username, 0, null, null);

    }
    @Override
    void updateExperience() {
        experience ++;
    }
    public void deleteUser(String username){
        for (User<String> u : IMDB.getInstance().users) {
            if (u.username.equals(username)) {
                IMDB.getInstance().users.remove(u);
                for (Production p : IMDB.getInstance().productions) {
                    for (Rating r : p.ratings) {
                        if (r.username.equals(username)) {
                            p.ratings.remove(r);
                            break;
                        }
                    }
                }
                // delete the requests from the user
                for (Request r : IMDB.getInstance().requests) {
                    if (r.user.equals(username)) {
                        IMDB.getInstance().requests.remove(r);
                        // check if the request was in requestholder
                        if (r.userSolver.equals("ADMIN")) {
                            RequestsHolder.removeARequest(r);
                        }
                        break;
                    }
                }
                // if the user is a contributor, add the productions to the admin
                if (u.accountType == AccountType.Contributor) {
                    for (Production p : IMDB.getInstance().productions) {
                        if (p instanceof Movie) {
                            for (String director : ((Movie) p).directors) {
                                if (director.equals(username)) {
                                    ((Admin)u).addedProductions.add((T) p.name);
                                    break;
                                }
                            }
                        }
                        if (p instanceof Series) {
                            for (String director : ((Series) p).directors) {
                                if (director.equals(username)) {
                                    ((Admin)u).addedProductions.add((T) p.name);
                                    break;
                                }
                            }
                        }
                    }
                }
                break;
            }
        }
    }
    public void update(String message) {
        if (notifications == null) {
            notifications = new ArrayList<>();
        }
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
        if (this.username != null)
            System.out.println("Username: " + this.username);
        if (this.accountType != null)
            System.out.println("Account type: " + this.accountType);
        if (this.experience != 0)
            System.out.println("Experience: " + this.experience);
        if (this.notifications != null)
            System.out.println("Notifications: " + this.notifications);
        if (this.favorites != null)
            System.out.println("Favorites: " + this.favorites);
        if (this.requests != null)
            System.out.println("Requests: " + this.requests);
        if (this.addedProductions != null)
            System.out.println("Added productions: " + this.addedProductions);
        info.displayCredentials();
        info.displayInformation();
    }
}
