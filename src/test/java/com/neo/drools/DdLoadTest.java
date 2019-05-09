package com.neo.drools;

import com.neo.drools.model.Message1;
import org.kie.api.io.ResourceType;
import org.kie.internal.KnowledgeBase;
import org.kie.internal.KnowledgeBaseFactory;
import org.kie.internal.builder.KnowledgeBuilder;
import org.kie.internal.builder.KnowledgeBuilderError;
import org.kie.internal.builder.KnowledgeBuilderErrors;
import org.kie.internal.builder.KnowledgeBuilderFactory;
import org.kie.internal.io.ResourceFactory;
import org.kie.internal.runtime.StatefulKnowledgeSession;

import java.io.UnsupportedEncodingException;




public class DdLoadTest {

	public static void main(String[] args) {
		//Rule, rule2 can be placed in the database, there is a unique code and they are, when the code is to execute the rules, it is OK to get it from the database according to the code, so the rule management system developed by itself can maintain the rules in the database. Just fine
		String rule = "package com.neo.drools\r\n";
		rule += "import com.neo.drools.model.Message;\r\n";
		rule += "rule \"rule1\"\r\n";
		rule += "\twhen\r\n";
		rule += "Message( status == 1, myMessage : msg )";
		rule += "\tthen\r\n";
		rule += "\t\tSystem.out.println( 1+\":\"+myMessage );\r\n";
		rule += "end\r\n";


		String rule2 = "package com.neo.drools\r\n";
		rule += "import com.neo.drools.model.Message;\r\n";

		rule += "rule \"rule2\"\r\n";
		rule += "\twhen\r\n";
		rule += "Message( status == 2, myMessage : msg )";
		rule += "\tthen\r\n";
		rule += "\t\tSystem.out.println( 2+\":\"+myMessage );\r\n";
		rule += "end\r\n";


		StatefulKnowledgeSession kSession = null;
		try {


			KnowledgeBuilder kb = KnowledgeBuilderFactory.newKnowledgeBuilder();
			//Load multiple rules 
			kb.add(ResourceFactory.newByteArrayResource(rule.getBytes("utf-8")), ResourceType.DRL);
			kb.add(ResourceFactory.newByteArrayResource(rule2.getBytes("utf-8")), ResourceType.DRL);

			KnowledgeBuilderErrors errors = kb.getErrors();
			for (KnowledgeBuilderError error : errors) {
				System.out.println(error);
			}
			KnowledgeBase kBase = KnowledgeBaseFactory.newKnowledgeBase();
			kBase.addKnowledgePackages(kb.getKnowledgePackages());

			kSession = kBase.newStatefulKnowledgeSession();


			Message1 message1 = new Message1();
			message1.setStatus(1);
			message1.setMsg("hello world!");

			Message1 message2 = new Message1();
			message2.setStatus(2);
			message2.setMsg("hi world!");

			kSession.insert(message1);
			kSession.insert(message2);
			kSession.fireAllRules();

		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} finally {
			if (kSession != null)
				kSession.dispose();
		}

	}
}

