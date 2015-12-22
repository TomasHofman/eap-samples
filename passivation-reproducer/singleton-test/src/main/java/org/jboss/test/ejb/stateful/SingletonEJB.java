package org.jboss.test.ejb.stateful;

import java.rmi.RemoteException;
import java.util.logging.Logger;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.ejb.Timeout;
import javax.ejb.Timer;
import javax.ejb.TimerConfig;
import javax.ejb.TimerService;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.jboss.test.ejb.sfsb.StatefulEJB;
import org.jboss.test.ejb.sfsb.StatefulEJBRemote;


@Singleton
@Startup
@TransactionManagement(value = TransactionManagementType.BEAN)
public class SingletonEJB {

    private static final String EJB_JNDI_NAME = "java:app/passivation-reproducer-sfsb-1.0/" + StatefulEJB.class.getSimpleName();
    private final Logger log = Logger.getLogger(this.getClass().getName());

    @Resource
    private TimerService timerService;

    @PostConstruct
    public void start() {
        TimerConfig config = new TimerConfig();
        config.setPersistent(false);
        // wait 5 sec for the server to be up, then 10 sec after
        timerService.createIntervalTimer(5 * 1000, 10 * 1000, config);
    }

    @Timeout
    public void timeout(Timer timer) throws NamingException, RemoteException, InterruptedException {
        InitialContext ctx = new InitialContext();

        StatefulEJBRemote instance1 = (StatefulEJBRemote) ctx.lookup(EJB_JNDI_NAME);
        instance1.test();

        Thread.sleep(1000);

        StatefulEJBRemote instance2 = (StatefulEJBRemote) ctx.lookup(EJB_JNDI_NAME);
        instance2.test();

        Thread.sleep(1000);

        StatefulEJBRemote instance3 = (StatefulEJBRemote) ctx.lookup(EJB_JNDI_NAME);
        instance3.test();

//        Thread.sleep(1000);

//        instance1.test();

        timer.cancel(); // run only once
    }
}
