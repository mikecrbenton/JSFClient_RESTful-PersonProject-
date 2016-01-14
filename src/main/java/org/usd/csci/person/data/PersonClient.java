/*
 * $Id$
 * $Name$
 */

package org.usd.csci.person.data;

import java.text.MessageFormat;
import javax.ws.rs.ClientErrorException;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Form;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * Jersey REST client generated for REST resource:PersonResource [/persons]<br>
 * USAGE:
 * <pre>
 *        PersonClient client = new PersonClient();
 *        Object response = client.XXX(...);
 *        // do whatever with response
 *        client.close();
 * </pre>
 *
 * @author Mike Benton CSC470
 */
public class PersonClient {
    private WebTarget webTarget;
    private Client client;
    private static final String BASE_URI = "http://localhost:8080/PersonRest/webresources";

    public PersonClient() {
        client = ClientBuilder.newClient();
        webTarget = client.target(BASE_URI).path("persons");
    }

    public Response deletePerson(String id) throws ClientErrorException {
        WebTarget resource = webTarget;
        resource = resource.path(MessageFormat.format("{0}", new Object[]{id}));
        return resource.request(MediaType.APPLICATION_JSON).delete(Response.class);
    }

    public String persons(String id) throws ClientErrorException {
        WebTarget resource = webTarget;
        resource = resource.path(MessageFormat.format("{0}", new Object[]{id}));
        return resource.request(MediaType.APPLICATION_JSON).get(String.class);
    }
    
    
    public String persons() throws ClientErrorException {
        WebTarget resource = webTarget;
        return resource.request(MediaType.APPLICATION_JSON).get(String.class);
        
        
    }
    
    //ADDED FOR ASSIGNMENT- SEARCH: OVERLOADED METHOD
    public String persons(String field, String lastname) throws ClientErrorException {
        
        Form form = new Form();
        form.param( field, lastname);
        WebTarget resource = webTarget;
        resource = resource.queryParam( field, lastname);
        return resource.request(MediaType.APPLICATION_JSON).get(String.class);
        
    }
    
    public Response createPerson(Object requestEntity) throws ClientErrorException {
        return webTarget.request(MediaType.APPLICATION_JSON).post(Entity.entity(requestEntity, MediaType.APPLICATION_JSON), Response.class);
    }

    public Response updatePerson(Object requestEntity, String id) throws ClientErrorException {
        return webTarget.path(java.text.MessageFormat.format("{0}", new Object[]{id})).request(MediaType.APPLICATION_JSON).put(Entity.entity(requestEntity, MediaType.APPLICATION_JSON), Response.class);
    }

    public void close() {
        client.close();
    }
    
}
