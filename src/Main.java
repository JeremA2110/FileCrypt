import java.io.*;
import java.util.Arrays;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {


        String op, filename, outputfile;
        Scanner sc = new Scanner(System.in);


        //choice between encryption mode or decryption mode in argument[0]
        if(!args[0].equals("-enc") && !args[0].equals("-dec"))
        {
            System.out.println("wrong mode : choose -enc or -dec mode");
            System.exit(1);
        }
        op = args[0];


        //Argument[1] and Argument[2] are to put an input file
        if(!args[1].equals("-in") && !args[2].isEmpty() )
        {
            System.out.println("Input File error");
            System.exit(1);
        }
        filename = args[2];

        //checking if the file exist. If it does not. Programme exit
        File file = new File(filename);
        if(!file.exists())
        {
            System.out.println("Invalid File");
            System.exit(1);
        }

        //Argument[3] and Argument[4] are to put an output file name
        if (!args[3].equals("-out") && !args[4].isEmpty()) {
            System.out.println("Output file error");
            System.exit(1);
        }

        //checking if the output file name is already use, if yes asking if you want to overwrite it.
        outputfile = args[4];
        File outfile = new File(outputfile);
        File outfilecheck = new File(outputfile + ".enc");


        //in ecryption mode
        if(outfilecheck.exists() && args[0].equals("-enc"))
        {
            System.out.println("output file name already exist do you want to overwrite it ? (Y/N)");
            String resp = sc.nextLine();
            if(!resp.contains("y") && !resp.contains("Y"))
                System.exit(1);
        }

        //in decryption mode
        if(outfile.exists() && args[0].equals("-dec"))
        {
            System.out.println("output file name already exist do you want to overwrite it ? (Y/N)");
            String resp = sc.nextLine();
            if(!resp.contains("y") && !resp.contains("Y"))
                System.exit(1);
        }

        //Argument[5] and Argument[6] are to put a password to encrypt and decrypt a file. have to be the same, for have the good result.
        if (!args[5].equals("-pass") && !args[6].isEmpty()) {
            System.out.println("Password error");
            System.exit(1);
        }
        char[] password = args[6].toCharArray();


        //Encryption mode
        if(op.equals("-enc"))
        {
            //checking password security
            if(password.length < 10)
            {
                System.out.println("WARNING! The password is short. A password of at least 10 characters is recommended.\nAre you sure you want to continue? (Y/N)");
                String resp = sc.nextLine();
                if(!resp.contains("y") && !resp.contains("Y"))
                    System.exit(1);
            }

            System.out.println("Retype password: ");
            char[] rePassword = sc.nextLine().toCharArray();

            //verifying the 2 passwords
            if(!Arrays.equals(password, rePassword))
            {
                System.out.println("The passwords are different.");
                System.exit(1);
            }

            //if file is a directory
            if(file.isDirectory())
            {
                File[] dirContent = file.listFiles();
                assert dirContent != null;
                for(File f : dirContent)
                {
                    if(f.isFile() && !f.getName().contains(".enc"))
                    {
                        System.out.println("\nFile: " + f.getName());
                        CryptoAES.Encrypt(f.getAbsolutePath(), password, outputfile);
                    }
                }
            }
            else //if file is a file
                CryptoAES.Encrypt(filename, password, outputfile);
        }
        else  //Decryption Mode
            {
            if(file.isDirectory())
            {
                File[] dirContent = file.listFiles();
                assert dirContent != null;
                for (File f : dirContent)
                {
                    if (f.isFile() && f.getName().contains(".enc"))
                    {
                        System.out.println("\nFile: " + f.getName());
                        CryptoAES.Decrypt(f.getAbsolutePath(), password, outputfile);
                    }
                }
            }
            else
                CryptoAES.Decrypt(filename, password, outputfile);
        }
    }
}