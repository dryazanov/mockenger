package com.socialstartup.mockenger.frontend.controller;

import com.socialstartup.mockenger.core.web.controller.base.AbstractController;
import com.socialstartup.mockenger.data.model.dict.RequestMethod;
import com.socialstartup.mockenger.data.model.persistent.mock.group.Group;
import com.socialstartup.mockenger.data.model.persistent.mock.request.AbstractRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;


@Controller
public class MainController extends AbstractController {

    /**
     * Logger
     */
    private static final Logger LOG = LoggerFactory.getLogger(MainController.class);

    @RequestMapping(value = {"", "/", "/index"}, method = GET)
    public String indexPage() {
        return "index";
    }

    @RequestMapping(value = {"/groups/{groupId}/view"}, method = GET)
    public String groupPage(@PathVariable(value = "groupId") String groupId, Model model) {
        Group group = findGroupById(groupId);

        if (group != null) {
            model.addAttribute("groupId", groupId);
            return "group/view";
        } else {
            return "404";
        }
    }

    @ModelAttribute("requestEntity")
    public AbstractRequest requestEntity() {
        return new AbstractRequest();
    }

    @RequestMapping(value = {"/requests/{requestId}/view"}, method = GET)
    public String requestEntityViewPage(@PathVariable(value = "requestId") String requestId, Model model) {
        AbstractRequest abstractRequest = getRequestService().findById(requestId);

        model.addAttribute("requestId", requestId);
        model.addAttribute("requestEntity", abstractRequest);
        model.addAttribute("requestMethods", RequestMethod.values());

        return "request/view";
    }

    @RequestMapping(value = {"/requests/{requestId}/save"}, method = POST)
    public String requestEntitySave(@PathVariable(value = "requestId") String requestId, AbstractRequest abstractRequest, Model model) {
        LOG.debug(abstractRequest.toString());

        return "request/view";
    }
}
