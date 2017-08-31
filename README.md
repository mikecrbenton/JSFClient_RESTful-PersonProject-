# JSFClient_RESTful-PersonProject-
CSC 470 Software Engineering // A introduction to a RESTful client that accesses RESTful_API(PersonProject)

## Assignment Directive:
You will create a RESTful service that will allow clients to store person object in a list of persons that we will keep in memory
(i.e. will not include a persistent database).  We want to be able to locate persons by Id. Therefore, we will store our Person objects 
in a HashMap ( Recall that a  HashMap is a data structure, based on hashing, which allows you to store object as key value pair with 
the primary advantage is that you can retrieve object on constant time i.e. O(1), if you know the key).  

Your RESTful service class will be a Singleton, meaning that there will be one instance of the class to service all clients. 
Because we need to worry about multiple clients accessing our person list at the same time, we will implement it as a ConcurrentHashMap. 
ConcurrentHashMap is introduced as an alternative of HashMap and provided all functions supported by HashMap with additional feature 
called "concurrency level", which allows ConcurrentHashMap to partition Map. ConcurrentHashMap allows multiple readers to read concurrently
without any blocking. This is achieved by partitioning Map into different parts based on concurrency level and locking only a portion of 
Map during updates. 

Your service will auto-generate the person id’s before they are placed in the list. Again, because we have to worry about multiple clients 
entering person data simultaneously, we will use an AtomicInteger to represent our person id. The AtomicInteger class has a number of uses,
but one is a drop-in replacement for an atomic counter. The significant feature of AtomicInteger which we exploit is the call to 
incrementAndGet() which allows us to increment the id and get it in one atomic instruction execution preventing concurrency issues with 
other clients.

Your service will be very simple. Clients will be able to **add** persons to the list, **update** persons in the list, **delete** persons
from the list, **get a list of all persons** and **get a person by id**:

## Code In Project Written:
* JSFClient_RESTful-PersonProject-/src/main/java/org/usd/csci/person/data/**Person.java**
  * Create constants to represent JSON keys for a Person Object
  * Create a method in the Person class "toJSON()" that returns a string representing a person in JSON format
* JSFClient_RESTful-PersonProject-/src/main/java/org/usd/csci/person/data/**PersonClient.java**
  * Add a GET method "Persons()" which will return all persons - Line 60 
* JSFClient_RESTful-PersonProject-/src/main/java/org/usd/csci/person/jsf/**PersonController.java**
  * Write a "constructList()" method that takes a String representing a list of Persons and adds Persons to a HashMap


