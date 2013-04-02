package com.evgeniysokolov.ddwrt;

import java.io.InputStream;
import java.util.Properties;

import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;

public class DDWrtController {

	public static String executeRemoteCommandTmp() throws Exception {
		String username = "root";
		String password = "";
		String hostname = "192.168.1.1";
		int port = 22;

		return executeRemoteCommand(username, password, hostname, port);
	}

	public static String executeRemoteCommand(String username, String password,
			String hostname, int port) throws Exception {
		JSch jsch = new JSch();
		Session session = jsch.getSession(username, hostname, 22);
		session.setPassword(password);

		// Avoid asking for key confirmation
		Properties prop = new Properties();
		prop.put("StrictHostKeyChecking", "no");
		session.setConfig(prop);

		session.connect();

		// SSH Channel
		ChannelExec channelssh = (ChannelExec) session.openChannel("exec");
		
		String command = "ls -al /";
		System.out.println("Command: " + command);
		
		channelssh.setCommand(command);
		
		channelssh.connect();

		InputStream in = channelssh.getInputStream();
		channelssh.connect();
		
		StringBuilder buffer = new StringBuilder();
		byte[] tmp = new byte[1024];
		while (true) {
			while (in.available() > 0) {
				int i = in.read(tmp, 0, 1024);
				if (i < 0)
					break;
				buffer.append(new String(tmp, 0, i));
			}
			if (channelssh.isClosed()) {
				System.out.println("exit-status: " + channelssh.getExitStatus());
				break;
			}
			try {
				Thread.sleep(1000);
			} catch (Exception ee) {
			}

		}
		channelssh.disconnect();
	    session.disconnect();
		return buffer.toString();
	}
}
