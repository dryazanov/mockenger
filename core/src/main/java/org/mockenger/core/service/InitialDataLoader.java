package org.mockenger.core.service;

import lombok.extern.slf4j.Slf4j;
import org.mockenger.core.service.account.AccountService;
import org.mockenger.data.model.dict.ProjectType;
import org.mockenger.data.model.dict.RoleType;
import org.mockenger.data.model.persistent.account.Account;
import org.mockenger.data.model.persistent.mock.group.Group;
import org.mockenger.data.model.persistent.mock.project.Project;
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
@Slf4j
@Component
@Profile("init")
public class InitialDataLoader implements CommandLineRunner {

	private final static String CONFIRM_STEP = "y";
	private static final String ANSI_RESET = "\u001B[0m";
	private static final String ANSI_RED = "\u001B[31m";
	private static final String ANSI_GREEN = "\u001B[32m";

	private final PrintStream printStream = System.out;

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
		printStream.println(ANSI_RED);
		printStream.println("!!!ATTENTION!!!");
		printStream.println("You've activated initial data loader. Some of the next steps might corrupt your data.");
		printStream.println("So, think twice and make sure you have a backup." + ANSI_RESET);
		printStream.println(ANSI_GREEN);
		printStream.println("Please type '" + CONFIRM_STEP + "' every time you want to proceed, or type any other character to skip the step.");
		printStream.println();
		printStream.println("Do you really want to start initialization process? " + ANSI_RESET);

		try {
			nextStep(null, () -> {
				nextStep("Replace all the existing accounts with the default one? ", this::initAccounts);
				nextStep("Replace all the existing projects, groups and requests with initial data? ", this::initProjectAndGroup);
				nextStep("Do you want to remove all the events? ", this::removeEvents);
				nextStep("Initialization has been finished. Do you want to start application? ",
						() -> log.info("Initialization finished, starting application..."),
						() -> System.exit(0));
			}, () -> {
				printStream.println("Initialization process has been skipped.");
				printStream.println();
				printStream.println("Starting application");
			});
		} catch (Exception e){
			log.warn("Something went wrong and it is not possible to read from the command line");
			log.warn("Initialization process has been skipped but application is starting up...");
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

		do {
			printStream.println(ANSI_GREEN + "Enter username for your new account: " + ANSI_RESET);
		}
		while ((username = scanner.nextLine()).isEmpty());

		printStream.println(ANSI_GREEN + "And password, please: " + ANSI_RESET);
		final String password = scanner.nextLine();


		log.info("Remove all the accounts");
		accountService.removeAll();

		log.info("Create an account");
		accountService.save(Account.builder()
				.username(username)
				.password(passwordEncoder.encode(password))
				.firstName("Default")
				.lastName("User")
				.role(RoleType.ADMIN)
				.build());
	}

	private Project initProject() {
		log.info("Remove all the projects, groups and requests");
		projectService.removeAll();

		log.info("Create default project");
		return projectService.save(Project.builder()
				.name("Default project")
				.code("DFLT")
				.type(ProjectType.HTTP)
				.sequence(0)
				.build());
	}

	private void initGroup(final String projectId) {
		log.info("Create default group");
		groupService.save(Group.builder()
				.name("Default group")
				.projectId(projectId)
				.code("GRP")
				.recording(true)
				.forwarding(false)
				.build());
	}

	private void removeEvents() {
		log.info("Remove events");
		eventService.removeAll();
	}

	private void nextStep(final String outputText, final NextStepDoer doer) {
		nextStep(outputText, doer, () -> { /* do nothing */ });
	}

	private void nextStep(final String outputText, final NextStepDoer doer, final NextStepDoer skipper) {
		if (outputText != null && outputText.length() > 0) {
			printStream.println(ANSI_GREEN + outputText + ANSI_RESET);
		}

		if (CONFIRM_STEP.equalsIgnoreCase(scanner.nextLine())) {
			doer.doNext();
		} else {
			skipper.doNext();
		}
	}


	private interface NextStepDoer {
		void doNext();
	}
}