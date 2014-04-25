package com.cx.util

import ch.ethz.ssh2.Connection
import ch.ethz.ssh2.Session
import ch.ethz.ssh2.StreamGobbler

/**
 * Created with IntelliJ IDEA.
 * User: alaplante
 * Date: 4/24/14
 * Time: 3:49 PM
 * To change this template use File | Settings | File Templates.
 */
class SshTool {
    public String runCommand(String hostname, String username, String password, String command)
    {
        Connection conn
        Session sess
        try {
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
            if (isAuthenticated == false)
                throw new IOException("Authentication failed.")

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
//            System.out.println("ExitCode: " + sess.getExitStatus());
            return result.toString()
        }
        catch (IOException e)
        {
            e.printStackTrace(System.err)
            return ""
        }  finally {
            try {
                if(sess != null) sess.close()
                if(conn != null) conn.close()
            } catch(Exception e) {
                System.out.println("Ignore error closing conn")
            }
        }
    }
}
