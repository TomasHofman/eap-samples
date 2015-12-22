package org.jboss.test.ejb.sfsb;

import java.rmi.RemoteException;
import java.util.logging.Logger;

import javax.ejb.PostActivate;
import javax.ejb.PrePassivate;
import javax.ejb.Remote;
import javax.ejb.Stateful;

import org.jboss.ejb3.annotation.Cache;


@Stateful
@Remote(StatefulEJBRemote.class)
@Cache("passivating")
public class StatefulEJB implements StatefulEJBRemote {

    private Logger logger = Logger.getLogger(getClass().getName());
    private Object object;

    public StatefulEJB() {
        object = TestObjectFactory.newObjectWithGuaranteedStackOverflow();
    }

    public void test() throws RemoteException {
        logger.info("Invoked test: " + object);
    }

    @PrePassivate
    public void prePassivate() {
        logger.info("pre-passivate: " + object);
    }

    @PostActivate
    public void postActivate() {
        logger.info("post-activate: " + object);
    }
}
