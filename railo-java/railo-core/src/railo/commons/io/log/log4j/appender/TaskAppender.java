package railo.commons.io.log.log4j.appender;

import org.apache.log4j.Appender;
import org.apache.log4j.AppenderSkeleton;
import org.apache.log4j.Layout;
import org.apache.log4j.spi.ErrorHandler;
import org.apache.log4j.spi.Filter;
import org.apache.log4j.spi.LoggingEvent;

import railo.print;
import railo.commons.io.log.log4j.appender.task.Task;
import railo.loader.engine.CFMLEngine;
import railo.loader.engine.CFMLEngineFactory;
import railo.runtime.config.Config;
import railo.runtime.exp.PageException;
import railo.runtime.spooler.ExecutionPlan;
import railo.runtime.spooler.ExecutionPlanImpl;
import railo.runtime.spooler.SpoolerEngine;

public class TaskAppender implements Appender {
	
	private Appender appender;
	private SpoolerEngine spoolerEngine;

	public TaskAppender(Config config,Appender appender){
		this.appender=appender;
		spoolerEngine = config.getSpoolerEngine();
		
	}

	@Override
	public void doAppend(LoggingEvent le) {
		spoolerEngine.add(new Task(appender,le));
	}

	@Override
	public void addFilter(Filter arg0) {
		appender.addFilter(arg0);
	}

	@Override
	public void clearFilters() {
		appender.clearFilters();
	}

	@Override
	public void close() {
		appender.close();
	}

	@Override
	public ErrorHandler getErrorHandler() {
		return appender.getErrorHandler();
	}

	@Override
	public Filter getFilter() {
		return appender.getFilter();
	}

	@Override
	public Layout getLayout() {
		return appender.getLayout();
	}

	@Override
	public String getName() {
		return appender.getName();
	}

	@Override
	public boolean requiresLayout() {
		return appender.requiresLayout();
	}

	@Override
	public void setErrorHandler(ErrorHandler arg0) {
		appender.setErrorHandler(arg0);
	}

	@Override
	public void setLayout(Layout arg0) {
		appender.setLayout(arg0);
	}

	@Override
	public void setName(String arg0) {
		appender.setName(arg0);
	}
}