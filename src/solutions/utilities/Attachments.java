package solutions.utilities;

import java.io.File;
import java.net.URL;
import java.nio.ByteBuffer;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import org.javacord.api.entity.message.Message;
import org.javacord.api.entity.message.MessageAttachment;

public class Attachments {
	public static URL[] getAttachmentLinks(Message message) {
		List<URL> links = new ArrayList<URL>();

		for (MessageAttachment attachment : message.getAttachments()) {
			links.add(attachment.getUrl());
		}

		return links.toArray(new URL[links.size()]);
	}
	
	public File[] getAttachmentFiles(Message message) {
		List<File> links = new ArrayList<File>();

		for (MessageAttachment attachment : message.getAttachments()) {
			links.add(new File(Paths.get(attachment.getUrl().toURI()).toFile()));
		}

		return links.toArray(new URL[links.size()]);
	}
}