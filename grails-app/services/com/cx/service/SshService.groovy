package com.cx.service

import ch.ethz.ssh2.Connection
import ch.ethz.ssh2.Session
import ch.ethz.ssh2.StreamGobbler
import grails.transaction.Transactional

@Transactional
class SshService {

    public String runCommand(String hostname, String username, String password, String command)
    {
        Connection conn
        Session sess
        try {
            log.info "Run SSH command $command against host $hostname"
            StringBuffer result = new StringBuffer()
            /* Create a connection instance */
            conn = new Connection(hostname)

            /* Now connect */
            conn.connect()

            /* Authenticate.
             * If you get an IOException saying something like
             * "Authentication method password not supported by the server at this stage."
             * then please check the FAQ.
             */
            boolean isAuthenticated = conn.authenticateWithPassword(username, password)
            if (isAuthenticated == false) {
                log.error "Authentication failed"
                throw new Exception("Authentication failed")
            }

            /* Create a session */
            sess = conn.openSession()
            sess.execCommand(command)

            /*
             * This basic example does not handle stderr, which is sometimes dangerous
             * (please read the FAQ).
             */
            InputStream stdout = new StreamGobbler(sess.getStdout())
            BufferedReader br = new BufferedReader(new InputStreamReader(stdout))

            while (true) {
                String line = br.readLine()
                if (line == null) break
                result.append(line).append("\n")
            }

            /* Show exit status, if available (otherwise "null") */
            System.out.println("ExitCode ${sess.getExitStatus()}")
            result.toString()
        }
        catch (Exception e) {
            log.error "Error running command $e"
            ""
        }  finally {
            try {
                if(sess != null) sess.close()
                if(conn != null) conn.close()
            } catch(Exception e) {
                log.info "Ignore error closing conn"
            }
        }
    }
}
