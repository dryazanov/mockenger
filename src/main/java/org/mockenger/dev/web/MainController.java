package org.mockenger.dev.web;

import org.mockenger.dev.model.mock.group.GroupEntity;
import org.mockenger.dev.model.mock.request.IRequestEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import static org.springframework.web.bind.annotation.RequestMethod.GET;


@Controller
public class MainController extends CommonController {

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

    @RequestMapping(value = {"/requests/{requestId}/view"}, method = GET)
    public String requestPage(@PathVariable(value = "requestId") String requestId, Model model) {
//        GroupEntity groupEntity = findGroupById(groupId);

        IRequestEntity requestEntity = getRequestService().findById(requestId);

        if (requestId != null) {
            model.addAttribute("requestId", requestId);
            return "request/view";
        } else {
            return "404";
        }
    }
}
