import java.util.*;
public class UserFactory {
    public static User createUser(User.Information info, AccountType accountType, String username, int experience, List<String> notifications, SortedSet favorites, List<Request> requests, SortedSet addedProductions){
        if(accountType == AccountType.Admin){
            return new Admin(info, accountType, username, experience, notifications, favorites, requests, addedProductions);
        }
        else if(accountType == AccountType.Contributor){
            return new Contributor(info, accountType, username, experience, notifications, favorites, requests, addedProductions);
        }
        else if(accountType == AccountType.Regular){
            return new Regular(info, accountType, username, experience, notifications, favorites);
        }
        else{
            return null;
        }
    }
}
