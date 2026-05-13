import utils.HashUtils;
import utils.Transaction;

public class Main {
    public static void main(String[] args) {
        // generate a random transaction to simulate network activity
        Transaction tx = Transaction.generateRandom();
        System.out.println("Transacción a minar: " + tx);

        // test how adding a different salt changes the entire resulting hash completely
        String data = tx.toString();


        // test to check if the hash changes

        // try hashing with salt 0
        String hash0 = HashUtils.sha256(data + 0);
        System.out.println("Hash con salt 0: " + hash0);

        // try hashing with salt 1
        String hash1 = HashUtils.sha256(data + 1);
        System.out.println("Hash con salt 1: " + hash1);
    }
}
