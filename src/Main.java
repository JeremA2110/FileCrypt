import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;


public class Main {
    public static void main(String[] args) throws IOException, InterruptedException {


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

        //checking if there is / at the end of directory's name
        if(args[2].charAt(args[2].length() - 1) == '/'){
            args[2] = args[2].substring(0, args[2].length() - 1);
            filename = args[2];
        }
        else{
            filename = args[2];
        }

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

        //Checking if the password is only composed of lowercase, uppercase letters and numbers
        if (!args[6].matches("^[a-zA-Z0-9]+$")){
            System.out.println("password have to be composed only of lowercase, uppercase letters and numbers");
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
                //create directory for put file encrypted
                File outdir= new File(outputfile);
                boolean isCreated = outdir.mkdir();
                String PathNameOut= outdir.getAbsolutePath();
                System.out.println(PathNameOut);


                File[] dirContent = file.listFiles();
                assert dirContent != null;
                for(File f : dirContent)
                {
                    if(f.isFile() && !f.getName().contains(".enc"))
                    {
                        System.out.println("\nFile: " + f.getName());
                        CryptoAES.EncryptDirectory(f.getAbsolutePath(), password);

                        movefile(PathNameOut, f);

                    }
                    if(f.isFile() && f.getName().contains(".enc"))
                    {
                        movefile2(PathNameOut, f);
                    }
                }
                //creating zip file with all files encrypted in
                String[] repertok  = CollectFile(outputfile);
                creationZip(repertok,outputfile);
            }
            else //if file is a file
                CryptoAES.Encrypt(filename, password, outputfile);
        }
        else  //Decryption Mode
            {
            if(file.isDirectory())
            {

                File outdir= new File(outputfile);
                boolean isCreated = outdir.mkdir();
                String PathNameOut= outdir.getAbsolutePath();
                System.out.println(PathNameOut);

                File[] dirContent = file.listFiles();
                assert dirContent != null;
                for (File f : dirContent)
                {
                    if (f.isFile() && f.getName().contains(".enc"))
                    {
                        System.out.println("\nFile: " + f.getName());
                        CryptoAES.DecryptDirectory(f.getAbsolutePath(), password);
                        movefile3(PathNameOut, f);
                    }
                }
            }
            else
                CryptoAES.Decrypt(filename, password, outputfile);
        }
    }

    private static void movefile(String PathOutDir, File f) throws IOException {
        String PathNameIn= f.getAbsolutePath()+".enc";
        Path in = Paths.get(PathNameIn);

        PathOutDir = PathOutDir + "/"+f.getName()+".enc";
        Path out = Paths.get(PathOutDir);

        Files.move(in, out);
    }

    private static void movefile2(String PathOutDir, File f) throws IOException {
        String PathNameIn= f.getAbsolutePath();
        Path in = Paths.get(PathNameIn);

        PathOutDir = PathOutDir + "/"+f.getName();
        Path out = Paths.get(PathOutDir);

        Files.move(in, out);
    }

    private static void movefile3(String PathOutDir, File f) throws IOException {
        String PathNameIn= f.getAbsolutePath();
        PathNameIn = PathNameIn.replace(".enc", "");
        Path in = Paths.get(PathNameIn);


        PathOutDir = PathOutDir + "/"+f.getName();
        PathOutDir = PathOutDir.replace(".enc", "");
        Path out = Paths.get(PathOutDir);

        Files.move(in, out);
    }

    private static String[] CollectFile(String filename) {

        ArrayList<String> repert = new ArrayList<String>();

        File file = new File(filename);

        File[] dirContent = file.listFiles();
        assert dirContent != null;
        for (File f : dirContent)
        {
            if (f.isFile())
            {
                String s1 = f.getPath();
                repert.add(s1); // add the new path of file
            }
        }

        String[] repertok = new String[repert.size()];
        repert.toArray(repertok);
        return repertok;
    }


    private static void creationZip(String[] repertoire, String outputname)
    {
        int BUFFER = 2048;
        try {
            BufferedInputStream buffi = null; // entry buffer
            byte[] data = new byte[BUFFER]; // exit buffer
            FileOutputStream dest = new FileOutputStream(outputname+".zip"); // final zip file
            BufferedOutputStream buff = new BufferedOutputStream(dest); // Creation of the exit buffer
            ZipOutputStream out = new ZipOutputStream(buff); //Write flow Zip, through the buffer
            out.setMethod(ZipOutputStream.DEFLATED); // Compression mode
            out.setLevel(9); // compression ratio

            for (String s : repertoire) {

                FileInputStream deb = new FileInputStream(s);
                buffi = new BufferedInputStream(deb, BUFFER);
                ZipEntry entry = new ZipEntry(s);
                out.putNextEntry(entry); // Assign entry to the output stream

                int count;
                while ((count = buffi.read(data, 0, BUFFER)) != -1) {
                    out.write(data, 0, count);
                }
                buffi.close();
            }
            out.close();

        } catch(Exception e) {
            e.printStackTrace();
        }
    }
}