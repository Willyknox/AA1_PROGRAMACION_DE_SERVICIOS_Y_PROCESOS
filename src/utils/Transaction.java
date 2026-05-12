package utils;

import java.util.Random;

public class Transaction {

    // class attributes are declared outside constructor/methods so they belong to the object
    private String from;
    private String to;
    private int amount;

    // constructors don't have return types (not even void or String) and take initial parameters
    public Transaction(String from, String to, int amount) {
        this.from = from;
        this.to = to;
        this.amount = amount;
    }

    public int getAmount() {
        return amount;
    }

    public String getFrom() {
        return from;
    }

    public String getTo() {
        return to;
    }

    // return type is the class itself 'Transaction' to return the new instance
    public static Transaction generateRandom() {
        // arrays use square brackets [] in the data type declaration
        String[] namesArray = {"Federico", "Fernando", "Maria", "José", "Luis", "Pedro", "Juan", "Ana", "Elena", "Carlos"};
        
        // Random is a class, we need to instantiate an object to use its methods
        Random random = new Random();
        
        String from = namesArray[random.nextInt(namesArray.length)];
        String to = namesArray[random.nextInt(namesArray.length)];
        
        // avoid sending money to oneself
        while (from.equals(to)) {
            to = namesArray[random.nextInt(namesArray.length)];
        }
        
        // +1 to avoid 0 amount transactions
        int amount = random.nextInt(100) + 1; 
        
        return new Transaction(from, to, amount);
    }

    @Override
    public String toString() {
        // compact format "from->to:amount" easy to send via sockets and hash
        return from + "->" + to + ":" + amount;
    }

}
