package com.socialstartup.mockenger.frontend.controller.web;

import com.socialstartup.mockenger.frontend.controller.CommonController;
import com.socialstartup.mockenger.model.mock.group.GroupEntity;
import com.socialstartup.mockenger.model.mock.request.IRequestEntity;
import com.socialstartup.mockenger.model.mock.request.RequestEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;


@Controller
public class MainController extends CommonController {

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
        GroupEntity groupEntity = findGroupById(groupId);

        if (groupEntity != null) {
            model.addAttribute("groupId", groupId);
            return "group/view";
        } else {
            return "404";
        }
    }

    @ModelAttribute("requestEntity")
    public RequestEntity requestEntity() {
        return new RequestEntity();
    }

    @RequestMapping(value = {"/requests/{requestId}/view"}, method = GET)
    public String requestEntityViewPage(@PathVariable(value = "requestId") String requestId, Model model) {
        IRequestEntity requestEntity = getRequestService().findById(requestId);

        model.addAttribute("requestId", requestId);
        model.addAttribute("requestEntity", requestEntity);
        model.addAttribute("requestMethods", RequestMethod.values());

        return "request/view";
    }

    @RequestMapping(value = {"/requests/{requestId}/save"}, method = POST)
    public String requestEntitySave(@PathVariable(value = "requestId") String requestId, RequestEntity requestEntity, Model model) {
        LOG.debug(requestEntity.toString());

        return "request/view";
    }
}
