package org.nuxeo;

import java.io.Serializable;
import java.util.Collections;
import java.util.Date;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Observer;
import org.jboss.seam.annotations.Scope;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.core.api.NuxeoPrincipal;
import org.nuxeo.ecm.webapp.helpers.EventNames;
import org.nuxeo.ecm.platform.audit.api.AuditLogger;
import org.nuxeo.ecm.platform.audit.api.LogEntry;
import org.nuxeo.runtime.api.Framework;


@Name("logdirectorychange")
@Scope(ScopeType.APPLICATION)
public class LogDirectoryChangeActionBean implements Serializable {

    private static final long serialVersionUID = 1L;

    private static final Log log = LogFactory.getLog(LogDirectoryChangeActionBean.class);

    protected AuditLogger logger;

    @In(create = true)
    private transient NuxeoPrincipal currentNuxeoPrincipal;

    @Observer(EventNames.DIRECTORY_CHANGED)
    public void run(String dirName) {
        AuditLogger logger = Framework.getService(AuditLogger.class);	
        LogEntry entry = newEntry(logger,dirName,new Date());
        logger.addLogEntries(Collections.singletonList(entry));
    }
    protected LogEntry newEntry(AuditLogger logger, String dirName, Date date) {
        LogEntry entry = logger.newLogEntry();
        entry.setEventId(EventNames.DIRECTORY_CHANGED);
        entry.setEventDate(date);
        entry.setCategory("directory change");
        entry.setPrincipalName(currentNuxeoPrincipal.getName());
        entry.setComment(dirName);
        return entry;
    }

}
