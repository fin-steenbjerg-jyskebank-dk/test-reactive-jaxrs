package dk.jyskebank.test;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

@Provider
public class RuntimeExceptionMapper implements ExceptionMapper<RuntimeException> {
    private static final Logger LOG = LoggerFactory.getLogger(RuntimeExceptionMapper.class);
    @Override
    public Response toResponse(RuntimeException exception) {
        LOG.info("Inside mapper {}", exception.getClass().getName(), exception);

        if (exception instanceof WebApplicationException e) {
            LOG.info("Status = {}, Message: {}", e.getResponse().getStatus(), e.getMessage());
            return Response
                .status(e.getResponse().getStatus())
                .entity(new ExceptionInformation("Unexpected exception", e))
                .type(ExceptionInformation.MEDIA_TYPE)
                .build();
        } else {
            return Response
                .status(Status.INTERNAL_SERVER_ERROR)
                .entity(new ExceptionInformation("Unexpected exception", exception))
                .type(ExceptionInformation.MEDIA_TYPE)
                .build();
        }
    }
}