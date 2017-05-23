package nlrmissues;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class App {

	public static void main(String[] args) {

		final ApplicationContext context=new ClassPathXmlApplicationContext("resources/spring.xml");
        
		java.awt.EventQueue.invokeLater(new Runnable() {
			
			public void run() {

                ConnectionFrame conF=(ConnectionFrame) context.getBean(ConnectionFrame.class);
                conF.setVisible(true);
				
			}
		});
		
	}

}
