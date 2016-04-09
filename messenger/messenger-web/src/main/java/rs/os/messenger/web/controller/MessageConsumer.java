package rs.os.messenger.web.controller;

import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import rs.os.messenger.common.data.SampleMessageData;
import rs.os.messenger.common.domain.Message;
import rs.os.messenger.web.manager.InboxesManager;

@Controller
@RequestMapping("/message")
public class MessageConsumer {

	@Autowired
	private InboxesManager inboxesManager;

	@InitBinder
	public void initBinder(WebDataBinder binder) {
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		binder.registerCustomEditor(Date.class, new CustomDateEditor(
				dateFormat, false) {

			@Override
			public void setAsText(String text) throws IllegalArgumentException {
				Long time = Long.valueOf(text);
				Date date = new Date(time);
				setValue(date);
			}

		});
	}

	@RequestMapping(method = RequestMethod.POST)
	@ResponseStatus(value = HttpStatus.OK)
	public void addMessage(Message message) {
		inboxesManager.addMessage(message);
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	@ResponseBody
	public String getMessageFromInbox(@PathVariable("id") String inboxId,
			OutputStream out) throws UnsupportedEncodingException {
		Message message = inboxesManager.pullOldestMessage(inboxId);
		return Message.pack(message);
	}

	@RequestMapping(method = RequestMethod.GET)
	public String getMessages(Model model) {
		List<Message> messages = Arrays.asList(SampleMessageData
				.getSampleMessagesInbox());
		model.addAttribute("messages", messages);
		return "message/messages";
	}
}