package pe.util.console;

import java.util.ArrayList;
import java.util.List;

import pe.util.Util;

public class ConsoleInputInterpreter implements Runnable{
	
	private Console console;
	private Thread interpreterThread;
	private static String[] classFileLocations = new String[]{"./bin","C:/Program Files/Java/jdk1.8.0_101/"};
	private static List<ConsoleCommand> commands;
	
	private static final ConsoleCommand HELP = new ConsoleCommand("help","Shows the help menu.");
	private static final ConsoleCommand INFO = new ConsoleCommand("info","Shows the description and use of a command.", 
										"(Command)");
	private static final ConsoleCommand EXECUTE = new ConsoleCommand("execute","Will invoke a method or constructor given a class, method name, and arguments.\n\tThe command connot contain mathematical operations not within a method or constructor.\n\tLook up Util.parseIntoObject() for more details on the command's format.",
										"(Java formatted command)");
	private static final ConsoleCommand CLIST = new ConsoleCommand("clist","Lists all of the possible commands or options for a given argument.");

	private static final ConsoleCommand SET_SCROLL_LOCK = new ConsoleCommand("setScrollLock", "Sets whether the console allows vertical scrolling",
										"(true/false)");

	public ConsoleInputInterpreter(Console console){
		this.console = console;
		this.interpreterThread = new Thread(this);
		
		interpreterThread.start();
	}
	
	public static void initConsoleInputInterpreter(Console console){
		if(commands != null){
			console.err("Console commands have already been initialized");
			return;
		}
		
		commands = new ArrayList<ConsoleCommand>();
		
		commands.add(HELP);
		commands.add(INFO);
		commands.add(EXECUTE);
		commands.add(CLIST);
		commands.add(SET_SCROLL_LOCK);
	}
	
	public void run() {
		String input = console.getInputText().replaceAll("\n", "").trim();
		console.clearInput();
		console.addPastCommand(input);
		
		console.log(input);
		if(input.length() == 0 || input.charAt(0) != '/') return;
		
		input = input.substring(1);
		
		String[] inputArr = input.split("\\s+");
		boolean commandFound = false;
		ConsoleCommand cmd = null;
		
		for(ConsoleCommand command:commands){
			if(command.getName().equals(inputArr[0])){
				commandFound = true;
				cmd = command;
				break;
			}
		}
		if(!commandFound){
			errCommandNameNotFound(inputArr[0]);
			return;
		}
		
		if(cmd.equals(HELP)) cmdHelp(inputArr);
		
		if(cmd.equals(INFO)) cmdInfo(inputArr);
		
		if(cmd.equals(EXECUTE)) cmdExecute(inputArr);
		
		if(cmd.equals(CLIST)) cmdCList(inputArr);
		
		if(cmd.equals(SET_SCROLL_LOCK)) cmdSetScrollLock(inputArr);
	}
	
	private void cmdSetScrollLock(String[] input){
		if(input.length != 2){
			errIncorrectUsageOfCommand(SET_SCROLL_LOCK);
			return;
		}
		
		String bool = input[1].toLowerCase();
		boolean boolFound = false;
		
		if(bool.equals("true") || bool.equals("t") || bool.equals("1")){
			console.setScrollLock(true);
			boolFound = true;
			console.log("Scroll Lock has been turned on.");
			return;
		}
		
		if(bool.equals("false") || bool.equals("f") || bool.equals("0")){
			console.setScrollLock(false);
			boolFound = true;
			console.log("Scroll Lock has been turned off.");
			return;
		}
		
		if(!boolFound){
			errIncorrectUsageOfCommand(SET_SCROLL_LOCK);
			return;
		}
	}
	
	private void cmdCList(String[] input){
		console.warn("\tThis command is currently not implemented");
	}
	
	private void cmdExecute(String[] input){
		if(input.length < 2){
			errIncorrectUsageOfCommand(EXECUTE);
			return;
		}
		
		String command = "";
		for(int i = 1; i < input.length; i++){
			command += input[i];
		}
		try{
		console.log(Util.parseIntoObject(command, new ArrayList<Object>(), classFileLocations));
		}catch (Exception e){
			String location = null;
			for(int ln = 0; ln < e.getStackTrace().length; ln++){
				if(e.getStackTrace()[ln].getLineNumber() != -1){
					location = e.getStackTrace()[ln].toString();
					break;
				}
			}
			console.err(e.getMessage() + "\n\t @ " + location);
		}
	}
	
	private void cmdInfo(String[] input){
		if(input.length != 2){
			errIncorrectUsageOfCommand(INFO);
			return;
		}
		
		ConsoleCommand searchCommand = null;
		boolean commandFound = false;
		for(ConsoleCommand command: commands){
			if(input[1].equals(command.getName())){
				searchCommand = command;
				commandFound = true;
				break;
			}
		}
		
		if(!commandFound){
			errIncorrectUsageOfCommand(INFO);
			return;
		}
		
		console.log("\tCommand: /" + searchCommand.getName());
		console.logln("\t\tArguments: ");
		
		String[] args = searchCommand.getArgs();
		if(args.length == 0) console.logln("none");
		for(int i = 0; i < searchCommand.getArgsLength(); i++){
			console.logln(args[i]);
		}
		console.logln("\n");
		console.log("\t\tDescription: " + searchCommand.getDescription());
	}
	
	private void cmdHelp(String[] input){
		if(input.length != 1){
			errIncorrectUsageOfCommand(HELP);
			return;
		}
		
		console.log("----------------------------Help----------------------------");
		for(ConsoleCommand command:commands){
			console.logln("\t/" + command.getName());
			String[] args = command.getArgs();
			for(int i = 0; i < args.length; i++){
				console.logln(" " + args[i]);
			}
			console.logln("\n");
			console.log("\t\t" + command.getDescription() + "\n");
		}
		console.log("------------------------------------------------------------");
	}
	
	private void errIncorrectUsageOfCommand(ConsoleCommand cmd){
		console.err("Error: Incorrect usage of /" + cmd.getName());
		console.errln("\tUsage: /" + cmd.getName());
		String args[] = cmd.getArgs();
		for(int i = 0; i < cmd.getArgsLength(); i++){
			console.errln(" " + args[i]);
		}
		console.errln("\n");
		console.err("\tType '/help' for help");
	}
	
	private void errCommandNameNotFound(Object cmdName){
		console.err("/" + cmdName + " was not found. Please enter a valid command");
		console.err("\tUsage: /(Command)");
		console.err("\tType '/help' for help");
	}
}
