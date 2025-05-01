package com.nnroad.client.service;

import java.util.List;

import com.nnroad.client.domain.SysClient;
import com.nnroad.client.mapper.SysClientMapper;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Service interface for SysClient operations.
 * 
 * @author nick
 * @date 2024-10-10
 */
public interface ISysClientService 
{

    /**
     * Retrieve a SysClient by its unique identifier.
     * 
     * @param id The unique identifier of the SysClient.
     * @return The SysClient object associated with the given id, or null if not found.
     */
    public SysClient selectSysClientById(Long id);

    /**
     * Retrieve a list of SysClient objects.
     * 
     * @param sysClient The criteria for querying clients.
     * @return A list of SysClient objects matching the criteria.
     */
    public List<SysClient> selectSysClientList(SysClient sysClient);

    /**
     * Add a new SysClient.
     * 
     * @param sysClient The SysClient object to be added.
     * @return The result of the operation (e.g., number of rows affected).
     */
    public int insertSysClient(SysClient sysClient);

    /**
     * Update an existing SysClient.
     * 
     * @param sysClient The SysClient object containing updated data.
     * @return The result of the operation (e.g., number of rows affected).
     */
    public int updateSysClient(SysClient sysClient);

    /**
     * Batch delete SysClient objects.
     * 
     * @param ids An array of unique identifiers for the SysClients to be deleted.
     * @return The result of the operation (e.g., number of rows affected).
     */
    public int deleteSysClientByIds(Long[] ids);

    /**
     * Delete a SysClient by its unique identifier.
     * 
     * @param id The unique identifier of the SysClient to be deleted.
     * @return The result of the operation (e.g., number of rows affected).
     */
    public int deleteSysClientById(Long id);
    
    /**
     * Get clients by vendor id
     * 
     * @param id Vendor primary key
     * @return List of client objects
     */
	public List<SysClient> getClientByVendorId(Long vendorId);

    boolean checkCode(SysClient client, String prefix);

    String resetAndGetCode(String prefix);

    /**
     * Deactivate a client by its unique identifier.
     *
     * @param id The unique identifier of the client to be deactivated.
     * @return the result of the operation (e.g., number of rows affected).
     */
    public int deactivateSysClientById(Long id);


}
