package ru.sergal.testtask.restapi.restful;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.sergal.testtask.localization.MessageLocalization;
import ru.sergal.testtask.restapi.restful.dto.MessageOut;
import ru.sergal.testtask.restapi.restful.dto.RequestBirthdayRestOut;
import ru.sergal.testtask.restapi.restful.dto.RequestGetResultOut;
import ru.sergal.testtask.service.BirthdayService;
import ru.sergal.testtask.service.dto.BirthdayResult;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.stream.Collectors;

@Component
@Path("/")
@Produces(MediaType.APPLICATION_JSON)
public class BirthdayResource {

    private static final Logger log = LoggerFactory.getLogger(BirthdayResource.class);

    @Autowired
    private MessageLocalization loc;

    @Autowired
    private BirthdayService service;

    @POST
    @Path("requestBirthday{noop: (/)?}{month: ((?<=/)\\d+)?}")
    public Response requestBirthday(@PathParam("month") Integer month) {
        log.info("Processing requestBirthday request for month {}", month);
        if ((month != null) && (month <= 0 || month > 12)) {
            log.error("Wrong month in query: {}",month);
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(MessageOut.withMessage(loc.getMessage("invalid.month.message")))
                    .build();
        }
        String uuid = service.startBirthdayCalc(month);
        return Response.ok().entity(new RequestBirthdayRestOut(uuid)).build();
    }

    @GET
    @Path("getResult")
    public Response getResult(@QueryParam("uuid") String uuid) {
        log.info("Processing getResult request for uuid {}", uuid);
        if (uuid == null) {
            log.error("Missing UUID in query");
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(MessageOut.withMessage(loc.getMessage("missing.uuid.message")))
                    .build();
        }
        BirthdayResult result = service.getBirthdayResult(uuid);
        switch (result.getStatus()) {
            case NONE:
                log.warn("Nonexisting UUID in query: {}", uuid);
                return Response.status(Response.Status.NOT_FOUND)
                        .entity(MessageOut.withMessage(loc.getMessage("state.none.message", uuid)))
                        .build();
            case IN_PROGRESS:
                log.info("Task {} still in progress", uuid);
                return Response.status(Response.Status.ACCEPTED)
                        .entity(MessageOut.withMessage(loc.getMessage("state.inprogress.message", uuid)))
                        .build();
            case ERROR:
                log.error("Some error was generated on task {}", uuid);
                return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                        .entity(MessageOut.withMessage(loc.getMessage("state.error.message", uuid)))
                        .build();
            case DONE:
            default:
                log.info("getResult query return the results");
                return Response.status(Response.Status.OK)
                        .entity(result.getBirthdayList().stream()
                        .map(b -> new RequestGetResultOut(b.getName(), b.getDays()))
                        .collect(Collectors.toList()))
                    .build();
        }
    }

}
