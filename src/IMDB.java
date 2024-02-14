import java.util.List;
import java.util.Map;
import java.time.LocalDateTime;
import java.util.Collections;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.annotations.SerializedName;

import java.io.FileReader;
import java.lang.reflect.Type;
import java.io.Reader;

import java.util.Scanner;

import java.time.format.DateTimeFormatter;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class IMDB {
    List<User<String>> users;
    List<Actor> actors;
    List<Request> requests;
    List<Production> productions;
    private static IMDB imdb = null;
    private IMDB() {
        System.out.println("IMDB created!");
    }
    public static IMDB getInstance() {
        if (imdb == null) {
            imdb = new IMDB();
        }
        return imdb;
    }

    void getAccounts() {
        Gson gson = new GsonBuilder()
                .registerTypeAdapter(User.class, new UserTypeAdapter())
                .registerTypeAdapter(LocalDateTime.class, new AccountsDateTimeTypeAdapter())
                .create();

        try (Reader reader = new FileReader("accounts.json")) {
            Type userListType = new TypeToken<List<User<String>>>() {}.getType();
            List<User<String>> userList = gson.fromJson(reader, userListType);

            if (userList != null) {
                this.users = userList;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    void getActors() {
        Gson gson = new Gson();
        try (Reader reader = new FileReader("actors.json")) {
            List<Actor> actors = gson.fromJson(reader,  new TypeToken<List<Actor>>() {}.getType());

            this.actors = actors;
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
    void getRequests() {
        Gson gson = new GsonBuilder()
                .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeTypeAdapter())
                .create();

        try (Reader reader = new FileReader("requests.json")) {

            List<Request> requests = gson.fromJson(reader,  new TypeToken<List<Request>>() {}.getType());

            for (Request request : requests) {
                if (request.userSolver.equals("ADMIN"))
                    RequestsHolder.addARequest(request);
            }

            this.requests = requests;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    void getProductions() {
        try (Reader reader = new FileReader("production.json")) {
            Gson gson = new GsonBuilder()
                    .registerTypeAdapter(Production.class, new ProductionDeserializer())
                    .create();

            Type productionListType = new TypeToken<List<Production>>() {}.getType();
            List<Production> productions = gson.fromJson(reader, productionListType);
            this.productions = productions;

            // search the accounts of the users that gave a rating and add them to the list of observers
            for (Production production : productions) {
                if (production.ratings != null) {
                    for (Rating rating : production.ratings) {
                        for (User<String> user : users) {
                            if (user.username.equals(rating.username)) {
                                production.registerObserver(user);
                                break;
                            }
                        }
                    }
                }
            }
            // add the user that added the production to the list of observers
            for (User<String> user : users) {
                if (user instanceof Contributor<String> contributor) {
                    for (String production : contributor.addedProductions) {
                        for (Production production1 : productions) {
                            if (production1.name.equals(production)) {
                                production1.registerObserver(contributor);
                                break;
                            }
                        }
                    }
                }
                if (user instanceof Admin<String> admin) {
                    for (String production : admin.addedProductions) {
                        for (Production production1 : productions) {
                            if (production1.name.equals(production)) {
                                production1.registerObserver(admin);
                                break;
                            }
                        }
                    }
                }

            }


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public User<String> login() {
        Scanner scanner = new Scanner(System.in);
        while(true) {
            System.out.print("Enter email: ");
            String username = scanner.nextLine().trim();
            System.out.print("Enter password: ");
            String password = scanner.nextLine().trim();

            for (User<String> user : users) {
                if (user.info.getEmail().equals(username) && user.info.getPassword().equals(password)) {
                    System.out.println("Login successful!");
                    return user;
                }
            }
            System.out.println("Login failed!");
        }
    }

    public void productionDisplayInfo() {
        System.out.println("Filter by:");
        System.out.println("1. Genre");
        System.out.println("2. Rating");
        System.out.println("3. None");
        System.out.print("Write your option: ");
        Scanner scanner = new Scanner(System.in);
        int option = scanner.nextInt();
        switch(option) {
            case 1:
                // filter by genere
                System.out.print("Chose a genre: ");
                String genre = scanner.next();
                for(Production production : productions) {
                    if (production.genres.contains(Genre.valueOf(genre))) {
                        if (production instanceof Movie movie) {
                            movie.displayInfo();
                        } else if (production instanceof Series series) {
                            series.displayInfo();
                        }
                    }
                }
                break;
            case 2:
                // filter by rating
                System.out.print("Chose a minimum rating: ");
                double rating = scanner.nextInt();
                productions.forEach(production -> {
                    if (production.averageRating >= rating) {
                        if (production instanceof Movie movie) {
                            movie.displayInfo();
                        } else if (production instanceof Series series) {
                            series.displayInfo();
                        }
                    }
                });
                break;
            case 3:
                productions.forEach(production -> {
                    if (production instanceof Movie movie) {
                        movie.displayInfo();
                    } else if (production instanceof Series series) {
                        series.displayInfo();
                    }
                    System.out.println("-----------");
                });
                break;
            default:
                System.out.println("Invalid option!");
        }
    }

    public void actorsDisplayInfo() {
        Collections.sort(actors);

        actors.forEach(actor -> {
            System.out.println("Name: " + actor.name);
            if(actor.biography != null)
                System.out.println("Biography: " + actor.biography);
            if(actor.performances != null)
                for (Map<String, String> performance : actor.performances) {
                    System.out.println("Performance Title: " + performance.get("title"));
                    System.out.println("Performance Type: " + performance.get("type"));
                }

            System.out.println("-----------");
        });
    }

    public void searchProdActor() {
        Scanner scanner1 = new Scanner(System.in);
        System.out.print("Name of the production/actor: ");
        String search = scanner1.nextLine().trim();
        productions.forEach(production -> {
            if (production.name.equals(search)) {
                if (production instanceof Movie movie) {
                    movie.displayInfo();
                } else if (production instanceof Series series) {
                    series.displayInfo();
                }
            }
        });
        actors.forEach(actor -> {
            if (actor.name.equals(search)) {
                System.out.println("Name: " + actor.name);
                System.out.println("Biography: " + actor.biography);

                for (Map<String, String> performance : actor.performances) {
                    System.out.println("Performance Title: " + performance.get("title"));
                    System.out.println("Performance Type: " + performance.get("type"));
                }

                System.out.println("-----------");
            }
        });
    }

    public void favorites(User<String> user) {
        System.out.println("Chose an option: ");
        System.out.println("1. See favorites");
        System.out.println("2. Add to favorites");
        System.out.println("3. Delete from favorites");
        System.out.print("Write your option: ");
        Scanner scanner1 = new Scanner(System.in);
        int option1 = scanner1.nextInt();
        scanner1.nextLine();
        switch (option1) {
            case 1:
                // see favorites
                System.out.println("Favorites: " + user.favorites);
                break;
            case 2:
                // add to favorites
                System.out.print("Name of the production/actor: ");
                String search = scanner1.nextLine();
                // search if the production/actor exists
                int ok = 0;
                for (Production production : productions) {
                    if (production.name.equals(search)) {
                        ok = 1;
                        break;
                    }
                }
                for (Actor actor : actors) {
                    if (actor.name.equals(search)) {
                        ok = 1;
                        break;
                    }
                }
                if (ok == 0) {
                    System.out.println("Invalid name!");
                    break;
                }
                user.addFavorite(search);
                break;
            case 3:
                // delete from favorites
                System.out.print("Name of the production/actor: ");
                Scanner scanner3 = new Scanner(System.in);
                search = scanner3.nextLine();

                // check if the production/actor exists in the favorites
                ok = 0;
                for (String favorite : user.favorites) {
                    if (favorite.equals(search)) {
                        ok = 1;
                        break;
                    }
                }
                if (ok == 0) {
                    System.out.println("Invalid name!");
                    break;
                }
                user.removeFavorite(search);
                break;
            default:
                System.out.println("Invalid option!");
        }
    }

    public void regularMenu(Regular<String> user) {
        while(true) {
            // add a try catch for the case when is not a number
            try {
                System.out.println("1. View productions details");
                System.out.println("2. View actors details");
                System.out.println("3. View notifications");
                System.out.println("4. Search for actor/movie/series");
                System.out.println("5. Go to favorites");
                System.out.println("6. Add/Delete a request");
                System.out.println("7. Write/Delete a review");
                System.out.println("8. View experience");
                System.out.println("9. Logout");
                System.out.print("Write your option: ");
                Scanner scanner = new Scanner(System.in);
                int option = scanner.nextInt();
                switch (option) {
                    case 1:
                        // view productions
                        productionDisplayInfo();
                        break;
                    case 2:
                        // view actors
                        actorsDisplayInfo();
                        break;
                    case 3:
                        // view notifications
                        user.notifications.forEach(notification -> {
                            System.out.println(notification);
                        });
                        break;
                    case 4:
                        // search a production/actor
                        searchProdActor();
                        break;
                    case 5:
                        // go to favorites
                        favorites(user);
                        break;
                    case 6:
                        // add a request
                        Scanner scanner4 = new Scanner(System.in);
                        System.out.println("1. Add a request: ");
                        System.out.println("2. Delete a request: ");
                        System.out.print("Write your option: ");
                        int option1 = scanner4.nextInt();
                        switch (option1) {
                            case 1:
                                // add a request
                                Request request = new Request();
                                user.createRequest(request);
                                break;
                            case 2:
                                // delete a request
                                request = new Request();

                                user.removeRequest(request);
                                break;
                            default:
                                System.out.println("Invalid option!");
                        }
                        break;
                    case 7:
                        // write/delete a review
                        Scanner scanner5 = new Scanner(System.in);
                        System.out.println("1. Write a review: ");
                        System.out.println("2. Delete a review: ");
                        System.out.print("Write your option: ");
                        option1 = scanner5.nextInt();
                        switch (option1) {
                            case 1:
                                // write a review
                                System.out.print("Name of the production: ");
                                Scanner scanner6 = new Scanner(System.in);
                                String search = scanner6.nextLine();
                                // search if the production/actor exists
                                int ok = 0;
                                Production p = null;
                                for (Production production : productions) {
                                    if (production.name.equals(search)) {
                                        p = production;
                                        ok = 1;
                                        break;
                                    }
                                }
                                if (ok == 0) {
                                    System.out.println("Invalid name!");
                                    break;
                                }
                                user.addAReview(p);
                                break;
                            case 2:
                                // delete a review
                                System.out.print("Name of the production/actor: ");
                                Scanner scanner7 = new Scanner(System.in);
                                search = scanner7.nextLine();
                                // search if the production/actor exists
                                ok = 0;
                                p = null;
                                for (Production production : productions) {
                                    if (production.name.equals(search)) {
                                        p = production;
                                        ok = 1;
                                        break;
                                    }
                                }
                                if (ok == 0) {
                                    System.out.println("Invalid name!");
                                    break;
                                }
                                user.deleteAReview(p);
                                break;
                            default:
                                System.out.println("Invalid option!");
                        }
                        break;
                    case 8:
                        // view experience
                        System.out.println("Experience: " + user.experience);
                        break;
                    case 9:
                        // logout
                        boolean ok = user.logout();
                        if (ok)
                            return;
                    default:
                        System.out.println("Invalid option!");
                }
            }
            catch (Exception e) {
                System.out.println("Invalid option!");
            }
        }
    }

    public void contributorMenu(Contributor<String> user) {
        while (true) {
            try {
                System.out.println("1. View productions");
                System.out.println("2. View actors");
                System.out.println("3. View notifications");
                System.out.println("4. Search a production/actor");
                System.out.println("5. Go to favorites");
                System.out.println("6. Add a request");
                System.out.println("7. Add/Delete a production/actor from system");
                System.out.println("8. View requests");
                System.out.println("9. Update information about a production/actor");
                System.out.println("10. Logout");
                System.out.print("Write your option: ");
                Scanner scanner = new Scanner(System.in);
                int option = scanner.nextInt();
                scanner.nextLine();
                switch (option) {
                    case 1:
                        // view productions
                        productionDisplayInfo();
                        break;
                    case 2:
                        // view actors
                        actorsDisplayInfo();
                        break;
                    case 3:
                        // view notifications
                        user.notifications.forEach(notification -> {
                            System.out.println(notification);
                        });
                        break;
                    case 4:
                        searchProdActor();
                        // search a production/actor
                        break;
                    case 5:
                        // go to favorites
                        favorites(user);
                        break;
                    case 6:
                        // add a request
                        System.out.println("1. Add a request: ");
                        System.out.println("2. Delete a request: ");
                        System.out.print("Write your option: ");
                        int option1 = scanner.nextInt();
                        switch (option1) {
                            case 1:
                                // add a request
                                Request request = new Request();
                                user.createRequest(request);
                                break;
                            case 2:
                                // delete a request
                                user.removeRequest(null);
                                break;
                            default:
                                System.out.println("Invalid option!");
                        }
                        break;
                    case 7:
                        // add/delete a production/actor from system
                        System.out.println("1. Add a production: ");
                        System.out.println("2. Delete a production: ");
                        System.out.println("3. Add an actor: ");
                        System.out.println("4. Delete an actor: ");
                        System.out.print("Write your option: ");
                        option1 = scanner.nextInt();
                        scanner.nextLine();
                        switch (option1) {
                            case 1:
                                // add a production
                                System.out.print("Name of the production: ");
                                String search = scanner.nextLine();
                                // search if the production exists
                                int ok = 0;
                                for (Production production : productions) {
                                    if (production.name.equals(search)) {
                                        ok = 1;
                                        break;
                                    }
                                }
                                if (ok == 1) {
                                    System.out.println("Production already in system!");
                                    break;
                                }
                                System.out.print("Type of the production: ");
                                String type = scanner.nextLine();
                                if (type.equals("Movie")) {
                                    Production p = new Movie();
                                    p.name = search;
                                    user.addProductionSystem(p);
                                } else if (type.equals("Series")) {
                                    Production p = new Series();
                                    p.name = search;
                                    user.addProductionSystem(p);
                                } else {
                                    System.out.println("Invalid type!");
                                    break;
                                }
                                break;
                            case 2:
                                // delete a production
                                System.out.print("Name of the production: ");
                                search = scanner.nextLine();
                                // search if the production/actor exists
                                ok = 0;
                                for (Production production : productions) {
                                    if (production.name.equals(search)) {
                                        ok = 1;
                                        break;
                                    }
                                }
                                if (ok == 0) {
                                    System.out.println("Invalid production!");
                                    break;
                                }
                                user.removeProductionSystem(search);
                                break;
                            case 3:
                                // add an actor
                                System.out.print("Name of the actor: ");
                                search = scanner.nextLine();
                                // search if the production/actor exists
                                ok = 0;
                                for (Actor actor : actors) {
                                    if (actor.name.equals(search)) {
                                        ok = 1;
                                        break;
                                    }
                                }
                                if (ok == 1) {
                                    System.out.println("Actor already in system!");
                                    break;
                                }
                                Actor a = new Actor(search);
                                user.addActorSystem(a);
                                break;
                            case 4:
                                // delete an actor
                                System.out.print("Name of the actor: ");
                                search = scanner.nextLine();
                                // search if the production/actor exists
                                ok = 0;
                                for (Actor actor : actors) {
                                    if (actor.name.equals(search)) {
                                        ok = 1;
                                        break;
                                    }
                                }
                                if (ok == 0) {
                                    System.out.println("Invalid actor");
                                    break;
                                }
                                user.removeActorSystem(search);
                                break;
                            default:
                                System.out.println("Invalid option!");
                        }
                        break;
                    case 8:
                        // view requests
                        for (Request request : requests) {
                            if (request.userSolver.equals(user.username))
                                request.displayInfo();
                            System.out.println("-----------");
                        }
                        // chose a request to solve
                        System.out.print("Do you want to solve a request? (y/n): ");
                        String option2 = scanner.nextLine();
                        if (option2.equals("n"))
                            break;
                        System.out.print("Write the description of the request you want to solve: ");
                        String description = scanner.nextLine();
                        for (Request request : requests) {
                            if (request.problem.equals(description)) {
                                user.solveRequest(request);
                                break;
                            }
                        }
                        break;
                    case 9:
                        // update information about a production/actor
                        System.out.println("1. Update a production: ");
                        System.out.println("2. Update an actor: ");
                        System.out.print("Write your option: ");
                        option1 = scanner.nextInt();
                        scanner.nextLine();
                        switch (option1) {
                            case 1:
                                // update a production
                                System.out.print("Name of the production: ");
                                String search = scanner.nextLine();
                                // search if the production exists
                                int ok = 0;
                                for (String production : user.addedProductions) {
                                    if (production.equals(search)) {
                                        ok = 1;
                                        break;
                                    }
                                }
                                if (ok == 0) {
                                    System.out.println("Invalid, production not from this user!");
                                    break;
                                }
                                Production p = null;
                                for (Production production : productions) {
                                    if (production.name.equals(search)) {
                                        p = production;
                                        break;
                                    }
                                }
                                user.updateProduction(p);
                                break;
                            case 2:
                                // update an actor
                                System.out.print("Name of the actor: ");
                                search = scanner.nextLine();
                                // search if the production/actor exists
                                ok = 0;
                                for (String actor : user.addedProductions) {
                                    if (actor.equals(search)) {
                                        ok = 1;
                                        break;
                                    }
                                }
                                if (ok == 0) {
                                    System.out.println("Invalid, actor not from this user!");
                                    break;
                                }
                                Actor a = null;
                                for (Actor actor : actors) {
                                    if (actor.name.equals(search)) {
                                        a = actor;
                                        break;
                                    }
                                }
                                user.updateActor(a);
                                break;
                            default:
                                System.out.println("Invalid option!");
                        }
                        break;
                    case 10:
                        // logout
                        boolean ok = user.logout();
                        if (ok)
                            return;
                    default:
                        System.out.println("Invalid option!");
                }
            }
            catch (Exception e) {
                System.out.println("Invalid option!");
            }
        }
    }

    public void adminMenu(Admin<String> user) {
        while (true) {
            try {
                System.out.println("1. View productions");
                System.out.println("2. View actors");
                System.out.println("3. View notifications");
                System.out.println("4. Search a production/actor");
                System.out.println("5. Go to favorites");
                System.out.println("6. Add/Delete a production/actor from system");
                System.out.println("7. View requests");
                System.out.println("8. Update information about a production/actor");
                System.out.println("9. Add/Delete a user");
                System.out.println("10. Logout");
                System.out.print("Write your option: ");
                Scanner scanner = new Scanner(System.in);
                int option = scanner.nextInt();
                switch (option) {
                    case 1:
                        // view productions
                        productionDisplayInfo();
                        break;
                    case 2:
                        // view actors
                        actorsDisplayInfo();
                        break;
                    case 3:
                        // view notifications
                        user.notifications.forEach(notification -> {
                            System.out.println(notification);
                        });
                        break;
                    case 4:
                        // search a production/actor
                        searchProdActor();
                        break;
                    case 5:
                        // go to favorites
                        favorites(user);
                        break;
                    case 6:
                        // add/delete a production/actor from system
                        System.out.println("1. Add a production: ");
                        System.out.println("2. Delete a production: ");
                        System.out.println("3. Add an actor: ");
                        System.out.println("4. Delete an actor: ");
                        System.out.print("Write your option: ");
                        int option1 = scanner.nextInt();
                        scanner.nextLine();
                        switch (option1) {
                            case 1:
                                // add a production
                                System.out.print("Name of the production: ");
                                String search = scanner.nextLine();
                                // search if the production exists
                                int ok = 0;
                                for (Production production : productions) {
                                    if (production.name.equals(search)) {
                                        ok = 1;
                                        break;
                                    }
                                }
                                if (ok == 1) {
                                    System.out.println("Production already in system!");
                                    break;
                                }
                                System.out.print("Type of the production: ");
                                String type = scanner.nextLine();
                                if (type.equals("Movie")) {
                                    Production p = new Movie();
                                    p.name = search;
                                    user.addProductionSystem(p);
                                } else if (type.equals("Series")) {
                                    Production p = new Series();
                                    p.name = search;
                                    user.addProductionSystem(p);
                                } else {
                                    System.out.println("Invalid type!");
                                    break;
                                }
                                break;
                            case 2:
                                // delete a production
                                System.out.print("Name of the production: ");
                                search = scanner.nextLine();
                                // search if the production/actor exists
                                ok = 0;
                                for (Production production : productions) {
                                    if (production.name.equals(search)) {
                                        ok = 1;
                                        break;
                                    }
                                }
                                if (ok == 0) {
                                    System.out.println("Invalid production!");
                                    break;
                                }
                                user.removeProductionSystem(search);
                                break;
                            case 3:
                                // add an actor
                                System.out.print("Name of the actor: ");
                                search = scanner.nextLine();
                                // search if the production/actor exists
                                ok = 0;
                                for (Actor actor : actors) {
                                    if (actor.name.equals(search)) {
                                        ok = 1;
                                        break;
                                    }
                                }
                                if (ok == 1) {
                                    System.out.println("Actor already in system!");
                                    break;
                                }
                                Actor a = new Actor(search);
                                user.addActorSystem(a);
                                break;
                            case 4:
                                // delete an actor
                                System.out.print("Name of the actor: ");
                                search = scanner.nextLine();
                                // search if the production/actor exists
                                ok = 0;
                                for (Actor actor : actors) {
                                    if (actor.name.equals(search)) {
                                        ok = 1;
                                        break;
                                    }
                                }
                                if (ok == 0) {
                                    System.out.println("Invalid actor");
                                    break;
                                }
                                user.removeActorSystem(search);
                                break;
                            default:
                                System.out.println("Invalid option!");
                        }
                        break;
                    case 7:
                        // view requests
                        for (Request request : requests) {
                            if (request.userSolver.equals(user.username))
                                request.displayInfo();
                            System.out.println("-----------");
                        }
                        // chose a request to solve
                        System.out.print("Do you want to solve a request? (y/n): ");
                        String option2 = scanner.nextLine();
                        if (option2.equals("n"))
                            break;
                        System.out.print("Write the description of the request you want to solve: ");
                        String description = scanner.nextLine();
                        for (Request request : requests) {
                            if (request.problem.equals(description)) {
                                user.solveRequest(request);
                                break;
                            }
                        }
                        break;
                    case 8:
                        // update information about a production/actor
                        System.out.println("1. Update a production: ");
                        System.out.println("2. Update an actor: ");
                        System.out.print("Write your option: ");
                        option1 = scanner.nextInt();
                        scanner.nextLine();
                        switch (option1) {
                            case 1:
                                // update a production
                                System.out.print("Name of the production: ");
                                String search = scanner.nextLine();
                                // search if the production exists
                                int ok = 0;
                                for (String production : user.addedProductions) {
                                    if (production.equals(search)) {
                                        ok = 1;
                                        break;
                                    }
                                }
                                if (ok == 0) {
                                    System.out.println("Invalid, production not from this user!");
                                    break;
                                }
                                Production p = null;
                                for (Production production : productions) {
                                    if (production.name.equals(search)) {
                                        p = production;
                                        break;
                                    }
                                }
                                user.updateProduction(p);
                                break;
                            case 2:
                                // update an actor
                                System.out.print("Name of the actor: ");
                                search = scanner.nextLine();
                                // search if the production/actor exists
                                ok = 0;
                                for (String actor : user.addedProductions) {
                                    if (actor.equals(search)) {
                                        ok = 1;
                                        break;
                                    }
                                }
                                if (ok == 0) {
                                    System.out.println("Invalid, actor not from this user!");
                                    break;
                                }
                                Actor a = null;
                                for (Actor actor : actors) {
                                    if (actor.name.equals(search)) {
                                        a = actor;
                                        break;
                                    }
                                }
                                user.updateActor(a);
                                break;
                            default:
                                System.out.println("Invalid option!");
                        }
                        break;
                    case 9:
                        // add/delete a user
                        System.out.println("1. Add a user: ");
                        System.out.println("2. Delete a user: ");
                        System.out.print("Write your option: ");
                        option1 = scanner.nextInt();
                        scanner.nextLine();
                        switch (option1) {
                            case 1:
                                System.out.print("Username:");
                                String username = scanner.nextLine();
                                user.addUser(username);
                                break;
                            case 2:
                                System.out.print("Username:");
                                username = scanner.nextLine();
                                user.deleteUser(username);
                                break;
                            default:
                                System.out.println("Invalid option!");
                        }
                        break;
                    case 10:
                        // logout
                        boolean ok = user.logout();
                        if (ok)
                            return;
                    default:
                        System.out.println("Invalid option!");
                }
            }
            catch (Exception e) {
                System.out.println("Invalid option!");
            }
        }
    }
    public void commandLine() {
        while(true) {
            System.out.println("Do you want to login? (y/n): ");
            Scanner scanner = new Scanner(System.in);
            String option = scanner.nextLine();
            if (option.equals("n"))
                break;
            User<String> utilizator = login();
            //System.out.println("Utilizatorul logat este: " + utilizator);
            switch (utilizator.accountType) {
                case Regular:
                    regularMenu((Regular<String>) utilizator);
                    break;
                case Contributor:
                    contributorMenu((Contributor<String>) utilizator);
                    break;
                case Admin:
                    adminMenu((Admin<String>) utilizator);
                    break;
                default:
                    System.out.println("Unknown user type");
            }
        }
    }

    public void run() {
        getActors();
        getRequests();
        getAccounts();
        getProductions();
        System.out.println("What do you want to use?");
        System.out.println("1. Command Line");
        System.out.println("2. Interface");
        Scanner scanner1 = new Scanner(System.in);
        String option1 = scanner1.nextLine();
        if (option1.equals("1")) {
            commandLine();
        } else if (option1.equals("2")) {
            SwingUtilities.invokeLater(() -> {
                new LoginFrame("Login Interface");
            });
        } else {
            System.out.println("Invalid option!");
        }



    }
    public static void main(String[] args) {
        getInstance();
        imdb.run();

    }
}