package org.leplan73.tamscout;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.io.PrintWriter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.util.ContextInitializer;

public class Logging {

	public static Logger logger_;

	public static void initLogger(Class<?> classn)
	{
		System.setProperty(ContextInitializer.CONFIG_FILE_PROPERTY, "logback-gui.xml");
		logger_ = LoggerFactory.getLogger(classn);
	}
	
	public static void debugintranet(boolean debug)
	{
		if (debug)
		{
			System.setProperty("org.slf4j.simpleLogger.log.org.apache.http", "debug");
			System.setProperty("org.slf4j.simpleLogger.log.org.apache.http.wire", "debug");
			ch.qos.logback.classic.Logger root = (ch.qos.logback.classic.Logger) org.slf4j.LoggerFactory.getLogger("org.leplan73.analytiscout.intranet");
			root.setLevel(Level.DEBUG);
		}
	}
	
	static public String dumpStack(String message, Throwable ex)
	{
		final StringBuilder messageBuilder = new StringBuilder();
		if (message != null)
		{
			messageBuilder.append(message).append(":\n");
		}
		OutputStream stream = new ByteArrayOutputStream();
		PrintWriter writer = new PrintWriter(new BufferedOutputStream(stream));
		ex.printStackTrace(writer);
		writer.flush();
		messageBuilder.append(stream.toString());
		return messageBuilder.toString();
	}
}
