package com.socialstartup.mockenger.core.web.controller.endpoint;

import com.socialstartup.mockenger.core.config.TestContext;
import com.socialstartup.mockenger.core.service.GroupService;
import com.socialstartup.mockenger.core.service.ProjectService;
import com.socialstartup.mockenger.core.util.CommonUtils;
import com.socialstartup.mockenger.data.model.dict.ProjectType;
import com.socialstartup.mockenger.data.model.persistent.mock.group.Group;
import com.socialstartup.mockenger.data.model.persistent.mock.project.Project;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.Iterator;

/**
 * Created by x079089 on 6/29/2015.
 */
@Ignore
@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration(classes = {TestContext.class})
public class AbstractControllerTest {
    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    private ProjectService projectService;

    @Autowired
    private GroupService groupService;

    private MockMvc mockMvc;

    protected static final String PROJECT_ID = "PROJECT_ID";
    protected static final String PROJECT_NAME_TEST = "Unit-test project";
    protected static final String GROUP_NAME_TEST = "Unit-test group";

    
    @Before
    public void setup() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.webApplicationContext).build();
    }


    protected Project getProject(String projectId) throws Exception {
        return this.projectService.findById(projectId);
    }

    protected Iterable<Project> getAllProjects() throws Exception {
        return this.projectService.findAll();
    }

    protected Project getAnyProject() throws Exception {
        return this.projectService.findAll().iterator().next();
    }

    protected void deleteProject(Project project) throws Exception {
        this.projectService.remove(project);
    }

    protected void deleteAllProjects() throws Exception {
        Iterator<Project> iterator = getAllProjects().iterator();
        while (iterator.hasNext()) {
            deleteProject(iterator.next());
        }
    }

    protected Project createProject() throws Exception {
        Project project = getNewProject();
        this.projectService.save(project);
        return project;
    }

    protected Project getNewProject() throws Exception {
        Project project = new Project(PROJECT_NAME_TEST, ProjectType.SIMPLE);
        project.setId(CommonUtils.generateUniqueId());
        return project;
    }

    // ===============
    // GROUP HELPERS
    // ===============

    protected Group getGroup(String groupId) throws Exception {
        return this.groupService.findById(groupId);
    }

    protected Iterable<Group> getAllGroups() throws Exception {
        return this.groupService.findAll();
    }

    protected void deleteAllGroups() throws Exception {
        Iterator<Group> iterator = getAllGroups().iterator();
        while (iterator.hasNext()) {
            deleteGroup(iterator.next());
        }
    }

    protected void deleteGroup(Group group) throws Exception {
        this.groupService.remove(group);
    }

    protected Group createGroup() throws Exception {
        Group group = getNewGroup();
        this.groupService.save(group);
        return group;
    }

    protected Group getNewGroup() throws Exception {
        Group group = new Group(PROJECT_ID, GROUP_NAME_TEST, true);
        group.setId(CommonUtils.generateUniqueId());
        return group;
    }
}
