

import java.sql.SQLException;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import org.h2.tools.Server;

import entities.Parent;

public class Main {


    public static void main(String[] args) {
    	Server server = null;
    	try {
    		server = Server.createTcpServer().start();

	    	EntityManagerFactory factory = Persistence.createEntityManagerFactory("pu");
	        EntityManager em = factory.createEntityManager();
	        System.out.println("Initial data");
	        System.out.println(em.createQuery("Select p from Parent p").getResultList());
	        
	        em.getTransaction().begin();
	        Parent parent = new Parent();
	    	parent.setId(1L);
	    	parent.setName("new name");
	    	em.merge(parent);
	        
	        em.getTransaction().commit();
	        System.out.println("Data after merge");
	        System.out.println(em.createQuery("Select p from Parent p").getResultList());

    	} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			if (server != null) {
				server.stop();
			}
    	}
		System.exit(0);
    }
}
