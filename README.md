File Crypt
Simple software to encrypt files with AES-256.

How to use
Download the last release here


//Encryption

filecrypt –enc –in <input file> -out <output file> -pass yourpassword

(also work with directory)

//Decryption

filecrypt –enc –in <input file> -out <output file> -pass samepassword


//How to build
Clone the repository
git clone


//Build
cd FileCrypt/src

javac -cp ../out/lib/*.jar CryptoAES.java

javac Main.java

jar cvfm ../out/FileCrypt.jar META-INF/MANIFEST.MF *.class
FileCrypt.jar is located in the "out" folder


Build with a different version of Bouncy Castle
Replace the file "out/lib/bcprov-jdk15on-159.jar" with the new one.

Edit the manifest in "src/META-INF/MANIFEST.MF"

Manifest-Version: 1.0
Main-Class: Main
Class-Path: lib/<THE_NEW_FILE.jar>

//How to use it 

Put FileCrypt.jar and lib directory (located in the out directory) in the directory where u want to encrypt or decrypt a file.
Open a terminal in this directory and type filecrypt –enc|-dec –in <input file> -out <output file> -pass yourpassword

//How it works
Generate 64 secure random bytes as Salt
Generate 12 secure random bytes as IV
Generate Encryption Key (EK) with PBKDF2
EK = PBKDF2(password, Salt, 50000, 256)
Encrypt/Decrypt with AES/GCM/NoPadding

During directory encryption a new Salt, IV and EK are generated for each file.

Built With
Bouncy Castle - Java implementation of cryptographic algorithms.

License
This project is licensed under the Apache License 2.0 - see the LICENSE file for details.

This software has been made for personal and educational purposes.

Source inspiration : Alexzava
