package org.oskari.example;

import fi.nls.oskari.annotation.OskariActionRoute;
import fi.nls.oskari.control.*;
import fi.nls.oskari.log.LogFactory;
import fi.nls.oskari.log.Logger;
import fi.nls.oskari.util.ResponseHelper;

/**
 * Dummy Rest action route
 */
@OskariActionRoute("MyAction")
public class MyActionHandler extends RestActionHandler {

    private static final Logger LOG = LogFactory.getLogger(MyActionHandler.class);

    public void preProcess(ActionParameters params) throws ActionException {
        // common method called for all request methods
        LOG.info(params.getUser(), "accessing route", getName());
    }

    @Override
    public void handleGet(ActionParameters params) throws ActionException {
        ResponseHelper.writeResponse(params, "Hello " + params.getUser().getFullName());
    }

    @Override
    public void handlePost(ActionParameters params) throws ActionException {
        throw new ActionException("This will be logged including stack trace");
    }

    @Override
    public void handlePut(ActionParameters params) throws ActionException {
        throw new ActionParamsException("Notify there was something wrong with the params");
    }

    @Override
    public void handleDelete(ActionParameters params) throws ActionException {
        throw new ActionDeniedException("Not deleting anything");
    }


}
