package org.jboss.test.ejb.sfsb;

import java.rmi.RemoteException;

import javax.ejb.Remote;

/**
 * @author Tomas Hofman (thofman@redhat.com)
 */
@Remote
public interface StatefulEJBRemote {

    void test() throws RemoteException;

}
