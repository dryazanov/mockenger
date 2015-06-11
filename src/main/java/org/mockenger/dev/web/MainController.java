package org.mockenger.dev.web;

import org.mockenger.dev.model.mocks.group.GroupEntity;
import org.mockenger.dev.service.GroupService;
import org.mockenger.dev.service.RequestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import static org.springframework.web.bind.annotation.RequestMethod.GET;


@Controller
public class MainController {

    private final String APPLICATION_JSONP_REQUEST_VALUE = "application/javascript";

    private final String APPLICATION_JSONP_RESPONSE_VALUE = "application/javascript;charset=UTF-8";

    private HttpHeaders responseHeaders = new HttpHeaders();

    @Autowired
    private GroupService groupService;

    @Autowired
    private RequestService requestService;


    public HttpHeaders getResponseHeaders() {
        return responseHeaders;
    }

    protected GroupService getGroupService() {
        return groupService;
    }

    public RequestService getRequestService() {
        return requestService;
    }


    /*@Autowired
    RequestOptions options;


    @ResponseBody
    @RequestMapping(value = {"/statistics"}, method = GET)
    public StatisticView getVisitors() {
        return new StatisticView(StatCollector.getInstance().getVisitors(),
                StatCollector.getInstance().getAverageRequestTime());
    }


    @ResponseBody
    @RequestMapping(value = {"/airports/{code}"}, method = GET)
    public AirportDetails getAirportByCode(@PathVariable String code) {
        return proxy.getAirportDetails(code);
    }


    @ResponseBody
    @RequestMapping(value = {"/request"}, method = GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public IDestinationFinder requestJson(@RequestParam(required = true) String origin,
                                     @RequestParam(required = true) String pos,
                                     @RequestParam(required = true) double minBudget,
                                     @RequestParam(required = true) double maxBudget,
                                     @RequestParam String orderBy, @RequestParam int page) {

        RequestOptions opts = options.getBuilder()
                .setOrigin(origin)
                .setPos(pos)
                .setMinBudget(minBudget)
                .setMaxBudget(maxBudget)
                .setOrderBy(orderBy)
                .setPage(page)
                .build();

        return proxy.getDestinations(opts);
    }


    @ResponseBody
    @RequestMapping(value = {"/request"}, method = GET)
    public ResponseEntity requestJsonp(@RequestParam(required = true) String origin,
                                     @RequestParam(required = true) String pos,
                                     @RequestParam(required = true) double minBudget,
                                     @RequestParam(required = true) double maxBudget,
                                     @RequestParam(required = true) String callback,
                                     @RequestParam String orderBy, @RequestParam int page) {

        IDestinationFinder df =
            proxy.getDestinations(
                options.getBuilder()
                    .setOrigin(origin)
                    .setPos(pos)
                    .setMinBudget(minBudget)
                    .setMaxBudget(maxBudget)
                    .setOrderBy(orderBy)
                    .setPage(page)
                    .build());

        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", APPLICATION_JSONP_RESPONSE_VALUE);

        return new ResponseEntity(new JSONPObject(callback, df), headers, HttpStatus.OK);
    }*/




    @RequestMapping(value = {"", "/", "/index"}, method = GET)
    public String redirectToWebUiIndex() {
        return "index";
    }



    /**
     *
     * @param groupId
     * @return
     */
    protected GroupEntity findGroupById(String groupId) {
        GroupEntity groupEntity = groupService.findById(groupId);

        if (groupEntity == null) {
            // TODO: Create and throw GroupNotFoundException
            throw new RuntimeException("Group with ID '" + groupId + "' not found");
        }

        return groupEntity;
    }
}
