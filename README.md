# Space Flights

https://myspacetrip.herokuapp.com/login

## Description

The application has been created to manage space travel. There are three entitie: Flight, Passenger and User.
Entities Flight and Passenger are linked each other with many to many relationship.

### Database architecture:

![image](src/main/resources/static/Selection_004.png)

### Security:

Application demands user authentication. After successful authentication, the application generates the JSON web token for authorization every request using the SHA-256 Secure Hash Algorithm.

![image](src/main/resources/static/Selection_005.png)

The user password is encrypted using the BCryptPasswordEncoder.


![image](src/main/resources/static/Selection_006.png)

### Application has several functionalities:
* create new fligth
* create new passenger
* update flight or passenger
* assign passenger to flight or flight to passenger
* delete flight from passenger account
* delete passenger from specific flight
* display list of flights or passengers
* searching for flight
* Add and update user - app does not send current password in request response.

### Applied technologies:
#### Backend:
* Java
* Spring boot
* Spring security + JWT
* MySQL
* Hibernate
* Maven

#### Frontend:
* React.js 
* CSS 
* Bootstrap

### To do:
* add two factor authentication with phone number
