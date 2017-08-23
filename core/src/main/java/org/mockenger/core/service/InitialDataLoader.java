package org.mockenger.core.service;

import org.mockenger.core.service.account.AccountService;
import org.mockenger.data.model.dict.ProjectType;
import org.mockenger.data.model.dict.RoleType;
import org.mockenger.data.model.persistent.account.Account;
import org.mockenger.data.model.persistent.mock.group.Group;
import org.mockenger.data.model.persistent.mock.project.Project;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.io.PrintStream;
import java.util.Scanner;

/**
 * @author Dmitry Ryazanov
 */
@Component
@Profile("init")
public class InitialDataLoader implements CommandLineRunner {

	private final Logger LOG = LoggerFactory.getLogger(InitialDataLoader.class);

	private final static String CONFIRM_STEP = "y";

	private final PrintStream prnt = System.out;

	// create a scanner so we can read the command-line input
	private final Scanner scanner = new Scanner(System.in);

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
		prnt.println("!!!ATTENTION!!!");
		prnt.println("You've activated initial data loader. Some of the next steps might corrupt your data. So, think twice and create backup.");
		prnt.println();
		prnt.println("Please type '" + CONFIRM_STEP + "' every time you want to proceed, or type any other character to skip the step.");
		prnt.println();
		prnt.print("Do you really want to start initialization process? ");

		try {
			nextStep(null, () -> {
				nextStep("Replace all the existing accounts with the default one? ", this::initAccounts);
				nextStep("Replace all the existing projects, groups and requests with initial data? ", this::initProjectAndGroup);
				nextStep("Do you want to remove all the events? ", this::removeEvents);
				nextStep("Initialization has been finished. Do you want to start application? ",
						() -> LOG.info("Initialization finished, starting application..."),
						() -> System.exit(0));
			}, () -> {
				prnt.println("Initialization process has been skipped.");
				prnt.println();
				prnt.println("Starting application");
			});

		} catch (Exception e){
			LOG.warn("Something went wrong and it is not possible to read from the command line");
			LOG.warn("Initialization process has been skipped but application is starting up...");
		}
	}

	private void initProjectAndGroup() {
		final Project project = initProject();

		if (project != null) {
			initGroup(project.getId());
		}
	}

	private void initAccounts() {
		String username;

		while ((username = scanner.nextLine()).isEmpty()) {
			prnt.print("Enter username for your new account: ");
		}

		prnt.print("And password, please: ");
		final String password = scanner.next();


		LOG.info("Remove all the accounts");
		accountService.removeAll();

		LOG.info("Create an account");
		accountService.save(Account.builder()
				.username(username)
				.password(passwordEncoder.encode(password))
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
				.code("GRP")
				.recording(true)
				.forwarding(false)
				.build());
	}

	private void removeEvents() {
		LOG.info("Remove events");
		eventService.removeAll();
	}

	private void nextStep(final String outputText, final NextStepDoer doer) {
		nextStep(outputText, doer, () -> { /* do nothing */ });
	}

	private void nextStep(final String outputText, final NextStepDoer doer, final NextStepDoer skipper) {
		if (outputText != null && outputText.length() > 0) {
			prnt.print(outputText);
		}

		if (CONFIRM_STEP.equalsIgnoreCase(scanner.next())) {
			doer.doNext();
		} else {
			skipper.doNext();
		}
	}


	private interface NextStepDoer {
		void doNext();
	}
}