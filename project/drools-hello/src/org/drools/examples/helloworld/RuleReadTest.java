
package org.drools.examples.helloworld;

import org.drools.examples.helloworld.HelloWorldExample.Message;
import org.kie.api.io.ResourceType;
import org.kie.internal.KnowledgeBase;
import org.kie.internal.KnowledgeBaseFactory;
import org.kie.internal.builder.KnowledgeBuilder;
import org.kie.internal.builder.KnowledgeBuilderError;
import org.kie.internal.builder.KnowledgeBuilderErrors;
import org.kie.internal.builder.KnowledgeBuilderFactory;
import org.kie.internal.io.ResourceFactory;
import org.kie.internal.logger.KnowledgeRuntimeLogger;
import org.kie.internal.logger.KnowledgeRuntimeLoggerFactory;
import org.kie.internal.runtime.StatefulKnowledgeSession;



public class RuleReadTest {

    public static final void main(final String[] args) throws Exception{
    	
    	// load up the knowledge base  
        KnowledgeBase kbase = readKnowledgeBase();  
        //创建会话 
        StatefulKnowledgeSession ksession = kbase.newStatefulKnowledgeSession(); 
        KnowledgeRuntimeLogger logger = KnowledgeRuntimeLoggerFactory.newFileLogger(ksession, "test");  
        // go !  
        Message message = new Message();  
        message.setMessage("my msg "+System.currentTimeMillis());  
        message.setStatus(Message.MY);  
        ksession.insert(message);  
        //执行规则  
        ksession.fireAllRules();
        logger.close();  
       
    }

   
    private static KnowledgeBase readKnowledgeBase() throws Exception {  
    	//创建规则构建器  
        KnowledgeBuilder kbuilder = KnowledgeBuilderFactory.newKnowledgeBuilder();
        //加载规则文件，并增加到构建器  
        String ruleFile = "org/drools/examples/helloworld/HelloWorld-my.drl";
        kbuilder.add(ResourceFactory.newClassPathResource(ruleFile), ResourceType.DRL);
        KnowledgeBuilderErrors errors = kbuilder.getErrors(); 
        //编译规则过程中发现规则是否有错误  
        if (errors.size() > 0) {
            for (KnowledgeBuilderError error: errors) {System.out.println("规则中存在错误，错误消息如下：");  
                System.err.println(error);  
            }  
            throw new IllegalArgumentException("Could not parse knowledge.");  
        }  
        //创建规则构建库  
        KnowledgeBase kbase = KnowledgeBaseFactory.newKnowledgeBase();
        //构建器加载的资源文件包放入构建库
        kbase.addKnowledgePackages(kbuilder.getKnowledgePackages());  
        return kbase;  
    }  
 
}
