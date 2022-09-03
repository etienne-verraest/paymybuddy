# Pay My Buddy
Pay My Buddy is a web application that allows users to transfer money to others users, called "buddies".
**The technical stack of this application consist of** :
- Java and Spring Boot Framework for the back-end logic
- Thymeleaf and Boostrap v5.2 for the front-end design
- MySQL for the database management system

<br>

## UML Class Diagram
![uml](./readme/paymybuddy-uml.png)

## Physical Data Model
![pdm](./readme/paymybuddy-mpd.png)

## Creating the database
In order to get started, you need to follow these steps :
1. Create a new database called `paymybuddy` with the following command : `CREATE DATABASE paymybuddy;`
2. Import the `schema.sql` file (in the main/resources folder) to create the tables
3. (Optional) Import the `data.sql` in your newly created database to have a set of data and try the application directly (*refer to 'Demo of Application' section for more information*)

## Externalized MySQL configuration
(When implemented, explain how to connect the application to the database with the settings.xml file)

## Application.properties
You can custom the application by editing the `application.properties` file in the main/resources folder.
1. `server.port= [number]` : sets the local port of the web application
2. `transactions.itemsPerPages = [number]` : sets the number of transactions per pages shown on the homepage

## Demo of the application
**Import the `datas.sql` file (in the main/resources folder) to import demonstration datas**
There are 3 users, with pre-filled fields. The main account is the number 1, from there you will be able to see transactions, bank account and connections
The 2 others users are used as "buddies" and don't have everything filled
1. Stéphane Robert (s.robert@localhost.fr:password), connected to user 2 and 3
2. Alice Vaillancour (a.vaillancour@localhost.fr:password), connected to user 1
3. Leo Luzzi (l.luzzi@localhost.fr:password), connected to user 1