package com.riabchych.cloudstock.endpoint;

import com.riabchych.cloudstock.annotation.CrossOrigin;
import com.riabchych.cloudstock.annotation.Secured;
import com.riabchych.cloudstock.entity.Barcode;
import com.riabchych.cloudstock.entity.EnumRole;
import com.riabchych.cloudstock.service.BarcodeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import java.net.URI;
import java.util.List;

@Component
@Path("/barcode")
public class BarcodeEndpoint {

    private static final Logger logger = LoggerFactory.getLogger(BarcodeEndpoint.class);
    private final BarcodeService barcodeService;

    @Autowired
    public BarcodeEndpoint(BarcodeService barcodeService) {
        this.barcodeService = barcodeService;
    }

    @GET
    @CrossOrigin
    @Path("/")
    @Secured({
            EnumRole.ROLE_ADMIN,
            EnumRole.ROLE_USER,
            EnumRole.ROLE_OWNER
    })
    @Produces(MediaType.APPLICATION_JSON)
    public Response getBarcodeDetails() {
        List<Barcode> list = barcodeService.getAllBarcodes();
        return Response.ok(list).build();
    }

    @GET
    @CrossOrigin
    @Path("/{id}")
    @Secured({
            EnumRole.ROLE_ADMIN,
            EnumRole.ROLE_USER,
            EnumRole.ROLE_OWNER
    })
    public Response getBarcodeById(@PathParam("id") Long id) {
        Barcode barcode = barcodeService.getBarcodeById(id);
        return Response.ok(barcode).build();
    }

    @POST
    @CrossOrigin
    @Path("/")
    @Secured({
            EnumRole.ROLE_USER,
            EnumRole.ROLE_ADMIN,
            EnumRole.ROLE_OWNER
    })
    @Consumes(MediaType.APPLICATION_JSON)
    public Response addBarcode(Barcode barcode) {
        boolean isAdded = barcodeService.addBarcode(barcode);
        if (!isAdded) {
            logger.info("Barcode already exits.");
            return Response.status(Status.CONFLICT).build();
        }
        return Response.created(URI.create("/api/barcode/" + barcode.getId())).build();
    }

    @PUT
    @CrossOrigin
    @Path("/{id}")
    @Secured({
            EnumRole.ROLE_ADMIN,
            EnumRole.ROLE_OWNER
    })
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response updateBarcode(Barcode barcode) {
        barcodeService.updateBarcode(barcode);
        return Response.ok(barcode).build();
    }

    @DELETE
    @CrossOrigin
    @Path("/{id}")
    @Secured({
            EnumRole.ROLE_ADMIN,
            EnumRole.ROLE_OWNER
    })
    @Consumes(MediaType.APPLICATION_JSON)
    public Response deleteBarcode(@PathParam("id") Long id) {
        barcodeService.deleteBarcode(id);
        return Response.noContent().build();
    }
} 