Reproducer for passivation issues
=================================

* <https://bugzilla.redhat.com/show_bug.cgi?id=1293016>
* <https://bugzilla.redhat.com/show_bug.cgi?id=1268424>

1. Set passivation store max-size to 1
    * Wildfly:  
/subsystem=ejb3/passivation-store=infinispan:write-attribute(name=max-size, value=1)
    * EAP 6:  
/subsystem=ejb3/file-passivation-store=file:write-attribute(name=max-size, value=1)
2. Deploy the ear, in about 5 seconds EJBs should be created and passivated

