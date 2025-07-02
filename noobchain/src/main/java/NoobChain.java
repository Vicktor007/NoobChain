package src.main.java;

import com.google.gson.GsonBuilder;

import java.security.Security;
import java.util.ArrayList;

public class NoobChain {

    public static ArrayList<Block> blockChain = new ArrayList<Block>();
    public static int difficulty = 5;
    public static Wallet walletA;
    public static Wallet walletB;

    public  static void main(String[] args) {

        // Setup bouncey castle as a security provider
        Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());

        // create the new wallets
        walletA = new Wallet();
        walletB = new Wallet();

        //Test public and private keys
        System.out.println("Public key: " + walletA.publicKey);
        System.out.println("Private key: " + walletA.privateKey);

        //create a test transaction from walletA to walletB
       Transaction transaction = new Transaction(walletA.publicKey, walletB.publicKey, 5, null);
       transaction.generateSignature(walletA.privateKey);
       // verify the signature works and verify it from the public key
        System.out.println("is signature verified? :");
        System.out.println("Transaction signature: " + transaction.verifySignature());


        blockChain.add(new Block("I'm the first block", "0"));
        System.out.println("Trying to mine block 1");
        blockChain.get(0).mineBlock(difficulty);

        blockChain.add(new Block("I'm the second block", blockChain.get(blockChain.size()-1).hash));
        System.out.println("Trying to mine block 2");
        blockChain.get(1).mineBlock(difficulty);

        blockChain.add(new Block("I'm the third block", blockChain.get(blockChain.size()-1).hash));
        System.out.println("Trying to mine block 3");
        blockChain.get(2).mineBlock(difficulty);

        System.out.println("\nBlockchain is valid: " + isChainValid());

        String blockChainJson = new GsonBuilder().setPrettyPrinting().create().toJson(blockChain);
        System.out.println("\nThe blockchain is: ");
        System.out.println(blockChainJson);



    }
    public static Boolean isChainValid(){

        Block currentBlock;
        Block previousBlock;
        String hashTarget = new String(new char[difficulty]).replace('\0', '0');

//        loop through blockchain to check hashes:
        for(int i = 1; i < blockChain.size(); i++){
            currentBlock = blockChain.get(i);
            previousBlock = blockChain.get(i-1);
//            compare registered hash and calculated hash:
            if(!currentBlock.hash.equals(currentBlock.calculateHash())){
                System.out.println("current block hashes not equal");
                return false;
            }

//            compare previous hash and registered previous hash
            if (!previousBlock.hash.equals(currentBlock.previousHash)) {
                System.out.println("previous block hashes not equal");
                return false;
            }

            if (!currentBlock.hash.substring(0, difficulty).equals(hashTarget)) {
                System.out.println("This block has not been mined");
                return false;
            }
        }
        return true;
    }
}
