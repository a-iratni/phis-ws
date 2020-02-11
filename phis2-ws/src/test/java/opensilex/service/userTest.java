//******************************************************************************
//                                userTest.java
// SILEX-PHIS
// Copyright © INRA 2019
// Creation date: Nov 22, 2019
// Contact: Expression userEmail is undefined on line 6, column 15 in file:///home/training/opensilex/phis-ws/phis2-ws/licenseheader.txt., anne.tireau@inra.fr, pascal.neveu@inra.fr
//******************************************************************************
package opensilex.service;


import javax.ws.rs.client.Entity;
import javax.ws.rs.core.Application;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import static junit.framework.TestCase.assertNotNull;
import opensilex.service.json.CustomJsonWriterReader;
import opensilex.service.resource.UserResourceService;
import opensilex.service.resource.VariableResourceService;
import opensilex.service.resource.dto.TokenDTO;
import opensilex.service.authentication.TokenResponseStructure;
import org.glassfish.jersey.client.ClientConfig;
import org.glassfish.jersey.jackson.JacksonFeature;
import org.glassfish.jersey.media.multipart.MultiPartFeature;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.test.JerseyTest;
import org.glassfish.jersey.test.TestProperties;
import static org.junit.Assert.assertEquals;
import org.junit.Test;

/**
 *
 * @author training
 */
public class userTest extends JerseyTest {

    @Override
    public Application configure() {
        enable(TestProperties.LOG_TRAFFIC);
        enable(TestProperties.DUMP_ENTITY);
        
        ResourceConfig resConf = new ResourceConfig
                                (
                                 UserResourceService.class
                                 ,VariableResourceService.class

                                
                                );

        resConf.register(MultiPartFeature.class);
        resConf.register(JacksonFeature.class);
        resConf.register(CustomJsonWriterReader.class);
//        resConf.register(SessionFactory.class);

        resConf.packages("io.swagger.jaxrs.listing;"
                + "opensilex.service.resource;"
                + "opensilex.service.json;"
                + "opensilex.service.resource.request.filter"
        );
        
        
         /* Annotation SessionInject to get current session and user
        Link between the object creator from the client request and the application 
        @see https://jersey.java.net/documentation/latest/ioc.html 
        resConf.register(new AbstractBinder() {
            @Override
            protected void configure() {
                
                // create session from given session id
                bindFactory(SessionFactory.class).to(Session.class);
                
                // Session injection from the defined type in SessionInjectResolver
                bind(SessionInjectResolver.class)
                        .to(new TypeLiteral<InjectionResolver<SessionInject>>() {
                        })
                        .in(Singleton.class);
            }
        });
        */

        return resConf;
    }
    
    
// Error reading entity from input stream.
//javax.ws.rs.ProcessingException
//	at org.glassfish.jersey.message.internal.InboundMessageContext.readEntity(InboundMessageContext.java:865)
    @Override
    protected void configureClient(ClientConfig config) {
        super.configureClient(config); //To change body of generated methods, choose Tools | Templates.
        config.register(CustomJsonWriterReader.class);

    }

    @Test
    public void getToken() {

        TokenDTO jsonToken = new TokenDTO();
        jsonToken.setGrant_type("password");
        jsonToken.setClient_id("123");
        jsonToken.setUsername("admin@opensilex.org");
        jsonToken.setPassword("21232f297a57a5a743894a0e4a801fc3");

        Entity<TokenDTO> userEntity = Entity.entity(jsonToken, MediaType.APPLICATION_JSON_TYPE);

        Response response = target("brapi/v1/token").request().post(userEntity); //Here we send POST request

        assertEquals("should return status 201 ( = CREATED)", 201, response.getStatus());
        assertNotNull("Should return TOKEN", response.getEntity().toString());

        TokenResponseStructure token = response.readEntity(TokenResponseStructure.class);
        String accessToken = token.getAccess_token();

        //-------------------
        Response response2 = target("users").request()
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken)
                .get();
        assertEquals("should return status 200", 200, response2.getStatus());
//  assertNotNull("Should return user list", response2.getEntity().toString());
        System.out.println(response2.getStatus());

        //----------------<<<<<<<<<<<<<<< NE MARCHE PAS !!!!!!!!!!!!!
        /*
        Response output = target("variables").request()
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken)
                .get();
        assertEquals("Should return status 200", 200, output.getStatus());
        System.out.println("variables GET");
        */
    }
      
    

}
