//import com.google.gson.*;
//import java.lang.reflect.Type;
//public class UserTypeAdapter implements JsonDeserializer<User> {
//
//    @Override
//    public User deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
//        JsonObject jsonObject = json.getAsJsonObject();
//        //User.Information information = context.deserialize(jsonObject.getAsJsonObject("information"), User.Information.class);
//        String userType = jsonObject.get("userType").getAsString();
//
//        switch (userType) {
//            case "Regular":
//                return context.deserialize(json, Regular.class);
//            case "Contributor":
//                return context.deserialize(json, Contributor.class);
//            case "Admin":
//                return context.deserialize(json, Admin.class);
//            // Add more cases for other user types
//            default:
//                throw new JsonParseException("Unknown user type: " + userType);
//        }
//    }
//}
import com.google.gson.*;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;
public class UserTypeAdapter implements JsonDeserializer<User> {

    @Override
    public User deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        JsonObject jsonObject = json.getAsJsonObject();
        User.Information info = context.deserialize(jsonObject.getAsJsonObject("information"), User.Information.class);
        AccountType accountType = AccountType.valueOf(jsonObject.get("userType").getAsString());
        String username = jsonObject.get("username").getAsString();
        // Convert experience from String to int, handling null case
        int experience = jsonObject.get("experience").isJsonNull() ? 0 : Integer.parseInt(jsonObject.get("experience").getAsString());

        List<String> notifications = context.deserialize(jsonObject.getAsJsonArray("notifications"), new TypeToken<List<String>>(){}.getType());
        SortedSet<String> favoriteActors = context.deserialize(jsonObject.getAsJsonArray("favoriteActors"), new TypeToken<SortedSet<String>>(){}.getType());
        SortedSet<String> favoriteProductions = context.deserialize(jsonObject.getAsJsonArray("favoriteProductions"), new TypeToken<SortedSet<String>>(){}.getType());
        List<Request> requests = context.deserialize(jsonObject.getAsJsonArray("requests"), new TypeToken<List<Request>>(){}.getType());
        //SortedSet addedProductions = context.deserialize(jsonObject.getAsJsonArray("addedProductions"), new TypeToken<SortedSet<String>>(){}.getType());
        SortedSet<String> productionsContribution = context.deserialize(jsonObject.getAsJsonArray("productionsContribution"), new TypeToken<SortedSet<String>>(){}.getType());
        SortedSet<String> actorsContribution = context.deserialize(jsonObject.getAsJsonArray("actorsContribution"), new TypeToken<SortedSet<String>>(){}.getType());

        SortedSet<String> favorites = (favoriteActors != null) ? new TreeSet<>(favoriteActors) : new TreeSet<>();
        if (favoriteProductions != null) {
            favorites.addAll(favoriteProductions);
        }

        SortedSet<String> addedProductions = (productionsContribution != null) ? new TreeSet<>(productionsContribution) : new TreeSet<>();
        if (actorsContribution != null) {
            addedProductions.addAll(actorsContribution);
        }

        return UserFactory.createUser(info, accountType, username, experience, notifications, favorites, requests, addedProductions);
    }
}
