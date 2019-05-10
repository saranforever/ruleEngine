package com.smi.drools.controller;

import com.smi.drools.model.Address;
import com.smi.drools.model.DocumentContext;
import com.smi.drools.model.Message;
import com.smi.drools.model.Rule;
import com.smi.drools.model.RuleConfig;
import com.smi.drools.model.fact.AddressCheckResult;
import com.smi.drools.repository.RuleRepository;
import com.smi.drools.service.ReloadDroolsRulesService;
import com.smi.drools.util.RuleConfigUtil;

import org.kie.api.runtime.KieSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.io.IOException;


@RequestMapping("/test")
@Controller
public class TestController {

    @Resource
    private ReloadDroolsRulesService rules;
    
    @Autowired
	private RuleRepository ruleRepository;

    @ResponseBody
    @RequestMapping("/address")
    public void test(int num){
        Address address = new Address();
        address.setPostcode(generateRandom(num));
        KieSession kieSession = ReloadDroolsRulesService.kieContainer.newKieSession();

        AddressCheckResult result = new AddressCheckResult();
        kieSession.insert(address);
        kieSession.insert(result);
        int ruleFiredCount = kieSession.fireAllRules();
        kieSession.destroy();
        System.out.println("Triggered" + ruleFiredCount + "Rule");

        if(result.isPostCodeResult()){
        	System.out.println("Rule verification");
        }

    }
    
    @ResponseBody
    @RequestMapping("/testmsg")
    public void testMessage(){
        KieSession kieSession = ReloadDroolsRulesService.kieContainer.newKieSession();

        Message message = new Message();
		message.setStatus(1);
		message.setMsg("hello test!");
        
        kieSession.insert(message);
        int ruleFiredCount = kieSession.fireAllRules();
        kieSession.destroy();
        System.out.println("Triggered" + ruleFiredCount + "Message");

    }
    
    @ResponseBody
    @RequestMapping("/testdoccontext")
    public void testDocumentContext(){
        KieSession kieSession = ReloadDroolsRulesService.kieContainer.newKieSession();

        DocumentContext documentContext = new DocumentContext();
        documentContext.setDocumentType("EDI");
        documentContext.setDocumentVersion("00401");
        documentContext.setSenderEid("1124");
        
        kieSession.insert(documentContext);
        int ruleFiredCount = kieSession.fireAllRules();
        kieSession.destroy();
        System.out.println("Triggered" + ruleFiredCount + "DocumentContext");
    }
    
    @ResponseBody
    @PostMapping("/createdocumentcontextrule")
    public String createDocRule(@RequestBody RuleConfig ruleConfig) {
    	String ruleStr = RuleConfigUtil.buildRule(ruleConfig);
		
		Rule rule = new Rule();
		rule.setContent(ruleStr);
		rule.setCreateTime("");
		rule.setRuleKey(ruleConfig.getModelType()+ruleConfig.getSalience());
		rule.setVersion(ruleConfig.getSalience()+"");
		
		ruleRepository.save(rule);
		rules.reload();
		
		return "rule created";
    }
    
    @ResponseBody
    @RequestMapping("/createrule")
    public String createRule() {
    	String ruleStr = "package com.smi.drools\r\n";
		ruleStr += "import com.smi.drools.model.Message;\r\n";
		ruleStr += "rule \"rule1\"\r\n";
		ruleStr += "\twhen\r\n";
		ruleStr += "Message( status == 1, myMessage : msg )";
		ruleStr += "\tthen\r\n";
		ruleStr += "\t\tSystem.out.println( 1+\":\"+myMessage );\r\n";
		ruleStr += "end\r\n";
		
		Rule rule = new Rule();
		rule.setContent(ruleStr);
		rule.setCreateTime("");
		rule.setRuleKey("score");
		rule.setVersion("1");
		
		ruleRepository.save(rule);
		rules.reload(rule);
		
		return "rule created";
    }

    /**
     * @return
     * @throws IOException
     */
    @ResponseBody
    @RequestMapping("/reload")
    public String reload() throws IOException {
        rules.reload();
        return "ok";
    }


    /**
     * @param num
     * @return
     */
    public String generateRandom(int num) {
        String chars = "0123456789";
        StringBuffer number=new StringBuffer();
        for (int i = 0; i < num; i++) {
            int rand = (int) (Math.random() * 10);
            number=number.append(chars.charAt(rand));
        }
        return number.toString();
    }
}
