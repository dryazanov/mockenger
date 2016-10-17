package com.socialstartup.mockenger.core.service;

import com.socialstartup.mockenger.core.service.account.AccountService;
import com.socialstartup.mockenger.data.model.dict.ProjectType;
import com.socialstartup.mockenger.data.model.dict.RoleType;
import com.socialstartup.mockenger.data.model.persistent.account.Account;
import com.socialstartup.mockenger.data.model.persistent.mock.group.Group;
import com.socialstartup.mockenger.data.model.persistent.mock.project.Project;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Scanner;

/**
 * @author Dmitry Ryazanov
 */
@Component
@Profile("init")
public class InitialDataLoader implements CommandLineRunner {

	private final Logger LOG = LoggerFactory.getLogger(InitialDataLoader.class);

	private final static String CONFIRM_STEP = "y";

	@Autowired
	private AccountService accountService;

	@Autowired
	private ProjectService projectService;

	@Autowired
	private GroupService groupService;

	@Autowired
	private EventService eventService;

	@Autowired
	private PasswordEncoder passwordEncoder;



	@Override
	public void run(final String... arguments) throws Exception {
		System.out.println("!!!ATTENTION!!!");
		System.out.println("You've activated initial data loader and we gonna ask a few questions");
		System.out.println("Please type '" + CONFIRM_STEP + "' every time you want to process, or type any other character to skip the step");
		System.out.print("Do you want to start initialization process? ");

		try {
			// create a scanner so we can read the command-line input
			final Scanner scanner = new Scanner(System.in);

			if (CONFIRM_STEP.equals(scanner.next())) {
				System.out.print("Replace all the existing accounts with the default one? ");

				if (CONFIRM_STEP.equals(scanner.next())) {
					initAccounts();
				}

				System.out.print("Replace all the existing projects, groups and requests with initial data? ");

				if (CONFIRM_STEP.equals(scanner.next())) {
					final Project project = initProject();

					if (project != null) {
						initGroup(project.getId());
					}
				}

				System.out.print("Do you want to remove all the events? ");

				if (CONFIRM_STEP.equals(scanner.next())) {
					removeEvents();
				}

				LOG.info("Initialization finished, starting application...");
			} else {
				System.out.println("Initialization process skipped");
			}
		}
		catch (Exception e){
			LOG.warn("Something went wrong and can't read from the command line");
			LOG.warn("Initialization process has been skipped but application is starting up...");
		}
	}

	private void initAccounts() {
		LOG.info("Remove all the accounts");
		accountService.removeAll();

		LOG.info("Create default 'admin' user");
		accountService.save(Account.builder()
				.username("admin")
				.password(passwordEncoder.encode("admin"))
				.firstName("Default")
				.lastName("User")
				.role(RoleType.ADMIN)
				.build());
	}

	private Project initProject() {
		LOG.info("Remove all the projects, groups and requests");
		projectService.removeAll();

		LOG.info("Create default project");
		return projectService.save(Project.builder()
				.name("Default project")
				.code("DFLT")
				.type(ProjectType.HTTP)
				.sequence(0)
				.build());
	}

	private void initGroup(final String projectId) {
		LOG.info("Create default group");
		groupService.save(Group.builder()
				.name("Default group")
				.projectId(projectId)
				.recording(true)
				.forwarding(false)
				.build());
	}

	private void removeEvents() {
		LOG.info("Remove events");
		eventService.removeAll();
	}
}