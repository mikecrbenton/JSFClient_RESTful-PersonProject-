/*
 * $Id$
 * $Name$
 */

package org.usd.csci.person.jsf;

import java.io.Serializable;
import java.text.ParseException; //RIGHT MANAGED BEAN???
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import javax.ws.rs.core.Response; //IMPORT FOR HASHMAP??? 10/16/14
import org.json.JSONArray; //IMPORT FOR PERSONCLIENT
import org.json.JSONException;
import org.json.JSONObject;
import org.usd.csci.person.data.Person;
import static org.usd.csci.person.data.Person.BIRTH_DATE_KEY;
import org.usd.csci.person.data.PersonClient;

/**
 *
 * @author Mike Benton CSC470
 */
@ManagedBean(name = "personController")
@SessionScoped
public class PersonController implements Serializable {

    //MAINTAIN A LOCAL COPY OF PERSONREST LIST OF PERSONS
    Map<Integer,Person> persons = new HashMap<Integer,Person>();
    
    //THIS INSTANSIATES A PERSONCLIENT "OBJECT" SO YOU CAN ACCESS THE METHODS
    //AND RETRIEVE THE INFORMATION FROM THE RESTFUL SERVICE
    PersonClient client = new PersonClient();
    
    //THIS INSTANSIATES A PERSON TO ACCESS THE OBJECT FIELDS
    Person current = new Person();
    
    //STRING TO RECIEVE SEARCH FOR LASTNAME
    String lastNameSearch ;
    
    /**
     * Creates a new instance of PersonController
     */
    
    //CONSTRUCTOR
    public PersonController() {
        
        try{
            constructList(client.persons());
        }catch(JSONException ex){
            FacesMessage facesMsg = new FacesMessage(FacesMessage.SEVERITY_ERROR,
                                                    ex.getMessage(),"");
            FacesContext.getCurrentInstance().addMessage(null,facesMsg);   
        }catch(ParseException ex){
            FacesMessage facesMsg = new FacesMessage(FacesMessage.SEVERITY_ERROR,
                                                    ex.getMessage(),"");
            FacesContext.getCurrentInstance().addMessage(null,facesMsg);   
        }
    }
    
    private void constructList(String jsonString) throws JSONException,
                                                         ParseException{
        //JSON OBJECT TO RECEIVE JSON STRING
        JSONObject obj = new JSONObject(jsonString);
        
        //GET JSON ARRAY FROM JSONOBJECT
        JSONArray jlist = obj.getJSONArray("persons");
        
        for(int i = 0; i<jlist.length();i++){
            
            //IN LOOP TEMPORARY JSON OBJECT TO RECEIVE EACH INDIVIDUAL JSON OBJECT IN ARRAY
            JSONObject temp = jlist.getJSONObject(i);
            
            // IN LOOP TEMP PERSON OBJECT TO PUT INTO HASHMAP
            Person aPerson = new Person();
            
            //GET THE CURRENT VALUE OF ID FOR HASHMAP  
            int hashMapID = temp.getInt("id");
            
            aPerson.setId(temp.getInt("id"));
            aPerson.setFirstname(temp.getString("firstname"));
            aPerson.setLastname(temp.getString("lastname"));
            
            String birthdateString = temp.getString("birthdate");
            Date dateObject = new SimpleDateFormat("MM/dd/yyyy").parse(birthdateString); 
            aPerson.setBirthdate(dateObject);
            
            persons.put(hashMapID, aPerson);
        }
    }
    
    
    public void prepareEdit(int id) {
        current = persons.get(id);
        if(current == null){
            FacesMessage facesMsg = new FacesMessage(FacesMessage.SEVERITY_ERROR,
            "Person Not Located","");
            FacesContext.getCurrentInstance().addMessage(null, facesMsg);
        }
    }
    
    
    
    public void edit() {
        try {
            
            String id = current.getId() + ""; // + "" PLUS STRING-CONVERT TO STRING
            Response response = client.updatePerson(current.toJSON(), id);
            
            if(response.getStatus() == Response.Status.BAD_REQUEST.getStatusCode()){
                FacesMessage facesMsg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Bad Request","");
                FacesContext.getCurrentInstance().addMessage(null, facesMsg);
                return;
            }
            //RECONSTRUCT CLIENT PERSON LIST FROM SERVER LIST
            constructList(client.persons());
            
        }catch(JSONException ex){
            FacesMessage facesMsg = new FacesMessage(FacesMessage.SEVERITY_ERROR, ex.getMessage(),"");
            FacesContext.getCurrentInstance().addMessage(null, facesMsg);
        } catch(ParseException ex){
            FacesMessage facesMsg = new FacesMessage(FacesMessage.SEVERITY_ERROR, ex.getMessage(),"");
            FacesContext.getCurrentInstance().addMessage(null, facesMsg);
        }
    }
    
    public void prepareCreate() {
        
        //INSTANTIATE A NEW PERSON(OBJECT) SO THERE ARE NO PREVIOUS VALUES
        current= new Person();
    }

    public void create() {
        try {
            
            Response response = client.createPerson(current.toJSON());
            
            if(response.getStatus() == Response.Status.BAD_REQUEST.getStatusCode()) {
                FacesMessage facesMsg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Bad Request","");
                FacesContext.getCurrentInstance().addMessage(null, facesMsg);
                return;
            }
            
            //RECONSTRUCT CLIENT PERSON LIST FROM SERVER LIST
            constructList(client.persons());
            
        } catch(JSONException ex){
            FacesMessage facesMsg = new FacesMessage(FacesMessage.SEVERITY_ERROR, ex.getMessage(),"");
            FacesContext.getCurrentInstance().addMessage(null, facesMsg);
        } catch(ParseException ex){
            FacesMessage facesMsg = new FacesMessage(FacesMessage.SEVERITY_ERROR, ex.getMessage(),"");
            FacesContext.getCurrentInstance().addMessage(null, facesMsg);
        }
    }
    /**
     * prepareDelete    The function of this method is to receive the id from the
     *                  form, and update the current Person Object to the correct
     *                  user for the client if they choose to hit the delete button in 
     *                  the "Are You Sure" delete dialog box
     * 
     * @param id 
     */
    public void prepareDelete(int id) {
        current = persons.get(id);
        if(current == null){
            FacesMessage facesMsg = new FacesMessage(FacesMessage.SEVERITY_ERROR,
            "Person Not Located","");
            FacesContext.getCurrentInstance().addMessage(null, facesMsg);
        }
    }
    //WAS (int id) parameter before change: added preparedelete(int id)
    public void delete() {   
        try {
            
            String id = current.getId() + ""; // + "" PLUS STRING-CONVERT TO STRING 
            Response response = client.deletePerson(id);
            
            if(response.getStatus() == Response.Status.BAD_REQUEST.getStatusCode()
                    || response.getStatus() == Response.Status.NOT_FOUND.getStatusCode() ) {
                FacesMessage facesMsg = new FacesMessage(FacesMessage.SEVERITY_ERROR, 
                        "Unable To Locate Person","");
                FacesContext.getCurrentInstance().addMessage(null, facesMsg);
                return;
            }
            
            //RECONSTRUCT CLIENT PERSON LIST FROM SERVER LIST
            constructList(client.persons());
            
        } catch(JSONException ex){
            FacesMessage facesMsg = new FacesMessage(FacesMessage.SEVERITY_ERROR, ex.getMessage(),"");
            FacesContext.getCurrentInstance().addMessage(null, facesMsg);
        } catch(ParseException ex){
            FacesMessage facesMsg = new FacesMessage(FacesMessage.SEVERITY_ERROR, ex.getMessage(),"");
            FacesContext.getCurrentInstance().addMessage(null, facesMsg);
        }
    }
    
    //ADDED FOR ASSIGNMENT******************************************************
    public void search() {
        
        String field = Person.LAST_NAME_KEY;
        String lastname = lastNameSearch;
        
        try{
            
            constructList(client.persons(field,lastname));
         
        } catch(JSONException ex){
            FacesMessage facesMsg = new FacesMessage(FacesMessage.SEVERITY_ERROR, ex.getMessage(),"");
            FacesContext.getCurrentInstance().addMessage(null, facesMsg);
        } catch(ParseException ex){
            FacesMessage facesMsg = new FacesMessage(FacesMessage.SEVERITY_ERROR, ex.getMessage(),"");
            FacesContext.getCurrentInstance().addMessage(null, facesMsg);
        }   
    } 
    
    public PersonClient getClient() {
        return client;
    }

    public void setClient(PersonClient client) {
        this.client = client;
    }

    public Person getCurrent() {
        return current;
    }

    public void setCurrent(Person current) {
        this.current = current;
    }

    public List<Person> getPersons() {
        List<Person> list = new ArrayList(persons.values());
        return list;
    }
    
    public String getLastNameSearch() {
        return lastNameSearch;
    }

    public void setLastNameSearch(String lastNameSearch) {
        this.lastNameSearch = lastNameSearch;
    }
    
    
    
}//END OF CLASS


        
