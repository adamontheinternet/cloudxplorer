package com.cx.service

import com.vmware.vim25.InvalidLogin
import com.vmware.vim25.mo.ServiceInstance

import java.rmi.RemoteException


class VcenterConnectionService {

    static transactional = true

    def connections = [:]

    private String buildConnectionKey(String vcenterUrl, String vcenterUsername) {
        "${vcenterUrl}_${vcenterUsername}"
    }

    public ServiceInstance createOrGetShareableServiceInstance(String vcenterUrl, String vcenterUsername, String vcenterPassword) throws Exception {
        String key = buildConnectionKey(vcenterUrl, vcenterUsername)
        if(!connections[key]) {
            log.info("Create new shareable service instance for $key")
            connections[key] = createServiceInstance(vcenterUrl, vcenterUsername, vcenterPassword)
        }
        connections[key]
    }

    public void destroyShareableServiceInstance(String vcenterUrl, String vcenterUsername) {
        String key = buildConnectionKey(vcenterUrl, vcenterUsername)
        if(connections[key]) {
            log.info("Destroy new shareable service instance for $key")
            try {
                connections[key].getServerConnection().logout()
                log.info("Closed connection to shared service instance on $key")
            } catch(Throwable t) { log.info("Ignore error closing connection to shared service instance on $key $t")}
            finally {connections[key] = null} // critical so we dont return a non-null closed connection
        }
    }

    private ServiceInstance createServiceInstance(String vcenterUrl, String vcenterUsername, String vcenterPassword) {
        connect(vcenterUrl, vcenterUsername, vcenterPassword)
    }

    private ServiceInstance connect(String url, String user, String password) throws RemoteException, MalformedURLException, Exception {
        try {
            new ServiceInstance(new URL(url), user, password, true);
        } catch(InvalidLogin il) {
            log.error("Invalid vCenter server credentials")
            throw new Exception("Invalid vCenter server credentials")
        } catch(Exception e) {
            log.error("Error connecting to vCenter server")
            throw new Exception("Error connecting to vCenter server")
        }
    }
}
