package edu.neu.ccs.cs5500.chucknorris.messenger.exception;

import edu.neu.ccs.cs5500.chucknorris.messenger.model.ErrorMessage;
import org.glassfish.jersey.message.internal.MediaTypes;

import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

/**
 * Created by yoganandc on 6/3/16.
 */

@Provider
public class GenericExceptionMapper implements ExceptionMapper<Throwable> {

    @Override
    public Response toResponse(Throwable ex) {
        if(ex instanceof WebApplicationException) {
            return ((WebApplicationException) ex).getResponse();
        }
        ErrorMessage msg = new ErrorMessage(ex.getMessage(), 500, "http://chucknorris-msd.herokuapp.com/");
        return Response.status(Response.Status.INTERNAL_SERVER_ERROR).type(MediaType.APPLICATION_JSON).entity(msg).build();
    }

}
