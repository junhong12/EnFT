# EnFT

## Description

* **EnFT (Encrypted File Transfer)** is a secure file transfer system over the network. The intention is for the user to utilize a client to upload/download files to/from a server, where the transfer will be secure and encrypted. The system is implemented with a custom protocol that is designed to be more secure and more reliable than the UDP protocol, but faster than the TCP protocol. However, the system cannot ensure the same reliability as TCP (which checks if each individual packet has been sent/received correctly), but this is mitigated with the implementation of a custom packet manager. This packet manager will measure various metrics of the file and its packets to ensure that the file has been transferred successfully. As the system does not follow the protcols of TCP, it allows the custom protocol to be faster. 

* **EnFT** is implemented with an assymmetric encryption design. Upon initial setup, the user will generate RSA 2048-bit public/private keys for both the client and the server in order to accomodate for both secure uploads to the server and secure downloads from the server. 
  * In the case of uploading to the server, the sender(client) requests the public key of the reciever(server). With this public key, the sender wraps each packet of the file to be sent with the receiever's public key. Then as each packet is received by the reciever, the receiver decrypts each packet with its own private key until all packets have been recieved and the file can be generated and stored on the server. 
  
    * The aspect of asymmetric encryption that makes it secure is the fact that the packets encrypted with the public key cannot be decrypted without the corresponding private key. This means that even if someone was to be snooping on the network, they are unable to decrypt the packets because they do not have access to the reciever's private key which should be securely stored on the reciever's side. Of course they can access the public key when the sender and reciever "handshake" to exchange keys, but nothing can happen without the private key. 
    
   * In the case of downloading from the server, the process is similar to the above except the sender and receiver is switched. 

## Setup

* **Generating public/private keys(under Client/Server directory)**

  * > openssl genrsa -out private.pem 2048

  * > openssl pkcs8 -topk8 -in private.pem -outform DER -out private.der -nocrypt

  * > openssl rsa -in private.pem -pubout -outform DER -out public.der
  
* **Running Client/Server(initial)**
  * > javac Client.java
  * > javac Server.java
  
* **Running/Client/Server**
  * > java Client "insert host name here" "insert port number here"
  * > java Server "insert host name here" "insert port number here"
    * The Server must be the one to initialize the host name and port number to allow for the Client to successfully connect to it
    
* **Transferring Files**
  * Prompts are included during the execution of the program to help choose a file to transfer
